package com.spring.zaibackend.controllers;


import com.spring.zaibackend.models.ERole;
import com.spring.zaibackend.models.User;
import com.spring.zaibackend.repositories.RoleRepository;
import com.spring.zaibackend.repositories.UserRepository;
import com.spring.zaibackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.ok("Get all users for admin.");
        } else {
            return ResponseEntity.ok("Get all users for user.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long requestingUserId = userDetails.getId();

        if(requestingUserId != Long.parseLong(id) && auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.badRequest().build();
        }

        if(this.userRepository.existsById(Long.parseLong(id))) {
            Optional<User> user = this.userRepository.findById(Long.parseLong(id));
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}