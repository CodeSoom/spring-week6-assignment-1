package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

public class TestUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String JWT_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";

    public static <T> T content(ResultActions actions, Class<T> type)
            throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult mvcResult = actions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(content, type);
    }

    public static boolean isJwt(String token) {
        return token.matches(JWT_REGEX);
    }
}
