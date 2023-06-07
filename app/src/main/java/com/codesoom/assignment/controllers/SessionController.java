package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.SessionService;
import com.codesoom.assignment.dto.LoginData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1. login : POST id, password -> token 생성후 반환
 * 2. logout : POST token, id - token 무효화, void
 */

@RestController
@RequestMapping("/session")
public class SessionController {

	private final SessionService sessionService;

	public SessionController(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	@PostMapping
	public String login(@RequestBody LoginData login) {
		return sessionService.login(login);
	}
}
