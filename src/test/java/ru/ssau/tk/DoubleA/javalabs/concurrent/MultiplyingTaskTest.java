package ru.ssau.tk.DoubleA.javalabs.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.*;

public class MultiplyingTaskTest extends AbstractTest {

    @Test
    void test() throws InterruptedException {
        TabulatedFunction function = new LinkedListTabulatedFunction(new double[]{1, 2, 3, 4}, new double[]{1, 1, 1, 1});

        Thread thread = new Thread(new MultiplyingTask(function));
        thread.start();
        thread.join();

        for (Point point : function) {
            Assertions.assertEquals(2.0, point.y, EPSILON);
        }

        function = new LinkedListTabulatedFunction(new double[]{1, 2, 3, 4}, new double[]{1, 4, 12, -123});

        thread = new Thread(new MultiplyingTask(function));
        thread.start();
        thread.join();

        double[] expected = new double[]{2, 8, 24, -246};
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(expected[i], function.getY(i), EPSILON);
        }
    }
}
