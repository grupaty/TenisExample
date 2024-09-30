package com.example.inQool.dto;

import com.example.inQool.model.Role;
import com.example.inQool.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String secondName;
    private String phoneNumber;
    private Role role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.secondName = user.getSecondName();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();
    }
}