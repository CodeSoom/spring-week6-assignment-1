package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인사를 요청한다.
 */
@RestController
public class HelloController {

    /**
     * 인사를 요청하면 인사말을 반환한다.
     * @return "Hello, world!"
     */
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
