package com.codesoom.assignment.jwt;

import com.codesoom.assignment.errors.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("JsonWebToken 클래스")
class JsonWebTokenTest {

    @Nested
    @DisplayName("generate() 메소드는")
    class Describe_generate {

        @Nested
        @DisplayName("JwtContents 객체를 매개변수로 받아")
        class Context_generate_with_JwtContents {
            JwtContents jwtContents;

            @BeforeEach
            void setUp() {
                jwtContents = new JwtContents("Dummy issuer", "Dummy subject", "Dummy audience");
            }

            @Test
            @DisplayName("Json Web Token을 생성해 반환한다.")
            void it_should_return_JsonWebToken() {
                String jwt = JsonWebToken.generate(jwtContents);

                assertThat(jwt).isNotNull();
                assertThat(jwt.split("\\.")).hasSize(3);
            }
        }
    }

    @Nested
    @DisplayName("verify() 메소드는")
    class Describe_verify {

        @Nested
        @DisplayName("유효한 JWT를 매개변수로 받으면")
        class Context_valid_jwt {
            String jwt;

            String DUMMY_ISSUER = "Dummy issuer";
            String DUMMY_SUBJECT = "Dummy subject";
            String DUMMY_AUDIENCE = "Dummy audience";

            @BeforeEach
            void setUp() {
                JwtContents jwtContents = new JwtContents(DUMMY_ISSUER, DUMMY_SUBJECT, DUMMY_AUDIENCE);
                jwt = JsonWebToken.generate(jwtContents);
            }

            @Test
            @DisplayName("JwtContents 객체를 반환한다.")
            void it_should_return_JwtContents() {
                JwtContents jwtContents = JsonWebToken.verify(jwt);

                assertThat(jwtContents.getIss()).isEqualTo(DUMMY_ISSUER);
                assertThat(jwtContents.getSub()).isEqualTo(DUMMY_SUBJECT);
                assertThat(jwtContents.getAud()).isEqualTo(DUMMY_AUDIENCE);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 JWT를 매개변수로 받으면")
        class Context_invalid_jwt {
            String jwt;

            @BeforeEach
            void setUp() {
                jwt = "invalid jwt";
            }

            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            void it_should_throw_IllegalArgumentException() {
                assertThatThrownBy(() -> JsonWebToken.verify(jwt))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }

}