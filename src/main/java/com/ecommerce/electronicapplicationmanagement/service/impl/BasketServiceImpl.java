package com.ecommerce.electronicapplicationmanagement.service.impl;

import com.ecommerce.electronicapplicationmanagement.converter.BasketConverter;
import com.ecommerce.electronicapplicationmanagement.converter.BasketItemConverter;
import com.ecommerce.electronicapplicationmanagement.dto.ReceiptDto;
import com.ecommerce.electronicapplicationmanagement.entity.Basket;
import com.ecommerce.electronicapplicationmanagement.entity.BasketItem;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.exception.InsufficientStockException;
import com.ecommerce.electronicapplicationmanagement.exception.ResourcesNotFoundException;
import com.ecommerce.electronicapplicationmanagement.repository.BasketItemRepository;
import com.ecommerce.electronicapplicationmanagement.repository.BasketRepository;
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

@Service
@AllArgsConstructor
@Slf4j
public class BasketServiceImpl implements BasketService {
    private final ProductService productService;
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final ProductRepository productRepository;

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
        return null;
    }

    @Override
    public Basket getOrCreateBasket(Long customerId) {
        return null;
    }
}
