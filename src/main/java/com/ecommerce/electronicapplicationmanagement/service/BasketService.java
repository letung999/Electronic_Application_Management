package com.ecommerce.electronicapplicationmanagement.service;

import com.ecommerce.electronicapplicationmanagement.dto.ReceiptDto;
import com.ecommerce.electronicapplicationmanagement.entity.Basket;
import com.ecommerce.electronicapplicationmanagement.request.AddBasketRequest;

public interface BasketService {
    /**
     * add to basket funtion
     *
     * @param request add basket request
     * @return the basket just have added
     */
    Basket addToBasket(AddBasketRequest request);

    /**
     * @param customerId
     * @param productId
     * @param quantity
     */
    void removeFromBasket(Long customerId, Long productId, int quantity);

    /**
     * @param customerId
     * @return
     */
    ReceiptDto calculateReceipt(Long customerId);

    /**
     * @param customerId
     * @return
     */

    Basket getOrCreateBasket(Long customerId);
}
