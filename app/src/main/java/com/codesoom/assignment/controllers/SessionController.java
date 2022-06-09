package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.dto.LoginResponseData;

/**
 * 사용자 인증(Authentication) 요청을 받아서 처리결과를 반환한다
 * <p>
 * All Known Implementing Classes:
 * JwtSessionController
 * </p>
 */
public interface SessionController {
    /**
     * LoginRequestData 형태로 사용자 인증 요청을 받아서 처리 결과를 LoginResponseData 형태로 반환한다
     * <p>
     * @param requestData Request Body로 전달된 사용자 정보를 직렬화하기 위한 객체
     * @return 사용자 인증 요청에 대한 처리결과를 역직렬화하기 위한 객체
     * </p>
     */
    LoginResponseData login(LoginRequestData requestData);
}
