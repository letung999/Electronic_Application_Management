package com.ecommerce.electronicapplicationmanagement.converter;

import com.ecommerce.electronicapplicationmanagement.entity.Basket;
import com.ecommerce.electronicapplicationmanagement.request.BaseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BasketConverter {
    BasketConverter INSTANCE = Mappers.getMapper(BasketConverter.class);

    Basket fromBaseRequest(BaseRequest baseRequest);
}
