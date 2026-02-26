package com.deniapolytech.FactorySystemWeb.dto;

import com.deniapolytech.FactorySystemWeb.model.entity.Client;
import com.deniapolytech.FactorySystemWeb.model.entity.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ClientTaskDTO {
    private Client client;
    private Integer taskId;
    private Integer taskStatus;

    public ClientTaskDTO() {
    }

    public ClientTaskDTO(Client client, Integer taskId, Integer taskStatus) {
        this.client = client;
        this.taskId = taskId;
        this.taskStatus = taskStatus;
    }

    public static ClientTaskDTO fromTask(Task task) {
        return new ClientTaskDTO(
                task.getClient(),
                task.getId(),
                task.getStatus()
        );
    }
}
