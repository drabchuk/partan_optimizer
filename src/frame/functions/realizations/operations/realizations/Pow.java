package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;
import static frame.functions.realizations.operations.Simplifier.*;

/**
 * Created by ����� on 05.10.2015.
 */
public class Pow implements Operation {
    private Operation o1;
    private Operation o2;

    public Pow(Operation o1, Operation o2) {
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
        return Math.pow(o1.eval(), o2.eval());
    }

    @Override
    public Operation dif() {
        return new Mult(
                        this
                        ,
                        new Sum(
                                new Mult(o2.dif(), new Ln(o1))
                                , new Mult(new Div(o2,o1), o1.dif())
                        )
        );
    }

    @Override
    public Operation partialDerivative(Var v) {
        if (o2 instanceof Const) {
            return new Mult(new Mult(o2, new Pow(o1, new Const(o2.eval() - 1.0)))
                    , o1.partialDerivative(v)).simplify();
        } else {
            return new Mult(
                    this.copy()
                    ,
                    new Sum(
                            new Mult(o2.partialDerivative(v), new Ln(o1))
                            , new Mult(new Div(o2, o1), o1.partialDerivative(v))
                    )
            ).simplify();
        }
    }

    @Override
    public String toString() {
        String s1 = !o1.isBinary() ? o1.toString() : "(" + o1.toString() + ")";
        String s2 = !o2.isBinary() ? o2.toString() : "(" + o2.toString() + ")";
        return s1 + "^" + s2;
    }

    @Override
    public Operation copy() {
        return new Pow(o1.copy(), o2.copy());
    }

    @Override
    public Operation simplify() {
        o1 = simplifyWhileNotEquals(o1);
        o2 = simplifyWhileNotEquals(o2);

        if(o1 instanceof Const && o2 instanceof Const) {
            return new Const(Math.pow(o1.eval(), o2.eval()));
        }
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
        if(o1 instanceof Const && o2 instanceof Const) {
            return new Const(Math.pow(o1.eval(), o2.eval()));
        }
        if(o2 instanceof Const && o2.eval() == 1) {
            return o1;
        }
        if(o2 instanceof Const && o2.eval() == 0) {
            return new Const(1);
        }
        if(o1 instanceof Pow) {
            //(a^b)^c = a^(b*c);
            return new Pow(((Pow) o1).getO1(), new Mult(((Pow) o1).getO2(), o2));
        }
        return this;
    }

    @Override
    public boolean equals(Operation origin) {
        return (origin instanceof Pow
                && o1.equals(((Pow) origin).getO1())
                && o2.equals(((Pow) origin).getO2()));
    }

    @Override
    public boolean isBinary() {
        return false;
    }
}
