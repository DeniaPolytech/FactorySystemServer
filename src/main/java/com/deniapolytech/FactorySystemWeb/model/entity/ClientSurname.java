package com.deniapolytech.FactorySystemWeb.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients_surname")
public class ClientSurname {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_surname;

    @Column(name = "surname", nullable = false, unique = true)
    private String surname;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "middlename", nullable = false, unique = true)
    private String middleName;
}
