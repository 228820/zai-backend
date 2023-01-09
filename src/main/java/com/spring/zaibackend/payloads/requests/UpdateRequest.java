package com.spring.zaibackend.payloads.requests;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.Set;

public class UpdateRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 20)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 80)
    private String password;

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() { return password; }
    public Set<String> getRole() {
        return this.role;
    }
}
