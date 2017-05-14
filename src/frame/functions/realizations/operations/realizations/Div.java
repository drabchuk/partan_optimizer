package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;
import static frame.functions.realizations.operations.Simplifier.*;

/**
 * Created by ����� on 05.10.2015.
 */
public class Div implements Operation {
    Operation o1;
    Operation o2;

    public Div(Operation o1, Operation o2) {
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
        try {
            return o1.eval() / o2.eval();
        } catch (ArithmeticException e) {
            return Double.MAX_VALUE;
        }
    }

    @Override
    public Operation dif() {
        return new Div(
                new Sum(new Mult(o1.dif(), o2)
                        , new Neg(new Mult(o2.dif(), o1))
                )
                , new Pow(o2, new Const(2))
        );
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Div(
                new Sum(new Mult(o1.partialDerivative(v), o2)
                        , new Neg(new Mult(o2.partialDerivative(v), o1))
                )
                , new Pow(o2, new Const(2))
        );
    }

    @Override
    public String toString() {
        String s1 = !o1.isBinary() ? o1.toString() : "(" + o1.toString() + ")";
        String s2 = !o1.isBinary() ? o2.toString() : "(" + o2.toString() + ")";
        return s1 + " / " + s2;
    }

    @Override
    public Operation copy() {
        return new Div(o1.copy(), o2.copy());
    }

    @Override
    public Operation simplify() {
        o1 = simplifyWhileNotEquals(o1);
        o2 = simplifyWhileNotEquals(o2);
        if (o1 instanceof Const && o1.eval() == 0 || o2 instanceof Const && o2.eval() == 0) {
            System.out.println("Division by zero");
            Exception e = new NullPointerException();
            return new Const(0);
        }
        if (o1 instanceof Const && o2 instanceof Const) {
            return new Const(o1.eval() / o2.eval());
        }
        if (o1 instanceof Var && o1 instanceof Var && o1 == o2) {
            return new Const(1);
        }
        if (o1 instanceof Pow && o2 instanceof Var && o2 == ((Pow) o1).getO1()) {
            return new Pow(o1, new Sum(((Pow) o1).getO2(), new Const(1)));
        }
        if (o2 instanceof Pow && o1 instanceof Var && o1 == ((Pow) o2).getO1()) {
            return new Pow(o2, new Sum(((Pow) o2).getO2(), new Const(1)));
        }
        if (o2 instanceof Pow && o1 instanceof Pow && ((Pow) o1).getO1() == ((Pow) o2).getO1()) {
            return new Pow(o2, new Sum(((Pow) o2).getO2(), ((Pow) o1).getO2()));
        }
        return this;
    }

    @Override
    public Operation toStand() {
        o1 = o1.toStand();
        o2 = o2.toStand();
        if (o1 instanceof Const && o1.eval() == 0 || o2 instanceof Const && o2.eval() == 0) {
            System.out.println("Division by zero");
            Exception e = new NullPointerException();
            return new Const(0);
        }
        if (o1 instanceof Const && o2 instanceof Const) {
            return new Const(o1.eval() / o2.eval());
        }
        if (o1 instanceof Var && o1 instanceof Var && o1 == o2) {
            return new Const(1);
        }
        return new Mult(o1, new Pow(o2, new Const(-1)));
    }

    @Override
    public boolean equals(Operation origin) {
        return (origin instanceof Div
                && o1.equals(
                    ((Div) origin).getO1()
                )
                && o2.equals(
                    ((Div) origin).getO2()
                )
        );
    }

    @Override
    public boolean isBinary() {
        return true;
    }
}
