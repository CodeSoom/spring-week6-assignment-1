package com.codesoom.assignment.domain;

/**
 * 비밀번호를 제공한다.
 */
@FunctionalInterface
public interface PasswordSupplier {
    String getPassword();
}
