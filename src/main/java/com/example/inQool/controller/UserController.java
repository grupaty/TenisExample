package com.example.inQool.controller;

import com.example.inQool.dto.UserDTO;
import com.example.inQool.model.User;
import com.example.inQool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<UserDTO> getUserByPhoneNumber(@PathVariable String phoneNumber) {
        User user = userService.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDTO userDto = new UserDTO(user);
        return ResponseEntity.ok(userDto);
    }
}