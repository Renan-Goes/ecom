package com.ecom.UnitTests.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.ProductService;
import com.github.tomakehurst.wiremock.WireMockServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductDeleteTest {

    public static String PRODUCT_DELETE_URL = "/product/removeProduct/";

    @Autowired
    private ProductService productService;

    private WireMockServer wireMockServer;
    
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        wireMockServer = new WireMockServer(1050);
        wireMockServer.start();
    }
    
    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        productRepository.deleteAll();
        wireMockServer.stop();
        System.out.println("Deleted repositories.");
    }
    
    @Test
    public void shouldDeleteProduct() {
        Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
        User user = new User("test@test.com", "password", "testname");
        product.setUser(user);
        userRepository.save(user);
        productRepository.save(product);

        when(productRepository.findByProductId(Mockito.any())).thenReturn(product);

        wireMockServer.stubFor(put(urlEqualTo(PRODUCT_DELETE_URL + product.getProductId()))
        .withHeader("Content-Type", equalTo("application/json"))
        .willReturn(aResponse().withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody("{\"productId\":\"01\",\"name\":\"Product for sale\"," +
                "\"price\":\"100.0\"," +
                "\"user\":\"testname\"}")));

        ResponseEntity<?> response = productService.removeProduct(product.getProductId(), user.getUserName());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    public void shouldNotDeleteProductBecauseProductIdIsInvalid() {
        Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
        User user = new User("test@test.com", "password", "testname");
        product.setUser(user);
        userRepository.save(user);

        wireMockServer.stubFor(put(urlEqualTo(PRODUCT_DELETE_URL + product.getProductId()))
        .withHeader("Content-Type", equalTo("application/json"))
        .willReturn(aResponse().withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody("{\"productId\":\"01\",\"name\":\"Product for sale\"," +
                "\"price\":\"100.0\"," +
                "\"user\":\"testname\"}")));

        ResponseEntity<?> response = productService.removeProduct(product.getProductId(), user.getUserName());
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    public void shouldNotDeleteProductBecauseUserIsInvalid() {
        Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
        User user = new User("test@test.com", "password", "testname");
        product.setUser(user);
        userRepository.save(user);
        productRepository.save(product);

        when(productRepository.findByProductId(Mockito.any())).thenReturn(product);

        wireMockServer.stubFor(put(urlEqualTo(PRODUCT_DELETE_URL + product.getProductId()))
        .withHeader("Content-Type", equalTo("application/json"))
        .willReturn(aResponse().withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody("{\"productId\":\"01\",\"name\":\"Product for sale\"," +
                "\"price\":\"100.0\"," +
                "\"user\":\"testname\"}")));

        ResponseEntity<?> response = productService.removeProduct(product.getProductId(), "someone");
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}