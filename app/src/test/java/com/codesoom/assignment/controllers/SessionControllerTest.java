package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserService userService;
    @Autowired
    JwtUtil jwtUtil;

    private static String VALID_TOKEN = "";
    @BeforeEach
    void setUp(){
        System.out.println("== set up ==");
        if(!userService.checkExistUserByEmail("kiheo@gmail.com")){
            userService.registerUser(UserRegistrationData.builder()
                    .email("kiheo@gmail.com")
                    .name("kiheo")
                    .password("1234")
                    .build());
            VALID_TOKEN = authenticationService.login(userService.findActiveUserByEmail("kiheo@gmail.com"));
        }
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(post("/session")
                        .content("{\"email\":\"kiheo@gmail.com\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));
    }
    //@Test
    void encode(){
        User user = new User();
        user.builder().id(1l);
        String accessToken = authenticationService.login(user);
        assertThat(accessToken).contains(".");
    }

    //@Test
    void decode(){
        jwtUtil.decode(VALID_TOKEN);
    }
}
