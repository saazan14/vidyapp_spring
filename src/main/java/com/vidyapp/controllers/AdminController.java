package com.vidyapp.controllers;

import com.vidyapp.dtos.UserDTO;
import com.vidyapp.models.User;
import com.vidyapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    private UserService userService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PutMapping("/update-role")
    public ResponseEntity<String> updateUserRole(@RequestParam Long userId,
                                                 @RequestParam String roleName) {
        userService.updateUserRole(userId, roleName);
        return ResponseEntity.ok("User role updated.");
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }






}
