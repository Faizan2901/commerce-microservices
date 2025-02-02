package com.codemind.product_service;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.codemind.product_service.dto.ProductRequest;
import com.codemind.product_service.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private static MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	static {
        mongoDBContainer.start();
    }

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {

	    ProductRequest productRequest = getProductRequest();

	    mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(productRequest)))
	            .andExpect(status().isCreated());
	    Assertions.assertTrue(productRepository.findAll().size()==1);
	}


	private ProductRequest getProductRequest() {

		return new ProductRequest("Dell", "Latitude", BigDecimal.valueOf(80000));

	}

}
