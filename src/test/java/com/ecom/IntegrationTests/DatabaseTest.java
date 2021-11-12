package com.ecom.IntegrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.util.Optional;

import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@ActiveProfiles("integration")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DatabaseTest {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {        
        String email = "email@test.com";
        String password = "password";
        String userName = "username";
        User user = new User(email, password, userName);

        userRepository.save(user);

        String productName = "testProduct";
        BigDecimal price = new BigDecimal("10.0");
        String description = "product description";
        Product product = new Product(productName, price, description);
        product.setUser(user);
        productRepository.save(product);
    }

    @Test
    public void shouldFindUser() {
        Optional<User> user = userRepository.findByEmail("email@test.com");
        assertEquals("username", user.get().getUserName());
    }

    @Test
    public void shouldNotFindUser() {
        String email = "nouser@test.com";
        System.out.println("Rep: " + userRepository);
        Optional<User> user = userRepository.findByEmail(email);
        assertFalse(user.isPresent());
    }    

    @Test
    public void shouldDeleteUser() {
        userRepository.deleteById("206977cf-c90e-4593-949c-119a69cd813f");
        Optional<User> foundUser = userRepository.findByEmail("test@test.com");

        assertEquals(false, foundUser.isPresent());
    }

    @Test
    public void shouldFindProductById() {
        Product product = new Product("product name", new BigDecimal("10.0"), "product description");
        User user = new User("test2@test.com", "password", "testname2");
        userRepository.save(user);
        product.setUser(user);

        productRepository.save(product);

        Product foundProduct = productRepository.findByProductId(product.getProductId());

        assertEquals("product name", foundProduct.getName());
    }

    @Test
    public void shouldNotFindProductById() {
        String id = "2c92c0817ca540f2017ca56305ff0003";
        System.out.println("Rep: " + userRepository);
        Product product = productRepository.findByProductId(id);
        assertEquals(product, null);
    }

    @Test
    public void shouldDeleteProduct() {
        productRepository.deleteById("23c91e3f-e535-4878-90a4-8cacef92a510");
        Product foundProduct = productRepository.findByProductId("23c91e3f-e535-4878-90a4-8cacef92a510");

        assertEquals(null, foundProduct);
    }
}

