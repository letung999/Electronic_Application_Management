package com.ecommerce.electronicapplicationmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DealDto {
    private String dealType;
    private LocalDateTime expiration;
    private BigDecimal discountValue;
}
