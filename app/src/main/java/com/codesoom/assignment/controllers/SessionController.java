package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.AccessToken;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {
    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 회원 인증을 마치고 엑세스 토큰을 반환한다
     *
     * @param userLoginData 회원 인증을 진행할 user
     * @return 생성된 엑세스 토큰
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody UserLoginData userLoginData) {
        SessionResponseData sessionResponseData = authenticationService.login(userLoginData);
        return sessionResponseData;
    }
}
