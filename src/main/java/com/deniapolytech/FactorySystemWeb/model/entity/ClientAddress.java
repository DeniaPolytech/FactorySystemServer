package com.deniapolytech.FactorySystemWeb.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients_address")
public class ClientAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_address;

    @Column(name = "address", nullable = false, unique = true)
    private String address;
}
