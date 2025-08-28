package com.ecommerce.electronicapplicationmanagement.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest extends BaseRequest {
    @NotBlank(message = "The name product is required")
    @Size(min = 3, max = 255, message = "Product name must be between 3 and 255 characters")
    private String name;

    @NotBlank(message = "The category product is required")
    private String category;

    @Min(value = 0, message = "The price can't less than zero")
    private BigDecimal price;

    @Min(value = 1, message = "The stock can't less than one stock")
    private int stock;

    @Builder.Default
    private boolean available = true;
}
