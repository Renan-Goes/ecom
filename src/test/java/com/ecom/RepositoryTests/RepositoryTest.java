package com.ecom.RepositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.util.Optional;

import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doAnswer;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RepositoryTest {
    
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
    public void shouldFindProductById() {
        Product product = new Product("product name", new BigDecimal("10.0"), "product description");
        User user = new User("test@test.com", "password", "testname");
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
}

