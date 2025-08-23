package com.ecommerce.electronicapplicationmanagement.exception;

import com.ecommerce.electronicapplicationmanagement.constant.MessageConstant;

public class DateTimeInValidException extends RuntimeException{
    public DateTimeInValidException(){
        super(MessageConstant.ERR_MSG_DATE_TIME_IS_INVALID);
    }
}
