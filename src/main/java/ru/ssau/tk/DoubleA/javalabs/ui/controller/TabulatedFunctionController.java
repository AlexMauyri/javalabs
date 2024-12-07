package ru.ssau.tk.DoubleA.javalabs.ui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.io.FunctionsIO;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedFunctionOperationService;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedIntegrationOperator;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.CustomFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.CustomFunctionService;
import ru.ssau.tk.DoubleA.javalabs.ui.AccessingAllClassesInPackage;
import ru.ssau.tk.DoubleA.javalabs.ui.FabricType;
import ru.ssau.tk.DoubleA.javalabs.ui.FunctionSerializer;
import ru.ssau.tk.DoubleA.javalabs.ui.TabulatedFunctionFactoryCookieHandler;
import ru.ssau.tk.DoubleA.javalabs.ui.annotation.SimpleFunction;
import ru.ssau.tk.DoubleA.javalabs.ui.dto.TabulatedFunctionOnArraysRequest;
import ru.ssau.tk.DoubleA.javalabs.ui.dto.TabulatedFunctionOnFunctionRequest;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RestController
public class TabulatedFunctionController {
    private final Map<String, MathFunction> allFunctions;
    private final CustomFunctionService customFunctionService;
    private final FunctionSerializer functionSerializer;
    private final TabulatedFunctionFactoryCookieHandler cookieHandler;

    public TabulatedFunctionController(@Autowired CustomFunctionService customFunctionService,
                                       @Autowired FunctionSerializer functionSerializer,
                                       @Autowired TabulatedFunctionFactoryCookieHandler cookieHandler)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Class<?>> classes = AccessingAllClassesInPackage.findAllClassesWithSimpleFunctionAnnotation("ru.ssau.tk.DoubleA.javalabs.functions");
        allFunctions = new HashMap<>();

        for (Class<?> clazz : classes) {
            SimpleFunction annotation = clazz.getAnnotation(SimpleFunction.class);
            allFunctions.put(annotation.localizedName(), (MathFunction) clazz.getConstructor().newInstance());
        }

        this.customFunctionService = customFunctionService;
        this.functionSerializer = functionSerializer;
        this.cookieHandler = cookieHandler;
    }

    @PostConstruct
    public void init() {
        List<CustomFunction> functions = customFunctionService.getCustomFunctions();
        for (CustomFunction function : functions) {
            allFunctions.put(function.getName(), functionSerializer.deserializeFunction(function.getSerializedFunction()));
        }
    }

    @GetMapping("/getFunctions")
    @ResponseBody
    public List<String> getFunctions() {
        return new ArrayList<>(allFunctions.keySet());
    }

    @PostMapping("/create/{functionName}")
    public void createFunction(@PathVariable(name = "functionName") String functionName, @RequestBody String[] functions) {
        if (allFunctions.containsKey(functionName)) {
            throw new IllegalArgumentException("Введенное имя функции уже существует");
        }
        List<MathFunction> functionList = Arrays.stream(functions).map(allFunctions::get).toList().reversed();
        CompositeFunction function = new CompositeFunction(functionList.get(0), functionList.get(1));
        for (int i = 2; i < functionList.size(); i++) {
            function = new CompositeFunction(function, functionList.get(i));
        }
        byte[] serializedFunction = functionSerializer.serializeCustomFunction(function);

        CustomFunction customFunction = customFunctionService.createFunction(functionName, serializedFunction);
        allFunctions.put(customFunction.getName(), functionSerializer.deserializeFunction(customFunction.getSerializedFunction()));
    }

    @PostMapping("/createTabulatedFunctionWithTableByte")
    @ResponseBody
    public byte[] createTabulatedFunctionWithTableByte(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                                                                              HttpServletRequest request,
                                                                              HttpServletResponse response) {
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getXValues(),
                tabulatedFunctionRequest.getYValues()
        );

        return functionSerializer.serializeByte(function);
    }

    @PostMapping("/createTabulatedFunctionWithTableJSON")
    @ResponseBody
    public String createTabulatedFunctionWithTableJSON(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getXValues(),
                tabulatedFunctionRequest.getYValues()
        );

        return functionSerializer.serializeJson(function);
    }

    @PostMapping("/createTabulatedFunctionWithTableXML")
    @ResponseBody
    public String createTabulatedFunctionWithTableXML(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getXValues(),
                tabulatedFunctionRequest.getYValues()
        );

        return functionSerializer.serializeXml(function);
    }

    @PostMapping("/createTabulatedFunctionWithFunctionByte")
    @ResponseBody
    public byte[] createTabulatedFunctionWithFunctionByte(@RequestBody TabulatedFunctionOnFunctionRequest tabulatedFunctionRequest,
                                                                                 HttpServletRequest request,
                                                                                 HttpServletResponse response) {
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedFunction function = factory.create(
                allFunctions.get(tabulatedFunctionRequest.getFunctionName()),
                tabulatedFunctionRequest.getFrom(),
                tabulatedFunctionRequest.getTo(),
                tabulatedFunctionRequest.getAmountOfPoints()
        );
        return functionSerializer.serializeByte(function);
    }

    @PostMapping("/createTabulatedFunctionWithFunctionJSON")
    @ResponseBody
    public String createTabulatedFunctionWithFunctionJSON(@RequestBody TabulatedFunctionOnFunctionRequest tabulatedFunctionRequest,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedFunction function = factory.create(
                allFunctions.get(tabulatedFunctionRequest.getFunctionName()),
                tabulatedFunctionRequest.getFrom(),
                tabulatedFunctionRequest.getTo(),
                tabulatedFunctionRequest.getAmountOfPoints()
        );
        return functionSerializer.serializeJson(function);
    }

    @PostMapping("/createTabulatedFunctionWithFunctionXML")
    @ResponseBody
    public String createTabulatedFunctionWithFunctionXML(@RequestBody TabulatedFunctionOnFunctionRequest tabulatedFunctionRequest,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        TabulatedFunctionFactory factory = cookieHandler.determineFabric(request, response);
        TabulatedFunction function = factory.create(
                allFunctions.get(tabulatedFunctionRequest.getFunctionName()),
                tabulatedFunctionRequest.getFrom(),
                tabulatedFunctionRequest.getTo(),
                tabulatedFunctionRequest.getAmountOfPoints()
        );
        return functionSerializer.serializeXml(function);
    }

}