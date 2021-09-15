package com.codesoom.assignment.domain;

import java.io.Serializable;


/**
 * 식별자 정보를 관리한다.
 */
public interface Identifier extends Serializable, EmailSupplier, PasswordSupplier {
}
