package com.deniapolytech.FactorySystemWeb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@Getter
@NoArgsConstructor
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "first_user_id", nullable = false, unique = false)
    private int firstUserId;
    
    @Column(name = "second_user_id", nullable = false, unique = false)
    private int secondUserId;
}
