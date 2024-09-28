package ru.ssau.tk.DoubleA.javalabs.concurrent;

import ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.UnitFunction;

import java.util.ArrayList;
import java.util.List;

public class MultiplyingTaskExecutor {
    private final static int THREAD_QUANTITY = 10;
    private final static int TIME_FOR_WAITING = 2000;


    public static void main(String[] args) {
        TabulatedFunction function = new LinkedListTabulatedFunction(new UnitFunction(), 1, 1000, 1000);

        List<Thread> list = new ArrayList<>();

        for (int i = 0; i < THREAD_QUANTITY; ++i) {
            list.add(new Thread(new MultiplyingTask(function)));
        }

        for (int i = 0; i < THREAD_QUANTITY; ++i) {
            list.get(i).start();
        }


        while (!list.isEmpty()) {

            for (int i = 0; i < list.size(); ++i) {
                if (!list.get(i).isAlive()) {
                    list.remove(i--);
                }
            }

        }

        System.out.println(function);
    }
}
