package com.ecom.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ecom.Config.Security.SecurityProperties;
import com.ecom.Forms.WishlistForm;
import com.ecom.Handlers.ExceptionDetails;
import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Models.Wishlist;
import com.ecom.Models.DTOs.WishlistDTO;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class WishlistService {

    private ProductRepository productRepository;
    private UserRepository userRepository;

    public WishlistService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    
    public ResponseEntity<?> addProducts(WishlistForm form) {
        
        GetUserByTokenService getUser = new GetUserByTokenService(this.userRepository);
        User buyer = getUser.run();

        List<Product> listOfProductsToBuy = new ArrayList<>();
        
        for(String productId : form.getListOfProductIds()) {
            
            Product productToBuy = productRepository.findByProductId(productId);

            if(productToBuy == null) {
                ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                "You're trying to buy a product that does not exist.", 
                "Check if '" + productId + "' is the correct ID or if the product exists.", new Date());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
            }
            
            listOfProductsToBuy.add(productToBuy);
        }

        Wishlist wishlist = new Wishlist(listOfProductsToBuy);

        WishlistDTO wishlistDTO = new WishlistDTO(wishlist, buyer.getUserName());

        for(Product productInList : listOfProductsToBuy) {

            if(productInList.getUser().getUserName() == buyer.getUserName()) {
                ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                "You're trying to buy your own product.", 
                "Remove your own product (with id: '" + productInList.getProductId() + 
                "') from the list in order to make the purchase.", new Date());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
            }

            RestTemplate restTemplate = new RestTemplate();
            
            try {
                restTemplate.delete(SecurityProperties.ENDPOINT_REMOVE_PRODUCT + 
                        productInList.getProductId());
            }
            catch(Exception e) {
                ExceptionDetails exception = new ExceptionDetails("Bad Gateway", 502, 
                        "Could not communicate with external server.", 
                        "Either the server is down or the product has not been registered.", new Date());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
            }

            productRepository.deleteById(productInList.getProductId());
        }

        return ResponseEntity.ok().body(wishlistDTO);
    }
}
