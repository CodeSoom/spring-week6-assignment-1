package com.codesoom.assignment.domain.user;

import com.codesoom.assignment.utils.UserSampleFactory;
import com.codesoom.assignment.common.exception.InvalidParamException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("User 클래스")
class UserTest {

    @Nested
    @DisplayName("Builder 는")
    class Describe_builder {
        @Nested
        @DisplayName("Input 매개변수가 주어지면")
        class Context_with_input_parameters {
            private final User given = new User(1L, "홍길동", "test1234", "hong@test.com");

            @Test
            @DisplayName("User 객체를 생성한다")
            void it_generate_user_object() {
                User actual = User.builder()
                        .id(1L)
                        .name("홍길동")
                        .password("test1234")
                        .email("hong@test.com")
                        .build();

                assertThat(actual).isInstanceOf(User.class);
                assertThat(actual.getId()).isEqualTo(given.getId());
                assertThat(actual.getName()).isEqualTo(given.getName());
                assertThat(actual.getPassword()).isEqualTo(given.getPassword());
                assertThat(actual.getEmail()).isEqualTo(given.getEmail());
            }
        }

        @Nested
        @DisplayName("이름, 비밀번호에 빈 값이 주어지면")
        class Context_with_empty_parameters {
            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {

                assertThatThrownBy(() -> {
                    User.builder()
                            .id(1L)
                            .name(null)
                            .password("")
                            .email("")
                            .build();
                }).isInstanceOf(InvalidParamException.class);

            }

        }
    }

    @Nested
    @DisplayName("modifyUserInfo 메소드는")
    class Describe_modifyUserInfo {
        @Nested
        @DisplayName("회원정보가 주어지면")
        class Context_with_modified_user_info {
            private final User givenUser = UserSampleFactory.createUser(1L);
            @Test
            @DisplayName("수정된 정보를 반영한다")
            void it_reflects_modified_info() {
                User modifiedUser = User.builder()
                        .id(1L)
                        .name("홍길동")
                        .password("test1234")
                        .email("test@gmail.com")
                        .build();

                givenUser.modifyUserInfo(modifiedUser);

                assertThat(givenUser.getName()).isEqualTo(modifiedUser.getName());
                assertThat(givenUser.getPassword()).isEqualTo(modifiedUser.getPassword());
                assertThat(givenUser.getEmail()).isEqualTo(modifiedUser.getEmail());
            }
        }
    }
}
