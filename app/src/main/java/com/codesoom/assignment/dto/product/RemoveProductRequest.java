package com.codesoom.assignment.dto.product;

import lombok.Getter;

public class RemoveProductRequest {
    private Long productId;

    private RemoveProductRequest(Long productId) {
        this.productId = productId;
    }

    public static RemoveProductRequest createRemoveRequestObjectFrom(long productId) {
        RemoveProductRequest removeProductRequest = new RemoveProductRequest(productId);
        return removeProductRequest;
    }

    public Long getProductId() {
        return productId;
    }
}
