package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;
import frame.functions.realizations.variables.Variable;

/**
 * Created by Денис on 05.10.2015.
 * @author Denis Drabchuk
 */
public class Var implements Operation, Variable {
    private double value;
    private String name;

    public Var(String name) {
        this.name = name;
    }

    public Var(double value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public double eval() {
        return value;
    }

    @Override
    public Operation dif() {
        return new Dif(this);
    }

    @Override
    public Operation partialDerivative(Var v) {
        if (this == v) {
            return new Const(1.0);
        } else {
            return new Const(0.0);
        }
    }

    @Override
    public String toString() {
        return name;
    }

//    @Override
//    public Operation copy() {
//        return new Var(value, name);
//    }


    @Override
    public Operation copy() {
        return this;
    }

    @Override
    public Operation simplify() {
        return this;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public Operation toStand() {
        return this;
    }

    @Override
    public boolean equals(Operation origin) {
        return (this == origin);
    }

    @Override
    public boolean isBinary() {
        return false;
    }
}
