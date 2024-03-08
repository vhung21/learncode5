package com.example.learncode5.DTO;


import com.example.learncode5.entities.Role;
import com.example.learncode5.entities.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private long id;
    private String fullName;
    private String username;
    private String email;
    private List<String> roles = new ArrayList<>();

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName=user.getFullName();
        this.email = user.getEmail();
        for (Role role : user.getRoles()) {
            this.roles.add(role.getName());
        }
    }
}
