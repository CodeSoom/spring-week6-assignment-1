package com.codesoom.assignment.dto.product;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class ProductData {

    @Getter
    @Builder
    public static class CreateProductRequest {
        @NotBlank
        private final String name;

        @NotBlank
        private final String maker;

        @NotNull
        private final Integer price;
        private final String imageUrl;

        @Override
        public String toString() {
            return "CreateProduct{" +
                    "name='" + name + '\'' +
                    ", maker='" + maker + '\'' +
                    ", price=" + price +
                    ", imageUrl='" + imageUrl + '\'' +
                    '}';
        }
    }

    @Getter
    @Builder
    public static class UpdateProductRequest {
        @NotNull
        private final Long id;

        @NotBlank
        private final String name;

        @NotBlank
        private final String maker;

        @NotNull
        private final Integer price;
        private final String imageUrl;

        @Override
        public String toString() {
            return "UpdateProductRequest{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", maker='" + maker + '\'' +
                    ", price=" + price +
                    ", imageUrl='" + imageUrl + '\'' +
                    '}';
        }
    }

    @Getter
    public static class RemoveProductRequest {
        @NotNull
        private final Long id;

        public RemoveProductRequest(Long id) {
            this.id = id;
        }
    }

}
