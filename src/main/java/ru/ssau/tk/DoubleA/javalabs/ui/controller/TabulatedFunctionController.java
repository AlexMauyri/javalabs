package ru.ssau.tk.DoubleA.javalabs.ui.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.ui.dto.TabulatedFunctionRequest;

@Controller
public class TabulatedFunctionController {

    @GetMapping("/createTF")
    public String create() {
        return "createTabulatedFunction";
    }

    @PostMapping("/createTF")
    public ResponseEntity<TabulatedFunction> createTF(@RequestBody TabulatedFunctionRequest request) {
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(request.getX(), request.getY());
        return ResponseEntity.ok(function);
    }

}
