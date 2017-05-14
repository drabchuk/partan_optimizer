package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;

/**
 * Created by ����� on 06.10.2015.
 */
public class Sin implements Operation {
    private Operation operation;

    public Sin() {
    }

    public Sin(Operation o) {
        this.operation = o.copy();
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public double eval() {
        return Math.sin(operation.eval());
    }

    @Override
    public Operation dif() {
        return new Mult(new Cos(operation), operation.dif());
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Mult(new Cos(operation), operation.partialDerivative(v));
    }

    @Override
    public Operation copy() {
        return new Sin(operation.copy());
    }

    @Override
    public String toString() {
        return "sin(" + operation.toString() + ")";
    }

    @Override
    public Operation simplify() {
        operation.simplify();
        if (operation instanceof Const) {
            return new Const(Math.sin(operation.eval()));
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
            return new Const(Math.sin(operation.eval()));
        }
        if (operation instanceof Sum) {
            //sin(_+_)
            Operation a = ((Sum) operation).getO1();
            Operation b = ((Sum) operation).getO2();
            if (a instanceof Const) {
                if (b instanceof Const)
                    //a&b - constants
                    return new Sin(new Const(a.eval() + b.eval()));
                else {
                    //a - const, b - !const
                    double phase = a.eval();
                    if (b instanceof Neg) {
                        //sin(const - b) = sin(b + (pi - const))
                        phase = 2*Math.PI - phase;
                        phase /= 2*Math.PI;
                        phase -= Math.floor(phase);
                        phase *= 2*Math.PI;
                        return new Sin(new Sum(new Const(phase), ((Neg) b).getO()));
                    } else {
                        // a = a - 2*pi*n
                        phase /= 2*Math.PI;
                        phase -= Math.floor(phase);
                        phase *= 2*Math.PI;
                        return new Sin(new Sum(new Const(phase), b));
                    }
                    //return this;
                }
            } else if (b instanceof Const) {
                //a - !const  b - const
                double phase = b.eval();
                if (a instanceof Neg) {
                    //sin(const - b) = sin(b + (pi - const))
                    phase = 2*Math.PI - phase;
                    phase /= 2*Math.PI;
                    phase -= Math.floor(phase);
                    phase *= 2*Math.PI;
                    return new Sin(new Sum(((Neg) a).getO(), new Const(phase)));
                } else {
                    // a = a - 2*pi*n
                    phase /= 2*Math.PI;
                    phase -= Math.floor(phase);
                    phase *= 2*Math.PI;
                    return new Sin(new Sum(a, new Const(phase)));
                }
            } else {
                //a - !const, b - !const
                //sin(a + b) = sin(a)sin(b + pi/2) + sin(a + pi/2)sin(b)
                return new Sum(new Mult(new Sin(a), new Sin(new Sum(b, new Const(Math.PI / 2)))), new Mult(new Sin(new Sum(a, new Const(Math.PI / 2))), new Sin(b)));
            }
        }// !sin(_+_)

        else if (operation instanceof Neg)
            //sin(-a) = sin(a + pi)
            return new Sin(new Sum(((Neg) operation).getO(), new Const(Math.PI)));
        return this;
    }

    @Override
    public boolean equals(Operation origin) {
        return (origin instanceof Sin && operation.equals(((Sin) origin).getOperation()));
    }

    @Override
    public boolean isBinary() {
        return false;
    }
}
