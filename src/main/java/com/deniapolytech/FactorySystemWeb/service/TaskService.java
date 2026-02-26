package com.deniapolytech.FactorySystemWeb.service;

import com.deniapolytech.FactorySystemWeb.dto.ClientTaskDTO;
import com.deniapolytech.FactorySystemWeb.model.entity.Client;
import com.deniapolytech.FactorySystemWeb.model.entity.Task;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import com.deniapolytech.FactorySystemWeb.repository.TasksRepository;
import com.deniapolytech.FactorySystemWeb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ClientTaskDTO> getUsersClients(User user){
        return tasksRepository.findAllByUser(user)
                .stream()
                .map(ClientTaskDTO::fromTask)
                .collect(Collectors.toList());
    }
}
