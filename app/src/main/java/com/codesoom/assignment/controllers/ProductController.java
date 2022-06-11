package com.codesoom.assignment.controllers;

import java.util.List;

import javax.validation.Valid;

import com.codesoom.assignment.utils.Permission;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.exception.DecodingInValidTokenException;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
	private final ProductService productService;
	private final AuthenticationService authenticationService;

	public ProductController(ProductService productService, AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
		this.productService = productService;
	}

	@GetMapping
	public List<Product> list() {
		return productService.getProducts();
	}

	@GetMapping("{id}")
	public Product detail(@RequestHeader("Authorization") String token, @PathVariable Long id) {
		try {
			authenticationService.decode(token);
		} catch (SecurityException e) {
			throw new DecodingInValidTokenException(token);
		}

		return productService.getProduct(id);
	}

	@PostMapping
	@Permission
	@ResponseStatus(HttpStatus.CREATED)
	public Product create(@RequestHeader("Authorization") String token, @RequestBody @Valid ProductData productData) {
		try {
			authenticationService.decode(token);
		} catch (SecurityException e) {
			throw new DecodingInValidTokenException(token);
		}
		return productService.createProduct(productData);
	}

	@PatchMapping("{id}")
	@Permission
	public Product update(@PathVariable Long id, @RequestBody @Valid ProductData productData) {
		return productService.updateProduct(id, productData);
	}

	@DeleteMapping("{id}")
	@Permission
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void destroy(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}
