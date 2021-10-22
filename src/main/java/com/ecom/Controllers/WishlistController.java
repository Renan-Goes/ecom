package com.ecom.Controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.ecom.Forms.WishlistForm;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.WishlistService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WishlistController {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private WishlistService wishlistService;

    public WishlistController(ProductRepository productRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.wishlistService = new WishlistService(productRepository, userRepository);
    }

    @PostMapping("/buy")
    @CacheEvict(value="productsCache", allEntries=true)
    @Transactional
    public ResponseEntity<?> addProducts(@RequestBody @Valid WishlistForm form) {
        return this.wishlistService.addProducts(form);
    }
    
}
