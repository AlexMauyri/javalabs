package ru.ssau.tk.DoubleA.javalabs.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ssau.tk.DoubleA.javalabs.security.Role;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

}
