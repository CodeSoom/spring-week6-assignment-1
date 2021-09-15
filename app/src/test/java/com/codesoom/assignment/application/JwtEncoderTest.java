package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.codesoom.assignment.domain.User;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

public class JwtEncoderTest {

    private static final String JWT_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$";

    @Test
    void encode() {
        String secret = "12345678901234567890123456789012";
        JwtEncoder jwtEncoder = new JwtEncoder(secret);

        User user = User.builder()
            .id(1L)
            .build();
        String accessToken = jwtEncoder.encode(user)
            .getAccessToken();

        assertThat(Pattern.matches(JWT_REGEX, accessToken))
            .isTrue();
    }
}
