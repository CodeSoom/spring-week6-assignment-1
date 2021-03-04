package com.codesoom.assignment.user.domain;

import lombok.AllArgsConstructor;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
     * 사용자 삭제 여부 (default: 삭제 안됨)
     */
    @Builder.Default
    private boolean deleted = false;

    /**
     * 사용자의 정보를 갱신합니다.
     * @param source 사용자 갱신 정보
     */
    public void changeWith(User source) {
        name = source.name;
        password = source.password;
    }

    /**
     * 사용자 정보를 삭제합니다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 입력받은 비밀번호가 삭제안된 사용자의 기존 비밀 번호와 동일하면 true를 리턴하고, 그렇지 않다면 false를 리턴합니다.
     */
    public boolean authenticate(String password) {
        return !deleted && password.equals(this.password);
    }
}
