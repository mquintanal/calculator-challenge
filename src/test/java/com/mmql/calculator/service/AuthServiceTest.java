package com.mmql.calculator.service;

import com.mmql.calculator.dto.AuthRequest;
import com.mmql.calculator.dto.RegisterRequest;
import com.mmql.calculator.model.User;
import com.mmql.calculator.repository.UserRepository;
import com.mmql.calculator.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private EmailValidationService emailValidationService;

    @InjectMocks private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setEmail("user1@example.com");
        request.setPassword("secret123");

        when(passwordEncoder.encode("secret123")).thenReturn("encodedPass");
        when(jwtService.generateToken("user1")).thenReturn("jwt-token");

        var response = authService.register(request);

        assertEquals("jwt-token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        AuthRequest request = new AuthRequest();
        request.setUsername("user1");
        request.setPassword("secret123");

        when(jwtService.generateToken("user1")).thenReturn("jwt-token");

        var response = authService.login(request);

        assertEquals("jwt-token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}