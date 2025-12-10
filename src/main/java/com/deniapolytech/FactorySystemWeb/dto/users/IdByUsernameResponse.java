package com.deniapolytech.FactorySystemWeb.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class IdByUsernameResponse {
    private boolean success;
    private String message;
    private int userId;

    public IdByUsernameResponse(boolean success, String message, int userId){
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    public IdByUsernameResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }
}
