package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.exceptions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunctionTest {
    LinkedListTabulatedFunction manualList = new LinkedListTabulatedFunction(new double[]{-3, 1.5, 6, 10.5, 15}, new double[]{9, 2.25, 36, 110.25, 225});
    LinkedListTabulatedFunction discreteList = new LinkedListTabulatedFunction(new SqrFunction(), -3, 15, 5);
    LinkedListTabulatedFunction manualList2 = new LinkedListTabulatedFunction(new double[]{9, 11}, new double[]{18, 25});

    @Test
    void testToString() {
        Assertions.assertEquals("LinkedListTabulatedFunction size = 5\n[-3.0; 9.0]\n[1.5; 2.25]\n[6.0; 36.0]\n[10.5; 110.25]\n[15.0; 225.0]", manualList.toString());
        Assertions.assertEquals("LinkedListTabulatedFunction size = 5\n[-3.0; 9.0]\n[1.5; 2.25]\n[6.0; 36.0]\n[10.5; 110.25]\n[15.0; 225.0]", discreteList.toString());
        Assertions.assertEquals("LinkedListTabulatedFunction size = 2\n[9.0; 18.0]\n[11.0; 25.0]", manualList2.toString());
    }

    @Test
    void testConstructor() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(new double[]{1.0}, new double[]{1.4}));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(new double[]{1.0}, new double[]{}));

        Assertions.assertThrows(DifferentLengthOfArraysException.class, () -> new LinkedListTabulatedFunction(new double[]{1.0, 2.0, 3.0}, new double[]{1.4, 2.8}));
        Assertions.assertThrows(DifferentLengthOfArraysException.class, () -> new LinkedListTabulatedFunction(new double[]{1.0, 2.0}, new double[]{1.4, 2.8, 4.2}));

        Assertions.assertThrows(ArrayIsNotSortedException.class, () -> new LinkedListTabulatedFunction(new double[]{1.0, 3.0, 2.0}, new double[]{1.4, 2.8, 4.2}));
        Assertions.assertThrows(ArrayIsNotSortedException.class, () -> new LinkedListTabulatedFunction(new double[]{1.0, 2.0, -3.0}, new double[]{1.4, 2.8, 4.2}));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(new SqrFunction(), 1.0, 2.0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(new SqrFunction(), 5.0, 5.0, 1));
    }

    @Test
    void testIterator() {
        Iterator<Point> iterator = manualList.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            Point point = iterator.next();
            Assertions.assertEquals(manualList.getX(i++), point.x);
        }
        Assertions.assertThrows(NoSuchElementException.class, iterator::next);

        i = 0;
        for (Point point : manualList) {
            Assertions.assertEquals(manualList.getX(i++), point.x);
        }
    }

    @Test
    void testGetCount() {
        Assertions.assertEquals(5, manualList.getCount());
        Assertions.assertEquals(5, discreteList.getCount());
    }

    @Test
    void testLRBound() {
        Assertions.assertEquals(-3, manualList.leftBound());
        Assertions.assertEquals(15, manualList.rightBound());
        Assertions.assertEquals(-3, discreteList.leftBound());
        Assertions.assertEquals(15, discreteList.rightBound());
    }

    @Test
    void testGet() {
        Assertions.assertEquals(6, manualList.getX(2));
        Assertions.assertEquals(6, discreteList.getX(2));
        Assertions.assertEquals(2.25, manualList.getY(1));
        Assertions.assertEquals(2.25, discreteList.getY(1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.getX(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.getX(5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.getX(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.getX(5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.getY(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.getY(5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.getY(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.getY(5));
    }

    @Test
    void testSetY() {
        manualList.setY(3, 169);
        Assertions.assertEquals(169, manualList.getY(3));

        discreteList.setY(3, 169);
        Assertions.assertEquals(169, discreteList.getY(3));

        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.setY(-1, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.setY(5, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.setY(-1, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.setY(5, 0));
    }

    @Test
    void testIndexOf() {
        manualList.setY(3, 169);
        discreteList.setY(3, 169);

        Assertions.assertEquals(4, manualList.indexOfX(15));
        Assertions.assertEquals(4, discreteList.indexOfX(15));
        Assertions.assertEquals(-1, manualList.indexOfX(20));
        Assertions.assertEquals(-1, discreteList.indexOfX(20));
        Assertions.assertEquals(3, manualList.indexOfY(169));
        Assertions.assertEquals(3, discreteList.indexOfY(169));
        Assertions.assertEquals(-1, manualList.indexOfY(110.25));
        Assertions.assertEquals(-1, discreteList.indexOfY(110.25));
    }

    @Test
    void testExtrapolateLeft() {
        manualList.setY(3, 110.25);
        discreteList.setY(3, 110.25);

        Assertions.assertEquals(13.5, manualList.extrapolateLeft(-6));
        Assertions.assertEquals(13.5, discreteList.extrapolateLeft(-6));
    }

    @Test
    void testExtrapolateRight() {
        Assertions.assertEquals(352.5, manualList.extrapolateRight(20));
        Assertions.assertEquals(352.5, discreteList.extrapolateRight(20));
    }

    @Test
    void testInterpolate() {
        Assertions.assertEquals(6.0, manualList.interpolate(2, 1));
        Assertions.assertEquals(13.5, discreteList.interpolate(3, 1));
        Assertions.assertEquals(69.0, manualList.interpolate(8, 2));
        Assertions.assertEquals(123.0, discreteList.interpolate(11, 3));
        Assertions.assertEquals(85.5, manualList.interpolate(9, 2));
        Assertions.assertEquals(85.5, discreteList.interpolate(9, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.interpolate(1, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.interpolate(2, 5));
        Assertions.assertThrows(InterpolationException.class, () -> manualList.interpolate(10.5, 2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.interpolate(3, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.interpolate(4, 5));
        Assertions.assertThrows(InterpolationException.class, () -> discreteList.interpolate(10.5, 3));
    }

    @Test
    void testApply() {
        Assertions.assertEquals(13.5, manualList.apply(-6));
        Assertions.assertEquals(13.5, discreteList.apply(-6));
        Assertions.assertEquals(352.5, manualList.apply(20));
        Assertions.assertEquals(352.5, discreteList.apply(20));
        Assertions.assertEquals(2.25, manualList.apply(1.5));
        Assertions.assertEquals(2.25, discreteList.apply(1.5));
        Assertions.assertEquals(85.5, manualList.apply(9));
        Assertions.assertEquals(85.5, discreteList.apply(9));
    }

    @Test
    void testFloorOfX() {
        Assertions.assertEquals(5, manualList.floorIndexOfX(15.5));
        Assertions.assertEquals(5, discreteList.floorIndexOfX(15.5));
        Assertions.assertEquals(0, manualList.floorIndexOfX(-3));
        Assertions.assertEquals(0, discreteList.floorIndexOfX(-3));

        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.floorIndexOfX(-4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.floorIndexOfX(-5));

        Assertions.assertEquals(6, manualList.floorNodeOfX(9).x);
        Assertions.assertEquals(6, discreteList.floorNodeOfX(9).x);
        Assertions.assertEquals(9, manualList.floorNodeOfX(-3).y);
        Assertions.assertEquals(9, discreteList.floorNodeOfX(-3).y);

        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.floorNodeOfX(-4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.floorNodeOfX(-5));
    }

    @Test
    void testGetNode() {
        Assertions.assertEquals(15, manualList.getNode(4).x);
        Assertions.assertEquals(15, discreteList.getNode(4).x);
        Assertions.assertEquals(9, manualList.getNode(0).y);
        Assertions.assertEquals(9, discreteList.getNode(0).y);

        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.getNode(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.getNode(5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.getNode(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.getNode(5));
    }

    @Test
    void testAddNode() {
        manualList.addNode(16, 256);
        discreteList.addNode(16, 256);
        Assertions.assertEquals(6, manualList.getCount());
        Assertions.assertEquals(6, discreteList.getCount());
        Assertions.assertEquals(4, manualList.floorIndexOfX(15.5));
        Assertions.assertEquals(4, discreteList.floorIndexOfX(15.5));
        Assertions.assertEquals(380, manualList.apply(20));
        Assertions.assertEquals(380, discreteList.apply(20));

        manualList2.addNode(14, 196);
        Assertions.assertEquals(3, manualList2.getCount());
        Assertions.assertEquals(538, manualList2.apply(20));
        Assertions.assertEquals(0, manualList2.floorIndexOfX(10));
        Assertions.assertEquals(3, manualList2.floorIndexOfX(18));

        manualList2.addNode(16, 256);
        Assertions.assertEquals(1, manualList2.floorIndexOfX(13));
        Assertions.assertEquals(2, manualList2.floorIndexOfX(15));
        Assertions.assertEquals(4, manualList2.floorIndexOfX(17));
        Assertions.assertEquals(4, manualList2.getCount());
        Assertions.assertEquals(9, manualList2.getX(0));
    }

    @Test
    void testRemove() {
        manualList2.addNode(14, 196);
        manualList2.addNode(16, 256);
        manualList2.addNode(32, 1024);

        manualList2.remove(0);
        Assertions.assertEquals(11, manualList2.getX(0));
        Assertions.assertEquals(4, manualList2.getCount());
        Assertions.assertEquals(196, manualList2.getNode(1).y);

        manualList.remove(2);
        Assertions.assertEquals(10.5, manualList.getX(2));
        Assertions.assertEquals(-1, manualList.indexOfX(6));
        Assertions.assertEquals(-1, manualList.indexOfY(36));

        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.remove(-2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manualList.remove(6));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.remove(-2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> discreteList.remove(6));

        manualList2.remove(0);
        manualList2.remove(2);
        Assertions.assertThrows(IllegalStateException.class, () -> manualList2.remove(0));
        Assertions.assertThrows(IllegalStateException.class, () -> manualList2.remove(1));
    }

    @Test
    void testInsert() {
        manualList2.addNode(16, 256);
        manualList2.addNode(32, 1024);

        manualList2.insert(10, 100);
        Assertions.assertEquals(5, manualList2.getCount());
        Assertions.assertEquals(1, manualList2.indexOfX(10));

        manualList2.insert(22, 484);
        Assertions.assertEquals(25, manualList2.getY(2));

        manualList2.insert(64, 4096);
        Assertions.assertEquals(22, manualList2.getNode(4).x);

        Assertions.assertEquals(1, manualList2.indexOfX(10));
        Assertions.assertEquals(3, manualList2.indexOfX(16));
        Assertions.assertEquals(4, manualList2.indexOfX(22));
        Assertions.assertEquals(5, manualList2.indexOfX(32));
        Assertions.assertEquals(6, manualList2.indexOfX(64));
    }
}
