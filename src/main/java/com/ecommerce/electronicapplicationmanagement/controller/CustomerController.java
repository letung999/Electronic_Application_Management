package com.ecommerce.electronicapplicationmanagement.controller;

import com.ecommerce.electronicapplicationmanagement.constant.PagingConstant;
import com.ecommerce.electronicapplicationmanagement.entity.Basket;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.request.AddBasketRequest;
import com.ecommerce.electronicapplicationmanagement.request.AddProductRequest;
import com.ecommerce.electronicapplicationmanagement.request.RemoveProductFromBasket;
import com.ecommerce.electronicapplicationmanagement.request.SearchProductRequest;
import com.ecommerce.electronicapplicationmanagement.response.Response;
import com.ecommerce.electronicapplicationmanagement.service.BasketService;
import com.ecommerce.electronicapplicationmanagement.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "CustomerController")
public class CustomerController extends BaseController {
    private final BasketService basketService;
    private final ProductService productService;

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

    @ApiResponse(
            responseCode = "400",
            description = "Invalid Basket request data",
            content = @Content(schema = @Schema(implementation = RemoveProductFromBasket.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Product isn't found",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Basket isn't found",
            content = @Content(schema = @Schema(implementation = Basket.class))
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflict data",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @DeleteMapping("/product/basket")
    public ResponseEntity<Response> removeProductFromBasket(@RequestBody @Valid RemoveProductFromBasket request) {
        var result = basketService.removeFromBasket(request);
        return responseStatusOK(result);
    }

    @ApiResponse(
            responseCode = "200",
            description = "Get product information successfully",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Product isn't found!",
            content = @Content(schema = @Schema(implementation = AddProductRequest.class))
    )
    @PostMapping("list")
    public ResponseEntity<Response> searchProduct(@RequestParam(defaultValue = PagingConstant.PAGE_DEFAULT) @Valid Integer page,
                                                  @RequestParam(defaultValue = PagingConstant.SIZE_DEFAULT) @Valid Integer size,
                                                  @RequestBody @Valid SearchProductRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        var result = productService.listFiltered(request, pageRequest);
        return responseStatusOK(result);
    }


    @ApiResponse(
            responseCode = "200",
            description = "Calculate information successfully",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @GetMapping("basket/receipt")
    public ResponseEntity<Response> calculateReceipt(@RequestParam() Long customerId) {
        var result = basketService.calculateReceipt(customerId);
        return responseStatusOK(result);
    }
}
