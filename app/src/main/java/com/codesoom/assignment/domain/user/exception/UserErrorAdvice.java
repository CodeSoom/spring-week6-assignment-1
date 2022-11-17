package com.codesoom.assignment.domain.user.exception;

import com.codesoom.assignment.domain.session.controller.SessionController;
import com.codesoom.assignment.domain.user.presentation.UserController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestControllerAdvice(basePackageClasses = {
        UserController.class,
        SessionController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface UserErrorAdvice {
}
