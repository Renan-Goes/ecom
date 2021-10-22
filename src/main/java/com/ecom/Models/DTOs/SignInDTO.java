package com.ecom.Models.DTOs;

import java.util.HashMap;
import java.util.Map;

import com.ecom.Models.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignInDTO {
    
  private Map<String, String> user = new HashMap<String, String>();
  private String token;

  public SignInDTO(User user, String token) {
    this.user.put("id", user.getUserId());
    this.user.put("name", user.getUserName());
    this.user.put("email", user.getEmail());

    this.token = token;
  }
}
