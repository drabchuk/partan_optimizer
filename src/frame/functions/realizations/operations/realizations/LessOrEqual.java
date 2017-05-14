package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;

/**
 * Created by ����� on 22.10.2015.
 */
public class LessOrEqual implements Operation {
    // o1 <= o2
    private Operation o1;
    private Operation o2;

    public LessOrEqual() {
    }

    public LessOrEqual(Operation o1, Operation o2) {
        this.o1 = o1.copy();
        this.o2 = o2.copy();
    }

    public Operation getO1() {
        return o1;
    }

    public Operation getO2() {
        return o2;
    }

    @Override
    public double eval() {
        return o1.eval() <= o2.eval() ? 1:-1;
    }

    @Override
    public Operation dif() {
        return this;
    }

    @Override
    public Operation partialDerivative(Var v) {
        return this;
    }

    @Override
    public Operation copy() {
        return new LessOrEqual(o1.copy(), o2.copy());
    }

    @Override
    public Operation simplify() {
        return this;
    }

    @Override
    public Operation toStand() {
        Operation o1_old = o1;
        Operation o2_old = o2;
        while (!((o1 = o1.toStand()).equals(o1_old) && (o2 = o2.toStand()).equals(o2_old))) {
            o1_old = o1;
            o2_old = o2;
        }
        double constant = 0;
        Operation operation = null;
        if (o1 instanceof Sum && ((Sum) o1).getO1() instanceof Const) {
            constant = ((Sum) o1).getO1().eval();
            operation = ((Sum) o1).getO2();
        } else if (o1 instanceof Const) {
            constant = o1.eval();
        } else {
            operation = o1;
        }
        if (o2 instanceof Sum && ((Sum) o2).getO1() instanceof Const) {
            constant += ((Sum) o2).getO1().eval();
            operation = operation == null? ((Sum) o2).getO2():new Sum(operation, ((Sum) o2).getO2());
        } else if (o2 instanceof Const) {
            constant += o2.eval();
        } else {
            operation = operation == null? o2:new Sum(operation, o2);
        }
        return new GraterOrEqual(new Neg(operation), new Const(-constant));
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public boolean equals(Operation origin) {
        return false;
    }

    @Override
    public String toString() {
        return o1.toString() + " <= " + o2.toString();
    }
}
