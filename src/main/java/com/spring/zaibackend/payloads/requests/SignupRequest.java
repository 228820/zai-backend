package com.spring.zaibackend.payloads.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignupRequest {
    @NotNull
    @Size(min = 3, max = 20)
    private String username;

    @NotNull
    @Size(max = 50)
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 40)
    private String password;

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() { return password; }

}