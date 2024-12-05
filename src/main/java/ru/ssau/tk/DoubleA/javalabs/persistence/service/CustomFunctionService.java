package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.DoubleA.javalabs.functions.CompositeFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.CustomFunctionDAO;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.CustomFunction;

import java.io.*;
import java.util.List;

@Service
public class CustomFunctionService {

    @Autowired
    private CustomFunctionDAO customFunctionDAO;

    public List<CustomFunction> getCustomFunctions() {
        return customFunctionDAO.findAll();
    }


    public CustomFunction createFunction(String functionName, List<MathFunction> functionList) {
        CompositeFunction function = new CompositeFunction(functionList.get(0), functionList.get(1));

        for (int i = 2; i < functionList.size(); i++) {
            function = new CompositeFunction(function, functionList.get(i));
        }

        return customFunctionDAO.save(new CustomFunction(functionName, serializeFunction(function)));
    }


    private byte[] serializeFunction(MathFunction function) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(function);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize function", e);
        }
    }
}
