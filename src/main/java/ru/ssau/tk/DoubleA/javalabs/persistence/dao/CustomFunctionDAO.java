package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.CustomFunction;

@Repository
public interface CustomFunctionDAO extends JpaRepository<CustomFunction, String> {

}
