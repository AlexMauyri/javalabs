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

    @PostMapping("/createTabulatedFunctionWithTable")
    @ResponseBody
    public String createTabulatedFunctionWithTable(@RequestBody TabulatedFunctionOnArraysRequest tabulatedFunctionRequest,
                                                                              HttpServletRequest request,
                                                                              HttpServletResponse response) throws JsonProcessingException {
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
                tabulatedFunctionRequest.getX(),
                tabulatedFunctionRequest.getY()
        );

        byte[] serializedFunction = serialize(function);
        String result = "{value:" + new ObjectMapper().writeValueAsString(serializedFunction) + "}";
        return result;
    }

    @PostMapping("/createTabulatedFunctionWithFunction")
    public ResponseEntity<TabulatedFunction> createTabulatedFunctionWithFunction(@RequestBody TabulatedFunctionOnFunctionRequest tabulatedFunctionRequest,
                                                                                 HttpServletRequest request,
                                                                                 HttpServletResponse response) {
        TabulatedFunctionFactory factory = determineFabric(request, response);
        TabulatedFunction function = factory.create(
            functions.get(tabulatedFunctionRequest.getFunctionName()),
            tabulatedFunctionRequest.getFrom(),
            tabulatedFunctionRequest.getTo(),
            tabulatedFunctionRequest.getAmountOfPoints()
        );
        return ResponseEntity.ok(function);
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
}
