package com.ecommerce.electronicapplicationmanagement.service;

import com.ecommerce.electronicapplicationmanagement.dto.ReceiptDto;
import com.ecommerce.electronicapplicationmanagement.entity.Basket;
import com.ecommerce.electronicapplicationmanagement.request.AddBasketRequest;
import com.ecommerce.electronicapplicationmanagement.request.RemoveProductFromBasket;

public interface BasketService {
    /**
     * add to basket function
     *
     * @param request add basket request
     * @return the basket just have added
     */
    Basket addToBasket(AddBasketRequest request);

    /**
     * remove from basket
     *
     * @param request remove product from basket request
     * @return the basket just have removed
     */
    Basket removeFromBasket(RemoveProductFromBasket request);

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
