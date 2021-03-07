package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserRepositoryTest {
    private User user;
    private Optional<User> foundUser;
    private final InMemoryUserRepository repository = new InMemoryUserRepository();
    private boolean isExist;

    @Given("올바른 유저가 주어졌을 때")
    public void givenRightUser() {
        String userEmail = "las@magical.dev";
        String userPassword = "TEST_PASSWORD";
        user = User.builder()
            .name("Las")
            .password(userPassword)
            .email(userEmail)
            .build();
    }

    @When("유저를 저장하는 경우")
    public void saveUser() {
        repository.save(user);
    }

    @Then("유저를 찾을 수 있다")
    public void findUser() {
        assertThat(repository.findById(user.getId())).isNotEmpty();
    }

    @Given("유저가 저장되었을 때")
    public void alreadyUserSaved() {
        String userEmail = "las@magical.dev";
        String userPassword = "TEST_PASSWORD";
        user = User.builder()
            .name("Las")
            .password(userPassword)
            .email(userEmail)
            .build();
        repository.save(user);
    }

    @Then("유저를 가져올 수 있다")
    public void foundUser() {
        assertThat(foundUser)
            .isNotEmpty();
        assertThat(foundUser.get())
            .isEqualTo(user);
    }

    @When("이메일로 유저를 가져오는 경우")
    public void findByEmail() {
        foundUser = repository.findByEmail(user.getEmail());
    }

    @When("이메일로 active한 유저를 가져오는 경우")
    public void findActiveUserByEmail() {
        foundUser = repository.findActiveUserByEmail(user.getEmail());
    }

    @Given("active하지 않은 유저가 저장되었을 때")
    public void alreadyNotActiveUserSaved() {
        String userEmail = "las@magical.dev";
        String userPassword = "TEST_PASSWORD";
        user = User.builder()
            .name("Las")
            .password(userPassword)
            .email(userEmail)
            .build();
        user.destroy();
        repository.save(user);
    }

    @Then("유저를 가져올 수 없다")
    public void notFoundUser() {
        assertThat(foundUser).isEmpty();
    }

    @When("아이디로 active한 유저를 가져오는 경우")
    public void findByIdAndDeletedIsFalse() {
        foundUser = repository.findByIdAndDeletedIsFalse(user.getId());
    }

    @When("저장된 user 의 이메일이 존재하는지 확인한다면")
    public void existsByEmail() {
        isExist = repository.existsByEmail(user.getEmail());
    }

    @Then("이메일이 존재한다")
    public void existEmail() {
        assertThat(isExist).isTrue();
    }

    @Given("유저가 존재하지 않을 때")
    public void givenNoUser() {
        // pass
    }

    @When("이메일이 존재하는지 확인한다면")
    public void existsByEmailWithNoUser() {
        String notExistEmail = "not exist email";
        isExist = repository.existsByEmail(notExistEmail);
    }

    @Then("이메일이 존재하지 않는다")
    public void notExistEmail() {
        assertThat(isExist).isFalse();
    }

    @When("존재하지 않는 id로 active한 유저를 가져오는 경우")
    public void findByIdAndDeletedIsFalseWithNotExistId() {
        Long notExistId = -1L;
        foundUser = repository.findByIdAndDeletedIsFalse(notExistId);
    }
}
