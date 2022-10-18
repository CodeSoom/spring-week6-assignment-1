package com.codesoom.assignment.common.mapper;

import com.codesoom.assignment.application.AuthCommand;
import com.codesoom.assignment.dto.AuthDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    AuthCommand.Login of(AuthDto.LoginParam request);

}
