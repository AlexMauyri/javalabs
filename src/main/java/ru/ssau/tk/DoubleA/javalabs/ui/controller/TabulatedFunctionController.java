package ru.ssau.tk.DoubleA.javalabs.ui.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ssau.tk.DoubleA.javalabs.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.DoubleA.javalabs.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.ui.dto.TabulatedFunctionRequest;

@Controller
public class TabulatedFunctionController {

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
