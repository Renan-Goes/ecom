package com.ecom.Controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.ecom.Forms.SignUpForm;
import com.ecom.Models.User;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
     
    private UserRepository userRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = new UserService(userRepository, passwordEncoder);
    }

    @PostMapping("/auth/signup")
    @Transactional
    public ResponseEntity<?> createUser(@RequestBody @Valid SignUpForm form) {
        User user = form.convert();
        return userService.createUser(user);
    }
}
