package com.ecom.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Models.DTOs.ProductDTO;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.ProductService;
import com.github.tomakehurst.wiremock.WireMockServer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
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
public class ProductTest {

    public static String PRODUCT_URL = "/product";
    public static String PRODUCT_UPDATE_URL = "/product/updateProduct/";

    @Autowired
    private ProductService productService;

    private WireMockServer wireMockServer;
    
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductRepository productRepository;
    
    private static MockedStatic<ProductDTO> mockedStatic;

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

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        mockedStatic = Mockito.mockStatic(ProductDTO.class);
    }

    @AfterAll
    public static void tearDownAll() {
        mockedStatic.close();
        System.out.println("Cleaned context and closed mock static");
    }

    @Test
    public void shouldGetAllProducts() {
        User user = new User("test@test.com", "password", "testname");
        Pageable paging = PageRequest.of(0, 10);
        List<Product> products = new ArrayList<>();
        Product product = new Product("test1", new BigDecimal("10.5"), "test1");
        product.setUser(user);
        products.add(product);
        Page<Product> productsPaged = new PageImpl<Product>(products);
        Page<ProductDTO> productsDTO = productsPaged.map(productToDTO -> 
                new ProductDTO(product.getUser(), productToDTO));

        when(productRepository.findAll(paging)).thenReturn(productsPaged);
        mockedStatic.when(() -> ProductDTO.convertPage(productsPaged)).thenReturn(productsDTO);
        
        ResponseEntity<?> response = productService.getProducts(paging);
        List<ProductDTO> listOfDTOs = ((List<ProductDTO>)response.getBody());
        String productName = listOfDTOs.get(0).getProduct().get("name");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test1", productName);
    }

    @Test
    public void shouldGetAllProductsFromUser() {
        User user = new User("test@test.com", "password", "testname");
        Pageable paging = PageRequest.of(0, 10);
        List<Product> products = new ArrayList<>();
        Product product = new Product("test2", new BigDecimal("10.5"), "test2");
        product.setUser(user);
        products.add(product);
        Page<Product> productsPaged = new PageImpl<Product>(products);
        Page<ProductDTO> productsDTO = productsPaged.map(productToDTO -> 
                new ProductDTO(product.getUser(), productToDTO));

        when(userRepository.findByUserName(Mockito.any())).thenReturn(Optional.of(user));
        when(productRepository.findByUserUserId(Optional.of(user).get().getUserId(), paging))
                .thenReturn(productsPaged);
        mockedStatic.when(() -> ProductDTO.convertPageByUser(productsPaged, user)).thenReturn(productsDTO);
        
        ResponseEntity<?> response = productService.getProductByUser("testname", paging);

        List<ProductDTO> listOfDTOs = ((List<ProductDTO>)response.getBody());
        String productName = listOfDTOs.get(0).getProduct().get("name");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test2", productName);
    }
    
    @Test
    public void shouldGetProductByPartOfName() {
        User user = new User("test@test.com", "password", "testname");
        Pageable paging = PageRequest.of(0, 10);
        List<Product> products = new ArrayList<>();
        Product product = new Product("test3", new BigDecimal("10.5"), "test3");
        product.setUser(user);
        products.add(product);
        Page<Product> productsPaged = new PageImpl<Product>(products);
        Page<ProductDTO> productsDTO = productsPaged.map(productToDTO -> 
                new ProductDTO(product.getUser(), productToDTO));

        when(productRepository.findByNameLike(Mockito.any(), Mockito.any())).thenReturn(productsPaged);
        mockedStatic.when(() -> ProductDTO.convertPage(productsPaged)).thenReturn(productsDTO);
        
        ResponseEntity<?> response = productService.getProductsLikeByName(paging, "tes");

        List<ProductDTO> listOfDTOs = ((List<ProductDTO>)response.getBody());
        String productName = listOfDTOs.get(0).getProduct().get("name");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test3", productName);
    }

    @Test
    public void shouldAddProduct() {
        Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
        User user = new User("test@test.com", "password", "testname");
        product.setUser(user);
        
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        wireMockServer.stubFor(post(urlEqualTo(PRODUCT_URL))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"productId\":\"01\",\"name\":\"product\"," +
                        "\"price\":\"100.0\"}")));

        ResponseEntity<?> response = productService.addProduct(product, user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(((ProductDTO)response.getBody()).getProduct().get("productId"), product.getProductId());        
    }
    
    @Test
    public void shouldNotAddProductBecauseExternalApiIsOff() {
        Product product = new Product("Product for sale", new BigDecimal("100.0"), "A nice product.");
        User user = new User("test@test.com", "password", "testname");
        product.setUser(user);
        
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        wireMockServer.stubFor(post(urlEqualTo(PRODUCT_URL))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse().withStatus(502)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"title\":\"Bad Gateway\",\"status\":\"502\"," +
                        "\"details\":\"Could not communicate with Registering API.\"," +
                    "\"developerMessage\":\"Try again or wait until server is up again.\"," +
                    "\"timestamp\":\"01-01-2021\"}")));

        ResponseEntity<?> response = productService.addProduct(product, user);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());     
    }
}
