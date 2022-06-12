package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * User 저장소
 */
public interface UserRepository {
    /**
     * 주어진 user 를 등록하고 등록된 user 를 반환
     *
     * @param user 등록할 user
     * @return 등록된 user
     */
    User save(User user);

    /**
     * 주어진 email 과 일치하는 email 의 유무를 boolean 형태로 반환
     *
     * @param email 주어진 email 식별자
     * @return email 이 있으면 true, 없으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 주어진 id 와 일치하는 user 를 반환, 없으면 null 로 반환
     *
     * @param id user 식별자
     * @return 주어진 id 와 일치하는 user, 일치하지 않으면 null
     */
    Optional<User> findById(Long id);

    /**
     * 주어진 id 와 일치하는 user 를 반환
     * 단, deleted = false 인 형태만 반환
     *
     * @param id user 식별자
     * @return `deleted = false` 상태로 변환한 user
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 주어진 email 과 일치하는 email 을 가지고 있는 user 를 반환, 없으면 null 로 반환
     *
     * @param email 주어진 email 식별자
     * @return 주어진 email 과 일치하면 user, 일치하지 않으면 null
     */
    Optional<User> findByEmail(String email);

    /**
     * 등록된 모든 user 를 삭제
     */
    void deleteAll();
}
