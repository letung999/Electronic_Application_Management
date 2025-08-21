package com.ecommerce.electronicapplicationmanagement.exception;


import lombok.Getter;
import lombok.Setter;

import static com.ecommerce.electronicapplicationmanagement.constant.MessageConstant.ERR_MSG_DATA_NOT_FOUND;

@Getter
@Setter
public class ResourcesNotFoundException extends RuntimeException {
    private String fieldName;
    private String value;

    public ResourcesNotFoundException(String fieldName, String value) {
        super(String.format("%s: " + ERR_MSG_DATA_NOT_FOUND + " %s: ", fieldName, value));
        this.fieldName = fieldName;
        this.value = value;
    }
}
