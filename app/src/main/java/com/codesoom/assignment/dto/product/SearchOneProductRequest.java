package com.codesoom.assignment.dto.product;


public class SearchOneProductRequest {
    private long productId;

    private SearchOneProductRequest(long productId) {
        this.productId = productId;
    }

    public static SearchOneProductRequest createSearchRequestObjectFrom(long productId) {
        SearchOneProductRequest removeProductRequest = new SearchOneProductRequest(productId);
        return removeProductRequest;
    }

    public long getProductId() {
        return productId;
    }
}
