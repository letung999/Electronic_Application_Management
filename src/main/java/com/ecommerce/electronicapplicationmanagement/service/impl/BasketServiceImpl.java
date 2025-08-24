package com.ecommerce.electronicapplicationmanagement.service.impl;

import com.ecommerce.electronicapplicationmanagement.converter.BasketConverter;
import com.ecommerce.electronicapplicationmanagement.converter.BasketItemConverter;
import com.ecommerce.electronicapplicationmanagement.dto.ReceiptDto;
import com.ecommerce.electronicapplicationmanagement.entity.Basket;
import com.ecommerce.electronicapplicationmanagement.entity.BasketItem;
import com.ecommerce.electronicapplicationmanagement.entity.Deal;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.exception.ConflictDataException;
import com.ecommerce.electronicapplicationmanagement.exception.InsufficientStockException;
import com.ecommerce.electronicapplicationmanagement.exception.ResourcesNotFoundException;
import com.ecommerce.electronicapplicationmanagement.repository.BasketItemRepository;
import com.ecommerce.electronicapplicationmanagement.repository.BasketRepository;
import com.ecommerce.electronicapplicationmanagement.repository.ProductRepository;
import com.ecommerce.electronicapplicationmanagement.request.AddBasketRequest;
import com.ecommerce.electronicapplicationmanagement.request.AddDealRequest;
import com.ecommerce.electronicapplicationmanagement.request.BaseRequest;
import com.ecommerce.electronicapplicationmanagement.service.BasketService;
import com.ecommerce.electronicapplicationmanagement.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BasketServiceImpl implements BasketService {
    private final ProductService productService;
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final ProductRepository productRepository;

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




    @Override
    public void removeFromBasket(Long customerId, Long productId, int quantity) {

    }

    @Override
    public ReceiptDto calculateReceipt(Long customerId) {
        return null;
    }

    @Override
    public Basket getOrCreateBasket(Long customerId) {
        return null;
    }
}
