package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinkedListTabulatedFunctionTest {

    @Test
    void test() {
        LinkedListTabulatedFunction manualList = new LinkedListTabulatedFunction(
                                                 new double[] {-3, 1.5, 6, 10.5, 15},
                                                 new double[] {9, 2.25, 36, 110.25, 225}
        );
        LinkedListTabulatedFunction discreteList = new LinkedListTabulatedFunction(new SqrFunction(), -3, 15, 5);

        // getCount() tests
        Assertions.assertEquals(5, manualList.getCount());
        Assertions.assertEquals(5, discreteList.getCount());

        // leftBound(), rightBound() tests
        Assertions.assertEquals(-3, manualList.leftBound());
        Assertions.assertEquals(15, manualList.rightBound());
        Assertions.assertEquals(-3, discreteList.leftBound());
        Assertions.assertEquals(15, discreteList.rightBound());

        // getX(), getY() tests
        Assertions.assertEquals(6, manualList.getX(2));
        Assertions.assertEquals(6, discreteList.getX(2));
        Assertions.assertEquals(2.25, manualList.getY(1));
        Assertions.assertEquals(2.25, discreteList.getY(1));

        // setY() tests
        manualList.setY(3, 169);
        Assertions.assertEquals(169, manualList.getY(3));
        discreteList.setY(3, 169);
        Assertions.assertEquals(169, discreteList.getY(3));

        // indexOfX(), indexOfY() tests
        Assertions.assertEquals(4, manualList.indexOfX(15));
        Assertions.assertEquals(4, discreteList.indexOfX(15));
        Assertions.assertEquals(-1, manualList.indexOfX(20));
        Assertions.assertEquals(-1, discreteList.indexOfX(20));
        Assertions.assertEquals(3, manualList.indexOfY(169));
        Assertions.assertEquals(3, discreteList.indexOfY(169));
        Assertions.assertEquals(-1, manualList.indexOfY(110.25));
        Assertions.assertEquals(-1, discreteList.indexOfY(110.25));

        manualList.setY(3, 110.25);
        discreteList.setY(3, 110.25);
        // extrapolateLeft() tests
        Assertions.assertEquals(13.5, manualList.extrapolateLeft(-6));
        Assertions.assertEquals(13.5, discreteList.extrapolateLeft(-6));
        // extrapolateRight() tests
        Assertions.assertEquals(352.5, manualList.extrapolateRight(20));
        Assertions.assertEquals(352.5, discreteList.extrapolateRight(20));
        // interpolate() tests
        Assertions.assertEquals(13.5, manualList.interpolate(-6, 0));
        Assertions.assertEquals(13.5, discreteList.interpolate(-6, 0));
        Assertions.assertEquals(352.5, manualList.interpolate(20, 5));
        Assertions.assertEquals(352.5, discreteList.interpolate(20, 5));
        Assertions.assertEquals(85.5, manualList.interpolate(9, 2));
        Assertions.assertEquals(85.5, discreteList.interpolate(9, 2));

        // apply() tests
        Assertions.assertEquals(13.5, manualList.apply(-6));
        Assertions.assertEquals(13.5, discreteList.apply(-6));
        Assertions.assertEquals(352.5, manualList.apply(20));
        Assertions.assertEquals(352.5, discreteList.apply(20));
        Assertions.assertEquals(2.25, manualList.apply(1.5));
        Assertions.assertEquals(2.25, discreteList.apply(1.5));
        Assertions.assertEquals(85.5, manualList.apply(9));
        Assertions.assertEquals(85.5, discreteList.apply(9));

        // floorIndexOfX() tests
        Assertions.assertEquals(5, manualList.floorIndexOfX(15.5));
        Assertions.assertEquals(5, discreteList.floorIndexOfX(15.5));
        Assertions.assertEquals(1, manualList.floorIndexOfX(4));
        Assertions.assertEquals(1, discreteList.floorIndexOfX(4));

        // floorNodeOfX() tests
        Assertions.assertEquals(6, manualList.floorNodeOfX(9).x);
        Assertions.assertEquals(6, discreteList.floorNodeOfX(9).x);
        Assertions.assertEquals(2.25, manualList.floorNodeOfX(4).y);
        Assertions.assertEquals(2.25, discreteList.floorNodeOfX(4).y);

        // getNode() tests
        Assertions.assertEquals(15, manualList.getNode(4).x);
        Assertions.assertEquals(15, discreteList.getNode(4).x);
        Assertions.assertEquals(9, manualList.getNode(0).y);
        Assertions.assertEquals(9, discreteList.getNode(0).y);

        manualList.addNode(16, 256);
        discreteList.addNode(16, 256);
        // addNode() tests
        Assertions.assertEquals(6, manualList.getCount());
        Assertions.assertEquals(6, discreteList.getCount());
        Assertions.assertEquals(4, manualList.floorIndexOfX(15.5));
        Assertions.assertEquals(4, discreteList.floorIndexOfX(15.5));
        Assertions.assertEquals(380, manualList.apply(20));
        Assertions.assertEquals(380, discreteList.apply(20));
        //
        LinkedListTabulatedFunction manualList2 = new LinkedListTabulatedFunction(new double[] {}, new double[] {});
        //
        manualList2.addNode(14, 196);
        Assertions.assertEquals(1, manualList2.getCount());
        Assertions.assertEquals(20, manualList2.apply(20));
        Assertions.assertEquals(0, manualList2.floorIndexOfX(10));
        Assertions.assertEquals(1, manualList2.floorIndexOfX(18));
        //
        manualList2.addNode(16, 256);
        Assertions.assertEquals(0, manualList2.floorIndexOfX(13));
        Assertions.assertEquals(0, manualList2.floorIndexOfX(15));
        Assertions.assertEquals(2, manualList2.floorIndexOfX(17));
        Assertions.assertEquals(2, manualList2.getCount());
        Assertions.assertEquals(14, manualList2.getX(0));

        // remove() tests
        manualList2.remove(0);
        manualList2.addNode(32, 1024);
        Assertions.assertEquals(16, manualList2.getX(0));
        Assertions.assertEquals(2, manualList2.getCount());
        Assertions.assertEquals(1024, manualList2.getNode(1).y);
        //
        manualList.remove(2);
        Assertions.assertEquals(10.5, manualList.getX(2));
        Assertions.assertEquals(-1, manualList.indexOfX(6));
        Assertions.assertEquals(-1, manualList.indexOfY(36));

        // insert() tests
        manualList2.insert(10, 100);
        Assertions.assertEquals(3, manualList2.getCount());
        Assertions.assertEquals(0, manualList2.indexOfX(10));
        manualList2.insert(22, 484);
        Assertions.assertEquals(484, manualList2.getY(2));
        manualList2.insert(64, 4096);
        Assertions.assertEquals(64, manualList2.getNode(4).x);
        //
        Assertions.assertEquals(0, manualList2.indexOfX(10));
        Assertions.assertEquals(1, manualList2.indexOfX(16));
        Assertions.assertEquals(2, manualList2.indexOfX(22));
        Assertions.assertEquals(3, manualList2.indexOfX(32));
        Assertions.assertEquals(4, manualList2.indexOfX(64));
    }
}
