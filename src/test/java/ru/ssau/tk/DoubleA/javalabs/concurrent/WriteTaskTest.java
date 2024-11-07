package ru.ssau.tk.DoubleA.javalabs.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.Point;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.UnitFunction;

public class WriteTaskTest {
    TabulatedFunction function = new ArrayTabulatedFunction(new UnitFunction(), 0, 3.5, 20);

    @Test
    void testWrite() throws InterruptedException {
        Thread thread = new Thread(new WriteTask(function, 50));
        thread.start();
        thread.join();

        for (Point point : function) {
            Assertions.assertEquals(50, point.y);
        }
    }
}
