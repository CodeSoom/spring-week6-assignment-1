package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.product.Product;

public interface ProductCommandService {

    /**
     * 새로운 상품을 추가하고 추가된 상품을 리턴한다.
     * @param command 새로운 상품정보
     * @return 추가된 상품
     */
    Product createProduct(ProductCommand.Register command);

    /**
     * 상품을 수정하고 수정된 상품을 리턴한다.
     * @param command 수정할 상품정보
     * @return 수정된 상품
     */
    Product updateProduct(ProductCommand.UpdateRequest command);

    /**
     * 상품ID의 상품을 삭제한다.
     * @param id 삭제할 상품 ID
     */
    void deleteProduct(Long id);
}
