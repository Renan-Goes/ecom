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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(value="Wishlist", description="Enables purchasing products.", tags={ "Wishlist" })
public class WishlistController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WishlistService wishlistService;

    @PostMapping("/buy")
    @CacheEvict(value="productsCache", allEntries=true)
    @Transactional
    @ApiOperation(value="Buys products from a list sent by the user, cannot purchase own products", tags={ "Product" })
    public ResponseEntity<?> addProducts(@RequestBody @Valid WishlistForm form) {
        return this.wishlistService.addProducts(form);
    }
    
}
