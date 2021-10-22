package com.ecom.Services;

import java.util.Date;
import java.util.Optional;

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
        System.out.println("User email: " + user.getEmail());
        Optional<User> foundUserOptional = userRepository.findByEmail(user.getEmail());

        if(!foundUserOptional.isEmpty()) {
            
            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                    "User already exists.", "Use another email.", new Date());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

        foundUserOptional = userRepository.findByUserName(user.getUserName());

        if (!foundUserOptional.isEmpty()) {
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
