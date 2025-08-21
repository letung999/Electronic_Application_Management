package com.ecommerce.electronicapplicationmanagement.exception;

import com.ecommerce.electronicapplicationmanagement.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                errors.put(fieldName, message != null ? message : "Invalid value");
            } else {
                String objectName = error.getObjectName();
                String message = error.getDefaultMessage();
                errors.put(objectName, message != null ? message : "Validation failed");
            }
        });

        ErrorResponse response = ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .status(HttpStatus.BAD_REQUEST.toString())
                .localDateTime(LocalDateTime.now())
                .error(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourcesNotFoundException(ResourcesNotFoundException ex,
                                                                          WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .statusCode(HttpStatusCode.valueOf(400).toString())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictDataException.class)
    public ResponseEntity<ErrorResponse> handleConflictDataException(ConflictDataException ex,
                                                                     WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .statusCode(HttpStatusCode.valueOf(400).toString())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}