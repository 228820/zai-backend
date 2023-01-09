package com.spring.zaibackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> getMessageForAdmin() {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "This is message for admin");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<?> getMessageForUser() {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "This is message for user or admin");
        return ResponseEntity.ok(response);
    }
}
