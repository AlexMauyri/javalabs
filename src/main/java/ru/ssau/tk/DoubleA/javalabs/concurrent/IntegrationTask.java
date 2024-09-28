package ru.ssau.tk.DoubleA.javalabs.concurrent;

import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;

import java.util.concurrent.Callable;

public class IntegrationTask implements Callable<Double> {
    private final TabulatedFunction functionToIntegrate;
    private final double lowerBound, upperBound;
    private final int numberOfSections;

    public IntegrationTask(TabulatedFunction functionToIntegrate, double lowerBound, double upperBound, int numberOfSections) {
        this.functionToIntegrate = functionToIntegrate;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.numberOfSections = numberOfSections + numberOfSections%2;
    }

    @Override
    public Double call() {
        double segmentLength = (upperBound - lowerBound) / numberOfSections;
        double summedPoints = functionToIntegrate.apply(lowerBound) + functionToIntegrate.apply(upperBound);

        for (int i = 1; i < numberOfSections; i++) {
            double x = lowerBound + i * segmentLength;
            summedPoints += (i % 2 == 0) ? 2 * functionToIntegrate.apply(x) : 4 * functionToIntegrate.apply(x);
        }

        return summedPoints * segmentLength / 3;
    }
}
