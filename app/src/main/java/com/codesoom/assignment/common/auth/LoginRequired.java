package com.codesoom.assignment.common.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 리소스 요청 시 로그인 인증이 필요한 경우에 사용되는 어노테이션입니다.
 * 비로그인 상태에서 리소스 요청을 허용하려면 allowGuest를 true로 설정해주세요.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
    boolean allowGuest() default false;
}
