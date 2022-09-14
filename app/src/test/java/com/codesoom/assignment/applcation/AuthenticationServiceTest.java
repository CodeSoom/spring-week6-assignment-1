package com.codesoom.assignment.applcation;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {

    private AuthenticationService service;
    private UserService userService;
    private final String SECRET = "12345678901234567890123456789012";

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        userService = mock(UserService.class);
        service = new AuthenticationService(jwtUtil, userService);
    }

    @Nested
    @DisplayName("")
    class Describe_{

        @Nested
        @DisplayName("")
        class Context_{

            @Test
            @DisplayName("")
            void It_(){
                String accessToken = service.login();

                assertThat(accessToken).contains(".xxxx");
            }
        }
    }
}
