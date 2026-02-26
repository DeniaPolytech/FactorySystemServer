package com.deniapolytech.FactorySystemWeb.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenResponse {
    private boolean success;
    private String message;
    private String token;
    private String username;
    private String role;

    public RefreshTokenResponse() {}

    public RefreshTokenResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RefreshTokenResponse(boolean success, String message, String token, String username, String role) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}