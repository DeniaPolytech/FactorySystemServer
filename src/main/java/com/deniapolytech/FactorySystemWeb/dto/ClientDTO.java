package com.deniapolytech.FactorySystemWeb.dto;

import com.deniapolytech.FactorySystemWeb.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientDTO {
    private int id_client;
    private ClientSurname surname;
    private ClientAddress address;
    private ClientNumber phone;
    private Company company;

    public static ClientDTO fromEntity(Client client) {
        return new ClientDTO(
                client.getIdClient(),
                client.getSurname(),
                client.getAddress(),
                client.getPhone(),
                client.getCompany()
        );
    }
}
