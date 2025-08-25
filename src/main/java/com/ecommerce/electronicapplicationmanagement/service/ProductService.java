package com.ecommerce.electronicapplicationmanagement.service;

import com.ecommerce.electronicapplicationmanagement.dto.ProductDto;
import com.ecommerce.electronicapplicationmanagement.entity.Deal;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.request.AddDealRequest;
import com.ecommerce.electronicapplicationmanagement.request.AddProductRequest;
import com.ecommerce.electronicapplicationmanagement.request.SearchProductRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product initProduct(AddProductRequest addProductRequest);

    Product removeProductById(Long id);

    List<ProductDto> listFiltered(SearchProductRequest searchProductRequest, Pageable pageable);

    Optional<Product> findById(Long id);

    Product updateStock(Product product, int delta);

    List<Deal> addDealForProduct(List<AddDealRequest> addDealRequest);
}
