package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * / URL에 대한 HTTP 요청을 처리하는 Controller 클래스
 */
@RestController
public class HelloController {
    /**
     * Hello, world!라는 문자열을 반환한다.
     *
     * @return Hello, world! 문자열
     */
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
