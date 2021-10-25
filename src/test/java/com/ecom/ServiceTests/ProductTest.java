// package com.ecom.ServiceTests;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// import com.ecom.Models.Product;
// import com.ecom.Models.User;
// import com.ecom.Models.DTOs.ProductDTO;
// import com.ecom.Repository.ProductRepository;
// import com.ecom.Repository.UserRepository;
// import com.ecom.Services.GetUserByTokenService;
// import com.ecom.Services.ProductService;

// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.junit.Test;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.runner.RunWith;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.MockitoAnnotations;
// import org.mockito.junit.MockitoJUnitRunner;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;

// @SpringBootTest
// @RunWith(MockitoJUnitRunner.Silent.class)
// public class ProductTest {
    
//     private UserRepository userRepository;
//     private ProductRepository productRepository;
//     private Authentication authentication;
//     private SecurityContext securityContext;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);

//         userRepository = Mockito.mock(UserRepository.class);
//         productRepository = Mockito.mock(ProductRepository.class);
//         authentication = Mockito.mock(Authentication.class);
//         securityContext = Mockito.mock(SecurityContext.class);

//         when(securityContext.getAuthentication()).thenReturn(authentication);
//         SecurityContextHolder.setContext(securityContext);        
//     }

//     @Test
//     public void shouldGetAllProducts() {
//         User user = new User("test@test.com", "password", "testname");
//         user.setUserId("1");
//         Pageable paging = PageRequest.of(0, 10);
//         List<Product> products = new ArrayList<>();
//         Page<Product> productsPaged = new PageImpl<Product>(products);

//         System.out.println("userRepository: " + userRepository);

//         when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
//         when(productRepository.findByUserUserId(user.getUserId(), paging)).thenReturn(productsPaged);

//         ProductService productService = new ProductService(productRepository, userRepository);
        
//         ResponseEntity<?> response = productService.getProducts(paging);

//         assertEquals(response.getStatusCode(), HttpStatus.OK);
//     }

//     @Test
//     public void shouldAddProduct() {
//         Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
//         User user = new User("test@test.com", "password", "testname");
//         when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
//         System.out.println("Before when product");
//         when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

//         System.out.println("Before functoin call");
//         ProductService productService = new ProductService(productRepository, userRepository);
//         ResponseEntity<?> response = productService.addProduct(product);

//         assertEquals(response.getStatusCode(), HttpStatus.CREATED);
//         assertEquals(((ProductDTO)response.getBody()).getProduct().get("productId"), product.getProductId());        
//     }
    
// }
