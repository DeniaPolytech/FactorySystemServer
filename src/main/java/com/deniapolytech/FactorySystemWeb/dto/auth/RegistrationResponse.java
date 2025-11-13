package com.deniapolytech.FactorySystemWeb.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationResponse {
    private boolean success;
    private String message;
    private String token;
    private String username;

    // Конструкторы
    public RegistrationResponse(boolean success, String message, String token, String username) {
        this.success = success;
        this.message = "Пользователь успешно зарегистрирован";
        this.token = token;
        this.username = username;
    }

    public RegistrationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }


}

