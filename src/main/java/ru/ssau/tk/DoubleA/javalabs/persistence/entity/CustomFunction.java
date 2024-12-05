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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private byte[] serializedFunction;

    public CustomFunction(String name, byte[] serializedFunction) {
        this.name = name;
        this.serializedFunction = serializedFunction;
    }
}
