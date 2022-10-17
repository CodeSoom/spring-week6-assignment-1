package com.codesoom.assignment.dto;

import com.codesoom.assignment.utils.UserSampleFactory;
import com.codesoom.assignment.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UserDto.UserInfo 클래스")
class UserDtoTest {
    @Nested
    @DisplayName("UserInfo 생성자는")
    class Describe_UserInfo_constructor {
        @Nested
        @DisplayName("User 엔티티가 주어지면")
        class Context_with_user_entity {
            private final User givenUser = UserSampleFactory.createUser(1L);

            @Test
            @DisplayName("UserInfo 객체로 변환한다")
            void it_converts_userinfo_object() {
                Assertions.assertThat(new UserDto.UserInfo(givenUser)).isInstanceOf(UserDto.UserInfo.class);
            }
        }
    }
}