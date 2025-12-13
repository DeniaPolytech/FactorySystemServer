package com.deniapolytech.FactorySystemWeb.dto;

import com.deniapolytech.FactorySystemWeb.model.entity.Contact;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContactDTO {
    private int Id;
    private UserDTO firstUser;
    private UserDTO secondUser;

    public static ContactDTO fromEntity(Contact contact) {
        return new ContactDTO(
                contact.getId(),
                UserDTO.fromEntity(contact.getFirstUser()),
                UserDTO.fromEntity(contact.getSecondUser())
        );
    }
}
