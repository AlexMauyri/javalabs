package ru.ssau.tk.DoubleA.javalabs.io;

import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TabulatedFunctionFileReader {
    public static void main(String[] args) {
        try(BufferedReader reader1 = new BufferedReader(new FileReader("input/function.txt"));
            BufferedReader reader2 = new BufferedReader(new FileReader("input/function.txt"))
        ) {
            TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
            TabulatedFunction tabulatedFunction1 = FunctionsIO.readTabulatedFunction(reader1, factory);
            factory = new LinkedListTabulatedFunctionFactory();
            TabulatedFunction tabulatedFunction2 = FunctionsIO.readTabulatedFunction(reader2, factory);

            System.out.println(tabulatedFunction1.toString());
            System.out.println(tabulatedFunction2.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
