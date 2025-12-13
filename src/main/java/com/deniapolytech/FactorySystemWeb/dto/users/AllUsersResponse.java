package com.deniapolytech.FactorySystemWeb.dto.users;

import com.deniapolytech.FactorySystemWeb.dto.UserDTO;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class AllUsersResponse {
    private boolean success;
    private String message;
    private List<UserDTO> data;

    public AllUsersResponse(boolean success, String message, List<UserDTO> data){
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public AllUsersResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }
}
