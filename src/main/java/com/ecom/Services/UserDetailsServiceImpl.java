package com.ecom.Services;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecom.Models.User;
import com.ecom.Models.UserDetailsImpl;
import com.ecom.Repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
    User foundUser = userRepository.findByEmail(email).get();
    if (foundUser == null) {
      throw new UsernameNotFoundException("Could not find user with email: '" + email + "'");
    } else {
      return new UserDetailsImpl(foundUser);
    }
  }
}
