package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserData;
import com.codesoom.assignment.errors.UserEmailDuplicateException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("UserServiceImpl 클래스")
@DataJpaTest
class UserServiceImplTest {

    private  UserService userService;

    @Autowired
    private  UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {

        userService = new UserServiceImpl(userRepository);

    }

    @Nested
    @DisplayName("createUser 메소드는")
    class Describe_createUser {

        @Nested
        @DisplayName("생성할 사용자가 있을 경우에")
        class Context_exist_user {

            UserData source;

            @BeforeEach
            void setUp() {

                source = UserData.builder()
                        .name("name1")
                        .email("email1")
                        .password("12345")
                        .build();
                
            }

            @Test
            @DisplayName("UserData 객체를 통하여 User 객체를 생성후 반환한다")
            void It_create_return_user() throws Exception {

                User createUser = userService.createUser(source);

                assertEquals("name1",createUser.getName());

            }

        }

        @Nested
        @DisplayName("생성할 사용자 이메일이 이미 존재한다면")
        class Context_duplicate_email {

            UserData data;
            User user;

            @BeforeEach
            void setUp() throws Exception {

                data = UserData.builder()
                        .name("name2")
                        .email("email2")
                        .password("12345")
                        .build();

                user = userService.createUser(data);

            }

            @Test
            @DisplayName("UserEmailDuplicateException 예외를 던진다")
            void It_create_return_user() throws Exception {

                assertThatThrownBy(() -> userService.createUser(data))
                        .hasMessage("이미 가입된 메일이 존재합니다.").isInstanceOf(UserEmailDuplicateException.class);

            }

        }

    }


    @Nested
    @DisplayName("updateUser 메소드는")
    class Describe_updateUser {

        @Nested
        @DisplayName("수정할 사용자 정보가 있을 경우에")
        class Context_exist_update_user {

            UserData updateSource;
            Long VALID_ID;

            @BeforeEach
            void setUp() throws Exception {

               User TEST_USER = userService.createUser( UserData.builder()
                                        .name("name1")
                                        .email("email1")
                                        .password("12345")
                                        .build() );

                VALID_ID = TEST_USER.getId();

                updateSource = UserData.builder()
                        .name("UPDATE_NAME")
                        .email("UPDATE_EMAIL")
                        .password("11111")
                        .build();

            }

            @Test
            @DisplayName("아이디와 수정 정보를 입력받아 사용자 정보를 수정한다")
            void It_return_update_user() {

                User user = userService.updateUser(VALID_ID, updateSource);

                assertEquals("UPDATE_NAME",user.getName());
                assertEquals("UPDATE_EMAIL", user.getEmail());
                assertEquals("11111", user.getPassword());

            }

            @Nested
            @DisplayName("수정할 사용자 정보가 없다면")
            class Context_exist_not_user {

                Long INVALID_ID = 1000L;
                UserData updateSource;

                @BeforeEach
                void setUp() {

                    userRepository.deleteAll();

                    updateSource = UserData.builder()
                            .name("UPDATE_NAME")
                            .email("UPDATE_EMAIL")
                            .password("11111")
                            .build();

                }

                @Test
                @DisplayName("UserNotFoundException 예외를 던진다.")
                void It_return_userNotFoundException() {

                    assertThatThrownBy(() -> userService.updateUser(INVALID_ID, updateSource)).isInstanceOf(UserNotFoundException.class);

                }

            }

        }

    }


    @AfterEach
    void clean() {

        userRepository.deleteAll();

    }

}

