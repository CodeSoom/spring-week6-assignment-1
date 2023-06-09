package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthorizationService;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginSuccessData;
import com.codesoom.assignment.errors.LoginFailException;
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

	LoginData LOGIN_DATA = new LoginData("jinny", "1234");
	LoginData INVALID_LOGIN_DATA = new LoginData("jinny", "1234###");


	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();

		LoginSuccessData LOGIN_SUCCESS_DATA = new LoginSuccessData("a.b.c");

		given(authorizationService.login(LOGIN_DATA)).willReturn(LOGIN_SUCCESS_DATA);
	}

	@Test
	public void login() throws Exception {
		mockMvc.perform(post("/session")
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(LOGIN_DATA))
				).andExpect(status().isOk());
	}

}
