package ru.ssau.tk.DoubleA.javalabs.io;

import ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedDifferentialOperator;

import java.io.*;

public class LinkedListTabulatedFunctionSerialization {
    public static void main(String[] args) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("output/serialized linked list functions.bin"))) {
            TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());

            TabulatedFunction function = new LinkedListTabulatedFunction(new SqrFunction(), -3, 15, 5);
            TabulatedFunction functionDerive1 = differentialOperator.derive(function);
            TabulatedFunction functionDerive2 = differentialOperator.derive(functionDerive1);

            FunctionsIO.serialize(bufferedOutputStream, function);
            FunctionsIO.serialize(bufferedOutputStream, functionDerive1);
            FunctionsIO.serialize(bufferedOutputStream, functionDerive2);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("output/serialized linked list functions.bin"))) {
            TabulatedFunction function = FunctionsIO.deserialize(bufferedInputStream);
            TabulatedFunction functionDerive1 = FunctionsIO.deserialize(bufferedInputStream);
            TabulatedFunction functionDerive2 = FunctionsIO.deserialize(bufferedInputStream);

            System.out.println(function.toString());
            System.out.println(functionDerive1.toString());
            System.out.println(functionDerive2.toString());
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
