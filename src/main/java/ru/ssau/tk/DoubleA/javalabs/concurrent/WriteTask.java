package ru.ssau.tk.DoubleA.javalabs.concurrent;

import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;

public class WriteTask implements Runnable {
    private final TabulatedFunction tabulatedFunction;
    private final double value;

    public WriteTask(TabulatedFunction tabulatedFunction, double value) {
        this.tabulatedFunction = tabulatedFunction;
        this.value = value;
    }

    @Override
    public void run() {
        for (int i = 0; i < tabulatedFunction.getCount(); i++) {
            synchronized (tabulatedFunction) {
                tabulatedFunction.setY(i, value);
                System.out.printf("Writing for index %d complete\n", i);
            }
        }
    }
}
