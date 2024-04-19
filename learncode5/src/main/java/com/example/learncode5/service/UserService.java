package com.example.learncode5.service;

import com.example.learncode5.DTO.UserDTO;
import com.example.learncode5.entities.Role;
import com.example.learncode5.entities.User;
import com.example.learncode5.mapper.Mapper;
import com.example.learncode5.payload.Response.ResponseObject;
import com.example.learncode5.repository.RoleRepository;
import com.example.learncode5.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    Mapper mapper;
    @Autowired
    RoleRepository roleRepository;

//    public ResponseEntity<ResponseObject> getUserList() {
//
//        List<User> userList = userRepository.findAll();
//        List<UserDTO> userDTOList = new ArrayList<>();
//        for (User user : userList) {
//            userDTOList.add(mapper.userToUserDTO(user));
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("thanh cong", "", userDTOList));
//    }

    public ResponseEntity<ResponseObject> getUserList() {
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = new ArrayList<>();
            for (User user : userList) {
                userDTOList.add(mapper.userToUserDTO(user));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("thanh cong", "", userDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("thất bại", e.getMessage(), null));
        }
    }

    public ResponseEntity<ResponseObject> deleteUserById(long id) {
        try {
            Optional<User> user = userRepository.findUserById(id);
            if (user.isPresent() == false) {
                return ResponseEntity.badRequest().body(new ResponseObject("failed", "id not exist", id));
            }
            userRepository.delete(user.get());
            return ResponseEntity.ok(new ResponseObject("success", "deleted successfully", mapper.userToUserDTO(user.get())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "An error occurred while deleting user", id));
        }
    }

    public ResponseEntity<ResponseObject> getUserByUsername(String username) {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent() == false) {
                return ResponseEntity.badRequest().body(new ResponseObject("failed", "username not exist", username));
            }
            return ResponseEntity.ok(new ResponseObject("success", "get user successfully", mapper.userToUserDTO(user.get())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Error", username));
        }
    }

    public ResponseEntity<ResponseObject> updateUser(UserDTO user, List<String> strRole) {
        try {
            Optional<User> existingUser = userRepository.findUserById(user.getId());

            if (existingUser.isPresent() == false) {
                return ResponseEntity.badRequest().body(
                        new ResponseObject("failed", "id not exists", null)
                );
            }

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!user.getEmail().matches(emailRegex)) {
                return ResponseEntity.badRequest()
                        .body(new ResponseObject("failed", "Invalid email format", null));
            }

            User u = existingUser.get();
            u.setFullName(user.getFullName());
            u.setEmail(user.getEmail());

            List<Role> roles = new ArrayList<>();
            if (strRole.size() == 0) {
                roles.add(roleRepository.findByName(Role.USER)
                        .orElseThrow(() -> new RuntimeException("user role not found")));
            } else {
                strRole.forEach(role -> {
                    switch (role) {
                        case "ADMIN":
                            roles.add(roleRepository.findByName(Role.ADMIN)
                                    .orElseThrow(() -> new RuntimeException("admin role not found")));
                            break;
                        default:
                            roles.add(roleRepository.findByName(Role.USER)
                                    .orElseThrow(() -> new RuntimeException("user role not found")));
                    }
                });
            }
            u.setRoles(roles);
            userRepository.save(u);
            return ResponseEntity.ok(new ResponseObject("success", "", mapper.userToUserDTO(u)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("failed", "An error occurred while updating user", null));
        }
    }




//    public static byte[] getUserReport(List<User> user, String format) throws JRException, FileNotFoundException {
//        File file = ResourceUtils.getFile("C:\\Users\\HUNG\\JaspersoftWorkspace\\MyReports\\learncode.jrxml");
//        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//
//        //Set report data
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(user);
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("title", "Item Report");
//
//        //Fill report
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//        byte[] reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
//        return reportContent;
//    }
}
