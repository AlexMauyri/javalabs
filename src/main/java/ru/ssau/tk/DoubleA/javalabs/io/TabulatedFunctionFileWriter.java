package ru.ssau.tk.DoubleA.javalabs.io;

import ru.ssau.tk.DoubleA.javalabs.functions.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionFileWriter {
    public static void main(String[] args) {
        try(BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter("output/array function.txt"));
            BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter("output/linked list function.txt"))
        ) {
            AbstractTabulatedFunction tabulatedFunction1 = new ArrayTabulatedFunction(new SqrFunction(), 1.0, 3.0, 25);
            AbstractTabulatedFunction tabulatedFunction2 = new LinkedListTabulatedFunction(new NRootCalculateFunction(5), 1.5, 10.0, 10);

            FunctionsIO.writeTabulatedFunction(bufferedWriter1, tabulatedFunction1);
            FunctionsIO.writeTabulatedFunction(bufferedWriter2, tabulatedFunction2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
