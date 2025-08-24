package com.ecommerce.electronicapplicationmanagement.service.impl;

import com.ecommerce.electronicapplicationmanagement.constant.Constant;
import com.ecommerce.electronicapplicationmanagement.constant.DealType;
import com.ecommerce.electronicapplicationmanagement.converter.DealConverter;
import com.ecommerce.electronicapplicationmanagement.converter.ProductConverter;
import com.ecommerce.electronicapplicationmanagement.dto.ProductDto;
import com.ecommerce.electronicapplicationmanagement.entity.Deal;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.exception.ConflictDataException;
import com.ecommerce.electronicapplicationmanagement.exception.DateTimeInValidException;
import com.ecommerce.electronicapplicationmanagement.exception.InsufficientStockException;
import com.ecommerce.electronicapplicationmanagement.exception.ResourcesNotFoundException;
import com.ecommerce.electronicapplicationmanagement.repository.BasketItemRepository;
import com.ecommerce.electronicapplicationmanagement.repository.DealRepository;
import com.ecommerce.electronicapplicationmanagement.repository.ProductRepository;
import com.ecommerce.electronicapplicationmanagement.request.AddDealRequest;
import com.ecommerce.electronicapplicationmanagement.request.AddProductRequest;
import com.ecommerce.electronicapplicationmanagement.request.BaseRequest;
import com.ecommerce.electronicapplicationmanagement.request.SearchProductRequest;
import com.ecommerce.electronicapplicationmanagement.service.ProductService;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.hibernate.TransactionException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final BasketItemRepository basketItemRepository;

    private final DealRepository dealRepository;

    @Override
    @Transactional
    public Product initProduct(AddProductRequest addProductRequests) {
        Product product = ProductConverter.INSTANCE.fromProductRequest(addProductRequests);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    @Retryable(value = JpaOptimisticLockingFailureException.class, maxAttempts = 3)
    public Product removeProductById(Long id, Long version) {
        try {
            Optional<Product> productOptional = productRepository.findProductByIdAndVersion(id, version);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                product.setLogicalDeleteFlag(Constant.INVALID_FLAG);

                // delete the basket references
                log.info("Start remove the basket references product with productId: {}", id);
                basketItemRepository.deleteByProductId(id);
                log.info("End remove the basket references product with productId: {}", id);

                // delete the deal references
                log.info("Start remove the deal references with productId: {}", id);
                dealRepository.deleteByProductId(id);
                log.info("End remove the deal references with productId: {}", id);

                return productRepository.save(product);
            } else {
                log.info("Product has already deleted or isn't exist");
                throw new ResourcesNotFoundException("productID", String.valueOf(id));
            }
        } catch (ConflictDataException e) {
            throw new ConflictDataException();
        }
    }

    @Override
    public List<ProductDto> listFiltered(SearchProductRequest request, Pageable pageable) {
        Page<Product> page = productRepository.findFiltered(
                request.getCategory(),
                request.getName(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getAvailable(),
                pageable);
        if (!page.isEmpty()) {
            return ProductConverter.INSTANCE.fromListProduct(page.getContent());
        } else {
            throw new ResourcesNotFoundException();
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    @Retryable(value = {
            OptimisticLockException.class,
            StaleObjectStateException.class,
            PersistenceException.class,
            TransactionException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100))
    @Override
    public Product updateStock(Product product, int delta) {
        product.setStock(product.getStock() + delta);
        if (product.getStock() < 0) {
            throw new InsufficientStockException(product.getName());
        }
        return productRepository.save(product);
    }

    @Recover
    public Product recover(org.springframework.orm.ObjectOptimisticLockingFailureException e,
                              Product product, int delta) {
        log.error("Recover triggered for ObjectOptimisticLockingFailureException. Product IDs: {}, Message: {}",
                product.getName(), e.getMessage());
        throw new ConflictDataException();
    }

    @Override
    public Product updateProduct(Long id, AddProductRequest addProductRequest) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(addProductRequest.getName());
            product.setPrice(addProductRequest.getPrice());
            product.setCategory(addProductRequest.getCategory());
            return productRepository.save(product);
        }
        return new Product();
    }

    /**
     * addDealForProduct
     *
     * @param request add deal request
     * @return the deal have been saved
     */

    @Override
    @Retryable(
            value = {
                    OptimisticLockException.class,
                    StaleObjectStateException.class,
                    PersistenceException.class,
                    TransactionException.class
            },
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public List<Deal> addDealForProduct(List<AddDealRequest> request) {
        log.info("Attempting to save deals for product:");
        Set<Long> productIdReqs = request.stream()
                .map(AddDealRequest::getProductId)
                .collect(Collectors.toSet());

        List<Product> products = productRepository.getInformationProduct(new ArrayList<>(productIdReqs));
        Set<Long> productIdsExist = products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());

        // filter the productId in database is not contains productId of the request
        Set<Long> productIdNotExist = productIdReqs.stream()
                .filter(req -> !productIdsExist.contains(req))
                .collect(Collectors.toSet());

        if (productIdNotExist.isEmpty()) {
            log.info("Start to save deal for product with productIds: {}", String.join(",",
                    Arrays.toString(productIdReqs.toArray())));

            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, product -> product));

            List<Deal> deals = request.stream().map(req -> {
                // set data for product
                BaseRequest baseRequest = new BaseRequest();

                Product product = ProductConverter.INSTANCE.fromBaseRequest(baseRequest);
                product.setId(req.getProductId());
                product.setVersion(productMap.get(req.getProductId()).getVersion());
                product.setName(productMap.get(req.getProductId()).getName());
                product.setPrice(productMap.get(req.getProductId()).getPrice());
                product.setStock(productMap.get(req.getProductId()).getStock());

                // convert from request to deal
                Deal deal = DealConverter.INSTANCE.fromAddDealRequest(req);
                DealType dealType = DealType.valueOf(req.getDealType());

                deal.setProduct(product);
                setExpiredDate(deal, req);
                deal.setDealType(getDealType(dealType));
                deal.setDiscountValue(calculateDiscountValue(req.getProductId(), productMap, dealType));
                return deal;
            }).toList();
            return dealRepository.saveAll(deals);

        } else {
            throw new ResourcesNotFoundException(String.join(" ",
                    Arrays.toString(productIdNotExist.toArray())));
        }
    }

    @Recover
    public List<Deal> recover(org.springframework.orm.ObjectOptimisticLockingFailureException e,
                              List<AddDealRequest> request) {
        log.error("Recover triggered for ObjectOptimisticLockingFailureException. Product IDs: {}, Message: {}",
                request.stream().map(AddDealRequest::getProductId).toList(), e.getMessage());
        throw new ConflictDataException();
    }


    /**
     * calculateDiscountValue
     *
     * @param productReqId requestId
     * @param productMap   mapping productId pair product object
     * @param dealType     type of deal
     * @return the result based on the deal and price of product
     */
    private BigDecimal calculateDiscountValue(Long productReqId, Map<Long, Product> productMap, DealType dealType) {
        return productMap.get(productReqId).getPrice().
                multiply(BigDecimal.valueOf(dealType.getDiscount()));
    }

    /**
     * getDealType
     *
     * @param dealType type of DealType
     * @return dealType based on the request
     */
    private String getDealType(DealType dealType) {
        String result;
        switch (dealType) {
            case DISCOUNT_10_PERCENT -> result = DealType.DISCOUNT_10_PERCENT.getNameDeal();
            case DISCOUNT_20_PERCENT -> result = DealType.DISCOUNT_20_PERCENT.getNameDeal();
            case DISCOUNT_30_PERCENT -> result = DealType.DISCOUNT_30_PERCENT.getNameDeal();
            case DISCOUNT_40_PERCENT -> result = DealType.DISCOUNT_40_PERCENT.getNameDeal();
            case DISCOUNT_50_PERCENT -> result = DealType.DISCOUNT_50_PERCENT.getNameDeal();
            default -> result = "";
        }
        return result;
    }

    /**
     * setExpiredDate if the expiredDate is before the current date,
     * it will be throw exception the expired date is not valid
     *
     * @param deal    deal for product
     * @param request request to add deal
     */
    private void setExpiredDate(Deal deal, AddDealRequest request) {
        if (request.getExpiration().isBefore(LocalDateTime.now())) {
            throw new DateTimeInValidException();
        } else {
            deal.setExpiration(request.getExpiration());
        }
    }
}
