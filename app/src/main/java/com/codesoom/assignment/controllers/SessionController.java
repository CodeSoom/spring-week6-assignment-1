package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * ACCESS TOKEN을 발급하거나 검증하여 처리 결과를 반환한다.
 */
@RestController
@RequestMapping("session")
public class SessionController {
    private final AuthenticationService authenticationService;

    private final UserService userService;

    public SessionController(AuthenticationService authenticationService,
                             UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * 로그인 데이터를 처리하고 회원이라면 토큰을 발급합니다.
     * @param loginData 토큰을 발급받기 위한 로그인 데이터
     * @return SessionResponseData 토큰은 담고있는 응답 객체
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody @Valid LoginData loginData) {
        User user = userService.findUserByEmailPassword(loginData);

        String accessToken = authenticationService.login(user.getId());

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
