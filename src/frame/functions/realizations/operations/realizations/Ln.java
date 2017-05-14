package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;

/**
 * Created by ����� on 05.10.2015.
 */
public class Ln implements Operation {
    private Operation operation;

    public Ln() {
    }

    public Ln(Operation operation) {
        this.operation = operation.copy();
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public double eval() {
        return Math.log(operation.eval());
    }

    @Override
    public Operation dif() {
        return new Div(operation.dif(), operation);
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Div(operation.partialDerivative(v), operation);
    }

    @Override
    public Operation copy() {
        return new Ln(operation.copy());
    }

    @Override
    public String toString() {
        return "ln(" + operation.toString() + ")";
    }

    @Override
    public Operation simplify() {
        operation.simplify();
        if(operation instanceof Const) {
            return new Const(Math.log(operation.eval()));
        }
        return this;
    }

    @Override
    public Operation toStand() {
        Operation operation_old = operation;
        while (!(operation = operation.toStand()).equals(operation_old)) {
            operation_old = operation;
        }
        if(operation instanceof Const) {
            return new Const(Math.log(operation.eval()));
        }
        return this;
    }

    @Override
    public boolean equals(Operation origin) {
        return (origin instanceof Ln && operation.equals(((Ln) origin).getOperation()));
    }

    @Override
    public boolean isBinary() {
        return false;
    }
}
