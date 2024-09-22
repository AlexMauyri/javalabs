package ru.ssau.tk.DoubleA.javalabs.io;

import org.junit.jupiter.api.*;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;

import java.io.*;
import java.nio.file.Path;

public class TabulatedFunctionFileWriterReaderTest extends AbstractTest {

    TabulatedFunction[] functions;

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
            try (BufferedWriter bufferedWriter  = new BufferedWriter(new FileWriter("temp/TabulatedFunctionFileWriterTest" + (index + 1)))) {
                FunctionsIO.writeTabulatedFunction(bufferedWriter, functions[index]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void readTest() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        for (int index = 0; index < functions.length; ++index) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("temp/TabulatedFunctionFileWriterTest" + (index + 1))))
            {
                TabulatedFunction function = FunctionsIO.readTabulatedFunction(bufferedReader, factory);
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
