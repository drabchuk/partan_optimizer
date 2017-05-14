package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;

/**
 * Created by ����� on 05.10.2015.
 */
public class Exp implements Operation {
    private Operation operation;

    public Exp(Operation operation) {
        this.operation = operation.copy();
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public double eval() {
        return Math.exp(operation.eval());
    }

    @Override
    public Operation dif() {
        return new Mult(new Exp(operation), operation.dif());
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Mult(new Exp(operation), operation.partialDerivative(v));
    }

    @Override
    public String toString() {
        return "exp(" + operation.toString()+ ")";
    }

    @Override
    public Operation copy() {
        return new Exp(operation.copy());
    }

    @Override
    public Operation simplify() {
        operation = operation.simplify();
        if (operation instanceof Const) {
            return new Const(Math.exp(operation.eval()));
        }
        return this;
    }

    @Override
    public Operation toStand() {
        Operation operation_old = operation;
        while (!(operation = operation.toStand()).equals(operation_old)) {
            operation_old = operation;
        }
        if (operation instanceof Const) {
            return new Const(Math.exp(operation.eval()));
        }
        return this;
    }

    @Override
    public boolean equals(Operation origin) {
        return (origin instanceof Exp && operation.equals(((Exp) origin).getOperation()));
    }

    @Override
    public boolean isBinary() {
        return false;
    }
}
