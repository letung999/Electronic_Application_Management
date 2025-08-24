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
     * @param customerId customerId
     * @return the information of receipt contains sub-amount
     * , item information, total amount after apply discount
     */
    ReceiptDto calculateReceipt(Long customerId);
}
