package com.codesoom.assignment.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.exception.DecodingInValidTokenException;
import com.codesoom.assignment.exception.ProductNotFoundException;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
	private static String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
	private static String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw11";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@MockBean
	private AuthenticationService authenticationService;

	private Product product = Product.builder()
		.id(1L)
		.name("쥐돌이")
		.maker("냥이월드")
		.price(5000)
		.build();

	@Test
	void list() throws Exception {
		given(productService.getProducts()).willReturn(Arrays.asList(product));

		mockMvc.perform(
				get("/products")
					.accept(MediaType.APPLICATION_JSON_UTF8)
			)
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("쥐돌이")));
	}

	@Nested
	@DisplayName("GET /products URL 은 ")
	class detail {
		@Nested
		@DisplayName("유효한 토큰이 주어지면")
		class WithValidToken {
			@Test
			@DisplayName("상태코드 200 을 응답한다.")
			void detailWithValidToken() throws Exception {
				product = Product.builder()
					.id(1L)
					.name("쥐돌이")
					.maker("냥이월드")
					.price(5000)
					.build();

				given(productService.getProduct(1L)).willReturn(product);

				mockMvc.perform(
						get("/products/1")
							.accept(MediaType.APPLICATION_JSON_UTF8)
							.header("Authorization", VALID_TOKEN)
					)
					.andExpect(status().isOk())
					.andExpect(content().string(containsString("쥐돌이")));
				verify(productService).getProduct(1L);
			}
		}

		@Nested
		@DisplayName("유효하지 않은 token 이 주어지면")
		class WithInValidToken {
			@Test
			@DisplayName("상태코드 400 을 응답한다.")
			void detailWithInValidToken() throws Exception {
				given(authenticationService.decode(INVALID_TOKEN))
					.willThrow(DecodingInValidTokenException.class);
				mockMvc.perform(
						get("/products/1")
							.header("Authorization", INVALID_TOKEN)
							.accept(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isBadRequest());

				verify(authenticationService).decode(INVALID_TOKEN);
			}
		}
	}

	@Test
	void detailWithExsitedProduct() throws Exception {

		given(productService.getProduct(1L)).willReturn(product);

		mockMvc.perform(
				get("/products/1")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.header("Authorization", "VALID_TOKEN")

			)
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("쥐돌이")));
	}

	@Test
	void detailWithNotExsitedProduct() throws Exception {
		given(productService.getProduct(1000L))
			.willThrow(new ProductNotFoundException(1000L));

		mockMvc.perform(get("/products/1000").header("Authorization", VALID_TOKEN))
			.andExpect(status().isNotFound());
	}

	@Nested
	@DisplayName("POST /products URL 은 ")
	class Create {
		@Nested
		@DisplayName("유효하지 않은 token 과 상품 정보가 주어지면")
		class WithInValidToken {
			@Test
			@DisplayName("상태코드 400 을 응답한다.")
			void createWithInValidToken() throws Exception {

				given(authenticationService.decode(INVALID_TOKEN))
					.willThrow(DecodingInValidTokenException.class);

				mockMvc.perform(
						post("/products")
							.accept(MediaType.APPLICATION_JSON_UTF8)
							.contentType(MediaType.APPLICATION_JSON)
							.header("Authorization", INVALID_TOKEN)
							.content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
								"\"price\":5000}")
					)
					.andExpect(status().isBadRequest());
			}
		}

		@Nested
		@DisplayName("유효한 token 과 상품 정보가 주어지면")
		class WithValidToken {
			@Test
			@DisplayName("상태코드 200 을 응답한다.")
			void createWithValidToken() throws Exception {
				given(productService.createProduct(any(ProductData.class)))
					.willReturn(product);

				mockMvc.perform(
						post("/products")
							.accept(MediaType.APPLICATION_JSON_UTF8)
							.contentType(MediaType.APPLICATION_JSON)
							.header("Authorization", VALID_TOKEN)
							.content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
								"\"price\":5000}")
					)
					.andExpect(status().isCreated())
					.andExpect(content().string(containsString("쥐돌이")));

				verify(productService).createProduct(any(ProductData.class));
			}
		}
	}

	@Test
	void createWithValidAttributes() throws Exception {
		given(productService.createProduct(any(ProductData.class)))
			.willReturn(product);

		mockMvc.perform(
				post("/products")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", VALID_TOKEN)

					.content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
						"\"price\":5000}")
			)
			.andExpect(status().isCreated())
			.andExpect(content().string(containsString("쥐돌이")));

		verify(productService).createProduct(any(ProductData.class));
	}

	@Test
	void createWithInvalidAttributes() throws Exception {
		mockMvc.perform(
				post("/products")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"name\":\"\",\"maker\":\"\"," +
						"\"price\":0}")
			)
			.andExpect(status().isBadRequest());
	}

	@Test
	void updateWithExistedProduct() throws Exception {
		given(productService.updateProduct(eq(1L), any(ProductData.class)))
			.will(invocation -> {
				Long id = invocation.getArgument(0);
				ProductData productData = invocation.getArgument(1);
				return Product.builder()
					.id(id)
					.name(productData.getName())
					.maker(productData.getMaker())
					.price(productData.getPrice())
					.build();
			});

		mockMvc.perform(
				patch("/products/1")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
						"\"price\":5000}")
			)
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("쥐순이")));

		verify(productService).updateProduct(eq(1L), any(ProductData.class));
	}

	@Test
	void updateWithNotExistedProduct() throws Exception {
		given(productService.updateProduct(eq(1000L), any(ProductData.class)))
			.willThrow(new ProductNotFoundException(1000L));

		mockMvc.perform(
				patch("/products/1000")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
						"\"price\":5000}")
			)
			.andExpect(status().isNotFound());

		verify(productService).updateProduct(eq(1000L), any(ProductData.class));
	}

	@Test
	void updateWithInvalidAttributes() throws Exception {
		mockMvc.perform(
				patch("/products/1")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"name\":\"\",\"maker\":\"\"," +
						"\"price\":0}")
			)
			.andExpect(status().isBadRequest());
	}

	@Test
	void destroyWithExistedProduct() throws Exception {
		mockMvc.perform(
				delete("/products/1")
			)
			.andExpect(status().isNoContent());

		verify(productService).deleteProduct(1L);
	}

	@Test
	void destroyWithNotExistedProduct() throws Exception {
		given(productService.deleteProduct(1000L))
			.willThrow(new ProductNotFoundException(1000L));

		mockMvc.perform(
				delete("/products/1000")
			)
			.andExpect(status().isNotFound());

		verify(productService).deleteProduct(1000L);
	}
}
