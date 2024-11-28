package ru.ssau.tk.DoubleA.javalabs.ui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.ssau.tk.DoubleA.javalabs.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.DoubleA.javalabs.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.ui.dto.TabulatedFunctionRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TabulatedFunctionController {

    Map<String, MathFunction> functions;

    public TabulatedFunctionController() {
        functions = new HashMap<>();
        functions.put("Тождественная функция", new IdentityFunction());
        functions.put("Квадратичная функция", new SqrFunction());
        functions.put("Единичная функция", new UnitFunction());
        functions.put("Нулевая функция", new ZeroFunction());
    }

    @GetMapping("/getFunctions")
    @ResponseBody
    public List<String> getFunctions() throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        System.out.println(mapper.writeValueAsString(functions.keySet()));
//        return "{names:" + mapper.writeValueAsString(functions.keySet()) + "}";
        return new ArrayList<>(functions.keySet());
    }

    @GetMapping("/createTabulatedFunctionWithTable")
    public String createWithTable() {
        return "createTabulatedFunctionWithTable";
    }

    @GetMapping("/createTabulatedFunctionWithFunction")
    public String createWithFunction() {
        return "createTabulatedFunctionWithFunction";
    }

    @PostMapping("/createTabulatedFunctionWithTable")
    public ResponseEntity<TabulatedFunction> createTF(@RequestBody TabulatedFunctionRequest request) {
        ArrayTabulatedFunction function;
        try {
            function = new ArrayTabulatedFunction(request.getX(), request.getY());
        } catch (IllegalArgumentException | DifferentLengthOfArraysException | ArrayIsNotSortedException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(function);
    }

}
