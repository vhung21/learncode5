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
public class RoleDTO {
    private long id;
    private String name;
    private List<String> users = new ArrayList<>();
    private List<String> permissions = new ArrayList<>();

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        for (User user : role.getUsers()) {
            this.users.add(user.getUsername());
        }
    }
}