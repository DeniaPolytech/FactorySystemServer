package com.deniapolytech.FactorySystemWeb.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company")
    private int id;

    @Column(name = "company_name", nullable = false, unique = true)
    private String name;
}
