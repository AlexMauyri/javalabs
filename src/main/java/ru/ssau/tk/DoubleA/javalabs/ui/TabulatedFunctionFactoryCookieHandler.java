package ru.ssau.tk.DoubleA.javalabs.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;

import java.util.HashMap;
import java.util.Map;

@Component
public class TabulatedFunctionFactoryCookieHandler {
    private final Map<FabricType, TabulatedFunctionFactory> factories;

    public TabulatedFunctionFactoryCookieHandler() {
        factories = new HashMap<>();
        factories.put(FabricType.ARRAY, new ArrayTabulatedFunctionFactory());
        factories.put(FabricType.LINKEDLIST, new LinkedListTabulatedFunctionFactory());
    }

    public TabulatedFunctionFactory determineFabric(HttpServletRequest request, HttpServletResponse response) {
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
}
