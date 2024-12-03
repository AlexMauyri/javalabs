package ru.ssau.tk.DoubleA.javalabs.ui;

import ru.ssau.tk.DoubleA.javalabs.ui.annotation.SimpleFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AccessingAllClassesInPackage {

    public static List<Class> findAllClassesWithSimpleFunctionAnnotation(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName)).filter(Objects::nonNull)
                .filter(funcClass -> funcClass.getAnnotation(SimpleFunction.class) != null)
                .collect(Collectors.toList());
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}