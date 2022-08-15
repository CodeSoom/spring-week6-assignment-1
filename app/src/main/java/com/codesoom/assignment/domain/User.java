package com.codesoom.assignment.domain;

import com.codesoom.assignment.dto.UserLoginData;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
public class User {
    @Id
    private String email;

    private String name;
    private String password;
    private boolean deleted;

    public User() {}

    @Builder
    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    /**
     * 주어진 로그인 정보와 유저 정보가 일치한지 확인합니다.
     *
     * @param data 로그인 정보
     * @return 일치하면 false, 아니면 true
     */
    public boolean isUnMatch(UserLoginData data) {
        return !Objects.equals(data.getEmail(), this.email) ||
                !Objects.equals(data.getPassword(), this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return deleted == user.deleted &&
                Objects.equals(email, user.email) &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password, deleted);
    }
}
