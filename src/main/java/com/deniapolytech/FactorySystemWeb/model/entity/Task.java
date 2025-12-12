package com.deniapolytech.FactorySystemWeb.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_task")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id_client", nullable = false, unique = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_worker", referencedColumnName = "id", nullable = false, unique = false)
    private User user;
}
