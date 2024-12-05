package ru.ssau.tk.DoubleA.javalabs.ui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.io.FunctionsIO;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedFunctionOperationService;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedIntegrationOperator;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.CustomFunctionDAO;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.CustomFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.CustomFunctionService;
import ru.ssau.tk.DoubleA.javalabs.ui.AccessingAllClassesInPackage;
import ru.ssau.tk.DoubleA.javalabs.ui.FabricType;
import ru.ssau.tk.DoubleA.javalabs.ui.annotation.SimpleFunction;
import ru.ssau.tk.DoubleA.javalabs.ui.dto.TabulatedFunctionOnArraysRequest;
import ru.ssau.tk.DoubleA.javalabs.ui.dto.TabulatedFunctionOnFunctionRequest;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
public class TabulatedFunctionController {
    private final Map<String, MathFunction> allFunctions;
    private final Map<FabricType, TabulatedFunctionFactory> factories;
    private boolean isFirstRequestForFunctions = true;

    @Autowired
    private CustomFunctionService customFunctionService;

    public TabulatedFunctionController() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Class> classes = AccessingAllClassesInPackage.findAllClassesWithSimpleFunctionAnnotation("ru.ssau.tk.DoubleA.javalabs.functions");
        allFunctions = new HashMap<>();

        for (Class clazz : classes) {
            SimpleFunction annotation = (SimpleFunction) clazz.getAnnotation(SimpleFunction.class);
            allFunctions.put(annotation.localizedName(), (MathFunction) clazz.getConstructor().newInstance());
        }

