package com.codesoom.assignment.controller.session;

import com.codesoom.assignment.application.auth.LoginRequest;
import com.codesoom.assignment.application.auth.UserLoginService;
import com.codesoom.assignment.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/session")
@RestController
public class SessionController {

    private final UserLoginService service;

    public SessionController(UserLoginService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TokenResponse login(@RequestBody LoginRequestDto loginRequestDto) {
        return new TokenResponse(service.login(loginRequestDto));
    }

    /** 로그인 요청 정보 */
    static class LoginRequestDto implements LoginRequest {

        private String email;
        private String password;

        public LoginRequestDto() {
        }

        public LoginRequestDto(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getPassword() {
            return password;
        }

    }

}
