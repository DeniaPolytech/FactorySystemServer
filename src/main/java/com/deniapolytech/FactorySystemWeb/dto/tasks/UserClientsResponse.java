package com.deniapolytech.FactorySystemWeb.dto.tasks;

import com.deniapolytech.FactorySystemWeb.model.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserClientsResponse {
    private boolean success;
    private String message;
    private List<Client> data;
}
