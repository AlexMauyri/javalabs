package ru.ssau.tk.DoubleA.javalabs.concurrent;

import ru.ssau.tk.DoubleA.javalabs.functions.*;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(new ConstantFunction(-1), 1, 15, 1000);

        Thread read = new Thread(new ReadTask(tabulatedFunction));
        Thread write = new Thread(new WriteTask(tabulatedFunction, 127.0));

        read.start();
        write.start();
    }
}