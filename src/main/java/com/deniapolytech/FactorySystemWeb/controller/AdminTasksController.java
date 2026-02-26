package com.deniapolytech.FactorySystemWeb.controller;

import com.deniapolytech.FactorySystemWeb.dto.ClientTaskDTO;
import com.deniapolytech.FactorySystemWeb.dto.tasks.MakeTaskResponse;
import com.deniapolytech.FactorySystemWeb.model.entity.Client;
import com.deniapolytech.FactorySystemWeb.model.entity.Task;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import com.deniapolytech.FactorySystemWeb.repository.ClientRepository;
import com.deniapolytech.FactorySystemWeb.repository.TasksRepository;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import com.deniapolytech.FactorySystemWeb.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/server/tasks")
public class AdminTasksController {
    private final UserRepository userRepository;
    private final TaskService taskService;
    private final TasksRepository tasksRepository;
    private final ClientRepository clientRepository;

    @Autowired
    AdminTasksController(UserRepository userRepository,
                         TaskService taskService, ClientRepository clientRepository,
                         TasksRepository tasksRepository){
        this.userRepository = userRepository;
        this.taskService = taskService;
        this.clientRepository = clientRepository;
        this.tasksRepository = tasksRepository;
    }


    @PostMapping("/clients/{id_client}/users/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MakeTaskResponse> makeTask(
            @PathVariable("id_client") int clientId,
            @PathVariable String username){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(new MakeTaskResponse(false, "Пользователь не найден"));
        }

        Client client = clientRepository.findByIdClient(clientId);

        if (client == null) {
            return ResponseEntity.badRequest()
                    .body(new MakeTaskResponse(false, "Клиент не найден"));
        }

        // Получаем задачи пользователя
        List<ClientTaskDTO> userTasks = taskService.getUsersClients(user);

        // Проверяем, существует ли уже задача для этого клиента
        boolean taskExists = userTasks.stream()
                .anyMatch(clientTaskDTO -> {
                    Client taskClient = clientTaskDTO.getClient();
                    return taskClient != null && taskClient.getIdClient() == clientId;
                });

        if (taskExists) {
            return ResponseEntity.badRequest()
                    .body(new MakeTaskResponse(false, "Задача уже существует для этого клиента"));
        }

        try {
            Task task = new Task(client, user);
            tasksRepository.save(task);
            return ResponseEntity.ok()
                    .body(new MakeTaskResponse(true, "Задание успешно создано"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MakeTaskResponse(false,
                            "Ошибка при создании задания: " + e.getMessage()));
        }
    }

}
