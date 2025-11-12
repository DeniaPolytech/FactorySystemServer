package com.deniapolytech.FactorySystemWeb.dto.contacts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TwoContactsRequest {
    private int firstUserId;
    private int secondUserId;
}
