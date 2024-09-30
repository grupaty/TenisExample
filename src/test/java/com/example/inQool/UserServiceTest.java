package com.example.inQool;

import com.example.inQool.dto.UserDTO;
import com.example.inQool.model.Role;
import com.example.inQool.model.User;
import com.example.inQool.service.UserService;
import com.example.inQool.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "Test" ,"User", "password", "1234567890", Role.USER, false);
    }


    @Test
    void testCreateUser() {
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        userService.createUser(user);

        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindByPhoneNumber() {
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(user);

        User foundUser = userService.findByPhoneNumber("1234567890");

        assertNotNull(foundUser);
        assertEquals("Test", foundUser.getFirstName());
        assertEquals("User", foundUser.getSecondName());
        verify(userRepository, times(1)).findByPhoneNumber("1234567890");
    }

    @Test
    void testGetAllUsers() {
        User user2 = new User(2L, "Test", "User2", "password2", "0987654321", Role.USER, false);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSoftDeleteUser() {
        userService.softDeleteUser(user);

        verify(userRepository, times(1)).delete(user);
    }
}