package com.codesoom.assignment.application;


import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.infra.InMemoryUserRepository;
import com.codesoom.assignment.infra.JpaUserRepository;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthenticationServiceTest {
    private User createdUser;
    private User authenticUser;
    private String userEmail;
    private String userPassword;
    UserRepository userRepository = new InMemoryUserRepository();
    AuthenticationService authenticationService = new AuthenticationService();

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
        authenticUser = authenticationService.authenticate(userEmail, userPassword);
    }

    @Then("올바른 유저로 인증된다")
    public void authenticatedAsRightUser() {
        assertThat(authenticUser).isEqualTo(createdUser);
    }
}
