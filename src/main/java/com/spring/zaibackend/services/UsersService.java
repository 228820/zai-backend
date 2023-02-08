package com.spring.zaibackend.services;

import com.spring.zaibackend.models.ERole;
import com.spring.zaibackend.models.Role;
import com.spring.zaibackend.models.User;
import com.spring.zaibackend.payloads.requests.UpdateRequest;
import com.spring.zaibackend.repositories.RoleRepository;
import com.spring.zaibackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UsersService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public Page<User> getAllUsers(int page) {
        if(page < 0) {
            return null;
        }
        return this.userRepository.findAll(PageRequest.of(page, 5, Sort.by("id").ascending()));
    }

    public User getUserById(Long userId) {
        if(this.userRepository.existsById(userId)) {
            Optional<User> user = this.userRepository.findById(userId);
            return user.get();
        } else {
            return null;
        }
    }

    public User updateUserById(User userToUpdate, UpdateRequest updateRequest) {
        userToUpdate.setUsername(updateRequest.getUsername());
        userToUpdate.setEmail(updateRequest.getEmail());


        if(updateRequest.getPassword() != "") {
            userToUpdate.setPassword(encoder.encode(updateRequest.getPassword()));
        }

        Set<String> strRoles = updateRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles.contains("user")) {
            Role userRole = roleRepository.findByRole(ERole.USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
        if (strRoles.contains("admin")){
            Role adminRole = roleRepository.findByRole(ERole.ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
        }
        userToUpdate.setRoles(roles);

        return this.userRepository.save(userToUpdate);
    }
}
