package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import java.util.List;

public interface GenericService<T> {
    T getById(int id);

    List<T> getAll();

    void create(T employee);

    void update(T employee);

    void deleteById(int id);
}
