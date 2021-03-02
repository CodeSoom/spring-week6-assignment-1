package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginDto;
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
    public String login(UserLoginDto userLoginDto){
        //TODO 존재하는 회원인지 확인
        //TODO 존재하지 않으면 에러, 존재하면 JWT
        return "";
    }
}
