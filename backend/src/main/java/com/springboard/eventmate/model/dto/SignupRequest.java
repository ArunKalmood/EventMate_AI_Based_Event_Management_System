package com.springboard.eventmate.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignupRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    //  NEW (OPTIONAL)
    private String role; // USER or ORGANIZER

    // getters & setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
	
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
	
    public void setPassword(String password) {
        this.password = password;
    }

    // NEW
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
