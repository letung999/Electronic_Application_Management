package com.ecommerce.electronicapplicationmanagement.exception;

import com.ecommerce.electronicapplicationmanagement.constant.MessageConstant;

public class ConflictDataException extends RuntimeException {
    public ConflictDataException(){
        super(MessageConstant.ERR_MSG_CONFLICT_DATA_EXCEPTION);
    }
}
