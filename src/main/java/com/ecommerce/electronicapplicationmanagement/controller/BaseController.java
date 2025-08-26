package com.ecommerce.electronicapplicationmanagement.controller;

import com.ecommerce.electronicapplicationmanagement.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * the abstract class BaseController to return the common data for rest api
 */
public abstract class BaseController {
    /**
     * Returns response status OK with data.
     *
     * @param <T>          the type parameter
     * @param responseData the response data
     * @return the response entity
     */
    public static <T> ResponseEntity<Response> responseStatusOK(T responseData) {
        return responseStatus(HttpStatus.OK, responseData);
    }

    /**
     * Returns response status Created with data.
     *
     * @param <T>          the type parameter
     * @param responseData the response data
     * @return the response entity
     */
    protected static <T> ResponseEntity<Response> responseStatusCreated(T responseData) {
        return responseStatus(HttpStatus.CREATED, responseData);
    }

    /**
     * Returns with a response status with data.
     *
     * @param <T>          the type parameter
     * @param status       the status
     * @param responseData the response data
     * @return the response entity
     */
    protected static <T> ResponseEntity<Response> responseStatus(HttpStatus status, T responseData) {
        Response domainResponse = Response.builder()
                .success(true)
                .data(responseData)
                .build();
        return ResponseEntity.status(status).body(domainResponse);
    }
}
