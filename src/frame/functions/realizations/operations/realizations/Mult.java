package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;
import static frame.functions.realizations.operations.Simplifier.*;
import java.util.ArrayList;

/**
 * Created by ����� on 05.10.2015.
 */
public class Mult implements Operation {
    Operation o1;
    Operation o2;

    public Mult(Operation o1, Operation o2) {
        this.o1 = o1.copy();
        this.o2 = o2.copy();
    }

    public Operation getO1() {
        return o1;
    }

    public Operation getO2() {
        return o2;
    }

    public Operation findOperation(Operation what) {
        if (o1 instanceof Mult) {
            Operation found = findOperation(what);
            if (found != null)
                return found;
        }
        if (o2 instanceof Mult) {
            Operation found = findOperation(what);
            if (found != null)
                return found;
        }
        return null;
    }

    public Operation findMultiplier(Var v) {
        if (o1 instanceof Mult) {
            Operation found = findMultiplier(v);
            if (found != null)
                return found;
        }
        if (o1 instanceof Pow && ((Pow) o1).getO1() instanceof Var && ((Pow) o1).getO1() == v) {
            return o1;
        }
        if (o2 instanceof Mult) {
            Operation found = findMultiplier(v);
            if (found != null)
                return found;
        }
        if (o2 instanceof Pow && ((Pow) o2).getO1() instanceof Var && ((Pow) o2).getO1() == v) {
            return o2;
        }
        return null;
    }

    @Override
    public double eval() {
        return o1.eval() * o2.eval();
    }

    @Override
    public Operation dif() {
        return new Sum(new Mult(o1, o2.dif()), new Mult(o2, o1.dif()));
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Sum(
                new Mult(o1, o2.partialDerivative(v))
                , new Mult(o2, o1.partialDerivative(v))).simplify();
    }

    @Override
    public String toString() {
        String s1 = !o1.isBinary() || (o1 instanceof Mult) || (o1 instanceof Div) ? o1.toString() : "(" + o1.toString() + ")";
        String s2 = !o2.isBinary() || (o2 instanceof Mult) || (o2 instanceof Div) ? o2.toString() : "(" + o2.toString() + ")";
        return s1 + " * " + s2;
    }

    @Override
    public Operation copy() {
        return new Mult(o1.copy(), o2.copy());
    }

