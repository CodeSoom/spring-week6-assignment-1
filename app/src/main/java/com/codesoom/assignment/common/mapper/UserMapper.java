package com.codesoom.assignment.common.mapper;

import com.codesoom.assignment.application.user.UserCommand;
import com.codesoom.assignment.dto.UserDto;
import com.codesoom.assignment.domain.user.User;
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
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserCommand.Register of(UserDto.RequestParam request);

    UserCommand.UpdateRequest of(Long id, UserDto.UpdateParam request);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserCommand.Register command);

    User toEntity(UserCommand.UpdateRequest command);
}
