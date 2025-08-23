package com.ecommerce.electronicapplicationmanagement.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeleteProductRequest {
    private Long id;

    private Long version;
}
