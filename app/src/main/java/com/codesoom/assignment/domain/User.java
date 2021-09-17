package com.codesoom.assignment.domain;

import com.codesoom.assignment.application.LoginForm;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class User implements LoginForm {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private String password;

    @Builder.Default
    @Column(name = "deleted")
    private boolean deleted = false;

    protected User() {
    }

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
     * 인증에 성공하면 true, 인증에 실패하면 false를 리턴합니다.
     *
     * @param password 비교할 패스워드
     * @return 인증에 성공하면 true, 인증에 실패하면 false
     */
    public boolean authenticate(String password) {
        return !deleted && password.equals(this.password);
    }
}
