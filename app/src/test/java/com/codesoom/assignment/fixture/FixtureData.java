package com.codesoom.assignment.fixture;


import com.codesoom.assignment.dto.LoginData;

public class FixtureData {

	public static final String SECRET_KEY = "12345678901234567890123456789010";
	public static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
	public static final String INVALID_TOKEN = VALID_TOKEN + "INVALID";

	public static final LoginData LOGIN_VALID = new LoginData("jinny@mail.com", "1234");
	public static final LoginData LOGIN_PW_FAIL = new LoginData("jinny@mail.com", "0000");
	public static final LoginData LOGIN_NOT_EXIST = new LoginData("jinny_no@mail.com", "0000");

	private FixtureData() {
	}
}
