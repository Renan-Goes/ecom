package com.ecom.Services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ecom.Config.Security.SecurityProperties;
import com.ecom.Handlers.ExceptionDetails;
import com.ecom.Models.Product;
import com.ecom.Models.User;
import com.ecom.Models.DTOs.SignUpDTO;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;
@Log4j2
public class UserService {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ProductRepository productRepository, 
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public ResponseEntity<?> createUser(User user) {
        Optional<User> foundUserOptional = userRepository.findByEmail(user.getEmail());

        if(!foundUserOptional.isEmpty()) {
            log.debug("Someone tried to create an account with the email " + user.getEmail() +
                    ", but an account already exists with email");

            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                    "User already exists.", "Use another email.", new Date());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

        foundUserOptional = userRepository.findByUserName(user.getUserName());

        if (!foundUserOptional.isEmpty()) {
            log.debug("Someone tried to create an account with the username " + user.getUserName() +
                    ", but an account already exists with the username");
            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                    "Username already exists.", "Use another username.", new Date());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        SignUpDTO signUpUser = new SignUpDTO(user);
        
        log.info("User successfully created");
        log.debug("User with email: '" + user.getEmail() + "' and username: '" + user.getUserName() +
                "' was created");

        return ResponseEntity.status(HttpStatus.CREATED).body(signUpUser);
    }

    public ResponseEntity<?> deleteUser() {
        GetUserByTokenService getUser = new GetUserByTokenService(this.userRepository);
        User user = getUser.run();
        
        if(user == null) {
            log.debug("The user that was attempted to be deleted could not be found by token");
            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                    "User was not found by token.", "Try to login in your account.", new Date());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

        List<Product> listOfProductsFromUser = productRepository.findByUserUserName(user.getUserName());

        for(Product productFromUser : listOfProductsFromUser) {
            
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.delete(SecurityProperties.ENDPOINT_REMOVE_PRODUCT + 
                        productFromUser.getProductId());
            }
            catch(Exception e) {
                ExceptionDetails exception = new ExceptionDetails("Bad Gateway", 502, 
                        "Could not communicate with external server.", 
                        "Either the server is down or the product has not been registered.", new Date());
                        
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
            }
            
            productRepository.deleteById(productFromUser.getProductId());
        }

        userRepository.deleteById(user.getUserId());

        log.debug("Use '" + user.getUserName() + "' was deleted, along with its products for sale");
        ExceptionDetails exception = new ExceptionDetails("OK", 200, 
                "Username was successfully deleted along its products for sale.", 
                user.getUserName() + " was deleted, along its products for sale.", new Date());

        return ResponseEntity.ok().body(exception);
    }
}
