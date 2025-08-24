package com.ecommerce.electronicapplicationmanagement.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @JsonIgnore
    private LocalDateTime updatedDate = LocalDateTime.now();

    @Builder.Default
    @JsonIgnore
    private String createdBy = "Admin";

    @Builder.Default
    @JsonIgnore
    private String updatedBy = "Admin";

    @Builder.Default
    @JsonIgnore
    private Long version = 0L;

    @Builder.Default
    @JsonIgnore
    private Short logicalDeleteFlag = 0;
}
