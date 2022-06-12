package com.codesoom.assignment.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private String password;

    @Builder.Default
    private boolean deleted = false;

    protected User() {}

    @Builder
    public User(Long id, String email, String name, String password, boolean deleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.deleted = deleted;
    }

    public void changeWith(User source) {
        name = source.name;
        password = source.password;
    }

    public void destroy() {
        deleted = true;
    }

    /**
     * 인증에 성공하면 true, 인증에 실패하면 false 를 반환한다.
     *
     * @param password 비교할 password
     * @return 인증에 성공하면 true, 인증에 실패하면 false
     */
    public boolean authenticate(String password) {
        return !deleted && password.equals(this.password);
    }
}
