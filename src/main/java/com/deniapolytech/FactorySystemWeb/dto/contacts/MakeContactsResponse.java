package com.deniapolytech.FactorySystemWeb.dto.contacts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class MakeContactsResponse {
    private boolean success;
    private String message;

    public MakeContactsResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }
}


