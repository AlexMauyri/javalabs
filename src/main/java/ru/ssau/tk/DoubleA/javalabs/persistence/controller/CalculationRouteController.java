package ru.ssau.tk.DoubleA.javalabs.persistence.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            @RequestParam("appliedValue") double appliedValue,
            @RequestParam("resultValue") double resultValue,
            @RequestBody String appliedFunctionData) {
        try {
            ObjectMapper functionSetMapper = new ObjectMapper();
            functionSetMapper.activateDefaultTyping(functionSetMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
            List<MathFunction> functionSet = functionSetMapper.readValue(appliedFunctionData, functionSetMapper.getTypeFactory().constructCollectionType(List.class, MathFunction.class));

            calculationRouteService.create(appliedValue, resultValue, functionSet);
            return ResponseEntity.status(HttpStatus.CREATED).body("Calculation created successfully");
        } catch (RuntimeException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating calculation");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalculationDataDTO> getCalculationById(@PathVariable("id") int id) {
        try {
            CalculationDataDTO calculationData = calculationRouteService.getByCalculationId(id);
            return ResponseEntity.ok(calculationData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CalculationDataDTO>> getAllCalculationsByFilter(
            @RequestParam(value = "appliedValue", required = false) Double appliedValue,
            @RequestParam(value = "resultValue", required = false) Double resultValue,
            @RequestParam(value = "operationX", required = false) Operations operationX,
            @RequestParam(value = "operationY", required = false) Operations operationY,
            @RequestParam(value = "sortingX", required = false) Sorting sortingX,
            @RequestParam(value = "sortingY", required = false) Sorting sortingY) {
        List<CalculationDataDTO> calculations = calculationRouteService.getAllByFilter(
                appliedValue, resultValue, operationX, operationY, sortingX, sortingY);
        return ResponseEntity.ok(calculations);
    }

    @GetMapping("/find")
    public ResponseEntity<Calculation> getCalculationByAppliedValueAndRoute(
            @RequestParam("appliedValue") double appliedValue,
            @RequestBody String appliedFunctionData) {
        try {
            ObjectMapper functionSetMapper = new ObjectMapper();
            functionSetMapper.activateDefaultTyping(functionSetMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
            List<MathFunction> functionSet = functionSetMapper.readValue(appliedFunctionData, functionSetMapper.getTypeFactory().constructCollectionType(List.class, MathFunction.class));

            Calculation calculation = calculationRouteService.getByAppliedValueAndRoute(appliedValue, functionSet);
            if (calculation != null) {
                return ResponseEntity.ok(calculation);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (RuntimeException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
