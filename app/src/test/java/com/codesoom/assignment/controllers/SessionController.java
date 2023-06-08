package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthorizationService;
import com.codesoom.assignment.dto.LoginData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/session")
@RestController
public class SessionController {

	private final AuthorizationService authorizationService;

	public SessionController(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@PostMapping
	public String login(@RequestBody @Valid LoginData login) {
		return authorizationService.login(login);
	}

}
