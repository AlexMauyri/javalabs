package ru.ssau.tk.DoubleA.javalabs.io;

import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.CompositeFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;

import java.io.*;

public class ArrayTabulatedFunctionJsonSerialization {
    public static void main(String[] args) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output/serialized array functions.json"))) {
            ArrayTabulatedFunction function = new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 1.0, 10.0, 10);

            FunctionsIO.serializeJson(bufferedWriter, function);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("output/serialized array functions.json"))) {
            TabulatedFunction function = FunctionsIO.deserializeJson(bufferedReader);

            System.out.println(function);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
