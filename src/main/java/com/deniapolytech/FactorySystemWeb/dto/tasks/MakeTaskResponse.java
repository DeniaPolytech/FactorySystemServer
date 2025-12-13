package com.deniapolytech.FactorySystemWeb.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MakeTaskResponse {
    private boolean success;
    private String message;
}
