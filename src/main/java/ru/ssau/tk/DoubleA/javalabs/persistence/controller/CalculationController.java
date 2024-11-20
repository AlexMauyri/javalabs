package ru.ssau.tk.DoubleA.javalabs.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.DoubleA.javalabs.persistence.Operations;
import ru.ssau.tk.DoubleA.javalabs.persistence.Sorting;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.CalculationService;

import java.util.List;

@RestController
@RequestMapping("/calculations")
public class CalculationController {

    @Autowired
    private CalculationService calculationService;

    @GetMapping("/{id}")
    public ResponseEntity<Calculation> getCalculation(@PathVariable("id") int id) {
        Calculation calculation = calculationService.getById(id);
        if (calculation != null) {
            return ResponseEntity.ok(calculation);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/hash/{hash}")
    public ResponseEntity<List<Calculation>> getCalculationByHash(@PathVariable("hash") long hash) {
        List<Calculation> calculations = calculationService.getByHash(hash);
        if (!calculations.isEmpty()) {
            return ResponseEntity.ok(calculations);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Calculation>> getBySort(
            @RequestParam(required = false, name = "appliedValue") Double appliedValue,
            @RequestParam(required = false, name = "resultValue") Double resultValue,
            @RequestParam(required = false, name = "operationX") Operations operationX,
            @RequestParam(required = false, name = "operationY") Operations operationY,
            @RequestParam(required = false, name = "sortingX") Sorting sortingX,
            @RequestParam(required = false, name = "sortingY") Sorting sortingY) {

        List<Calculation> calculations = calculationService.getAllByFilter(
                appliedValue, resultValue, operationX, operationY, sortingX, sortingY);
        return ResponseEntity.ok(calculations);
    }

    @GetMapping
    public ResponseEntity<List<Calculation>> getAll() {
        List<Calculation> calculations = calculationService.getAll();
        return ResponseEntity.ok(calculations);
    }

    @PostMapping
    public ResponseEntity<Calculation> create(@RequestBody Calculation calculation) {
        calculationService.create(calculation);
        return ResponseEntity.status(HttpStatus.CREATED).body(calculation);
    }

    @PutMapping
    public ResponseEntity<Calculation> update(@RequestBody Calculation calculation) {
        Calculation existingCalculation = calculationService.getById(calculation.getId());
        if (existingCalculation != null) {
            calculationService.update(calculation);
            return ResponseEntity.ok(calculation);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        Calculation calculation = calculationService.getById(id);
        if (calculation != null) {
            calculationService.deleteById(id);
            return ResponseEntity.ok("Deleted element with id " + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no element with id " + id);
        }
    }
}
