package ru.ssau.tk.DoubleA.javalabs.io;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.*;

import java.io.*;
import java.nio.file.Path;

public class ArrayTabulatedFunctionXMLTest extends AbstractTest {
    ArrayTabulatedFunction[] functions;

    @BeforeEach
    void init() {
        functions = new ArrayTabulatedFunction[] {
                new ArrayTabulatedFunction(new SqrFunction(), 1.0, 3.0, 25),
                new ArrayTabulatedFunction(new NRootCalculateFunction(5), 10.0, 90.0, 25),
                new ArrayTabulatedFunction(new DerivativeFunction(new SqrFunction()), 15.0, 40.0, 20),
                new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 5.0, 30.0, 15),
                new ArrayTabulatedFunction(new DerivativeFunction(new NRootCalculateFunction(3)), 13.0, 50.0, 25)
        };
    }

    @Test
    void writeTest() {
        for (int index = 0; index < functions.length; ++index) {
            try (BufferedWriter bufferedWriter  = new BufferedWriter(new FileWriter("temp/TabulatedFunctionXMLTest" + (index + 1) + ".xml"))) {
                FunctionsIO.serializeXml(bufferedWriter, functions[index]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void readTest() {
        for (int index = 0; index < functions.length; ++index) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("temp/TabulatedFunctionXMLTest" + (index + 1) + ".xml")))
            {
                TabulatedFunction function = FunctionsIO.deserializeXml(bufferedReader);
                Assertions.assertEquals(functions[index].toString(), function.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterAll
    static void close() {
        Path path = Path.of("temp//");

        cleanUpDirectory(path);
    }
}
