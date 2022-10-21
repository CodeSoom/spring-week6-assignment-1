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

    /**
     * 로그인 Dto 객체를 로그인 커맨드 객체로 변환한다.
     * @param request 로그인 Dto
     * @return 로그인 커맨드
     */
    AuthCommand.Login of(AuthDto.LoginParam request);

}
