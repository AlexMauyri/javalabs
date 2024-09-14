public class ConstantFunction implements MathFunction {
    private final double CONSTANT;

    public ConstantFunction(double CONSTANT) {
        this.CONSTANT = CONSTANT;
    }

    @Override
    public double apply(double x) {
        return CONSTANT;
    }

    public double getCONSTANT() {
        return CONSTANT;
    }
}
