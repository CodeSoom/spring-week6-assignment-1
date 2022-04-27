package com.codesoom.assignment.application.users;

import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 회원 변경을 담당합니다.
 */
@Transactional
@Service
public class UserCommandService implements UserSaveService, UserUpdateService, UserDeleteService {

    private final UserRepository repository;

    public UserCommandService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User saveUser(UserSaveRequest userSaveRequest) {
        return repository.save(userSaveRequest);
    }

    @Override
    public User updateUser(Long id, UserSaveRequest userSaveRequest) {
        User user = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("%에 해당하는 회원을 찾을 수 없어 수정에 실패하였습니다.", id)));

        user.update(userSaveRequest.user());

        return user;
    }

    @Override
    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("%에 해당하는 회원을 찾을 수 없어 삭제에 실패하였습니다.", id)));

        repository.delete(user);
    }

}
