package ru.ssau.tk.DoubleA.javalabs.concurrent;

import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;

public class MultiplyingTask implements Runnable {
    private final TabulatedFunction function;

    public MultiplyingTask(TabulatedFunction function) {
        this.function = function;
    }

    @Override
    public void run() {
        for (int i = 0; i < function.getCount(); ++i) {
            function.setY(i, function.getY(i) * 2);
        }

        System.out.println("Current thread " + Thread.currentThread().getName() + " completed the task");
    }
}
