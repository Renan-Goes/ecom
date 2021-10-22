package com.ecom.RepositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Models.DTOs.ProductDTO;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.GetUserByTokenService;
import com.ecom.Services.ProductService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
public class RepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldNotFindUser() {
        String email = "nouser@test.com";
        Optional<User> user = userRepository.findByEmail(email);
        assertEquals(user.get(), null);
    }

    @Test
    public void shouldFindUser() {
        String email = "test@test.com";
        System.out.println("Rep: " + userRepository);
        User user = userRepository.findByEmail(email).get();
        assertEquals(user.getEmail(), email);
    }

    @Test
    public void shouldFindProductById() {
        String id = "2c92c0817ca540f2017ca56305ff0003";
        Product product = productRepository.findByProductId(id);
        assertEquals(id, product.getProductId());
    }
}

