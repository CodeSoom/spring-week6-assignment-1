package com.codesoom.assignment.extend.domain.user;

import com.codesoom.assignment.extend.domain.BaseTime;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Table(name = "users")
@Entity(name = "Users")
public class User extends BaseTime {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long userId;
    
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    protected User() {
    }

    @Builder
    private User(Long userId, String email, String name, String password, boolean deleted) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.deleted = deleted;
    }

    public void update(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void destroy() {
        deleted = true;
    }
}
