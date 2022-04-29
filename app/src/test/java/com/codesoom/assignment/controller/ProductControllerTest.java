package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.product.ProductCommandServiceTestDouble;
import com.codesoom.assignment.application.product.ProductQueryServiceTestDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("ProductController 에서")
class ProductControllerTest {
    private static final String PRODUCT_NAME = "상품1";
    private static final String PRODUCT_MAKER = "메이커1";
    private static final Integer PRODUCT_PRICE = 100000;
    private static final String PRODUCT_IMAGE_URL = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F9941A1385B99240D2E";

    @Autowired
    private ProductQueryServiceTestDouble productQueryServiceTestDouble;
    @Autowired
    private ProductCommandServiceTestDouble productCommandServiceTestDouble;
    private ProductController productController;

}