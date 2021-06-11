package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 자원에 접근할 수 있는 권한에 대한 요청을 처리합니다.
 */
@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {

    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 권한을 부여받기 위한 요청을 처리합니다.
     *
     * @param loginData 인증을 위한 로그인 정보
     * @return 권한을 부여하는 토큰을 포함한 응답
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(
            @RequestBody @Valid LoginData loginData
    ) {
        String accessToken = authenticationService.login(loginData);
        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
