package com.ecommerce.electronicapplicationmanagement.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class AddDealRequest extends BaseRequest{
    @NotNull(message = "The productId can't null")
    private Long productId;

    private String dealType;

    @NotNull(message = "The expiration can't null")
    private LocalDateTime expiration;
}
