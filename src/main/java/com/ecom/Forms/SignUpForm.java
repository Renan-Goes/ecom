package com.ecom.Forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.ecom.Models.User;

import lombok.Data;

@Data
public class SignUpForm {
    
    @NotEmpty
    @NotBlank
    @Email(message="Email must be valid")
    private String email;

    @NotEmpty
    @NotBlank
    @Size(min=5, max=30, message="Password must contain between 5 and 30 characters.")
    private String password;

    @NotEmpty
    @NotBlank
    @Size(min=5, max=20, message="Username must contain between 5 and 20 characters.")
    private String userName;

    public User convert() {
        return new User(email, password, userName);
    }
}
