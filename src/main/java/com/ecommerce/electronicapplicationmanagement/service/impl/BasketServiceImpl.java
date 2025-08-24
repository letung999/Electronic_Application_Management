package com.ecommerce.electronicapplicationmanagement.service.impl;

import com.ecommerce.electronicapplicationmanagement.converter.BasketConverter;
import com.ecommerce.electronicapplicationmanagement.converter.BasketItemConverter;
import com.ecommerce.electronicapplicationmanagement.dto.BasketItemDto;
import com.ecommerce.electronicapplicationmanagement.dto.ReceiptDto;
import com.ecommerce.electronicapplicationmanagement.entity.Basket;
import com.ecommerce.electronicapplicationmanagement.entity.BasketItem;
import com.ecommerce.electronicapplicationmanagement.entity.Deal;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.exception.InsufficientStockException;
import com.ecommerce.electronicapplicationmanagement.exception.ResourcesNotFoundException;
import com.ecommerce.electronicapplicationmanagement.repository.BasketItemRepository;
import com.ecommerce.electronicapplicationmanagement.repository.BasketRepository;
import com.ecommerce.electronicapplicationmanagement.repository.DealRepository;
import com.ecommerce.electronicapplicationmanagement.repository.ProductRepository;
import com.ecommerce.electronicapplicationmanagement.request.AddBasketRequest;
import com.ecommerce.electronicapplicationmanagement.request.BaseRequest;
import com.ecommerce.electronicapplicationmanagement.request.RemoveProductFromBasket;
import com.ecommerce.electronicapplicationmanagement.service.BasketService;
import com.ecommerce.electronicapplicationmanagement.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BasketServiceImpl implements BasketService {
    private final ProductService productService;
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final ProductRepository productRepository;
    private final DealRepository dealRepository;

    @Transactional
    @Override
    public Basket addToBasket(AddBasketRequest request) {
        log.info("Adding product ID: {} with quantity: {} to basket for customer ID: {}",
                request.getProductId(),
                request.getQuantity(),
                request.getCustomerId());

        Product product = productRepository.findProductById(request.getProductId())
                .orElseThrow(
                        () -> new ResourcesNotFoundException("productID", String.valueOf(request.getProductId())));
        if (product.getStock() < request.getQuantity()) {
            log.error("Insufficient stock for product ID: {}. Available: {}, requested: {}",
                    request.getProductId(),
                    product.getStock(),
                    request.getQuantity());
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }

        Basket basket = basketRepository.findByCustomerId(request.getCustomerId())
                .orElseGet(() -> {
                    log.info("Creating new basket for customer ID: {}", request.getCustomerId());
                    BaseRequest baseRequest = new BaseRequest();
                    Basket newBasket = BasketConverter.INSTANCE.fromBaseRequest(baseRequest);
                    newBasket.setCustomerId(request.getCustomerId());
                    return basketRepository.save(newBasket);
                });

        BasketItem item = basketItemRepository.findByBasketIdAndProductId(basket.getId(),
                        request.getProductId())
                .orElseGet(() -> {
                    log.info("Creating new basket item for product ID: {} in basket ID: {}",
                            request.getProductId(),
                            basket.getId());
                    BaseRequest baseRequest = new BaseRequest();
                    BasketItem newItem = BasketItemConverter.INSTANCE.fromBaseRequest(baseRequest);
                    newItem.setBasket(basket);
                    newItem.setProduct(product);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + request.getQuantity());
        basketItemRepository.save(item);
        productService.updateStock(product, -request.getQuantity());

        log.info("Added product ID: {} to basket ID: {} with total quantity: {}",
                request.getProductId(),
                basket.getId(),
                item.getQuantity());
        return basket;
    }


    @Transactional
    @Override
    public Basket removeFromBasket(RemoveProductFromBasket request) {
        log.info("Removing product ID: {} from basket for customer ID: {}, quantity: {}",
                request.getProductId(), request.getCustomerId(), request.getQuantity());
        // Find product is active
        Product product = productRepository.findProductById(request.getProductId())
                .orElseThrow(
                        () -> new ResourcesNotFoundException("productID", String.valueOf(request.getProductId())));

        // Find active basket
        Basket basket = basketRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Basket isn't found for customer ID: " + request.getCustomerId()));

        // Find active BasketItem
        BasketItem item = basketItemRepository.findByBasketIdAndProductId(basket.getId(), request.getProductId())
                .orElseThrow(() -> new ResourcesNotFoundException(String.valueOf(request.getProductId())));

        // Calculate quantity to remove
        int quantityToRemove = (request.getQuantity() < item.getQuantity())
                ? request.getQuantity()
                : item.getQuantity();

        // Update BasketItem
        if (request.getQuantity() < item.getQuantity()) {
            item.setQuantity(item.getQuantity() - quantityToRemove);
            log.info("Reduced quantity of product ID: {} in basket ID: {} by {}, new quantity: {}",
                    request.getProductId(), basket.getId(), quantityToRemove, item.getQuantity());
        } else {
            item.setLogicalDeleteFlag((short) 0);
            log.info("Removed product ID: {} from basket ID: {}", request.getProductId(), basket.getId());
        }
        basketItemRepository.save(item);

        // Restore stock
        productService.updateStock(product, quantityToRemove);

        log.info("Removed {} units of product ID: {} from basket ID: {}", quantityToRemove, request.getProductId(), basket.getId());
        return basket;
    }

    @Override
    public ReceiptDto calculateReceipt(Long customerId) {
        // Find active basket
        log.info("get basket for customer ID: {}", customerId);
        Basket basket = basketRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Basket isn't found for customer ID: " + customerId));

        List<BasketItem> basketItemList = basket.getItems();
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<BasketItemDto> basketItemDtoList = new ArrayList<>();

        if (!basketItemList.isEmpty()) {
            // find deals for productIds
            var productIds = basketItemList.stream().map(x -> x.getProduct().getId()).toList();
            List<Deal> deals = dealRepository.findByProductAndExpirationAfter(productIds, LocalDateTime.now());
            Map<Long, BigDecimal> discountMap = new HashMap<>();

            if (!deals.isEmpty()) {
                discountMap = deals.stream()
                        .collect(Collectors.toMap(
                                deal -> deal.getProduct().getId(),
                                Deal::getDiscountValue));
            }
            for (BasketItem basketItem : basketItemList) {
                BasketItemDto basketItemDto = new BasketItemDto();
                BigDecimal itemPrice = calculateProductInBasket(basketItem.getProduct(), basketItem.getQuantity(), discountMap);
                totalPrice = totalPrice.add(itemPrice);

                basketItemDto.setProductId(basketItem.getProduct().getId());
                basketItemDto.setQuantity(basketItemDto.getQuantity());
                basketItemDto.setSubtotal(itemPrice);
                basketItemDtoList.add(basketItemDto);
            }
        }
        return ReceiptDto.builder()
                .total(totalPrice)
                .items(basketItemDtoList)
                .build();
    }

    /**
     * calculateProductInBasket
     * @param product product
     * @param quantity quantity
     * @param discountMap map of productId pair discount value
     * @return total of product after apply the discount
     */
    private BigDecimal calculateProductInBasket(Product product, Integer quantity, Map<Long, BigDecimal> discountMap) {
        BigDecimal totalDiscountOfProduct = BigDecimal.ZERO;
        if (!discountMap.isEmpty()) {
            totalDiscountOfProduct = discountMap.entrySet().stream()
                    .filter(x -> x.getKey().equals(product.getId()))
                    .map(Map.Entry::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return product.getPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .subtract(totalDiscountOfProduct);
    }

    @Override
    public Basket getOrCreateBasket(Long customerId) {
        return null;
    }
}
