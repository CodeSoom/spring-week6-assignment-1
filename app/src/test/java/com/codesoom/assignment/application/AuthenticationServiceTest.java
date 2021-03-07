package com.codesoom.assignment.application;


import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidAccessesTokenException;
import com.codesoom.assignment.errors.InvalidUserInformationException;
import com.codesoom.assignment.infra.InMemoryUserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthenticationServiceTest {
    private User createdUser;
    private User authenticUser;
    private String userEmail;
    private String userPassword;
    private Exception exception;
    private String token;

    UserRepository userRepository = new InMemoryUserRepository();
    AuthenticationService authenticationService = new AuthenticationService(userRepository);

    @Given("이미 생성된 유저의 email와 password가 주어졌을 때")
    public void givenAlreadyCreatedUserEmailAndPassword() {
        userEmail = "las@magical.dev";
        userPassword = "TEST_PASSWORD";
        createdUser = User.builder()
            .name("Las")
            .password(userPassword)
            .email(userEmail)
            .build();
        userRepository.save(createdUser);
    }

    @When("인증을 하게된다면")
    public void authenticate() {
        try {
            authenticUser = authenticationService.authenticate(userEmail, userPassword);
        } catch (InvalidUserInformationException invalidUserInformationException) {
            exception = invalidUserInformationException;
        }
    }

    @Then("올바른 유저로 인증된다")
    public void authenticatedAsRightUser() {
        assertThat(authenticUser).isEqualTo(createdUser);
    }

    @Given("올바르지 않은 email와 password가 주어졌을 때")
    public void givenInvalidInformation() {
        userEmail = "las@magical.dev";
        userEmail = "TEST_PASSWORD";
    }

    @Then("에러가 발생한다")
    public void raiseError() {
        assertThat(exception).isNotNull();
    }

    @Given("올바른 토큰이 주어졌을 때")
    public void givenRightToken() {
        userEmail = "las@magical.dev";
        userPassword = "TEST_PASSWORD";
        createdUser = User.builder()
            .name("Las")
            .password(userPassword)
            .email(userEmail)
            .build();
        userRepository.save(createdUser);

        token = authenticationService.issueToken(createdUser);
    }

    @When("토큰을 인증한다면")
    public void validateToken() {
        try {
            authenticationService.validateToken(token);
        } catch (InvalidAccessesTokenException invalidAccessesTokenException) {
            exception = invalidAccessesTokenException;
        }
    }

    @Then("에러가 발생하지 않는다")
    public void doseNotRaiseError() {
        assertThat(exception).isNull();
    }
}
