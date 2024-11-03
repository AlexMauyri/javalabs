package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public interface DAO<T> {
    default String loadQueryFromFile(String filename) {
        String filePath = "sql/" + filename;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) throw new IllegalArgumentException("File not found: " + filePath);

        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    T read(int id);

    List<T> readAll();

    void create(T entity);

    void update(T entity);

    void delete(int id);
}
