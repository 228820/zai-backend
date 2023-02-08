package com.spring.zaibackend.payloads.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class UpdateRequest {
    @NotNull
    @Size(min = 3, max = 20)
    private String username;

    @NotNull
    @Size(max = 20)
    @Email
    private String email;

    private Set<String> role;

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
