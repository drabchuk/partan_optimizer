package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;

/**
 * Created by ����� on 06.10.2015.
 */
public class Cos implements Operation {
    private Operation operation;

    public Cos(Operation o) {
        this.operation = o.copy();
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public double eval() {
        return Math.cos(operation.eval());
    }

    @Override
    public Operation dif() {
        return new Neg(new Mult(new Sin(operation), operation.dif()));
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Neg(new Mult(new Sin(operation), operation.partialDerivative(v)));
    }

    @Override
    public Operation copy() {
        return new Cos(operation.copy());
    }

    @Override
    public String toString() {
        return "cos(" + operation.toString() + ")";
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public Operation simplify() {
        operation.simplify();
        if (operation instanceof Const) {
            return new Const(operation.eval());
        }
        return this;
    }

    @Override
    public Operation toStand() {
        Operation operation_old = operation;
        while (!(operation = operation.toStand()).equals(operation_old)) {
            operation_old = operation;
        }
        return new Sin(new Sum(operation, new Const(Math.PI/2)));
    }

    @Override
    public boolean equals(Operation origin) {
        return (origin instanceof Cos && operation.equals(((Cos) origin).getOperation()));
    }
}
