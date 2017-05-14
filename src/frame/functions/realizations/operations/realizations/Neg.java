package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;
import static frame.functions.realizations.operations.Simplifier.*;

/**
 * Created by ����� on 05.10.2015.
 */
public class Neg implements Operation {
    private Operation o;

    public Neg(Operation o) {
        this.o = o.copy();
    }

    public Operation getO() {
        return o;
    }

    @Override
    public double eval() {
        return -o.eval();
    }

    @Override
    public Operation dif() {
        return new Neg(o.dif());
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Neg(o.partialDerivative(v));
    }

    @Override
    public String toString() {
        String s = !o.isBinary() ? o.toString() : "(" + o.toString() + ")";
        return "-" + s;
    }

    @Override
    public Operation copy() {
        return new Neg(o.copy());
    }

    @Override
    public Operation simplify() {
        o = simplifyWhileNotEquals(o);

        if (o instanceof Const) {
            return new Const(-o.eval());
        }
        if (o instanceof Neg) {
            return ((Neg) o).getO();
        }
        return this;
    }

    @Override
    public Operation toStand() {
        Operation operation_old = o;
        while (!(o = o.toStand()).equals(operation_old)) {
            operation_old = o;
        }
        if (o instanceof Neg) {
            return ((Neg) o).o;
        }
        if (o instanceof Const) {
            return new Const(-o.eval());
        }
        return this;
    }

    @Override
    public boolean equals(Operation origin) {
        return (origin instanceof Neg && o.equals(((Neg) origin).getO()));
    }

    @Override
    public boolean isBinary() {
        return false;
    }
}
