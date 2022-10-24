package com.codesoom.assignment.presentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 요청 header 에 있는 Access Token 을 검증한다.
 * 핸들러 메서드의 파라미터에 붙이고 파라미터의 타입은 {@link com.codesoom.assignment.domain.User} 로 한다.
 *
 * @see AccessTokenValidationArgumentResolver
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AccessTokenValidation {
}
