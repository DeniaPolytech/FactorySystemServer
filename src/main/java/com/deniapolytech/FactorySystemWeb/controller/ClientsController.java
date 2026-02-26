package com.deniapolytech.FactorySystemWeb.controller;

import com.deniapolytech.FactorySystemWeb.model.entity.Client;
import com.deniapolytech.FactorySystemWeb.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("admin/server/clients")
public class ClientsController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/not-in-tasks")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<Client>> getClientsNotInTasks() {
        List<Client> clients = clientRepository.findClientsNotInTasks();
        return ResponseEntity.ok(clients);
    }
}
