package com.codesoom.assignment.domain;

/**
 * 이메일을 제공한다.
 */
@FunctionalInterface
public interface EmailSupplier {
    String getEmail();
}
