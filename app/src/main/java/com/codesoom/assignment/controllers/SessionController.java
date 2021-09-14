// REST
// 1. Create -> 로그인 => Token
// 2. Read (w/Token) -> 세션 정보 -> 유효한가?, 내 정보
// 3. Update (w/Token) -> 토큰 재발급
// 4. Delete -> 로그아웃 => Token 무효화
// sessions (복수형) -> session (단수형) 사용.

package com.codesoom.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void login() {

    }
}
