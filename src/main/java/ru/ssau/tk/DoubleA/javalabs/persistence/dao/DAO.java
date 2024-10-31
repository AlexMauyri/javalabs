package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> read(int id);

    List<T> readAll();

    void create(T entity);

    void update(T entity);

    void delete(int id);
}
