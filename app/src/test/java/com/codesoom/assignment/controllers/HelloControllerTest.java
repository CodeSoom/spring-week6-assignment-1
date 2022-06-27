package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * HelloController에 대한 테스트 클래스
 */
class HelloControllerTest {
    @Test
    void sayHello() {
        HelloController controller = new HelloController();

        assertThat(controller.sayHello()).isEqualTo("Hello, world!");
    }
}
