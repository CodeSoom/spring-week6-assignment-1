package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.JsonWebTokenResponse;
import com.codesoom.assignment.token.JsonWebTokenAttribute;
import com.codesoom.assignment.token.JsonWebTokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 회원 인증에 대한 HTTP 요청 처리
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    private final UserService userService;

    private final JsonWebTokenManager jsonWebTokenManager;

    public SessionController(UserService userService, JsonWebTokenManager jsonWebTokenManager) {
        this.userService = userService;
        this.jsonWebTokenManager = jsonWebTokenManager;
    }

    /**
     * 인증 토큰을 리턴합니다.
     * @param params 인증에 필요한 데이터
     * @return 인증 토큰 데이터
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JsonWebTokenResponse authentication(@RequestBody Map<String, String> params) {

        final User foundUser = userService.
                findUserByEmailAndPassword(params.get("email"), params.get("password"));

        JsonWebTokenAttribute jwtAttribute = JsonWebTokenAttribute.builder()
                .jwtId(String.valueOf(foundUser.getId()))
                .build();

        return new JsonWebTokenResponse(jsonWebTokenManager.createToken(jwtAttribute));
    }
}