        factories = new HashMap<>();
        factories.put(FabricType.ARRAY, new ArrayTabulatedFunctionFactory());
        factories.put(FabricType.LINKEDLIST, new LinkedListTabulatedFunctionFactory());
    }

    @PostMapping("/create/{functionName}")
    public void createFunction(@PathVariable(name = "functionName") String functionName, @RequestBody String[] functions) {
        List<MathFunction> functionList = Arrays.stream(functions).map(allFunctions::get).toList().reversed();
        CustomFunction customFunction = customFunctionService.createFunction(functionName, functionList);
        allFunctions.put(customFunction.getName(), deserializeFunction(customFunction.getSerializedFunction()));
    }

    @GetMapping("/getFunctions")
    @ResponseBody
    public List<String> getFunctions() {
        if (isFirstRequestForFunctions) {
            List<CustomFunction> functions = customFunctionService.getCustomFunctions();

            for (CustomFunction function : functions) {
                allFunctions.put(function.getName(), deserializeFunction(function.getSerializedFunction()));
            }
            isFirstRequestForFunctions = false;
            System.out.println(allFunctions);
        }
        return new ArrayList<>(allFunctions.keySet());
    }


    @PostMapping("/apply/{xValue}")
    @ResponseBody
    public String applyValue(@PathVariable(name = "xValue") double xValue,
                             @RequestBody TabulatedFunctionOnArraysRequest functionRequest,
                             HttpServletRequest request,
                             HttpServletResponse response) throws JsonProcessingException {
        TabulatedFunctionFactory factory = determineFabric(request, response);
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
        TabulatedFunctionFactory factory = determineFabric(request, response);
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
        TabulatedFunctionFactory factory = determineFabric(request, response);
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
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getXValues(),
                tabulatedFunctionRequest.getYValues()
        );
        TabulatedIntegrationOperator operator = new TabulatedIntegrationOperator(threadCount);
        return new ObjectMapper().writeValueAsString(operator.integrate(function, DEFAULT_NUMBER_OF_SECTIONS));
    }

    @PostMapping("/createTabulatedFunctionWithTableByte")
    @ResponseBody
    public byte[] createTabulatedFunctionWithTableByte(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                                                                              HttpServletRequest request,
                                                                              HttpServletResponse response) {
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getXValues(),
                tabulatedFunctionRequest.getYValues()
        );

        return serialize(function);
    }

    @PostMapping("/createTabulatedFunctionWithTableJSON")
    @ResponseBody
    public String createTabulatedFunctionWithTableJSON(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getXValues(),
                tabulatedFunctionRequest.getYValues()
        );

        return serializeJson(function);
    }

    @PostMapping("/createTabulatedFunctionWithTableXML")
    @ResponseBody
    public String createTabulatedFunctionWithTableXML(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getXValues(),
                tabulatedFunctionRequest.getYValues()
        );

        return serializeXml(function);
    }

    @PostMapping("/createTabulatedFunctionWithFunctionByte")
    @ResponseBody
    public byte[] createTabulatedFunctionWithFunctionByte(@RequestBody TabulatedFunctionOnFunctionRequest tabulatedFunctionRequest,
                                                                                 HttpServletRequest request,
                                                                                 HttpServletResponse response) {
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
                allFunctions.get(tabulatedFunctionRequest.getFunctionName()),
                tabulatedFunctionRequest.getFrom(),
                tabulatedFunctionRequest.getTo(),
                tabulatedFunctionRequest.getAmountOfPoints()
        );
        return serialize(function);
    }

    @PostMapping("/createTabulatedFunctionWithFunctionJSON")
    @ResponseBody
    public String createTabulatedFunctionWithFunctionJSON(@RequestBody TabulatedFunctionOnFunctionRequest tabulatedFunctionRequest,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
                allFunctions.get(tabulatedFunctionRequest.getFunctionName()),
                tabulatedFunctionRequest.getFrom(),
                tabulatedFunctionRequest.getTo(),
                tabulatedFunctionRequest.getAmountOfPoints()
        );
        return serializeJson(function);
    }

    @PostMapping("/createTabulatedFunctionWithFunctionXML")
    @ResponseBody
    public String createTabulatedFunctionWithFunctionXML(@RequestBody TabulatedFunctionOnFunctionRequest tabulatedFunctionRequest,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
                allFunctions.get(tabulatedFunctionRequest.getFunctionName()),
                tabulatedFunctionRequest.getFrom(),
                tabulatedFunctionRequest.getTo(),
                tabulatedFunctionRequest.getAmountOfPoints()
        );
        return serializeXml(function);
    }

    @PostMapping("/convertFromBLOB")
    @ResponseBody
    public String convertFromBLOB(@RequestBody byte[] bytes) throws JsonProcessingException {
        TabulatedFunction function = null;
        try(BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(bytes))) {
            function = FunctionsIO.deserialize(inputStream);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return new ObjectMapper().writeValueAsString(function);
    }

    @PostMapping("/convertFromXML")
    @ResponseBody
    public String convertFromXML(@RequestBody String xmlFunction) throws JsonProcessingException {
        TabulatedFunction function = null;
        try(BufferedReader reader = new BufferedReader(new StringReader(xmlFunction))) {
            function = FunctionsIO.deserializeXml(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ObjectMapper().writeValueAsString(function);
    }

    private TabulatedFunctionFactory determineFabric(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("fabricType")) {
                return factories.get(FabricType.valueOf(cookie.getValue()));
            }
        }
        saveDefaultFactoryTypeCookieIfNotExists(response);
        return factories.get(FabricType.ARRAY);
    }

    private void saveDefaultFactoryTypeCookieIfNotExists(HttpServletResponse response) {
        Cookie cookie = new Cookie("fabricType", FabricType.ARRAY.name());
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
    }

    private byte[] serialize(TabulatedFunction function) {
        byte[] serializedFunction = null;
        try(ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream outputStream = new BufferedOutputStream(byteOutputStream)) {
            FunctionsIO.serialize(outputStream, function);
            serializedFunction = byteOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serializedFunction;
    }

    private String serializeJson(TabulatedFunction function) {
        String serializedFunction = null;
        try(StringWriter stringWriter = new StringWriter();
            BufferedWriter bufferedWriter = new BufferedWriter(stringWriter)) {
            FunctionsIO.serializeJson(bufferedWriter, function);
            serializedFunction = stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serializedFunction;
    }

    private String serializeXml(TabulatedFunction function) {
        String serializedFunction = null;
        try(StringWriter stringWriter = new StringWriter();
            BufferedWriter bufferedWriter = new BufferedWriter(stringWriter)) {
            FunctionsIO.serializeXml(bufferedWriter, function);
            serializedFunction = stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serializedFunction;
    }

    private MathFunction deserializeFunction(byte[] data) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (MathFunction) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize function", e);
        }
    }
}