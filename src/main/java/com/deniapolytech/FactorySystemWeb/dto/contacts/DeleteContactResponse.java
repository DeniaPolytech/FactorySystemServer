package com.deniapolytech.FactorySystemWeb.dto.contacts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteContactResponse {
    private boolean success;
    private String message;

}
