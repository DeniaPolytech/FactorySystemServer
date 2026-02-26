package com.deniapolytech.FactorySystemWeb.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenValidationResponse {
    private boolean success;
    private String message;
    private String username;
    private String role;

    public TokenValidationResponse() {}

    public TokenValidationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public TokenValidationResponse(boolean success, String message, String username, String role) {
        this.success = success;
        this.message = message;
        this.username = username;
        this.role = role;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}