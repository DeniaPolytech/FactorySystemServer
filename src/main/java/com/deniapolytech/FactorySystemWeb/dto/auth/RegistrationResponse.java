package com.deniapolytech.FactorySystemWeb.dto.auth;

public class RegistrationResponse {
    private boolean success;
    private String message;

    // Конструкторы
    public RegistrationResponse(boolean b, String пользовательУспешноЗарегистрирован, String token, String username) {}

    public RegistrationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Геттеры и сеттеры
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

