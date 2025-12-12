package com.deniapolytech.FactorySystemWeb.repository;

import com.deniapolytech.FactorySystemWeb.model.entity.Client;
import com.deniapolytech.FactorySystemWeb.model.entity.Task;
import com.deniapolytech.FactorySystemWeb.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByUser(User user);
}
