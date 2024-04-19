package com.example.learncode5.controller;

import com.example.learncode5.DTO.UserDTO;
import com.example.learncode5.entities.User;
import com.example.learncode5.payload.Response.ResponseObject;
import com.example.learncode5.repository.UserRepository;
import com.example.learncode5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")

public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @GetMapping
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    // dung de kiem soat truy cap vao phuong thuc
    public ResponseEntity<ResponseObject> getUserList()
    {
        return userService.getUserList();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable long id)
    {
        return userService.deleteUserById(id);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> getUserByUsername(@PathVariable String username)
    {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO)
    {
        userDTO.setId(id);
        ResponseEntity<ResponseObject> response = userService.updateUser(userDTO, userDTO.getRoles() );
        return response;
    }

//    @GetMapping("item-report/{format}")
//    public ResponseEntity<Resource> getItemReport(@PathVariable String format) throws JRException, IOException {
//
//        byte[] reportContent = UserService.getUserReport(userRepository.findAll(), format);
//
//        ByteArrayResource resource = new ByteArrayResource(reportContent);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .contentLength(resource.contentLength())
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        ContentDisposition.attachment()
//                                .filename("item-report." + format)
//                                .build().toString())
//                .body((Resource) resource);
//    }
    //phản hồi của người dùng
}
