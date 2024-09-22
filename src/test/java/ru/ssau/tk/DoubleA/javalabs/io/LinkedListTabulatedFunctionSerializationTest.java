package ru.ssau.tk.DoubleA.javalabs.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.*;

import java.io.*;
import java.nio.file.Path;

public class LinkedListTabulatedFunctionSerializationTest extends AbstractTest {
    LinkedListTabulatedFunction[] functionsOut = new LinkedListTabulatedFunction[]{
            new LinkedListTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 1.0, 10.0, 10),
            new LinkedListTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), -16.5, -1.7, 25),
            new LinkedListTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 7.8, 9.8, 15),
            new LinkedListTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 6.0, 112.0, 5),
            new LinkedListTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), -124.0, 256.0, 50)
    };
    TabulatedFunction[] functionsIn = new TabulatedFunction[5];

    @Test
    void testWrite() {
        try (BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream("temp/ListTabFun_serialize_test.bin"))) {
            for (int index = 0; index < 5; index++) {
                FunctionsIO.serialize(bufferedWriter, functionsOut[index]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRead() {
        try (BufferedInputStream bufferedReader = new BufferedInputStream(new FileInputStream("temp/ListTabFun_serialize_test.bin"))) {
            for (int index = 0; index < 5; index++) {
                functionsIn[index] = FunctionsIO.deserialize(bufferedReader);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (int index = 0; index < 5; index++) {
            Assertions.assertEquals(functionsOut[index].toString(), functionsIn[index].toString());
            Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, functionsOut[index]);
            Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, functionsIn[index]);
        }

        cleanUpDirectory(Path.of("temp"));
    }
}
