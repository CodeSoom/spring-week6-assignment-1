package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * /session URL에 대한 HTTP 요청을 처리하는 Controller 클래스
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 토큰이 담긴 DTO를 반환한다.
     *
     * @return 토큰이 담긴 DTO
     */
    @PostMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@PathVariable Long userId) {
        String token = authenticationService.login(userId);

        return SessionResponseData.builder()
                .token(token)
                .build();
    }
}
