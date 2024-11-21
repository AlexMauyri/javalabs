package ru.ssau.tk.DoubleA.javalabs.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;

public class NRootCalculateFunction implements MathFunction {
    @Serial
    private static final long serialVersionUID = -3283696080044004085L;
    private int rootDegree;

    @JsonCreator
    public NRootCalculateFunction(@JsonProperty("rootDegree") int rootDegree) {
        this.rootDegree = rootDegree;
    }

    public int getRootDegree() {
        return rootDegree;
    }

    public void setRootDegree(int rootDegree) {
        this.rootDegree = rootDegree;
    }

    @Override
    public double apply(double x) {
        // Calculate n-th degree root by the logarithmic method
        return Math.pow(Math.E, (1 / ((double) rootDegree)) * Math.log(x));
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(rootDegree);
    }
}
