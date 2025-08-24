package com.ecommerce.electronicapplicationmanagement.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class BasketItemDto {
    private Long productId;
    private int quantity;
    private BigDecimal subtotal;
}
