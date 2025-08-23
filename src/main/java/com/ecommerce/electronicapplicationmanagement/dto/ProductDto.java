package com.ecommerce.electronicapplicationmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private Long id;

    private String name;

    private String category;

    private BigDecimal price;

    private Integer stock;

    private Boolean available;
}
