package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * User 관련 DB 처리를 하는 Repository 인터페이스
 */
public interface UserRepository {
    /**
     * User를 저장한 후에 저장된 User를 반환한다.
     *
     * @param user 저장할 User
     * @return 저장된 User
     */
    User save(User user);

    /**
     * email이 존재하면 true를, 존재하지 않으면 false를 반환한다.
     *
     * @param email 존재하는지 확인할 email
     */
    boolean existsByEmail(String email);

    /**
     * 매개변수로 주어진 id와 id가 일치하는 User를 찾고, 찾은 User를 반환한다.
     *
     * @param id 조회할 User의 id
     * @return 조회된 User
     */
    Optional<User> findById(Long id);

    /**
     * 매개변수로 주어진 id와 id가 일치하면서 deleted가 false인 User를 찾고, 찾은 User를 반환한다.
     *
     * @param id 조회할 User의 id
     * @return 조회된 User
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 매개변수로 주어진 email과 email이 일치하는 User를 찾고, 찾은 User를 반환한다.
     *
     * @param email 조회할 User의 email
     * @return 조회된 User
     */
    Optional<User> findByEmail(String email);
}
