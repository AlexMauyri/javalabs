package ru.ssau.tk.DoubleA.javalabs.bootloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "ru.ssau.tk.DoubleA.javalabs")
@EntityScan("ru.ssau.tk.DoubleA.javalabs.persistence.entity")
@EnableJpaRepositories(basePackages = {"ru.ssau.tk.DoubleA.javalabs.persistence.dao"})
public class MathApplication {

    public static void main(String[] args) {
        SpringApplication.run(MathApplication.class, args);
    }
}
