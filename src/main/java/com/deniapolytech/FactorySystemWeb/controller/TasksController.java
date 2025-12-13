package com.deniapolytech.FactorySystemWeb.controller;

import com.deniapolytech.FactorySystemWeb.dto.ClientDTO;
import com.deniapolytech.FactorySystemWeb.dto.UserDTO;
import com.deniapolytech.FactorySystemWeb.dto.tasks.UserClientsResponse;
import com.deniapolytech.FactorySystemWeb.model.entity.Client;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import com.deniapolytech.FactorySystemWeb.repository.TasksRepository;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import com.deniapolytech.FactorySystemWeb.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("client/server/tasks")
public class TasksController {
    private final TasksRepository tasksRepository;
    private final UserRepository userRepository;
    private final TaskService taskService;

    @Autowired
    TasksController(TasksRepository tasksRepository, UserRepository userRepository, TaskService taskService){
        this.tasksRepository = tasksRepository;
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserClientsResponse> getUserClients(
            @PathVariable String username
            ){
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(new UserClientsResponse(false, "Пользователь не найден", null));
        }

        List<Client> clients = taskService.getUsersClients(user);
        List<ClientDTO> clientDTOs = clients.stream()
                .map(ClientDTO::fromEntity)
                .toList();
        return ResponseEntity.ok()
                .body(new UserClientsResponse(true, "Контакты получены", clientDTOs));
    }
}
