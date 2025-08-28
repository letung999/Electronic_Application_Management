package com.ecommerce.electronicapplicationmanagement.controller;

import com.ecommerce.electronicapplicationmanagement.entity.Product;
import com.ecommerce.electronicapplicationmanagement.request.AddProductRequest;
import com.ecommerce.electronicapplicationmanagement.response.ErrorResponse;
import com.ecommerce.electronicapplicationmanagement.response.Response;
import com.ecommerce.electronicapplicationmanagement.service.BasketService;
import com.ecommerce.electronicapplicationmanagement.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private BasketService basketService;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String URL = "/admin";

    @Test
    void testCreateProductSuccess() throws JsonProcessingException {
        //set up
        HttpHeaders headers = setHeaders();
        AddProductRequest request = AddProductRequest.builder()
                .category("electrical")
                .price(BigDecimal.valueOf(20.00))
                .name("Macbook M2 Pro")
                .stock(1)
                .build();

        Product product = new Product();
        product.setName("Macbook M2 Pro");
        product.setPrice(BigDecimal.valueOf(20.00));
        product.setStock(20);
        product.setCategory("electrical");
        // action
        Mockito.when(productService.initProduct(Mockito.any())).thenReturn(product);
        HttpEntity<AddProductRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Response> response = restTemplate.exchange(URL + "/product", HttpMethod.POST, entity, Response.class);
        Product result = objectMapper.readValue(objectMapper.writeValueAsString(response.getBody().getData()), Product.class);
        // assert
        Assertions.assertTrue(response.getBody().isSuccess());
        Assertions.assertEquals(product.getName(), result.getName());
        Assertions.assertEquals(product.getPrice(), result.getPrice());
        Assertions.assertEquals(product.getStock(), result.getStock());
        Assertions.assertEquals(product.getCategory(), result.getCategory());

    }

    @Test
    void testCreateProductWithNameProductLessThanThreeCharacter() {
        //set up
        HttpHeaders headers = setHeaders();
        AddProductRequest request = AddProductRequest.builder()
                .category("electrical")
                .price(BigDecimal.valueOf(20.00))
                .name("Ma")
                .stock(1)
                .build();

        Product product = new Product();
        product.setName("Macbook M2 Pro");
        product.setPrice(BigDecimal.valueOf(20.00));
        product.setStock(20);
        product.setCategory("electrical");

        // action
        HttpEntity<AddProductRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(URL + "/product", HttpMethod.POST, entity, ErrorResponse.class);

        // assert
        Assertions.assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Assertions.assertEquals("Product name must be between 3 and 255 characters", response.getBody().getError().get("name"));
    }

    @Test
    void testCreateProductWithPriceProductLessThanZero() {
        //set up
        HttpHeaders headers = setHeaders();
        AddProductRequest request = AddProductRequest.builder()
                .category("electrical")
                .price(BigDecimal.valueOf(-1L))
                .name("Macbook M2 Pro")
                .stock(1)
                .build();

        Product product = new Product();
        product.setName("Macbook M2 Pro");
        product.setPrice(BigDecimal.valueOf(20.00));
        product.setStock(20);
        product.setCategory("electrical");

        //action
        HttpEntity<AddProductRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(URL + "/product", HttpMethod.POST, entity, ErrorResponse.class);

        //assert
        Assertions.assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Assertions.assertEquals("The price can't less than zero", response.getBody().getError().get("price"));
    }

    @Test
    void testCreateProductWithStockProductLessThanOne() {
        //set up
        HttpHeaders headers = setHeaders();
        AddProductRequest request = AddProductRequest.builder()
                .category("electrical")
                .price(BigDecimal.valueOf(20L))
                .name("Macbook M2 Pro")
                .stock(0)
                .build();

        Product product = new Product();
        product.setName("Macbook M2 Pro");
        product.setPrice(BigDecimal.valueOf(20.00));
        product.setStock(20);
        product.setCategory("electrical");


        //action
        HttpEntity<AddProductRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(URL + "/product", HttpMethod.POST, entity, ErrorResponse.class);

        //assert
        Assertions.assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Assertions.assertEquals("The stock can't less than one stock", response.getBody().getError().get("stock"));
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
