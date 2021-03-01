package com.codesoom.assignment.healthcheck;

import com.codesoom.assignment.healthcheck.controller.HelloController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloControllerTest {
    @Test
    void sayHello() {
        HelloController controller = new HelloController();

        assertThat(controller.sayHello()).isEqualTo("Hello, world!");
    }
}
