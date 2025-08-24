package com.ecommerce.electronicapplicationmanagement.controller;

import com.ecommerce.electronicapplicationmanagement.entity.Basket;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.request.AddBasketRequest;
import com.ecommerce.electronicapplicationmanagement.response.Response;
import com.ecommerce.electronicapplicationmanagement.service.BasketService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "CustomerController")
public class CustomerController extends BaseController {
    private final BasketService basketService;

    @ApiResponse(
            responseCode = "201",
            description = "Basket created successfully",
            content = @Content(schema = @Schema(implementation = Basket.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid Basket request data",
            content = @Content(schema = @Schema(implementation = AddBasketRequest.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Product isn't found",
            content = @Content(schema = @Schema(implementation = AddBasketRequest.class))
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflict data",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @PostMapping("/basket")
    public ResponseEntity<Response> addProductToBasket(@RequestBody @Valid AddBasketRequest request) {
        var result = basketService.addToBasket(request);
        return responseStatusCreated(result);
    }
}
