package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PointTest {

    @Test
    void test() {
        Point point = new Point(3.0, 5.0);

        Assertions.assertEquals(point, new Point(3.0, 5.0));
        Assertions.assertNotEquals(point, new Point(3.5, 5.0));
        Assertions.assertNotEquals(point, new Point(3.0, 5.5));
        Assertions.assertNotEquals(point, new Point(3.7, 5.5));
        Assertions.assertNotEquals(point, "Hello");
        Assertions.assertNotEquals(point, null);
    }
}
