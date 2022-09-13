package com.codesoom.assignment.applcation;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AuthenticationServiceTest {

    private AuthenticationService service;
    private final String SECRET = "12345678901234567890123456789012";

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        service = new AuthenticationService(jwtUtil);
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
