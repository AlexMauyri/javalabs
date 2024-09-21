package ru.ssau.tk.DoubleA.javalabs.io;

import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static ru.ssau.tk.DoubleA.javalabs.io.FunctionsIO.writeTabulatedFunction;

public class TabulatedFunctionFileOutputStream {
    public static void main(String[] args) {
        ArrayTabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{9, 11}, new double[]{18, 25});
        LinkedListTabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(new SqrFunction(), -3, 15, 5);

        try (BufferedOutputStream arrayTabFunOut = new BufferedOutputStream(new FileOutputStream("output/array function.bin"));
             BufferedOutputStream listTabFunOut = new BufferedOutputStream(new FileOutputStream("output/linked list function.bin"))) {

            writeTabulatedFunction(arrayTabFunOut, arrayFunction);
            writeTabulatedFunction(listTabFunOut, linkedListFunction);

        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
