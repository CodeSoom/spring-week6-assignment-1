package com.codesoom.assignment.applcation;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.utils.JwtUtil;
import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {

    private AuthenticationService service;
    private UserService userService;
    private Mapper mapper;
    private final String SECRET = "12345678901234567890123456789010";

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        userService = mock(UserService.class);
        mapper = mock(Mapper.class);
        service = new AuthenticationService(jwtUtil, userService , mapper);
    }

    @Nested
    @DisplayName("login()")
    class Describe_Login{

        @Nested
        @DisplayName("파라미터에 해당하는 사용자가 존재한다면")
        class Context_{

            private final Long userId = 1L;

            @Test
            @DisplayName("")
            void It_(){
                fail("작성 필요");
            }
        }
    }
}
