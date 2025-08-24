package com.ecommerce.electronicapplicationmanagement.exception;

import com.ecommerce.electronicapplicationmanagement.constant.MessageConstant;
import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {
    private final String name;

    public InsufficientStockException(String name) {
        super(String.format(MessageConstant.ERR_MSG_INSUFFICIENT_FOR_PRODUCT + "%s", name));
        this.name = name;
    }
}
