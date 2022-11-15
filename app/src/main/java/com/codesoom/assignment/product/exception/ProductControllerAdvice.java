package com.codesoom.assignment.product.exception;

import com.codesoom.assignment.product.presentation.ProductController;
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
        ProductController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface ProductControllerAdvice {
}
