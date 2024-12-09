package ru.ssau.tk.DoubleA.javalabs.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "custom_function")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private int userId;
    private String name;
    private byte[] serializedFunction;

    public CustomFunction(int userId, String name, byte[] serializedFunction) {
        this.userId = userId;
        this.name = name;
        this.serializedFunction = serializedFunction;
    }
}
