package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.product.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@lombok.Generated
public class ProductDto {
    @lombok.Generated
    @Getter
    @Setter
    @ToString
    public static class RequestParam {
//        private Long id;

        @NotBlank(message = "상품명은 필수항목 입니다.")
        private String name;

        @NotBlank(message = "제조사는 필수항목 입니다.")
        private String maker;

        @NotNull(message = "금액은 필수항목 입니다.")
        @Range(min = 1000, max = 1000000, message = "금액은 {min} ~ {max}원 사이만 입력할 수 있습니다.")
        private Long price;

        private String imageUrl;
    }

    @lombok.Generated
    @Getter
    @ToString
    public static class ProductInfo {
        private final Long id;

        private final String name;

        private final String maker;

        private final Long price;

        private final String imageUrl;

        public ProductInfo(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.maker = product.getMaker();
            this.price = product.getPrice();
            this.imageUrl = product.getImageUrl();
        }
    }
}
