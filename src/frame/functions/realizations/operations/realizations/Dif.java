package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;

/**
 * Created by ����� on 05.10.2015.
 */
public class Dif implements Operation {
    Operation operation;

    public Dif(Operation operation) {
        this.operation = operation.copy();
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Const(0);
    }

    @Override
    public double eval() {
        return 0;
    }

    @Override
    public Operation dif() {
        //return operation.dif();
        return new Dif(this);
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return operation instanceof Var ? "d" + operation.toString() : "d(" + operation.toString() + ")";
    }

    @Override
    public Operation copy() {
        return new Dif(operation.copy());
    }

    @Override
    public Operation simplify() {
        operation.simplify();
        return this;
    }

    @Override
    public Operation toStand() {
        Operation operation_old = operation;
        while (!(operation = operation.toStand()).equals(operation_old)) {
            operation_old = operation;
        }
        if (operation instanceof Dif && ((Dif) operation).getOperation() instanceof Var) {
            return new Const(0);
        } else {
            return this;
        }
    }

    @Override
    public boolean equals(Operation origin) {
        return (origin instanceof Dif && operation.equals(((Dif) origin).getOperation()));
    }

    @Override
    public boolean isBinary() {
        return false;
    }
}
