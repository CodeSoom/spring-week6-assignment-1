package com.codesoom.assignment.extend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("usersRepository")
public interface UserRepository extends JpaRepository<User, Long> {
}
