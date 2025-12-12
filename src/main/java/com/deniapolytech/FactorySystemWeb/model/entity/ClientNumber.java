package com.deniapolytech.FactorySystemWeb.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients_numbers")
public class ClientNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_number;

    @Column(name = "number", nullable = false, unique = true)
    private String number;
}
