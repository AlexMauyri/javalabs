package ru.ssau.tk.DoubleA.javalabs.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.AppliedFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.AppliedFunctionService;

import java.util.List;

@RestController
@RequestMapping("/functions")
public class AppliedFunctionController {

    @Autowired
    private AppliedFunctionService appliedFunctionService;

    @GetMapping("/{id}")
    public ResponseEntity<AppliedFunction> getFunction(@PathVariable int id) {
        AppliedFunction function = appliedFunctionService.getById(id);
        if (function != null) {
            return ResponseEntity.ok(function);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/calculation/{calculationId}")
    public ResponseEntity<List<AppliedFunction>> getCalculationFunctions(@PathVariable int calculationId) {
        List<AppliedFunction> functions = appliedFunctionService.getByCalculationId(calculationId);
        if (!functions.isEmpty()) {
            return ResponseEntity.ok(functions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<AppliedFunction>> getAll() {
        List<AppliedFunction> functions = appliedFunctionService.getAll();
        return ResponseEntity.ok(functions);
    }

    @PostMapping
    public ResponseEntity<AppliedFunction> create(@RequestBody AppliedFunction function) {
        appliedFunctionService.create(function);
        return ResponseEntity.status(HttpStatus.CREATED).body(function);
    }

    @PutMapping
    public ResponseEntity<AppliedFunction> update(@RequestBody AppliedFunction function) {
        AppliedFunction existingFunction = appliedFunctionService.getById(function.getId());
        if (existingFunction != null) {
            appliedFunctionService.update(function);
            return ResponseEntity.ok(function);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        AppliedFunction function = appliedFunctionService.getById(id);
        if (function != null) {
            appliedFunctionService.deleteById(id);
            return ResponseEntity.ok("Deleted element with id " + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no element with id " + id);
        }
    }
}
