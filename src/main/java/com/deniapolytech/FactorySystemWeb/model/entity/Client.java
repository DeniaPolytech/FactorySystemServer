package com.deniapolytech.FactorySystemWeb.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private int idClient;

    @ManyToOne
    @JoinColumn(name = "id_surname", referencedColumnName = "id_surname", nullable = false, unique = false)
    private ClientSurname surname;

    @ManyToOne
    @JoinColumn(name = "id_address", referencedColumnName = "id_address", nullable = false, unique = false)
    private ClientAddress address;

    @ManyToOne
    @JoinColumn(name = "id_phone", referencedColumnName = "id_number", nullable = false, unique = false)
    private ClientNumber phone;

    @ManyToOne
    @JoinColumn(name = "id_company", referencedColumnName = "id_company", nullable = false, unique = false)
    private Company company;
}
