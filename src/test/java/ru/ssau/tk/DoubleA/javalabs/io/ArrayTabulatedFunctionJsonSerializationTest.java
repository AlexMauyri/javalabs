package ru.ssau.tk.DoubleA.javalabs.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.*;

import java.io.*;
import java.nio.file.Path;

public class ArrayTabulatedFunctionJsonSerializationTest extends AbstractTest {
    ArrayTabulatedFunction[] functionsOut = new ArrayTabulatedFunction[]{
            new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 1.0, 10.0, 10),
            new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), -16.5, -1.7, 25),
            new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 7.8, 9.8, 15),
            new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 6.0, 112.0, 5),
            new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), -124.0, 256.0, 50)
        };
    TabulatedFunction[] functionsIn = new TabulatedFunction[5];

    @Test
    void testWrite() {
        for (int index = 1; index <= 5; index++) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("temp/JSON_serialize_test_" + index + ".json"))) {
                FunctionsIO.serializeJson(bufferedWriter, functionsOut[index-1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testRead() {
        for (int index = 1; index <= 5; index++) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("temp/JSON_serialize_test_" + index + ".json"))) {
                functionsIn[index-1] = FunctionsIO.deserializeJson(bufferedReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int index = 0; index < 5; index++) {
            Assertions.assertEquals(functionsOut[index].toString(), functionsIn[index].toString());
            Assertions.assertInstanceOf(ArrayTabulatedFunction.class, functionsOut[index]);
            Assertions.assertInstanceOf(ArrayTabulatedFunction.class, functionsIn[index]);
        }

        cleanUpDirectory(Path.of("temp"));
    }
}
