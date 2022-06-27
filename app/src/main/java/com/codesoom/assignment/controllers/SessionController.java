package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 세션과 관련된 HTTP 요청을 담당한다.
 */
@RestController
@RequestMapping("session")
@CrossOrigin
@RequiredArgsConstructor
public class SessionController {

    private final AuthenticationService authenticationService;

    /**
     * 주어진 사용자 로그인 정보에 해당하는 액세스 토큰을 응답한다.
     *
     * @param userLoginData 사용자 로그인 정보
     * @return 생성한 액세스 토큰
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody UserLoginData userLoginData) {
        String accessToken = authenticationService.login(userLoginData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
