package com.deniapolytech.FactorySystemWeb.dto.contacts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MakeContactsRequest {
    private int firstUserId;
    private int secondUserId;
}
