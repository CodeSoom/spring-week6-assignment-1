package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.LoginNotMatchPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * session과 관련된 HTTP 요청을 처리합니다.
 */
@CrossOrigin
@RestController
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 사용자를 로그인 처리하고, 세션 응답 데이터를 리턴합니다.
     *
     * @return 세션 응답 데이터
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody @Valid UserLoginData userLoginData) {
        String accessToken = authenticationService.login(userLoginData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginNotMatchPasswordException.class)
    ErrorResponse handleLoginNotMatchPasswordException(LoginNotMatchPasswordException e) {
        return new ErrorResponse(e.getMessage());
    }
}
