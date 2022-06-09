package com.codesoom.assignment.controllers;

import com.codesoom.assignment.Utf8WebTest;
import com.codesoom.assignment.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

//@WebMvcTest(SessionController.class)
//@Utf8WebTest
//@DisplayName("SessionController 클래스")
//class SessionControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthenticationService authenticationService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    private ObjectMapper objectMapper;
//
//    private final SessionRequestData sessionRequestData;
//
//    @BeforeEach
//    void setUp() {
//        sessionRequestData.builer()
//                .email("choyumin01@gmail.com")
//                .password("1234!@#$")
//                .build();
//    }
//
//    @Nested
//    @DisplayName("[POST] /session")
//    class Describe_post {
//
//        @Nested
//        @DisplayName("등록된 user 의 sessionRequestData 가 주어지면")
//        class Context_with_valid_sessionRequestData {
//
//            @BeforeEach
//            void setUp() {
//                User user = User.builder()
//                        .email(sessionRequestData.getEmail())
//                        .password(sessionRequestData.getPassword())
//                        .build();
//
//
//            }
//        }
//    }
//}