package com.ecom.Services;

import com.ecom.Models.User;
import com.ecom.Repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;

public class GetUserByTokenService {

    private UserRepository userRepository;

    public GetUserByTokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User run() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User foundUser = this.userRepository.findByEmail(email).get();

        if(foundUser == null) {
            return null;
        }
    
        return foundUser;
      }
}
