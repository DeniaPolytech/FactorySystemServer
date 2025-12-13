package com.deniapolytech.FactorySystemWeb.dto.contacts;

import com.deniapolytech.FactorySystemWeb.dto.ContactDTO;
import com.deniapolytech.FactorySystemWeb.model.entity.Contact;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllContactsResponse {
    private boolean success;
    private String message;
    private List<ContactDTO> data;

    public AllContactsResponse(){};

    public AllContactsResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public AllContactsResponse(boolean success, String message, List<ContactDTO> data){
        this.success = success;
        this.message = message;
        this.data = data;
    }

}
