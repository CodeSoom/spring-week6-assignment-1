package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthorizationService;
import com.codesoom.assignment.dto.LoginData;
import java.util.HashMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/session")
@RestController
@CrossOrigin
public class SessionController {

	private final AuthorizationService authorizationService;

	public SessionController(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@PostMapping
	public HashMap login(@RequestBody @Valid LoginData login) {
		String accessToken = authorizationService.login(login);
		HashMap result = new HashMap();
		result.put("accessToken", accessToken);

		return result;
	}

}
