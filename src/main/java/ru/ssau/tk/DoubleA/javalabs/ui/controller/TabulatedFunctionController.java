package ru.ssau.tk.DoubleA.javalabs.ui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.io.FunctionsIO;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.ui.FabricType;
import ru.ssau.tk.DoubleA.javalabs.ui.dto.TabulatedFunctionOnArraysRequest;
import ru.ssau.tk.DoubleA.javalabs.ui.dto.TabulatedFunctionOnFunctionRequest;

import java.io.*;
import java.util.*;

@Controller
public class TabulatedFunctionController {
    private final Map<String, MathFunction> functions;
    private final Map<FabricType, TabulatedFunctionFactory> factories;

    public TabulatedFunctionController() {
        functions = new HashMap<>();
        functions.put("Тождественная функция", new IdentityFunction());
        functions.put("Квадратичная функция", new SqrFunction());
        functions.put("Единичная функция", new UnitFunction());
        functions.put("Нулевая функция", new ZeroFunction());

        factories = new HashMap<>();
        factories.put(FabricType.ARRAY, new ArrayTabulatedFunctionFactory());
        factories.put(FabricType.LINKEDLIST, new LinkedListTabulatedFunctionFactory());
    }

    @GetMapping("/getFunctions")
    @ResponseBody
    public List<String> getFunctions() {
        return new ArrayList<>(functions.keySet());
    }


    @GetMapping("/createTabulatedFunctionWithTable")
    public String createWithTable() {
        return "createTabulatedFunctionWithTable";
    }

    @GetMapping("/createTabulatedFunctionWithFunction")
    public String createWithFunction() {
        return "createTabulatedFunctionWithFunction";
    }

    @GetMapping("/doDifferential")
    public String doDifferential() {
        return "doDifferential";
    }

    @GetMapping("/doIntegral")
    public String doIntegral() {
        return "doIntegral";
    }

    @GetMapping("/functionArithmetic")
    public String functionArithmetic() {
        return "functionArithmetic";
    }

    @GetMapping("popup/createTabulatedFunction")
    public String chooseMethod() {
        return "popup/createTabulatedFunction";
    }

    @GetMapping("popup/tableCreation")
    public String createFromTable() {
        return "popup/tableCreation";
    }

    @GetMapping("popup/functionCreation")
    public String createFromFunction() {
        return "popup/functionCreation";
    }

    @PostMapping("/createTabulatedFunctionWithTableByte")
    @ResponseBody
    public byte[] createTabulatedFunctionWithTableByte(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                                                                              HttpServletRequest request,
                                                                              HttpServletResponse response) {
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getX(),
                tabulatedFunctionRequest.getY()
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
                tabulatedFunctionRequest.getX(),
                tabulatedFunctionRequest.getY()
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
                tabulatedFunctionRequest.getX(),
                tabulatedFunctionRequest.getY()
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
            functions.get(tabulatedFunctionRequest.getFunctionName()),
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
                functions.get(tabulatedFunctionRequest.getFunctionName()),
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
                functions.get(tabulatedFunctionRequest.getFunctionName()),
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
}
