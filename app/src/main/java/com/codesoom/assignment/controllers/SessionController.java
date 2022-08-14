// REST
// 1. Create -> 로그인 => Token
// 2. Read (w/Token) -> 세션 정보 -> 유효한가?, 내 정보
// 3. Update (w/Token) -> 토큰 재발급
// 4. Delete -> 로그아웃 => 토큰 무효화
// sessions (복수형) -> session (단수형) 사용.

package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.dto.SessionRegistrationData;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.errors.SessionValidationException;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(
            @RequestBody SessionRegistrationData registrationData
    ) {
        String accessToken = authenticationService.login(registrationData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SessionValidationException.class)
    public ErrorResponse handleSessionValidationException(SessionValidationException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
