package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * User 에 대한 HTTP 요청 컨트롤
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 주어진 User 를 저장하고 저장된 User 를 반환
     *
     * @param registrationData 저장할 User(UserRegistrationData)
     * @return 저장된 User(UserResultData)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(
            @RequestBody @Valid UserRegistrationData registrationData
    ) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }

    /**
     * 주어진 id 와 일치하는 User 를 수정하여 반환
     *
     * @param id User 식별자
     * @param modificationData 수정할 User(UserModificationData)
     * @return 수정된 User(UserResultData)
     */
    @PatchMapping("{id}")
    UserResultData update(
            @PathVariable Long id,
            @RequestBody @Valid UserModificationData modificationData
    ) {
        User user = userService.updateUser(id, modificationData);
        return getUserResultData(user);
    }

    /**
     * 주어진 id 와 일치하는 User 삭제
     *
     * @param id User 식별자
     * @throws UserNotFoundException User 식별자로 User 를 찾지 못하는 경우
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * 주어진 User 를 빌더 패턴을 이용해 저장
     *
     * @param user 주어진 User
     * @return 저장된 UserResultData(User)
     */
    private UserResultData getUserResultData(User user) {
        return UserResultData.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
