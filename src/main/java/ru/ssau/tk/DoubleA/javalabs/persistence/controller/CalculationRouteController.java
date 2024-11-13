package ru.ssau.tk.DoubleA.javalabs.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.Operations;
import ru.ssau.tk.DoubleA.javalabs.persistence.Sorting;
import ru.ssau.tk.DoubleA.javalabs.persistence.dto.CalculationDataDTO;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.CalculationRouteService;

import java.util.List;

@RestController
@RequestMapping("/calculations_routes")
public class CalculationRouteController {

    @Autowired
    private CalculationRouteService calculationRouteService;

    @PostMapping
    public ResponseEntity<String> createCalculation(
            @RequestParam double appliedValue,
            @RequestParam double resultValue,
            @RequestBody List<MathFunction> appliedFunctionData) {
        try {
            calculationRouteService.create(appliedValue, resultValue, appliedFunctionData);
            return ResponseEntity.status(HttpStatus.CREATED).body("Calculation created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating calculation");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalculationDataDTO> getCalculationById(@PathVariable int id) {
        try {
            CalculationDataDTO calculationData = calculationRouteService.getByCalculationId(id);
            return ResponseEntity.ok(calculationData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CalculationDataDTO>> getAllCalculationsByFilter(
            @RequestParam(required = false) Double appliedValue,
            @RequestParam(required = false) Double resultValue,
            @RequestParam(required = false) Operations operationX,
            @RequestParam(required = false) Operations operationY,
            @RequestParam(required = false) Sorting sortingX,
            @RequestParam(required = false) Sorting sortingY) {
        List<CalculationDataDTO> calculations = calculationRouteService.getAllByFilter(
                appliedValue, resultValue, operationX, operationY, sortingX, sortingY);
        return ResponseEntity.ok(calculations);
    }

    @GetMapping("/find")
    public ResponseEntity<Calculation> getCalculationByAppliedValueAndRoute(
            @RequestParam double appliedValue,
            @RequestBody List<MathFunction> appliedFunctionData) {
        Calculation calculation = calculationRouteService.getByAppliedValueAndRoute(appliedValue, appliedFunctionData);
        if (calculation != null) {
            return ResponseEntity.ok(calculation);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
