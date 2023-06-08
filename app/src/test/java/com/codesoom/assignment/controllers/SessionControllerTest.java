package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthorizationService;
import com.codesoom.assignment.dto.LoginData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthorizationService authorizationService;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();

		given(authorizationService.login(any())).willReturn("a.b.c");
	}

	@Test
	public void login() throws Exception {
		String userId = "jinny";
		String password = "1234";
		LoginData loginData = new LoginData(userId, password);

		mockMvc.perform(post("/session")
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginData))
				).andExpect(status().isOk())
				.andExpect(content().string(containsString(".")));
	}

}
