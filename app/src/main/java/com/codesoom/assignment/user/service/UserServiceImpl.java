package com.codesoom.assignment.user.service;

import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.exception.UserNotFoundException;
import com.codesoom.assignment.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User foundUser = userRepository
                .findById(id)
                .orElseThrow(() ->new UserNotFoundException(id));

        foundUser.change(user);
        return foundUser;
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() ->new UserNotFoundException(id));
        userRepository.deleteById(id);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
