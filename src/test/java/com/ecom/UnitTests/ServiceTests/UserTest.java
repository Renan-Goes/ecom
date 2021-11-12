package com.ecom.UnitTests.ServiceTests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import com.ecom.Models.User;
import com.ecom.Models.DTOs.SignUpDTO;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;
import com.ecom.Services.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserTest {
    
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        productRepository.deleteAll();
        System.out.println("Deleted repositories.");
    }

    @Test
    public void shouldCreateUser() {
        User user = new User("test2@test.com", "password", "testname2");
        
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserService userService = new UserService(userRepository, productRepository, passwordEncoder);
        ResponseEntity<?> response = userService.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(((SignUpDTO)response.getBody()).getUser().get("email"), user.getEmail());
    }     

    @Test
    public void shouldNotCreateUserBecauseItAlreadyExists() {
        User user = new User("test@test.com", "12345", "testname");
        userRepository.save(user);
        
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        
        UserService userService = new UserService(userRepository, productRepository, passwordEncoder);
        ResponseEntity<?> response = userService.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldNotCreateUserBecausePasswordTooSmall() {
        User user = new User("test4@test.com", "12", "testname4");
        
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        
        UserService userService = new UserService(userRepository, productRepository, passwordEncoder);
        ResponseEntity<?> response = userService.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldDeleterUser() {
        User user = new User("test3@test.com", "password", "testname3");
        userRepository.save(user);
        
        when(productRepository.findByUserUserName(Mockito.any())).thenReturn(new ArrayList<>());

        UserService userService = new UserService(userRepository, productRepository, passwordEncoder);

        ResponseEntity<?> response = userService.deleteUser(user);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