    @Override
    public Operation simplify() {
        o1 = simplifyWhileNotEquals(o1);
        o2 = simplifyWhileNotEquals(o2);
        if (o1 instanceof Const && o1.eval() == 0.0 || o2 instanceof Const && o2.eval() == 0.0) {
            return new Const(0.0);
        }
        if (o1 instanceof Const && o2 instanceof Const) {
            return new Const(o1.eval() * o2.eval());
        }
        if (o1 instanceof Var && o1 instanceof Var && o1 == o2) {
            return new Pow(o1, new Const(2));
        }
        if (o1 instanceof Pow && o2 instanceof Var && o2 == ((Pow) o1).getO1()) {
            return new Pow(o1, new Sum(((Pow) o1).getO2(), new Const(1)));
        }
        if (o2 instanceof Pow && o1 instanceof Var && o1 == ((Pow) o2).getO1()) {
            return new Pow(o2, new Sum(((Pow) o2).getO2(), new Const(1)));
        }
        if (o2 instanceof Pow && o1 instanceof Pow && ((Pow) o1).getO1() == ((Pow) o2).getO1()) {
            return new Pow(((Pow) o2).getO1(), new Sum(((Pow) o2).getO2(), ((Pow) o1).getO2()));
        }
        if (o1 instanceof Div) {
            return new Div(new Mult(o2, ((Div) o1).getO1()), ((Div) o1).getO2());
        }
        if (o2 instanceof Div) {
            return new Div(new Mult(o1, ((Div) o2).getO1()), ((Div) o2).getO2());
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
        if (o1 instanceof Const && o1.eval() == 0 || o2 instanceof Const && o2.eval() == 0) {
            return new Const(0);
        }
        if (o1 instanceof Const && o2 instanceof Const) {
            return new Const(o1.eval() * o2.eval());
        }
        if (o1 instanceof Const && o2 instanceof Neg) {
            return new Mult(new Const(-((Const) o1).getVal()), ((Neg) o2).getO());
        }
        if (o2 instanceof Const && o1 instanceof Neg) {
            return new Mult(new Const(-((Const) o2).getVal()), ((Neg) o1).getO());
        }
        /*
        if (o1 instanceof Pow) {
            Operation baseO1 = ((Pow) o1).getO1();
            Operation powO1 = ((Pow) o1).getO2();
            if (o2 instanceof Pow) {
                Operation baseO2 = ((Pow) o2).getO2();
                Operation powO2 = ((Pow) o2).getO2();
                if (baseO1.equals(baseO2)) {
                    return new Pow(baseO1, new Sum(powO1, powO2));
                }
            } else {
                Operation baseO2 = o2;
                if (baseO1.equals(baseO2)) {
                    return new Pow(baseO1, new Sum(powO1, new Const(1)));
                }
            }
        } else {
            Operation baseO1 = o1;
            if (o2 instanceof Pow) {
                Operation baseO2 = ((Pow) o2).getO2();
                Operation powO2 = ((Pow) o2).getO2();
                if (baseO1.equals(baseO2)) {
                    return new Pow(baseO1, new Sum(powO2, new Const(1)));
                }
            } else {//not pows
                Operation baseO2 = o2;
                if (baseO1.equals(baseO2)) {
                    return new Pow(baseO1, new Const(2));
                }
            }
        }*/
        if (o1 instanceof Sum) {
            if (o2 instanceof Sum) {
                //(_+_)*(_+_)
                Operation a = ((Sum) o1).getO1();
                Operation b = ((Sum) o1).getO2();
                Operation c = ((Sum) o2).getO1();
                Operation d = ((Sum) o2).getO2();
                Operation ac = new Mult(a, c);
                Operation ad = new Mult(a, d);
                Operation bc = new Mult(b, c);
                Operation bd = new Mult(b, d);
                return new Sum(new Sum(ac, ad), new Sum(bc, bd));
            } else {
                //(a+b)*(c);
                Operation a = ((Sum) o1).getO1();
                Operation b = ((Sum) o1).getO2();
                Operation ac = new Mult(a, o2);
                Operation bc = new Mult(b, o2);
                return new Sum(ac, bc);
            }
        } else {
            if (o2 instanceof Sum) {
                //(a)*(b+c)
                Operation b = ((Sum) o2).getO1();
                Operation c = ((Sum) o2).getO2();
                Operation ab = new Mult(o1, b);
                Operation ac = new Mult(o1, c);
                return new Sum(ab, ac);
            } else {
                //a*b
                return abbreviate();
            }
        }
    }

    private Operation abbreviate() {
        ArrayList<Operation> leaves = new ArrayList<Operation>();
        ArrayList<Operation> new_leaves = new ArrayList<Operation>();
        getMultLeaves(this, leaves);
        Operation current_operation;
        Operation local_power;
        double general_constant = 1;
        while (!leaves.isEmpty()) {
            if ((current_operation = leaves.remove(0)) instanceof Pow) {
                local_power = ((Pow) current_operation).getO2();
                current_operation = ((Pow) current_operation).getO1();
            } else {
                local_power = new Const(1);
            }
            if (current_operation instanceof Const) {
                general_constant *= current_operation.eval();
                continue;
            } else {
                for (int i = 0; i < leaves.size(); i++) {
                    Operation leave = leaves.get(i);
                    if (leave.equals(current_operation)) {
                        local_power = new Sum(local_power, new Const(1));
                        leaves.remove(i);
                    } else if (leave instanceof Pow) {
                        Operation base = ((Pow) leave).getO1();
                        Operation pow = ((Pow) leave).getO2();
                        if (base.equals(current_operation)) {
                            local_power = new Sum(local_power, pow);
                            leaves.remove(i);
                        }
                    }
                }
            }
            if (local_power.equals(new Const(1))) {
                new_leaves.add(current_operation);
            } else if (local_power.equals(new Const(0))) {
                new_leaves.add(new Const(1));
            } else {
                new_leaves.add(new Pow(current_operation, local_power));
            }
        }
        if (general_constant == 1) {
            return makeMultOperationByAL(new_leaves);
        } else {
            return new Mult(new Const(general_constant), makeMultOperationByAL(new_leaves));
        }
    }

    private Operation makeMultOperationByAL(ArrayList<Operation> al) {
        if (al.isEmpty()) {
            return new Const(0);
        }
        Operation res = al.remove(0);
        for (Operation leave : al) {
            res = new Mult(res, leave);
        }
        return res;
    }

    //This method is suitable for all binary commutative operations with o1 and o2
    @Override
    public boolean equals(Operation origin) {
        ArrayList<Operation> this_leaves = new ArrayList<Operation>();
        ArrayList<Operation> origin_leaves = new ArrayList<Operation>();
        getMultLeavesForEqual(this, this_leaves);
        getMultLeavesForEqual(origin, origin_leaves);
        return alEquals(this_leaves, origin_leaves);
    }

    private boolean alEquals(ArrayList<Operation> al1, ArrayList<Operation> al2) {
        if (al1.isEmpty())
            if (al2.isEmpty())
                return true;
            else if (al2.isEmpty())
                return false;
        Operation comparable_element = al1.get(0);
        for (int i = 0; i < al2.size(); i++) {
            if (comparable_element.equals(al2.get(i))) {
                al1.remove(0);
                al2.remove(i);
                return alEquals(al1, al2);
            }
        }
        return false;
    }

    private void getMultLeavesForEqual(Operation arg, ArrayList<Operation> leaves) {
        if (arg instanceof Mult) {
            Operation o1 = ((Mult) arg).getO1();
            Operation o2 = ((Mult) arg).getO2();
            if (o1 instanceof Mult) {
                getMultLeaves(o1, leaves);
            } else {
                leaves.add(o1);
            }
            if (o2 instanceof Mult) {
                getMultLeaves(o2, leaves);
            } else {
                leaves.add(o2);
            }
        }
    }

    private void getMultLeaves(Operation arg, ArrayList<Operation> leaves) {
        if (arg instanceof Mult) {
            Operation o1 = ((Mult) arg).getO1().toStand();
            Operation o2 = ((Mult) arg).getO2().toStand();
            if (o1 instanceof Mult) {
                getMultLeaves(o1, leaves);
            } else {
                leaves.add(o1);
            }
            if (o2 instanceof Mult) {
                getMultLeaves(o2, leaves);
            } else {
                leaves.add(o2);
            }
        }
    }

    @Override
    public boolean isBinary() {
        return true;
    }
}
