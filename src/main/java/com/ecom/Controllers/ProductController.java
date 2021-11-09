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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(value="Product", description="Manages products to add, delete, list all, " +
        "list by user and list by part of name.", tags={ "Product" })
public class ProductController {
     
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    @PostMapping("/addproduct")
    @CacheEvict(value="productsCache", allEntries=true)
    @ApiOperation(value="Add a single product", tags={ "Product" })
    @Transactional
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductForm form) {
        Product product = form.convert();
        
        GetUserByTokenService getUser = new GetUserByTokenService(this.userRepository);
        User seller = getUser.run();
        
        return productService.addProduct(product, seller);
    }

    @GetMapping("/products")
    @Cacheable("productsCache")
    @ApiOperation(value="Get all products paged", tags={ "Product" })
    public ResponseEntity<?> getProducts(@RequestParam(required=false, defaultValue="0") int page, 
            @RequestParam(required=false, defaultValue="10") int quantity) {
        
        Pageable paging = PageRequest.of(page, quantity);

        return productService.getProducts(paging);
    }
    
    @GetMapping("/products/{userName}")
    @ApiOperation(value="Get all products from a user paged", tags={ "Product" })
    public ResponseEntity<?> getProductByUser(@PathVariable String userName, 
            @RequestParam(required=false, defaultValue="0") int page, 
            @RequestParam(required=false, defaultValue="10") int quantity) {

        Pageable paging = PageRequest.of(page, quantity);

        return productService.getProductByUser(userName, paging);
    }

    @DeleteMapping("/products/{productId}")
    @Transactional
    @ApiOperation(value="Remove a product", tags={ "Product" })
    public ResponseEntity<?> removeProductForSale(@PathVariable String productId) {
        
        GetUserByTokenService getUser = new GetUserByTokenService(this.userRepository);
        User userByToken = getUser.run();

        return productService.removeProduct(productId, userByToken.getUserName());
    }
    
    @GetMapping("/productsearch/{productName}")
    @ApiOperation(value="Get all products by part of its name paged", tags={ "Product" })
    public ResponseEntity<?> getProductsLikeByName(@PathVariable String productName, 
            @RequestParam(required=false, defaultValue="0") int page, 
            @RequestParam(required=false, defaultValue="10") int quantity) {

        Pageable paging = PageRequest.of(page, quantity);

        return productService.getProductsLikeByName(paging, productName);
    }     

    @PatchMapping("/updateproduct/{productId}")
    @ApiOperation(value="Update fields of a product by its id in the url and fields as json", tags={ "Product" })
    @Transactional
    public ResponseEntity<?> updateProduct(@PathVariable String productId, 
            @RequestBody UpdateProductForm form) {
                
        GetUserByTokenService getUser = new GetUserByTokenService(this.userRepository);
        User user = getUser.run();

        return productService.updateProduct(productId, form, user);
    }
}
