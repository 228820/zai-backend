package com.spring.zaibackend.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UsersController {


    @GetMapping("/")
    public String getAllUsers() {
        return "Get all users.";
    }

    @PutMapping("/user")
    public String updateUser(@RequestParam String id) {
        return "Update user with id: " + id + '.';
    }
}