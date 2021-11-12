package com.ecom.IntegrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ecom.Forms.UpdateProductForm;
import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.ProductService;
import com.ecom.Services.UserService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@ActiveProfiles("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IntegrationProductTest {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    public void shouldRegistNewProduct() {   
        User user = userRepository.findByUserName("testname").get();
        Product product = new Product("A new product", new BigDecimal(10), "A new product description");
        product.setUser(user);

        ResponseEntity<?> response = productService.addProduct(product, user);
        
        // If it could not communicate with the external API, it would return a 502 error
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }    

    @Test
    public void shouldUpdateProduct() {
        User user = userRepository.findByUserName("testname").get();
        Product product = new Product("Another product", new BigDecimal(10), "Another product description");	
        product.setUser(user);
        
        productService.addProduct(product, user);

        Map<String, String> productValues = new HashMap<String, String>();
        productValues.put("name", "A product I'm selling");
        productValues.put("price", "60.0");
        UpdateProductForm productForm = new UpdateProductForm(productValues);

        ResponseEntity<?> response = productService.updateProduct(product.getProductId(), productForm, user);
        
        // If it could not communicate with the external API, it would return a 502 error
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    public void shouldDeleteProduct() {
        User user = userRepository.findByUserName("testname").get();
        Product product = new Product("A product to be deleted", new BigDecimal(10), 
                "A product to be deleted description");	
        product.setUser(user);
        
        productService.addProduct(product, user);

        ResponseEntity<?> response = productService.removeProduct(product.getProductId(), "testname");
        
        // If it could not communicate with the external API, it would return a 502 error
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
