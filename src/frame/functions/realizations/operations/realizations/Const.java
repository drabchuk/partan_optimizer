package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;

/**
 * Created by ����� on 05.10.2015.
 */
public class Const implements Operation {
    private double val;

    public Const() {
    }

    public double getVal() {
        return val;
    }

    public Const(double val) {
        this.val = val;
    }

    @Override
    public double eval() {
        return val;
    }

    @Override
    public Operation dif() {
        return new Const(0);
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public Operation copy() {
        return new Const(val);
    }

    @Override
    public Operation simplify() {
        return this;
    }

    @Override
    public Operation toStand() {
        return this;
    }

    @Override
    public boolean equals(Operation origin) {
        return (origin instanceof Const && this.val == ((Const) origin).getVal());
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Const(0);
    }
}
