package com.ecom.Controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.ecom.Forms.SignUpForm;
import com.ecom.Models.User;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.GetUserByTokenService;
import com.ecom.Services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController    
@RequiredArgsConstructor
@Api(value="User", description="Manages adding and deleting users.", tags={ "User" })
public class UserController {
     
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

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
        GetUserByTokenService getUser = new GetUserByTokenService(this.userRepository);
        User user = getUser.run();
        return userService.deleteUser(user);
    }
}
