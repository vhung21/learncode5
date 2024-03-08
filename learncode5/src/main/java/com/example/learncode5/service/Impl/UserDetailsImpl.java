package com.example.learncode5.service.Impl;

import com.example.learncode5.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String fullname;
    private String username;
    private String password;
    private String email;
    private Collection<? extends GrantedAuthority> roles;

    public UserDetailsImpl(){

    }
    public static UserDetailsImpl build(User user){
        System.out.println(user.getRoles());
        List<GrantedAuthority> authorities=user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities);
    }

    public UserDetailsImpl(Long id,String fullname, String username, String password, String email, Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.fullname=fullname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
    public String getEmail(){
        return this.email;
    }
    public String getFullName(){
        return this.fullname;
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}