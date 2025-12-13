package com.deniapolytech.FactorySystemWeb.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, unique = true)
    private String passwordHash;

    @Column(name = "role", nullable = false, unique = false)
    private String role;

    public User(String username, String email, String passwordHash, String role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role) ||
                "ADMIN".equalsIgnoreCase(this.role);
    }

    public boolean isClient() {
        return "client".equalsIgnoreCase(this.role) ||
                "CLIENT".equalsIgnoreCase(this.role);
    }

    public String getRoleWithPrefix() {
        return "ROLE_" + this.role.toUpperCase();
    }

    public String getRoleUpperCase() {
        return this.role.toUpperCase();
    }
}
