package com.example.learncode5.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "role")
public class Role{
    public static final String ADMIN = "admin";
    public static final String USER = "user";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users= new ArrayList<>();

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
}
