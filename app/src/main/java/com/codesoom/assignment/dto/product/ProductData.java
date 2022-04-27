package com.codesoom.assignment.dto.product;

import com.codesoom.assignment.domain.product.Product;
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

        public Product toEntity() {
            return Product.builder()
                    .name(name)
                    .maker(maker)
                    .price(price)
                    .imageUrl(imageUrl)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class UpdateProductRequest {

        private Long id;

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

        public void initProductId(long id) {
            this.id = id;
        }

        public Product toEntity() {
            return Product.builder()
                    .id(id)
                    .name(name)
                    .maker(maker)
                    .price(price)
                    .imageUrl(imageUrl)
                    .build();
        }
    }

    @Getter
    public static class RemoveProductRequest {
        private Long productId;

        private RemoveProductRequest(long productId) {
            this.productId = productId;
        }

        public static RemoveProductRequest initProductId(long productId) {
            RemoveProductRequest removeProductRequest = new RemoveProductRequest(productId);
            return removeProductRequest;
        }
    }

    @Getter
    public static class SearchOneProductRequest {
        private Long productId;

        private SearchOneProductRequest(Long productId) {
            this.productId = productId;
        }

        public static SearchOneProductRequest initProductId(long productId) {
            SearchOneProductRequest searchOneProductRequest = new SearchOneProductRequest(productId);
            return searchOneProductRequest;
        }
    }

}
