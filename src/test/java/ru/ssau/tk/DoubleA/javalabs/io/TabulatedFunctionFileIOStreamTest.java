package ru.ssau.tk.DoubleA.javalabs.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;

import java.io.*;
import java.nio.file.Path;

import static ru.ssau.tk.DoubleA.javalabs.io.FunctionsIO.readTabulatedFunction;
import static ru.ssau.tk.DoubleA.javalabs.io.FunctionsIO.writeTabulatedFunction;

public class TabulatedFunctionFileIOStreamTest extends AbstractTest {
    TabulatedFunction[] functionsOut = new TabulatedFunction[]{
            new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 1.0, 10.0, 10),
            new LinkedListTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), -16.5, -1.7, 25),
            new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 7.8, 9.8, 15),
            new LinkedListTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), 6.0, 112.0, 5),
            new ArrayTabulatedFunction(new CompositeFunction(new SqrFunction(), new SqrFunction()), -124.0, 256.0, 50)
    };
    TabulatedFunction[] functionsIn = new TabulatedFunction[5];
    TabulatedFunction[] functionsInA = new TabulatedFunction[5];
    TabulatedFunction[] functionsInLL = new TabulatedFunction[5];

    @Test
    void testWrite() {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("temp/binary_ATF_LLTF.bin"))) {
            for (int index = 0; index < 5; index++) {
                writeTabulatedFunction(outputStream, functionsOut[index]);
                writeTabulatedFunction(outputStream, functionsOut[index]);
                writeTabulatedFunction(outputStream, functionsOut[index]);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void testRead() {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("temp/binary_ATF_LLTF.bin"))) {
            for (int index = 0; index < 5; index++) {
                if (index % 2 == 0) {
                    functionsIn[index] = readTabulatedFunction(inputStream, new ArrayTabulatedFunctionFactory());
                } else {
                    functionsIn[index] = readTabulatedFunction(inputStream, new LinkedListTabulatedFunctionFactory());
                }
                functionsInA[index] = readTabulatedFunction(inputStream, new ArrayTabulatedFunctionFactory());
                functionsInLL[index] = readTabulatedFunction(inputStream, new LinkedListTabulatedFunctionFactory());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        for (int index = 0; index < 5; index++) {
            Assertions.assertEquals(functionsOut[index].toString(), functionsIn[index].toString());
            if (index % 2 == 0) {
                Assertions.assertInstanceOf(ArrayTabulatedFunction.class, functionsIn[index]);
                Assertions.assertInstanceOf(ArrayTabulatedFunction.class, functionsOut[index]);
            } else {
                Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, functionsIn[index]);
                Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, functionsOut[index]);
            }
            Assertions.assertInstanceOf(ArrayTabulatedFunction.class, functionsInA[index]);
            Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, functionsInLL[index]);
        }

        cleanUpDirectory(Path.of("temp"));
    }
}
