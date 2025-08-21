package com.ecommerce.electronicapplicationmanagement.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@Builder
public class ErrorInvalidArgumentResponse {
    private LocalDateTime localDateTime;
    private String path;
    private String status;
    private Map<String, String> error;
}
