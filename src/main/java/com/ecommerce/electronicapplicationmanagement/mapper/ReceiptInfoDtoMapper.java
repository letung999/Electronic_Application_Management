package com.ecommerce.electronicapplicationmanagement.mapper;

import java.math.BigDecimal;


/**
 * the interface mapper receipt
 */
public interface ReceiptInfoDtoMapper {
    Long getCustomerId();

    BigDecimal getPrice();

    Integer getQuantity();

    BigDecimal getTotalPrice();
}

