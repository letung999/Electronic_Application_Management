package com.ecommerce.electronicapplicationmanagement.converter;

import com.ecommerce.electronicapplicationmanagement.dto.DealDto;
import com.ecommerce.electronicapplicationmanagement.entity.Deal;
import com.ecommerce.electronicapplicationmanagement.request.AddDealRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DealConverter {
    DealConverter INSTANCE = Mappers.getMapper(DealConverter.class);

    DealDto fromDeal(Deal deal);

    Deal fromAddDealRequest(AddDealRequest request);

    List<DealDto> fromDealDto(List<Deal> deals);
}
