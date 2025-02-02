package com.codemind.product_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codemind.product_service.dto.ProductRequest;
import com.codemind.product_service.dto.ProductResponse;
import com.codemind.product_service.model.Product;
import com.codemind.product_service.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public void createProduct(ProductRequest productRequest) {
		Product product = new Product(productRequest.getName(), productRequest.getDescription(),
				productRequest.getPrice());
		productRepository.save(product);
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(product -> mapToProductResponse(product)).collect(Collectors.toList());
	}

	public ProductResponse mapToProductResponse(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
	}

}
