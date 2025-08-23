package com.ecommerce.electronicapplicationmanagement.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequest {
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedDate = LocalDateTime.now();

    @Builder.Default
    private String createdBy = "Admin";

    @Builder.Default
    private String updatedBy = "Admin";

    @Builder.Default
    private Long version = 0L;

    @Builder.Default
    private Short logicalDeleteFlag = 0;
}
