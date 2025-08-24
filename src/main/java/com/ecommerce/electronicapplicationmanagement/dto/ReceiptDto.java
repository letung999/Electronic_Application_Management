package com.ecommerce.electronicapplicationmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ReceiptDto {
    private List<BasketItemDto> items = new ArrayList<>();
    private BigDecimal total;
}
