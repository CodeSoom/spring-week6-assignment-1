package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 세션과 관련된 HTTP 요청을 처리합니다.
 */
@RestController
@RequestMapping("/session")
@CrossOrigin
@RequiredArgsConstructor
public class SessionController {

    private final AuthenticationService authenticationService;

    /**
     *  주어진 사용자 로그인 정보로 토큰을 응답합니다. // 여기서 '생성된 토큰' 이라는 표현은 토큰 생성을 담당하는게 아니라서 빼는게 좋은가..?
     * @param userLoginDetail 사용자 로그인 정보
     * @return 토큰 문자열
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void login(@RequestBody UserLoginDetail userLoginDetail) {
        return;
    }
}
