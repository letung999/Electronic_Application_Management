package com.ecommerce.electronicapplicationmanagement.service;

import com.ecommerce.electronicapplicationmanagement.dto.ReceiptDto;
import com.ecommerce.electronicapplicationmanagement.entity.Basket;

public interface BasketService {
    /**
     *
     * @param customerId
     * @param productId
     * @param quantity
     */
    void addToBasket(Long customerId, Long productId, int quantity);

    /**
     *
     * @param customerId
     * @param productId
     * @param quantity
     */
    void removeFromBasket(Long customerId, Long productId, int quantity);

    /**
     *
     * @param customerId
     * @return
     */
    ReceiptDto calculateReceipt(Long customerId);

    /**
     *
     * @param customerId
     * @return
     */

    Basket getOrCreateBasket(Long customerId);
}
