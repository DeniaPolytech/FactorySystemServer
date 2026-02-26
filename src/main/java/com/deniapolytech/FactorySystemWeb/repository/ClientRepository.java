package com.deniapolytech.FactorySystemWeb.repository;

import com.deniapolytech.FactorySystemWeb.model.entity.Client;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByIdClient(int idClient);
    @Query("SELECT c FROM Client c WHERE c.idClient NOT IN (SELECT t.client.idClient FROM Task t)")
    List<Client> findClientsNotInTasks();

}
