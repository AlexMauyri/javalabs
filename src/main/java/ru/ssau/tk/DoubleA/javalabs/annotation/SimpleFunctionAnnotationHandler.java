package ru.ssau.tk.DoubleA.javalabs.annotation;

import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleFunctionAnnotationHandler {
    public static Map<String, MathFunction> putSimpleFunctions() {
        Map<String, MathFunction> allFunctions = new HashMap<String, MathFunction>();
        List<Class<?>> classes = AccessingAllClassesInPackage.findAllClassesWithSimpleFunctionAnnotation("ru.ssau.tk.DoubleA.javalabs.functions");

        try {
            for (Class<?> clazz : classes) {
                SimpleFunction annotation = clazz.getAnnotation(SimpleFunction.class);
                allFunctions.put(annotation.localizedName(), (MathFunction) clazz.getConstructor().newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return allFunctions;
    }
}
