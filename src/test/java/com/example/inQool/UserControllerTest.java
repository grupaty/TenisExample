package com.example.inQool.controller;

import com.example.inQool.dto.UserDTO;
import com.example.inQool.model.Role;
import com.example.inQool.model.User;
import com.example.inQool.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User(1L, "TEST", "ONE", "password1", "1234567890", Role.USER, false);
        User user2 = new User(2L, "TEST", "TWO", "password2", "0987654321", Role.USER, false);
        List<User> userList = Arrays.asList(user1, user2);


        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("TEST", response.getBody().get(0).getFirstName());
        assertEquals("ONE", response.getBody().get(0).getSecondName());
        assertEquals("TEST", response.getBody().get(1).getFirstName());
        assertEquals("TWO", response.getBody().get(1).getSecondName());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserByPhoneNumber_UserFound() {
        User user = new User(1L, "TEST", "ONE", "password1", "1234567890", Role.USER, false);
        when(userService.findByPhoneNumber("1234567890")).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.getUserByPhoneNumber("1234567890");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("TEST", response.getBody().getFirstName());
        assertEquals("ONE", response.getBody().getSecondName());
        verify(userService, times(1)).findByPhoneNumber("1234567890");
    }

    @Test
    public void testGetUserByPhoneNumber_UserNotFound() {
        when(userService.findByPhoneNumber("1234567890")).thenReturn(null);

        ResponseEntity<UserDTO> response = userController.getUserByPhoneNumber("1234567890");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).findByPhoneNumber("1234567890");
    }
}