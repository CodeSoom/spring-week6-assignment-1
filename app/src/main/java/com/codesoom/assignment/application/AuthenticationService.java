package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.UserLoginData;
import io.jsonwebtoken.Claims;

public interface AuthenticationService {
    /**
     * 유저 정보가 유효하면 토큰을 리턴한다.
     *
     * @param data 유저 정보
     * @return 토큰
     */
    String login(UserLoginData data);

    /**
     * 복호화한 토큰을 리턴한다.
     *
     * @param token 토큰
     * @return 토큰 정보
     */
    Claims decode(String token);
}
