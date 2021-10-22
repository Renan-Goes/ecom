package com.ecom.Controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.ecom.Forms.SignInForm;
import com.ecom.Forms.SignUpForm;
import com.ecom.Models.User;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController    
@Api(value="User", description="Manages adding and deleting users.", tags={ "User" })
public class UserController {
     
    private UserRepository userRepository;
    private UserService userService;
    private ProductRepository productRepository;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, ProductRepository productRepository, 
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.userService = new UserService(userRepository, productRepository, passwordEncoder);
    }

    @PostMapping("/auth/signup")
    @Transactional
    @ApiOperation(value="Creates user with unique email and username", tags={ "User" })
    public ResponseEntity<?> createUser(@RequestBody @Valid SignUpForm form) {
        User user = form.convert();
        return userService.createUser(user);
    }

    @DeleteMapping("/auth/deleteaccount")
    @Transactional
    @ApiOperation(value="Deletes the user alongside the products it has for sale", tags={ "Product" })
    public ResponseEntity<?> deleteUser() {
        return userService.deleteUser();
    }
}
