package ru.ssau.tk.DoubleA.javalabs.io;

import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.CompositeFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedDifferentialOperator;

import java.io.*;

public class ArrayTabulatedFunctionSerialization {
    public static void main(String[] args) {
        try(BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("output/serialized array functions.bin"))) {
            TabulatedFunction function1 = new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 1.0, 10.0, 10);
            TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
            TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);
            TabulatedFunction function2 = operator.derive(function1);
            TabulatedFunction function3 = operator.derive(function2);

            FunctionsIO.serialize(outputStream, function1);
            FunctionsIO.serialize(outputStream, function2);
            FunctionsIO.serialize(outputStream, function3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("output/serialized array functions.bin"))) {
            TabulatedFunction function1 = FunctionsIO.deserialize(inputStream);
            TabulatedFunction function2 = FunctionsIO.deserialize(inputStream);
            TabulatedFunction function3 = FunctionsIO.deserialize(inputStream);

            System.out.println(function1.toString());
            System.out.println(function2.toString());
            System.out.println(function3.toString());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
