package com.codesoom.assignment.common.mapper;

import com.codesoom.assignment.application.product.ProductCommand;
import com.codesoom.assignment.dto.ProductDto;
import com.codesoom.assignment.domain.product.Product;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductCommand.Register of(ProductDto.RequestParam request);

    ProductCommand.UpdateRequest of(Long id, ProductDto.RequestParam request);

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductCommand.Register command);

    Product toEntity(ProductCommand.UpdateRequest command);
}
