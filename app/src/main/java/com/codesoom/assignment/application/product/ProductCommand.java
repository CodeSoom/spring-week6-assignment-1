package com.codesoom.assignment.application.product;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@lombok.Generated
public class ProductCommand {

    @lombok.Generated
    @Getter
    @Builder
    @ToString
    public static class Register {
        private final String name;

        private final String maker;

        private final Long price;

        private final String imageUrl;

    }

    @lombok.Generated
    @Getter
    @Builder
    @ToString
    public static class UpdateRequest {
        private final Long id;

        private final String name;

        private final String maker;

        private final Long price;

        private final String imageUrl;

    }
}
