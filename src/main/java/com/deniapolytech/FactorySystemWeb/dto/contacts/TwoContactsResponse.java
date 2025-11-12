package com.deniapolytech.FactorySystemWeb.dto.contacts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TwoContactsResponse {
    private boolean success;
    private String message;
    private Boolean isContact;

    public TwoContactsResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public TwoContactsResponse(boolean success, String message, boolean isContact){
        this.success = success;
        this.message = message;
        this.isContact = isContact;
    }
}
