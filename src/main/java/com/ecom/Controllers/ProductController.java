package com.ecom.Controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.ecom.Forms.ProductForm;
import com.ecom.Forms.UpdateProductForm;
import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.GetUserByTokenService;
import com.ecom.Services.ProductService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
     
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private ProductService productService;

    public ProductController(ProductRepository productRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = new ProductService(productRepository, userRepository);
    }

    @PostMapping("/addproduct")
    @CacheEvict(value="productsCache", allEntries=true)
    @Transactional
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductForm form) {
        Product product = form.convert();
        
        return productService.addProduct(product);
    }

    @GetMapping("/products")
    @Cacheable("productsCache")
    public ResponseEntity<?> getProducts(@RequestParam(required=false, defaultValue="0") int page, 
            @RequestParam(required=false, defaultValue="10") int quantity) {
        
        Pageable paging = PageRequest.of(page, quantity);

        return productService.getProducts(paging);
    }
    
    @GetMapping("/products/{userName}")
    public ResponseEntity<?> getProductByUser(@PathVariable String userName, 
            @RequestParam(required=false, defaultValue="0") int page, 
            @RequestParam(required=false, defaultValue="10") int quantity) {

        Pageable paging = PageRequest.of(page, quantity);

        return productService.getProductByUser(userName, paging);
    }

    @DeleteMapping("/products/{productId}")
    @Transactional
    public ResponseEntity<?> removeProductForSale(@PathVariable String productId) {
        
        GetUserByTokenService getUser = new GetUserByTokenService(this.userRepository);
        User userByToken = getUser.run();

        return productService.removeProduct(productId, userByToken.getUserName());
    }
    
    @GetMapping("/productsearch/{productName}")
    public ResponseEntity<?> getProductsLikeByName(@PathVariable String productName, 
            @RequestParam(required=false, defaultValue="0") int page, 
            @RequestParam(required=false, defaultValue="10") int quantity) {

        Pageable paging = PageRequest.of(page, quantity);

        return productService.getProductsLikeByName(paging, productName);
    }     

    @RequestMapping(value="/updateproduct/{productId}")
    @Transactional
    public ResponseEntity<?> updateProduct(@PathVariable String productId, 
            @RequestBody UpdateProductForm form) {
        return productService.updateProduct(productId, form);
    }
}
