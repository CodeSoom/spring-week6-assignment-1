package com.codesoom.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String name;

    private String password;

    private boolean deleted = false;

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

    public boolean authenticate(String password) {
        return !deleted && password.equals(this.password);
    }
}
