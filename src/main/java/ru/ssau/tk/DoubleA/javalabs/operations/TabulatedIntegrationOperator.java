package ru.ssau.tk.DoubleA.javalabs.operations;

import ru.ssau.tk.DoubleA.javalabs.concurrent.IntegrationTask;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TabulatedIntegrationOperator implements MathFunction {
    private final ExecutorService executorService;
    private final int numberOfThreads;

    public TabulatedIntegrationOperator(int numberOfThreads) {
        this.executorService = Executors.newFixedThreadPool(numberOfThreads);
        this.numberOfThreads = numberOfThreads;
    }

    public double integrate(TabulatedFunction tabulatedFunction, int overallNumberOfSections) throws InterruptedException, ExecutionException {
        double integrationSegmentLength = (tabulatedFunction.rightBound() - tabulatedFunction.leftBound()) / numberOfThreads;
        List<Future<Double>> futures = new ArrayList<>();

        for (int threadIndex = 0; threadIndex < numberOfThreads; threadIndex++) {
            double leftBound = tabulatedFunction.leftBound() + threadIndex * integrationSegmentLength;
            double rightBound = leftBound + integrationSegmentLength;

            int sectionsPerThread = Math.max(overallNumberOfSections / numberOfThreads, 1);
            IntegrationTask task = new IntegrationTask(tabulatedFunction, leftBound, rightBound, sectionsPerThread);

            futures.add(executorService.submit(task));
        }

        double summedIntegrationResult = 0;
        for (Future<Double> future : futures) {
            summedIntegrationResult += future.get();
        }

        executorService.shutdown();
        return summedIntegrationResult;
    }

    @Override
    public double apply(double x) {
        throw new UnsupportedOperationException();
    }
}
