package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.dto.UserResultData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * /users URL에 대한 HTTP 요청을 처리하는 Controller 클래스
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
     * User를 생성한 후, 생성된 User의 정보가 담긴 DTO를 반환한다.
     *
     * @param registrationData 생성할 User의 정보가 담긴 DTO
     * @return 생성된 User의 정보가 담긴 DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }

    /**
     * User 정보를 수정한 후, 수정된 User의 정보가 담긴 DTO를 반환한다.
     *
     * @param id 수정할 User의 id
     * @param modificationData 수정할 정보가 담긴 DTO
     * @return 수정된 User의 정보가 담긴 DTO
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
     * User를 삭제한다.
     *
     * @param id 삭제할 User의 id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * User의 정보가 담긴 DTO를 반환한다.
     *
     * @param user 반환하고 싶은 User
     * @return User의 정보가 담긴 DTO
     */
    private UserResultData getUserResultData(User user) {
        return UserResultData.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
