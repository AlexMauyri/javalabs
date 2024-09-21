package ru.ssau.tk.DoubleA.javalabs.io;

import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedDifferentialOperator;

import java.io.*;

import static ru.ssau.tk.DoubleA.javalabs.io.FunctionsIO.readTabulatedFunction;

public class TabulatedFunctionFileInputStream {
    public static void main(String[] args) {
        try (BufferedInputStream inputFileStream = new BufferedInputStream(new FileInputStream("input/binary function.bin"))) {
            System.out.println(readTabulatedFunction(inputFileStream, new ArrayTabulatedFunctionFactory()).toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        System.out.println("Введите размер и значения функции");
        try {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            TabulatedFunction consoleFunction = readTabulatedFunction(consoleReader, new LinkedListTabulatedFunctionFactory());

            TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
            TabulatedFunction consoleFunctionDerivative = differentialOperator.derive(consoleFunction);

            System.out.println(consoleFunctionDerivative.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
