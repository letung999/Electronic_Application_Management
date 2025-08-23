package com.ecommerce.electronicapplicationmanagement.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductRequest {
    private String name;

    private String category;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer stock;

    private Boolean available;
}
