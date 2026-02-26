package com.deniapolytech.FactorySystemWeb.controller;

import com.deniapolytech.FactorySystemWeb.dto.ClientDTO;
import com.deniapolytech.FactorySystemWeb.dto.ClientTaskDTO;
import com.deniapolytech.FactorySystemWeb.dto.tasks.ChangeTaskStatusResponse;
import com.deniapolytech.FactorySystemWeb.dto.tasks.UserClientsResponse;
import com.deniapolytech.FactorySystemWeb.model.entity.Client;
import com.deniapolytech.FactorySystemWeb.model.entity.Task;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import com.deniapolytech.FactorySystemWeb.repository.TasksRepository;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import com.deniapolytech.FactorySystemWeb.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<UserClientsResponse> getUserClients(
            @PathVariable String username
    ){
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(new UserClientsResponse(false, "Пользователь не найден", null));
        }

        // Используем новый метод для получения клиентов с задачами
        List<ClientTaskDTO> clientTasks = taskService.getUsersClients(user);

        return ResponseEntity.ok()
                .body(new UserClientsResponse(true, "Контакты получены", clientTasks));
    }

    @PostMapping("/{idTask}/{status}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ChangeTaskStatusResponse> changeTaskStatus(
            @PathVariable int idTask,
            @PathVariable int status
    ) {
        try {
            Task task = tasksRepository.findById(idTask);

            if (task == null) {
                return ResponseEntity.badRequest()
                        .body(new ChangeTaskStatusResponse(false, "Задача не найдена"));
            }

            task.setStatus(status);
            tasksRepository.save(task);

            return ResponseEntity.ok()
                    .body(new ChangeTaskStatusResponse(true, "Статус задачи успешно изменен"));

        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                    .body(new ChangeTaskStatusResponse(false, "Ошибка при изменении статуса: " + e.getMessage()));
        }
    }
}
