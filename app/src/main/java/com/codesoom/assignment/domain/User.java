package com.codesoom.assignment.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 사용자 정보를 다룬다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User {
    /** 사용자 식별자 */
    @Id
    @GeneratedValue
    private Long id;

    /** 사용자 이름 */
    private String name;

    /** 사용자 이메일 */
    private String email;

    /** 사용자 비밀번호 */
    private String password;

    /** 사용자 삭제여부 */
    private boolean deleted = false;

    @Builder
    public User(Long id, String name, String email, String password, boolean deleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.deleted = deleted;
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean authenticate(String password) {
        return !deleted && password.equals(this.password);
    }

//사용자 수정은 dozerMapper가 대신합니다.
//    /** 사용자 정보를 수정한다. */
//    public void update(User user) {
//        this.name = name;
//        this.password = password;
//    }
}
