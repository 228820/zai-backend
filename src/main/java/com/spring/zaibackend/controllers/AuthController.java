package com.spring.zaibackend.controllers;

import com.spring.zaibackend.models.ERole;
import com.spring.zaibackend.models.Role;
import com.spring.zaibackend.models.User;
import com.spring.zaibackend.payloads.requests.LoginRequest;
import com.spring.zaibackend.payloads.requests.SignupRequest;
import com.spring.zaibackend.repositories.RoleRepository;
import com.spring.zaibackend.repositories.UserRepository;
import com.spring.zaibackend.security.jwt.JwtUtils;
import com.spring.zaibackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().toArray()[0].toString();

        HashMap<String, String> response = new HashMap<>();
        response.put("id", userDetails.getId().toString());
        response.put("accessToken", jwt);
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        HashMap<String, String> response = new HashMap<>();
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            response.put("Error", "Username is already taken!");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            response.put("Error", "Email is already in use!");
            return ResponseEntity.badRequest().body(response);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByRole(ERole.USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            response.put("Message", e.toString());
        }
        response.put("Message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }
}