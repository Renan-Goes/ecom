package com.ecom.ServiceTests;
import static org.mockito.Mockito.when;

import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserTest {
    
    private UserRepository userRepository;
    private Authentication authentication;
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userRepository = Mockito.mock(UserRepository.class);
        authentication = Mockito.mock(Authentication.class);
        securityContext = Mockito.mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
