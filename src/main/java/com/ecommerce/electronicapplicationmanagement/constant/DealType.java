package com.ecommerce.electronicapplicationmanagement.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DealType {
    DISCOUNT_10_PERCENT("DISCOUNT_10_PERCENT", 0.1),
    DISCOUNT_20_PERCENT("DISCOUNT_20_PERCENT", 0.2),
    DISCOUNT_30_PERCENT("DISCOUNT_30_PERCENT", 0.3),
    DISCOUNT_40_PERCENT("DISCOUNT_40_PERCENT", 0.4),
    DISCOUNT_50_PERCENT("DISCOUNT_50_PERCENT", 0.5);
    private final String nameDeal;
    private final Double discount;
}
