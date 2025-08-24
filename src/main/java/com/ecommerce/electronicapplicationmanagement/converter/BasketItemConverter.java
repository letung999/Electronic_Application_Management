package com.ecommerce.electronicapplicationmanagement.converter;

import com.ecommerce.electronicapplicationmanagement.entity.BasketItem;
import com.ecommerce.electronicapplicationmanagement.request.BaseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BasketItemConverter {
    BasketItemConverter INSTANCE = Mappers.getMapper(BasketItemConverter.class);

    BasketItem fromBaseRequest(BaseRequest baseRequest);
}
