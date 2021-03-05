package com.codesoom.assignment.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 사용자 정보.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    /**
     * 사용자 식별자.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 사용자 이메일.
     */
    private String email;

    /**
     * 사용자 이름.
     */
    private String name;

    /**
     * 사용자 비밀번호.
     */
    private String password;

    /**
     * 사용자 삭제 여부.
     */
    private boolean deleted;

    @Builder
    public User(Long id, String email, String name, String password, boolean deleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.deleted = deleted;
    }

    /**
     * 사용자 정보를 생성합니다.
     * @param email 사용자 이메일
     * @param name 사용자 이름
     * @param password 사용자 비밀번호
     * @return 생성된 사용자 정보
     */
    public static User create(String email, String name, String password) {
        return User.builder()
                .email(email)
                .name(name)
                .password(password)
                .deleted(false)
                .build();
    }

    /**
     * 사용자의 정보를 갱신합니다.
     * @param source 사용자 갱신 정보
     */
    public void changeWith(User source) {
        name = source.name;
        password = source.password;
    }

    /**
     * 사용자 정보를 삭제했다고 표시합니다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 입력받은 비밀번호가 삭제안된 사용자의 주어진 비밀 번호와 동일하면 true를 리턴하고, 그렇지 않다면 false를 리턴합니다.
     */
    public boolean authenticate(String password) {
        return !deleted && password.equals(this.password);
    }
}
