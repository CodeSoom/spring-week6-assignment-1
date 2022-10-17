package com.codesoom.assignment.utils;

import com.codesoom.assignment.application.product.ProductCommand;
import com.codesoom.assignment.dto.ProductDto;
import com.codesoom.assignment.domain.product.Product;

import java.util.Random;
import java.util.UUID;

import static com.codesoom.assignment.utils.ProductSampleFactory.FieldName.MAKER;
import static com.codesoom.assignment.utils.ProductSampleFactory.FieldName.NAME;
import static com.codesoom.assignment.utils.ProductSampleFactory.FieldName.PRICE;

public class ProductSampleFactory {

    private static final Random random = new Random();

    public static Product createProduct() {
        return Product.builder()
                .name("고양이 장난감" + random.nextInt(100))
                .maker("제조사" + random.nextInt(100))
                .price(randomPrice())
                .imageUrl(UUID.randomUUID().toString() + ".png")
                .build();
    }

    public static Product createProduct(Long id) {
        Product.ProductBuilder product = Product.builder();

        System.out.println(product.toString());

        product.id(id)
                .name("고양이 장난감" + random.nextInt(100))
                .maker("제조사" + random.nextInt(100))
                .price(randomPrice())
                .imageUrl(UUID.randomUUID().toString() + ".png")
                .build();

        return product.build();
    }

    public static ProductDto.RequestParam createRequestParam() {
        ProductDto.RequestParam request = new ProductDto.RequestParam();
        request.setName("고양이 장난감" + random.nextInt(100));
        request.setMaker("제조사" + random.nextInt(100));
        request.setPrice(randomPrice());
        request.setImageUrl(UUID.randomUUID().toString() + ".png");

        return request;
    }

    public static ProductDto.RequestParam createRequestParamWith(ProductSampleFactory.FieldName fieldName, ProductSampleFactory.ValueType valueType) {
        final ProductDto.RequestParam request = new ProductDto.RequestParam();
        String testValue;

        if (valueType == ProductSampleFactory.ValueType.NULL) {
            testValue = null;
        } else if (valueType == ProductSampleFactory.ValueType.EMPTY) {
            testValue = "";
        } else {
            testValue = "  ";
        }

        request.setName(fieldName == NAME ? testValue : "고양이 장난감" + random.nextInt(100));
        request.setMaker(fieldName == MAKER ? testValue : UUID.randomUUID().toString());
        request.setPrice(fieldName == PRICE ? null : randomPrice());

        return request;
    }

    public static Product.ProductBuilder createProductParamWith(ProductSampleFactory.FieldName fieldName, ProductSampleFactory.ValueType valueType) {
        final Product.ProductBuilder builder = Product.builder();
        String testValue;

        if (valueType == ProductSampleFactory.ValueType.NULL) {
            testValue = null;
        } else if (valueType == ProductSampleFactory.ValueType.EMPTY) {
            testValue = "";
        } else {
            testValue = "  ";
        }

        builder.name(fieldName == NAME ? testValue : "고양이 장난감" + random.nextInt(100));
        builder.maker(fieldName == MAKER ? testValue : UUID.randomUUID().toString());
        builder.price(fieldName == PRICE ? null : randomPrice());

        return builder;
    }

    public static ProductCommand.Register of(Product product) {
        ProductCommand.Register.RegisterBuilder registerBuilder = ProductCommand.Register.builder();

        System.out.println(registerBuilder.toString());

        return registerBuilder.name(product.getName())
                .maker(product.getMaker())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }

    public static ProductCommand.UpdateRequest of(Long id, Product product) {
        return ProductCommand.UpdateRequest.builder()
                .id(id)
                .name(product.getName())
                .maker(product.getMaker())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }

    public static Long randomPrice() {
        return (long) (Math.ceil((Math.random() * 10000 + 10000) / 1000) * 1000);
    }

    public enum FieldName {
        NAME("name", "상품명"),
        MAKER("maker", "제조사"),
        PRICE("price", "금액");

        private String name;
        private String description;

        FieldName(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum ValueType {
        NULL, EMPTY, BLANK
    }
}
