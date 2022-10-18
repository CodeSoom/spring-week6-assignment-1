package com.codesoom.assignment.application.user.implement;

import com.codesoom.assignment.application.user.UserQueryService;
import com.codesoom.assignment.common.exception.EntityNotFoundException;
import com.codesoom.assignment.common.response.ErrorCode;
import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codesoom.assignment.common.response.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * @throws EntityNotFoundException 회원정보가 없을 경우
     */
    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }
}
