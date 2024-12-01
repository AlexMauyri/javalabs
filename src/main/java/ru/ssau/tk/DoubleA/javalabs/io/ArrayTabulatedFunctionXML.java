package ru.ssau.tk.DoubleA.javalabs.io;

import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;

import java.io.*;

public class ArrayTabulatedFunctionXML {
    public static void main(String[] args) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output/serialized array xml.xml"))) {
            ArrayTabulatedFunction function = new ArrayTabulatedFunction(new SqrFunction(), -3, 15, 5);
            FunctionsIO.serializeXml(bufferedWriter, function);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("output/serialized array xml.xml"))) {
            ArrayTabulatedFunction function = (ArrayTabulatedFunction) FunctionsIO.deserializeXml(bufferedReader);

            System.out.println(function);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
