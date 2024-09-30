package com.example.inQool;

import com.example.inQool.controller.LoginController;
import com.example.inQool.model.User;
import com.example.inQool.security.JWTUtil;
import com.example.inQool.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        String phoneNumber = "1234567890";
        String password = "password1";
        User user = new User(1L, "First", "Last", phoneNumber, password, null, false);
        String token = "mocked_jwt_token";

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password))).thenReturn(null);
        when(userService.findByPhoneNumber(phoneNumber)).thenReturn(user);
        when(jwtUtil.generateToken(phoneNumber)).thenReturn(token);

        ResponseEntity<?> response = loginController.login(phoneNumber, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful!", response.getBody());
        assertEquals("Bearer " + token, ((HttpHeaders) response.getHeaders()).getFirst("Authorization"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1)).findByPhoneNumber(phoneNumber);
        verify(jwtUtil, times(1)).generateToken(phoneNumber);
    }

    @Test
    public void testLogin_Failure() {
        String phoneNumber = "1234567890";
        String password = "password1";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<?> response = loginController.login(phoneNumber, password);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, never()).findByPhoneNumber(anyString());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    public void testRegisterUser_Success() {
        User user = new User(1L, "First", "Last", "password1", "1234567890", null, false);

        doNothing().when(userService).createUser(any(User.class));

        ResponseEntity<?> response = loginController.registerUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully!", response.getBody());

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    public void testRegisterUser_Failure() {
        User user = new User(1L, "First", "Last", "password1", "1234567890", null, false);

        doThrow(new RuntimeException("Error creating user")).when(userService).createUser(any(User.class));

        ResponseEntity<?> response = loginController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User could not be registered", response.getBody());

        verify(userService, times(1)).createUser(any(User.class));
    }
}
