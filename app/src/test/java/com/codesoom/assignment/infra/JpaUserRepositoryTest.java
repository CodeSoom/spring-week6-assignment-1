package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class JpaUserRepositoryTest {
    @Test
    public void test(){
        JpaUserRepository repository = new JpaUserRepository();

        String userEmail = "las@magical.dev";
        Long id = 1L;
        User user = User.builder()
            .email(userEmail)
            .id(id)
            .build();
        
        given(
            repository.findByEmailAndDeletedIsFalse(userEmail)
        ).willReturn(java.util.Optional.of(user));

        Optional<User> foundUser = repository.findActiveUserByEmail(userEmail);

        assertThat(foundUser).isNotEmpty();
        assertThat(foundUser.get()).isEqualTo(user);
    }
}
