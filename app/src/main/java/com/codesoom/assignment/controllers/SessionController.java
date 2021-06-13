package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.errors.UserPasswordWrongException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원의 로그인에 대한 http 요청을 처리합니다.
 */

@RestController
@CrossOrigin
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;
    private UserService userService;

    public SessionController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * 회원정보를 인증, 로그인 상태를 확인합니다.
     *
     * @param user 접속하려는 회원정보
     * @return 접속한 회원정보의 토큰
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody User user) {
        User checkUser = userService.findUserByEmail(user.getEmail());
        if(!user.getPassword().equals(checkUser.getPassword())) {
            throw new UserPasswordWrongException();
        }
        Long id = checkUser.getId();

        String accessToken = authenticationService.login(id);
        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
