package com.ecommerce.electronicapplicationmanagement.converter;

import com.ecommerce.electronicapplicationmanagement.dto.ProductDto;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.request.AddProductRequest;
import com.ecommerce.electronicapplicationmanagement.request.BaseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductConverter {
    ProductConverter INSTANCE = Mappers.getMapper(ProductConverter.class);

    Product fromProductRequest(AddProductRequest addProductRequest);

    AddProductRequest fromProduct(Product product);

    List<ProductDto> fromListProduct(List<Product> products);

    List<Product> fromListProductDto(List<ProductDto> productDtoList);

    Product fromBaseRequest(BaseRequest baseRequest);
}
