package com.ecom.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ecom.Forms.UpdateProductForm;
import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Models.DTOs.ProductDTO;
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
public class ProductUpdateTest {

    public static String PRODUCT_UPDATE_URL = "/product/updateProduct/";

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
    public void shouldUpdateProduct() {
        Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
        User user = new User("test@test.com", "password", "testname");
        product.setUser(user);
        userRepository.save(user);
        productRepository.save(product);

        Map<String, String> productValues = new HashMap<String, String>();
        productValues.put("name", "A product I'm selling");
        productValues.put("price", "10.0");
        UpdateProductForm productForm = new UpdateProductForm(productValues);

        when(productRepository.findByProductId(Mockito.any())).thenReturn(product);

        wireMockServer.stubFor(put(urlEqualTo(PRODUCT_UPDATE_URL + product.getProductId()))
        .withHeader("Content-Type", equalTo("application/json"))
        .willReturn(aResponse().withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody("{\"productId\":\"01\",\"name\":\"A product I'm selling\"," +
                "\"price\":\"10.0\"," +
                "\"user\":\"testname\"}")));
        
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        ResponseEntity<?> response = productService.updateProduct(product.getProductId(), productForm, user);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(((ProductDTO)response.getBody()).getProduct().get("name"), "A product I'm selling");
        assertEquals(((ProductDTO)response.getBody()).getProduct().get("price"), "10.0");
    }
    
    @Test
    public void shouldNotUpdateProductBecauseFieldIsInvalid() {
        Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
        User user = new User("test@test.com", "password", "testname");
        product.setUser(user);
        userRepository.save(user);
        productRepository.save(product);

        Map<String, String> productValues = new HashMap<String, String>();
        productValues.put("productId", "101");
        UpdateProductForm productForm = new UpdateProductForm(productValues);

        when(productRepository.findByProductId(Mockito.any())).thenReturn(product);
        
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        ResponseEntity<?> response = productService.updateProduct(product.getProductId(), productForm, user);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    
    @Test
    public void shouldNotUpdateProductBecauseProductIdIsInvalid() {
        Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
        User user = new User("test@test.com", "password", "testname");
        product.setUser(user);
        userRepository.save(user);
        productRepository.save(product);

        Map<String, String> productValues = new HashMap<String, String>();
        productValues.put("name", "A product I'm selling");
        productValues.put("price", "10.0");
        UpdateProductForm productForm = new UpdateProductForm(productValues);

        ResponseEntity<?> response = productService.updateProduct(product.getProductId(), productForm, user);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    public void shouldNotUpdateProductBecauseExternalApiIsOff() {
        Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
        User user = new User("test@test.com", "password", "testname");
        product.setUser(user);
        userRepository.save(user);
        productRepository.save(product);

        Map<String, String> productValues = new HashMap<String, String>();
        productValues.put("name", "A product I'm selling");
        productValues.put("price", "10.0");
        UpdateProductForm productForm = new UpdateProductForm(productValues);
        
        when(productRepository.findByProductId(Mockito.any())).thenReturn(product);

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        wireMockServer.stubFor(post(urlEqualTo(PRODUCT_UPDATE_URL + product.getProductId()))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse().withStatus(502)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"title\":\"Bad Gateway\",\"status\":\"502\"," +
                        "\"details\":\"Could not communicate with Registering API.\"," +
                    "\"developerMessage\":\"Try again or wait until server is up again.\"," +
                    "\"timestamp\":\"01-01-2021\"}")));

        ResponseEntity<?> response = productService.updateProduct(product.getProductId(), productForm, user);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());     
    }
}
