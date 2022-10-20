package com.codesoom.assignment.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    /**
     * 회원 전체 리스트을 조회하고 리턴한다.
     * @return 회원 전체 리스트
     */
    List<User> findAll();

    /**
     * 회원 ID로 회원을 조회하고 회원정보를 리턴한다.
     * @param id 회원 ID
     * @return 회원정보
     */
    Optional<User> findById(Long id);

    /**
     * 회원 이메일과 패스워드로 회원을 조회하고 회원정보를 리턴한다.
     * @param email 회원 이메일
     * @param password 회원 패스워드
     * @return 회원정보
     */
    Optional<User> findByEmailAndPassword(String email, String password);

    /**
     * 회원 ID로 회원을 조회하고 이메일 존재여부를 리턴한다.
     * @param id 회원 ID
     * @return ID 존재여부
     */
    boolean existsById(Long id);

    /**
     * 회원 이메일로 회원을 조회하고 이메일 존재여부를 리턴한다.
     * @param email 회원 이메일
     * @return 이메일 존재여부
     */
    boolean existsByEmail(String email);

    /**
     * 새로운 회원을 등록하고 리턴한다.
     * @param user 새로운 회원 정보
     * @return 등록된 회원 정보
     */
    User save(User user);

    /**
     * 회원정보를 삭제한다.
     * @param user 삭제할 회원정보
     */
    void delete(User user);

}
