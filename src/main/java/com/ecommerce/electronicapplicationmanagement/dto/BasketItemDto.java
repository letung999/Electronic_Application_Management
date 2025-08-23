package com.ecommerce.electronicapplicationmanagement.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BasketItemDto {
    private Long productId;
    private int quantity;
    private BigDecimal subtotal;

    public BasketItemDto(Long productId, int quantity, BigDecimal subtotal) {
        this.productId = productId;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
}
