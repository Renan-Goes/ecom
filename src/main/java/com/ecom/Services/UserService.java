package com.ecom.Services;

import java.util.Date;

import com.ecom.Handlers.ExceptionDetails;
import com.ecom.Models.User;
import com.ecom.Models.DTOs.SignUpDTO;
import com.ecom.Repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public ResponseEntity<?> createUser(User user) {
        User foundUser = userRepository.findByEmail(user.getEmail()).get();
        System.out.println("Found user: " + foundUser);

        if(foundUser != null) {            
            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                    "User already exists.", "Use another email.", new Date());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

        foundUser = userRepository.findByUserName(user.getUserName());
        if (foundUser != null) {
            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                    "Username already exists.", "Use another username.", new Date());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        SignUpDTO signUpUser = new SignUpDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpUser);
    }
}
