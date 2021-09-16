package com.codesoom.assignment.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private String password;

    @Builder.Default
    @Column(name = "deleted")
    private boolean deleted = false;

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
