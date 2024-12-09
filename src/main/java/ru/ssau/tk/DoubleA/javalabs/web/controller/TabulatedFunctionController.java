package ru.ssau.tk.DoubleA.javalabs.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.DoubleA.javalabs.annotation.SimpleFunctionAnnotationHandler;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.CustomFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.CustomFunctionService;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.UserService;
import ru.ssau.tk.DoubleA.javalabs.web.serializer.FunctionSerializer;
import ru.ssau.tk.DoubleA.javalabs.web.cookie.TabulatedFunctionFactoryCookieHandler;
import ru.ssau.tk.DoubleA.javalabs.web.dto.TabulatedFunctionOnArraysRequest;
import ru.ssau.tk.DoubleA.javalabs.web.dto.TabulatedFunctionOnFunctionRequest;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class TabulatedFunctionController {
    private final CustomFunctionService customFunctionService;
    private final TabulatedFunctionFactoryCookieHandler cookieHandler;
    private final UserService userService;
    
    private Map<String, MathFunction> allFunctions;

    @GetMapping("/getFunctions")
    @ResponseBody
    public List<String> getFunctions(HttpServletRequest request) {
        updateAllFunctionsMap(request);
        return new ArrayList<>(allFunctions.keySet());
    }

    private void updateAllFunctionsMap(HttpServletRequest request) {
        allFunctions = SimpleFunctionAnnotationHandler.putSimpleFunctions();
        int userId = userService.getUserIdByUsername(request.getRemoteUser());
        List<CustomFunction> functions = customFunctionService.getCustomFunctionsByUserId(userId);
        for (CustomFunction function : functions) {
            allFunctions.put(function.getName(), FunctionSerializer.deserializeFunction(function.getSerializedFunction()));
        }
    }

    @PostMapping("/create/{functionName}")
    public void createFunction(@PathVariable(name = "functionName") String functionName,
                               @RequestBody String[] functions,
                               HttpServletRequest request) {
        if (allFunctions.containsKey(functionName)) {
            throw new IllegalArgumentException("Введенное имя функции уже существует");
        }

        List<MathFunction> functionList = Arrays.stream(functions).map(allFunctions::get).toList().reversed();
        CompositeFunction function = CompositeFunction.createCompositeFunctionFromList(functionList);

        byte[] serializedFunction = FunctionSerializer.serializeCustomFunction(function);
        int userId = userService.getUserIdByUsername(request.getRemoteUser());
        CustomFunction customFunction = customFunctionService.createFunction(userId, functionName, serializedFunction);

        allFunctions.put(customFunction.getName(), FunctionSerializer.deserializeFunction(customFunction.getSerializedFunction()));
    }

    @DeleteMapping("/delete/{functionName}")
    public void deleteFunction(@PathVariable(name = "functionName") String functionName,
                               HttpServletRequest request) {
        int userId = userService.getUserIdByUsername(request.getRemoteUser());
        customFunctionService.deleteCustomFunctionByUserIdAndFunctionName(userId, functionName);
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

        return FunctionSerializer.serializeByte(function);
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

        return FunctionSerializer.serializeJson(function);
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

        return FunctionSerializer.serializeXml(function);
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
        return FunctionSerializer.serializeByte(function);
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
        return FunctionSerializer.serializeJson(function);
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
        return FunctionSerializer.serializeXml(function);
    }

}