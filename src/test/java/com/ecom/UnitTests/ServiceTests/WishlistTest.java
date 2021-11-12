package com.ecom.UnitTests.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecom.Forms.WishlistForm;
import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.WishlistService;
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
public class WishlistTest {

    public static String PRODUCT_DELETE_URL = "/product/removeProduct/";

    @Autowired
    private WishlistService wishlistService;

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
    public void shouldBuyProducts() {
        User buyer = new User("buyer", "password", "buyer");
        userRepository.save(buyer);
        List<String> productsIds = new ArrayList<>();
        productsIds.add("1");
        productsIds.add("2");
        Map<String, Product> products = new HashMap<String, Product>();
        Product product1 = new Product("test1", new BigDecimal("10.5"), "test1");
        Product product2 = new Product("test2", new BigDecimal("12.5"), "test2");
        User user = new User("test@test.com", "password", "testname");
        product1.setUser(user);
        product2.setUser(user);
        userRepository.save(user);
        productRepository.save(product1);
        productRepository.save(product2);
        products.put("1", product1);
        products.put("2", product2);

        WishlistForm wishlistForm = new WishlistForm(productsIds);
        
        for(String productId : productsIds) {
                    
            when(productRepository.findByProductId(Mockito.any()))
                    .thenReturn(products.get(productId));
                    
            wireMockServer.stubFor(delete(urlEqualTo(PRODUCT_DELETE_URL + "null"))
                    .willReturn(aResponse().withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"title\":\"OK\",\"status\":\"200\"," +
                            "\"details\":\"The product was deleted from the database.\"," +
                        "\"developerMessage\":\"The product was deleted from user\"," +
                        "\"timestamp\":\"01-01-2021\"}")));
        }

        ResponseEntity<?> response = wishlistService.addProducts(wishlistForm, buyer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    public void shouldNotBuyProductsBecauseSecondIdIsInvalid() {
        User buyer = new User("buyer", "password", "buyer");
        userRepository.save(buyer);
        List<String> productsIds = new ArrayList<>();
        productsIds.add("1");
        productsIds.add("2");
        Map<String, Product> products = new HashMap<String, Product>();
        Product product1 = new Product("test1", new BigDecimal("10.5"), "test1");
        Product product2 = new Product("test2", new BigDecimal("12.5"), "test2");
        User user = new User("test@test.com", "password", "testname");
        product1.setUser(user);
        product2.setUser(user);
        userRepository.save(user);
        productRepository.save(product1);
        productRepository.save(product2);
        products.put("1", product1);
        products.put("2", product2);

        WishlistForm wishlistForm = new WishlistForm(productsIds);
        
        when(productRepository.findByProductId("1"))
                .thenReturn(product1);
        
        for(String productId : productsIds) {
                    
            wireMockServer.stubFor(delete(urlEqualTo(PRODUCT_DELETE_URL + "null"))
                    .willReturn(aResponse().withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"title\":\"OK\",\"status\":\"200\"," +
                            "\"details\":\"The product was deleted from the database.\"," +
                        "\"developerMessage\":\"The product was deleted from user\"," +
                        "\"timestamp\":\"01-01-2021\"}")));
        }

        ResponseEntity<?> response = wishlistService.addProducts(wishlistForm, buyer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void shouldNotBuyProductsBecauseUserIsTryingToBuyFromItself() {
        List<String> productsIds = new ArrayList<>();
        productsIds.add("1");
        productsIds.add("2");
        Map<String, Product> products = new HashMap<String, Product>();
        Product product1 = new Product("test1", new BigDecimal("10.5"), "test1");
        Product product2 = new Product("test2", new BigDecimal("12.5"), "test2");
        User user = new User("test@test.com", "password", "testname");
        product1.setUser(user);
        product2.setUser(user);
        userRepository.save(user);
        productRepository.save(product1);
        productRepository.save(product2);
        products.put("1", product1);
        products.put("2", product2);

        WishlistForm wishlistForm = new WishlistForm(productsIds);
        
        for(String productId : productsIds) {
                    
            when(productRepository.findByProductId(Mockito.any()))
                    .thenReturn(products.get(productId));

            wireMockServer.stubFor(delete(urlEqualTo(PRODUCT_DELETE_URL + "null"))
                    .willReturn(aResponse().withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"title\":\"OK\",\"status\":\"200\"," +
                            "\"details\":\"The product was deleted from the database.\"," +
                        "\"developerMessage\":\"The product was deleted from user\"," +
                        "\"timestamp\":\"01-01-2021\"}")));
        }

        ResponseEntity<?> response = wishlistService.addProducts(wishlistForm, user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void shouldNoyBuyProductsBecauseExternalApiIsOff() {
        User buyer = new User("buyer", "password", "buyer");
        userRepository.save(buyer);
        List<String> productsIds = new ArrayList<>();
        productsIds.add("1");
        productsIds.add("2");
        Map<String, Product> products = new HashMap<String, Product>();
        Product product1 = new Product("test1", new BigDecimal("10.5"), "test1");
        Product product2 = new Product("test2", new BigDecimal("12.5"), "test2");
        User user = new User("test@test.com", "password", "testname");
        product1.setUser(user);
        product2.setUser(user);
        userRepository.save(user);
        productRepository.save(product1);
        productRepository.save(product2);
        products.put("1", product1);
        products.put("2", product2);

        WishlistForm wishlistForm = new WishlistForm(productsIds);
        
        for(String productId : productsIds) {
                    
            when(productRepository.findByProductId(Mockito.any()))
                    .thenReturn(products.get(productId));
                    
            wireMockServer.stubFor(delete(urlEqualTo(PRODUCT_DELETE_URL + "null"))
                    .willReturn(aResponse().withStatus(502)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"title\":\"Bad Gateway\",\"status\":\"502\"," +
                        "\"details\":\"Could not communicate with external server.\"," +
                        "\"developerMessage\":\"Either the server is down or the product has not been registered.\"," +
                        "\"timestamp\":\"01-01-2021\"}")));
        }

        ResponseEntity<?> response = wishlistService.addProducts(wishlistForm, buyer);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    }
}
