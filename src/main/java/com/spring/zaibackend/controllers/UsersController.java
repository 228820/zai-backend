package com.spring.zaibackend.controllers;


import com.spring.zaibackend.models.User;
import com.spring.zaibackend.payloads.requests.UpdateRequest;
import com.spring.zaibackend.security.services.UserDetailsImpl;
import com.spring.zaibackend.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    UsersService usersService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(@RequestParam int page) {
        Page<User> users = this.usersService.getAllUsers(page);
        if(users == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long requestingUserId = userDetails.getId();

        if(requestingUserId != Long.parseLong(id) && auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.badRequest().build();
        }

        User user = this.usersService.getUserById(Long.parseLong(id));
        if(user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") String id, @Valid @RequestBody UpdateRequest updateRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long requestingUserId = userDetails.getId();

        if(requestingUserId != Long.parseLong(id) && auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.badRequest().build();
        }

        HashMap<String, String> response = new HashMap<>();

        User user = this.usersService.getUserById(Long.parseLong(id));
        if(user != null) {
            try {
                User updatedUser = this.usersService.updateUserById(user, updateRequest);
                return ResponseEntity.ok(updatedUser);
            } catch (DataIntegrityViolationException e) {
                response.put("message", "Login or email already taken");
                return ResponseEntity.badRequest().body(response);
            } catch (IllegalArgumentException e) {
                response.put("message", "Invalid user data");
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}