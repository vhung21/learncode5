package com.example.learncode5.controller;

import com.example.learncode5.entities.Role;
import com.example.learncode5.entities.User;
import com.example.learncode5.jwt.JwtProvider;
import com.example.learncode5.payload.Request.LoginRequest;
import com.example.learncode5.payload.Request.RegistrationRequest;
import com.example.learncode5.payload.Response.JwtResponse;
import com.example.learncode5.payload.Response.ResponseObject;
import com.example.learncode5.repository.RoleRepository;
import com.example.learncode5.repository.UserRepository;
import com.example.learncode5.service.Impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth/")
class AuthController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("login")
    public ResponseEntity<ResponseObject> login(@RequestBody LoginRequest loginRequest) {
        if (userRepository.existsByUsername(loginRequest.getUsername()) == false) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("failed", "Username was not exists", null));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.createToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        System.out.println("dang nhap thanh cong");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("success", "",
                        new JwtResponse(
                                jwt,
                                userDetails.getUsername(),
                                userDetails.getEmail(),
                                roles)
                )
        );
    }

    @PostMapping("registration")
    public ResponseEntity<ResponseObject> signUp(@RequestBody RegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("failed", "Username was exist", null));
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!registrationRequest.getEmail().matches(emailRegex)) {
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("failed", "Invalid email format", null));
        }

        if (userRepository.existsByEmail(registrationRequest.getEmail()))
            return ResponseEntity.badRequest()
                    .body(new ResponseObject("failed", "Email was exists", null));

        User user = new User();
        user.setFullName(registrationRequest.getFullName());
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());

        Set<String> strRole = registrationRequest.getRoles();
        List<Role> roles = new ArrayList<>();
        if (strRole.size() == 0) {
            roles.add(roleRepository.findByName(Role.USER)
                    .orElseThrow(() -> new RuntimeException("user role not found")));
        } else {
            strRole.forEach(role -> {
                switch (role) {
                    case "admin":
                        roles.add(roleRepository.findByName(Role.ADMIN)
                                .orElseThrow(() -> new RuntimeException("admin role not found")));
                        break;
                    default:
                        roles.add(roleRepository.findByName(Role.USER)
                                .orElseThrow(() -> new RuntimeException("user role not found")));

                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("success", "User registered successfully!", ""));
    }

}
