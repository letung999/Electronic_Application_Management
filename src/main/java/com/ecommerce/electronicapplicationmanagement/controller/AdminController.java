package com.ecommerce.electronicapplicationmanagement.controller;

import com.ecommerce.electronicapplicationmanagement.constant.PagingConstant;
import com.ecommerce.electronicapplicationmanagement.converter.DealConverter;
import com.ecommerce.electronicapplicationmanagement.converter.ProductConverter;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.request.AddDealRequest;
import com.ecommerce.electronicapplicationmanagement.request.AddProductRequest;
import com.ecommerce.electronicapplicationmanagement.request.SearchProductRequest;
import com.ecommerce.electronicapplicationmanagement.response.Response;
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

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "AdminController")
public class AdminController extends BaseController {
    private final ProductService productService;

    @ApiResponse(
            responseCode = "201",
            description = "Product created successfully",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid Product request data",
            content = @Content(schema = @Schema(implementation = AddProductRequest.class))
    )
    @PostMapping("/product")
    public ResponseEntity<Response> createProduct(@RequestBody @Valid AddProductRequest request) {
        var result = productService.initProduct(request);
        return responseStatusCreated(ProductConverter.INSTANCE.fromProduct(result));
    }


    @ApiResponse(
            responseCode = "200",
            description = "Product removed successfully!",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Product has already removed, or isn't exist in database!",
            content = @Content(schema = @Schema(implementation = AddProductRequest.class))
    )
    @ApiResponse(
            responseCode = "409",
            description = "Data is conflict!",
            content = @Content(schema = @Schema(implementation = AddProductRequest.class))
    )
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Response> removeProduct(@PathVariable Long id) {
        var result = productService.removeProductById(id);
        return responseStatusOK(result);
    }


    @ApiResponse(
            responseCode = "201",
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
            responseCode = "201",
            description = "Add deal information successfully!",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Product isn't found!",
            content = @Content(schema = @Schema(implementation = AddProductRequest.class))
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflict data!",
            content = @Content(schema = @Schema(implementation = AddProductRequest.class))
    )
    @PostMapping("product/deal")
    public ResponseEntity<Response> addDeal(@RequestBody @Valid List<AddDealRequest> request) {
        var result = productService.addDealForProduct(request);
        return responseStatusCreated(DealConverter.INSTANCE.fromDealDto(result));
    }
}
