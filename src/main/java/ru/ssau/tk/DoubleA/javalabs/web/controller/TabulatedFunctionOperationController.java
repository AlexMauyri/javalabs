package ru.ssau.tk.DoubleA.javalabs.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedFunctionOperationService;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedIntegrationOperator;
import ru.ssau.tk.DoubleA.javalabs.web.cookie.TabulatedFunctionFactoryCookieHandler;
import ru.ssau.tk.DoubleA.javalabs.web.dto.TabulatedFunctionOnArraysRequest;

import java.util.concurrent.ExecutionException;

@RestController
public class TabulatedFunctionOperationController {

    private final TabulatedFunctionFactoryCookieHandler cookieHandler;

    public TabulatedFunctionOperationController(@Autowired TabulatedFunctionFactoryCookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;
    }

    @PostMapping("/apply/{xValue}")
    @ResponseBody
    public String applyValue(@PathVariable(name = "xValue") double xValue,
                             @RequestBody TabulatedFunctionOnArraysRequest functionRequest,
                             HttpServletRequest request,
                             HttpServletResponse response) throws JsonProcessingException {
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedFunction function = factory.create(
                functionRequest.getXValues(),
                functionRequest.getYValues()
        );

        return new ObjectMapper().writeValueAsString(function.apply(xValue));
    }

    @PostMapping("/doOperation/{operation}")
    @ResponseBody
    public String doOperation(@RequestBody TabulatedFunctionOnArraysRequest[] tables,
                              @PathVariable("operation") String operation,
                              HttpServletRequest request,
                              HttpServletResponse response) throws JsonProcessingException {
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(factory);
        TabulatedFunction firstFunction = factory.create(
                tables[0].getXValues(),
                tables[0].getYValues()
        );

        TabulatedFunction secondFunction = factory.create(
                tables[1].getXValues(),
                tables[1].getYValues()
        );
        TabulatedFunction function = switch (operation) {
            case "+" -> service.addition(firstFunction, secondFunction);
            case "-" -> service.subtraction(firstFunction, secondFunction);
            case "*" -> service.multiply(firstFunction, secondFunction);
            case "÷" -> service.divide(firstFunction, secondFunction);
            default -> throw new IllegalStateException("Unexpected value: " + operation);
        };

        return new ObjectMapper().writeValueAsString(function);
    }

    @PostMapping("/doDifferential")
    @ResponseBody
    public String doDifferential(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws JsonProcessingException {
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getXValues(),
                tabulatedFunctionRequest.getYValues()
        );

        return new ObjectMapper().writeValueAsString(operator.derive(function));
    }

    @PostMapping("/doIntegral/{threads}")
    @ResponseBody
    public String doIntegral(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                             @PathVariable(name = "threads") int threadCount,
                             HttpServletRequest request,
                             HttpServletResponse response) throws JsonProcessingException, ExecutionException, InterruptedException {
        int DEFAULT_NUMBER_OF_SECTIONS = 30;
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getXValues(),
                tabulatedFunctionRequest.getYValues()
        );
        TabulatedIntegrationOperator operator = new TabulatedIntegrationOperator(threadCount);
        return new ObjectMapper().writeValueAsString(operator.integrate(function, DEFAULT_NUMBER_OF_SECTIONS));
    }
}
