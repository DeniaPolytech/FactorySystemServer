package com.deniapolytech.FactorySystemWeb.dto;

import com.deniapolytech.FactorySystemWeb.model.entity.Task;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskDTO {
    private int id;
    private ClientDTO client;
    private UserDTO user;

    public static TaskDTO fromEntity(Task task) {
        return new TaskDTO(
                task.getId(),
                ClientDTO.fromEntity(task.getClient()),
                UserDTO.fromEntity(task.getUser())
        );
    }
}
