package com.deniapolytech.FactorySystemWeb.dto.contacts;

import com.deniapolytech.FactorySystemWeb.model.Contact;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllContactsResponse {
    private boolean success;
    private String message;
    private List<Contact> data;

    public AllContactsResponse(){};

    public AllContactsResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public AllContactsResponse(boolean success, String message, List<Contact> data){
        this.success = success;
        this.message = message;
        this.data = data;
    }

}
