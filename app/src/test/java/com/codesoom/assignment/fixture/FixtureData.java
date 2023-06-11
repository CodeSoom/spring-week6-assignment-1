package com.codesoom.assignment.fixture;


import com.codesoom.assignment.dto.LoginData;

public class FixtureData {

	public static final String SECRET_KEY = "12345678901234567890123456789010";
	public static final LoginData LOGIN_VALID = new LoginData("jinny@mail.com", "1234");
	public static final LoginData LOGIN_PW_FAIL = new LoginData("jinny@mail.com", "0000");
	public static final LoginData LOGIN_NOT_EXIST = new LoginData("jinny_no@mail.com", "0000");

	private FixtureData() {
	}

	public enum AccessTokenFixture {
		VALID_TOKEN(1L, "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw"),
		SIGNATURE_FAIL_TOKEN(1L, VALID_TOKEN.getToken() + "INVALID"),
		USER_NOT_EXISTS_TOKEN(99L, "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjk5fQ.j7fOjctI4DaJ47C5gQcu_ueOYoT2Eg-hyJiK7F9T85M");

		private final Long userId;
		private final String token;

		AccessTokenFixture(long userId, String token) {
			this.userId = userId;
			this.token = token;
		}

		public String getToken() {
			return this.token;
		}
		public Long getUserId() {
			return this.userId;
		}
	}


}
