package com.ecommerce.electronicapplicationmanagement.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBasketRequest {
    @NotNull(message = "The customerId can't null")
    private Long customerId;

    @NotNull(message = "The productId can't null")
    private Long productId;

    @Min(value = 1, message = "The quantity can't less than one product")
    private Integer quantity;
}
