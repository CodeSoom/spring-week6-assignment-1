package com.codesoom.assignment.domain;

import java.util.Optional;

public interface UserRepository {
    /** 주어진 아이디에 해당하는 사용자를 조회한다. */
    Optional<User> findById(Long id);

    /** 주어진 이메일에 해당하는 사용자가 존재하는 검사한다. */
    boolean existsByEmail(String email);

    /** 주어진 유저를 저장하고 해당 객체를 리턴한다. */
    User save(User user);

    /** 주어진 유저를 삭제한다. */
    void delete(User user);

//    Optional<User> findByIdAndDeletedIsFalse(Long id);
//
//    Optional<User> findByEmail(String email);
}
