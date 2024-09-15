package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompositeFunctionTest extends AbstractTest {

    CompositeFunction mainFunction;

    @Test
    void testSimpleAndSimple() {
        mainFunction = new CompositeFunction(new IdentityFunction(), new SqrFunction());
        Assertions.assertEquals(121, mainFunction.apply(11));

        mainFunction = new CompositeFunction(new NRootCalculateFunction(7), new SqrFunction());
        Assertions.assertEquals(25, mainFunction.apply(78125), EPSILON);

        mainFunction = new CompositeFunction(new DerivativeFunction(new SqrFunction()), new IdentityFunction());
        Assertions.assertEquals(-630, mainFunction.apply(-315), EPSILON);
    }

    CompositeFunction compFunction1;

    @Test
    void testCompositeAndSimple() {
        compFunction1 = new CompositeFunction(new SqrFunction(), new SqrFunction());
        mainFunction = new CompositeFunction(compFunction1, new NRootCalculateFunction(4));
        Assertions.assertEquals(4384498, mainFunction.apply(4384498), EPSILON);

        compFunction1 = new CompositeFunction(new SqrFunction(), new SqrFunction());
        mainFunction = new CompositeFunction(new DerivativeFunction(compFunction1), new IdentityFunction());
        Assertions.assertEquals(256, new DerivativeFunction(compFunction1).apply(4), EPSILON);

        compFunction1 = new CompositeFunction(new SqrFunction(), new UnitFunction());
        mainFunction = new CompositeFunction(compFunction1, new ConstantFunction(123));
        Assertions.assertEquals(0, new DerivativeFunction(compFunction1).apply(999));
    }

    CompositeFunction compFunction2;

    @Test
    void testCompositeAndComposite() {
        compFunction1 = new CompositeFunction(new SqrFunction(), new IdentityFunction());
        compFunction2 = new CompositeFunction(new NRootCalculateFunction(30), new SqrFunction());
        mainFunction = new CompositeFunction(compFunction1, compFunction2);
        Assertions.assertEquals(4, mainFunction.apply(32768), EPSILON);

        compFunction1 = new CompositeFunction(new SqrFunction(), new IdentityFunction());
        compFunction2 = new CompositeFunction(new SqrFunction(), new ZeroFunction());
        mainFunction = new CompositeFunction(compFunction1, compFunction2);
        Assertions.assertEquals(0, mainFunction.apply(98884));

        compFunction1 = new CompositeFunction(new SqrFunction(), new IdentityFunction());
        compFunction2 = new CompositeFunction(new ConstantFunction(6202.010302), new SqrFunction());
        mainFunction = new CompositeFunction(compFunction1, compFunction2);
        Assertions.assertEquals(38464931.786114131204, mainFunction.apply(1), EPSILON);
    }

    ArrayTabulatedFunction arrayTabFunction1;

    @Test
    void testATabulatedAndSimple() {
        arrayTabFunction1 = new ArrayTabulatedFunction(new double[] {0, 2, 8, 12, 18}, new double[] {0, 4, 64, 144, 324});
        mainFunction = new CompositeFunction(new DerivativeFunction(arrayTabFunction1), new IdentityFunction());
        Assertions.assertEquals(30, mainFunction.apply(15), EPSILON);

        arrayTabFunction1 = new ArrayTabulatedFunction(new SqrFunction(), 0, 5, 50);
        mainFunction = new CompositeFunction(arrayTabFunction1, new SqrFunction());
        Assertions.assertEquals(17.523567165456, mainFunction.apply(2.046), EPSILON);

        arrayTabFunction1 = new ArrayTabulatedFunction(new SqrFunction(), 33, 33, 1);
        mainFunction = new CompositeFunction(arrayTabFunction1, new SqrFunction());
        Assertions.assertEquals(144, mainFunction.apply(12));

    }

    LinkedListTabulatedFunction listTabFunction1;

    @Test
    void testLTabulatedAndSimple() {
        listTabFunction1 = new LinkedListTabulatedFunction(new double[] {-20, -16, -9, -6, -2}, new double[] {400, 256, 81, 36, 4});
        mainFunction = new CompositeFunction(new DerivativeFunction(listTabFunction1), new IdentityFunction());
        Assertions.assertEquals(-36, mainFunction.apply(-18), EPSILON);

        listTabFunction1 = new LinkedListTabulatedFunction(new SqrFunction(), 0, 5, 50);
        mainFunction = new CompositeFunction(listTabFunction1, new SqrFunction());
        Assertions.assertEquals(252.688188876561, mainFunction.apply(3.987), EPSILON);

        listTabFunction1 = new LinkedListTabulatedFunction(new SqrFunction(), 33, 33, 1);
        mainFunction = new CompositeFunction(listTabFunction1, new SqrFunction());
        Assertions.assertEquals(9, mainFunction.apply(3));
    }

    @Test
    void testATabulatedAndComposite() {
        compFunction1 = new CompositeFunction(new UnitFunction(), new ZeroFunction());
        arrayTabFunction1 = new ArrayTabulatedFunction(new double[] {0, 2, 8, 12, 18}, new double[] {0, 4, 64, 144, 324});
        mainFunction = new CompositeFunction(compFunction1, arrayTabFunction1);
        Assertions.assertEquals(0, mainFunction.apply(15));

        compFunction1 = new CompositeFunction(new NRootCalculateFunction(2), new SqrFunction());
        arrayTabFunction1 = new ArrayTabulatedFunction(new SqrFunction(), 0, 5, 50);
        mainFunction = new CompositeFunction(arrayTabFunction1, compFunction1);
        Assertions.assertEquals(4.186116, mainFunction.apply(2.046), EPSILON);

        compFunction1 = new CompositeFunction(new SqrFunction(), new SqrFunction());
        arrayTabFunction1 = new ArrayTabulatedFunction(new SqrFunction(), 33, 33, 1);
        mainFunction = new CompositeFunction(compFunction1, arrayTabFunction1);
        Assertions.assertEquals(6561, mainFunction.apply(9));
    }

    @Test
    void testLTabulatedAndComposite() {
        compFunction1 = new CompositeFunction(new UnitFunction(), new ZeroFunction());
        listTabFunction1 = new LinkedListTabulatedFunction(new double[] {-20, -16, -9, -6, -2}, new double[] {400, 256, 81, 36, 4});
        mainFunction = new CompositeFunction(listTabFunction1, compFunction1);
        Assertions.assertEquals(0, mainFunction.apply(-12.5));

        compFunction1 = new CompositeFunction(new NRootCalculateFunction(2), new SqrFunction());
        listTabFunction1 = new LinkedListTabulatedFunction(new SqrFunction(), 0, 5, 50);
        mainFunction = new CompositeFunction(compFunction1, listTabFunction1);
        Assertions.assertEquals(20.657025, mainFunction.apply(4.545), EPSILON);

        compFunction1 = new CompositeFunction(new SqrFunction(), new SqrFunction());
        listTabFunction1 = new LinkedListTabulatedFunction(new SqrFunction(), 33, 33, 1);
        mainFunction = new CompositeFunction(listTabFunction1, compFunction1);
        Assertions.assertEquals(1851.89072896, mainFunction.apply(-6.56), EPSILON);
    }

    ArrayTabulatedFunction arrayTabFunction2;

    @Test
    void testATabulatedAndATabulated() {
        arrayTabFunction1 = new ArrayTabulatedFunction(new double[] {0, 2.43, 8.05, 12.95, 18.33}, new double[] {-32, 89, -69, 121, -98});
        arrayTabFunction2 = new ArrayTabulatedFunction(new double[] {-9, -2.2, -0.4, 1, 3.69}, new double[] {-81, -1331.0/1125.0, -8.0/1125.0, 1.0/9.0, (41.0*369.0*369.0)/1000000.0});
        mainFunction = new CompositeFunction(arrayTabFunction2, arrayTabFunction1);
        Assertions.assertEquals(-515.535576, mainFunction.apply(15), EPSILON);

        arrayTabFunction1 = new ArrayTabulatedFunction(new SqrFunction(), 0, 15, 50);
        arrayTabFunction2 = new ArrayTabulatedFunction(new double[] {0, 2, 8, 12, 18}, new double[] {0, 4, 64, 144, 324});
        mainFunction = new CompositeFunction(arrayTabFunction1, arrayTabFunction2);
        Assertions.assertEquals(26.063848, mainFunction.apply(2.046), EPSILON);

        arrayTabFunction1 = new ArrayTabulatedFunction(new SqrFunction(), -18, 3, 100);
        arrayTabFunction2 = new ArrayTabulatedFunction(arrayTabFunction1, 0, 5, 50);
        mainFunction = new CompositeFunction(arrayTabFunction2, arrayTabFunction1);
        Assertions.assertEquals(244.724517, mainFunction.apply(9), EPSILON);
    }

    LinkedListTabulatedFunction listTabFunction2;

    @Test
    void testLTabulatedAndLTabulated() {
        listTabFunction1 = new LinkedListTabulatedFunction(new double[] {0, 2.43, 8.05, 12.95, 18.33}, new double[] {-32, 89, -69, 121, -98});
        listTabFunction2 = new LinkedListTabulatedFunction(new double[] {-9, -2.2, -0.4, 1, 3.69}, new double[] {-81, -1331.0/1125.0, -8.0/1125.0, 1.0/9.0, (41.0*369.0*369.0)/1000000.0});
        mainFunction = new CompositeFunction(listTabFunction2, listTabFunction1);
        Assertions.assertEquals(-2984.056973, mainFunction.apply(-7.15), EPSILON);

        listTabFunction1 = new LinkedListTabulatedFunction(new SqrFunction(), 0, 15, 50);
        listTabFunction2 = new LinkedListTabulatedFunction(new double[] {0, 2, 8, 12, 18}, new double[] {0, 4, 64, 144, 324});
        mainFunction = new CompositeFunction(listTabFunction1, listTabFunction2);
        Assertions.assertEquals(0.078979, mainFunction.apply(0.129), EPSILON);

        listTabFunction1 = new LinkedListTabulatedFunction(new SqrFunction(), -18, 3, 100);
        listTabFunction2 = new LinkedListTabulatedFunction(listTabFunction1, 0, 5, 50);
        mainFunction = new CompositeFunction(listTabFunction2, listTabFunction1);
        Assertions.assertEquals(3326.682277, mainFunction.apply(101), EPSILON);
    }

    @Test
    void testATabulatedAndLTabulated() {
        arrayTabFunction1 = new ArrayTabulatedFunction(new double[] {0, 2.43, 8.05, 12.95, 18.33}, new double[] {-32, 89, -69, 121, -98});
        listTabFunction1 = new LinkedListTabulatedFunction(new double[] {0, 2, 8, 12, 18}, new double[] {0, 4, 64, 144, 324});
        mainFunction = new CompositeFunction(listTabFunction1, arrayTabFunction1);
        Assertions.assertEquals(-1411.592936, mainFunction.apply(6.66), EPSILON);

        arrayTabFunction1 = new ArrayTabulatedFunction(new SqrFunction(), -18, 3, 100);
        listTabFunction1 = new LinkedListTabulatedFunction(new double[] {-9, -2.2, -0.4, 1, 3.69}, new double[] {-81, -1331.0/1125.0, -8.0/1125.0, 1.0/9.0, (41.0*369.0*369.0)/1000000.0});
        mainFunction = new CompositeFunction(arrayTabFunction1, listTabFunction1);
        Assertions.assertEquals(4194.833734, mainFunction.apply(-66.6), EPSILON);

        arrayTabFunction1 = new ArrayTabulatedFunction(new SqrFunction(), 0, 15, 50);
        listTabFunction1 = new LinkedListTabulatedFunction(arrayTabFunction1, 0, 5, 50);
        mainFunction = new CompositeFunction(listTabFunction1, arrayTabFunction1);
        Assertions.assertEquals(198801.909493, mainFunction.apply(666), EPSILON);
    }
}