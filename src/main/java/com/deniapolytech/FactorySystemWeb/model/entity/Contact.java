package com.deniapolytech.FactorySystemWeb.model.entity;

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

    @ManyToOne
    @JoinColumn(name = "first_user_id", referencedColumnName = "id", nullable = false, unique = false)
    private User firstUser;

    @ManyToOne
    @JoinColumn(name = "second_user_id", referencedColumnName = "id", nullable = false, unique = false)
    private User secondUser;

    public Contact(User firstUser, User secondUser){
        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }

}
