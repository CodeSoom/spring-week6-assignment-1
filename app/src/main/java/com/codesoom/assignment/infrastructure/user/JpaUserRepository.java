package com.codesoom.assignment.infrastructure.user;

import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
}
