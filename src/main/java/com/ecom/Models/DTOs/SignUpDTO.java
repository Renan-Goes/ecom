package com.ecom.Models.DTOs;

import java.util.HashMap;
import java.util.Map;

import com.ecom.Models.User;

import lombok.Data;

@Data
public class SignUpDTO {
    
    private Map<String, String> user = new HashMap<String, String>();
  
    public SignUpDTO(User user) {
      this.user.put("id", user.getUserId());
      this.user.put("name", user.getUserName());
      this.user.put("email", user.getEmail());
    }    
}
