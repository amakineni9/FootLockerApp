package com.dwprojects.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String first_name;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String last_name;

    @Column(name = "is_owner", nullable = false)
    private boolean isOwner;

    public User(String password, String email, String first_name, String last_name, boolean isOwner) {
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.isOwner = isOwner;
    }
}
