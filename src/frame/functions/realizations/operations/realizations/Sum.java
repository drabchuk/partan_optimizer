package frame.functions.realizations.operations.realizations;

import frame.functions.realizations.operations.Operation;
import static frame.functions.realizations.operations.Simplifier.*;

import java.util.ArrayList;

/**
 * Created by ����� on 05.10.2015.
 */
public class Sum implements Operation {
    private Operation o1;
    private Operation o2;

    public Sum(Operation o1, Operation o2) {
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
        return o1.eval() + o2.eval();
    }

    @Override
    public Operation dif() {
        return new Sum(o1.dif(), o2.dif());
    }

    @Override
    public Operation partialDerivative(Var v) {
        return new Sum(o1.partialDerivative(v), o2.partialDerivative(v)).simplify();
    }

    @Override
    public String toString() {
        String s1 = o1.toString();
        String s2 = o2.toString();
        if (o2 instanceof Neg)
            return s1 + " " + s2;
        else if (o1 instanceof Neg)
            return s2 + " " + s1;
        else
            return s1 + " + " + s2;
    }

    @Override
    public Operation copy() {
        return new Sum(o1.copy(), o2.copy());
    }

    @Override
    public Operation simplify() {
        o1 = simplifyWhileNotEquals(o1);
        o2 = simplifyWhileNotEquals(o2);
        if (o1 instanceof Const && o2 instanceof Const) {
            return new Const(o1.eval() + o2.eval());
        }
        if (o1 instanceof Const && o1.eval() == 0) {
            return o2;
        }
        if (o2 instanceof Const && o2.eval() == 0) {
            return o1;
        } else return this;
    }

    @Override
    public Operation toStand() {
        //changing 02.03.2016
        //o1 = o1.simplify();
        //o2 = o2.simplify();
        //changing 02.03.2016
        Operation o1_old = o1;
        Operation o2_old = o2;
        while (!(
                        (o1 = o1.toStand()).equals(o1_old)
                        && (o2 = o2.toStand()).equals(o2_old)
                )) {
            o1_old = o1;
            o2_old = o2;
        }

        /*if (o1 instanceof Const && o2 instanceof Const) {
            return new Const(o1.eval() + o2.eval());
        }*/
        Operation operation_old = this;
        Operation operation_new = factor_constants_out(operation_old);
        while(!operation_new.equals(operation_old)) {
            operation_old = operation_new;
            operation_new = factor_constants_out(operation_old);
        }

        /*if (operation_new.equals(this))
            return this;
        else
            return operation_new;*/
        return operation_new;
    }

    private Operation factor_constants_out(Operation oper) {
        ArrayList<Operation> leaves = new ArrayList<Operation>();
        ArrayList<Operation> new_leaves = new ArrayList<Operation>();
        getSumLeaves(oper, leaves);
        Operation current_operation;
        double local_constant;
        double general_constant = 0.0;
        while (!leaves.isEmpty()) {
            current_operation = leaves.remove(0);
            local_constant = 1;
            if (current_operation instanceof  Const) {
                general_constant += current_operation.eval();
            } else {
                if (current_operation instanceof Mult) {
                    if (((Mult) current_operation).getO1() instanceof Const) {
                        local_constant = ((Mult) current_operation).getO1().eval();
                        current_operation = ((Mult) current_operation).getO2();
                    } else if (((Mult) current_operation).getO2() instanceof Const) {
                        local_constant = ((Mult) current_operation).getO2().eval();
                        current_operation = ((Mult) current_operation).getO1();
                    }
                }
                for (int i = 0; i < leaves.size(); i++) {
                    Operation leave = leaves.get(i);
                    if (leave.equals(current_operation)) {
                        local_constant++;
                        leaves.remove(i);
                    } else if (leave instanceof Mult) {
                        if (((Mult) leave).getO1() instanceof Const) {
                            if (((Mult) leave).getO2().equals(current_operation)) {
                                local_constant += ((Mult) leave).getO1().eval();
                                leaves.remove(i);
                            }
                        } else if (((Mult) leave).getO2() instanceof Const) {
                            if (((Mult) leave).getO1().equals(current_operation)) {
                                local_constant += ((Mult) leave).getO2().eval();
                                leaves.remove(i);
                            }
                        }
                    }
                }
                if (local_constant == 1) {
                    new_leaves.add(current_operation);
                } else {
                    new_leaves.add(new Mult(new Const(local_constant), current_operation));
                }
            }
        }
        if (general_constant == 0.0)
            return makeSumOperationByAL(new_leaves);
        else
            return new Sum(new Const(general_constant), makeSumOperationByAL(new_leaves));
    }

    private Operation makeSumOperationByAL(ArrayList<Operation> al) {
        if (al.isEmpty()) {
            return new Const(0);
        }
        Operation res = al.remove(0);
        for (Operation leave : al) {
            res = new Sum(res, leave);
        }
        return res;
    }

    @Override
    public boolean equals(Operation origin) {
        ArrayList<Operation> this_leaves = new ArrayList<Operation>();
        ArrayList<Operation> origin_leaves = new ArrayList<Operation>();
        getSumLeavesForEqual(this, this_leaves);
        getSumLeavesForEqual(origin, origin_leaves);
        return alEquals(this_leaves, origin_leaves);
    }

    private boolean alEquals(ArrayList<Operation> al1, ArrayList<Operation> al2) {
        if (al1.isEmpty())
            if (al2.isEmpty())
                return true;
            else
                return false;
        if (al2.isEmpty())
            if (al1.isEmpty())
                return true;
            else
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

    private void getSumLeavesForEqual(Operation arg, ArrayList<Operation> leaves) {
        if (arg instanceof Sum) {
            Operation o1 = ((Sum) arg).getO1();
            Operation o2 = ((Sum) arg).getO2();
            //Operation o1 = ((Sum) arg).getO1().toStand();
            //Operation o2 = ((Sum) arg).getO2().toStand();
            if (o1 instanceof Sum) {
                getSumLeaves(o1, leaves);
            } else {
                leaves.add(o1);
            }
            if (o2 instanceof Sum) {
                getSumLeaves(o2, leaves);
            } else {
                leaves.add(o2);
            }
        }
    }

    private void getSumLeaves(Operation arg, ArrayList<Operation> leaves) {
        if (arg instanceof Sum) {
            Operation o1 = ((Sum) arg).getO1().toStand();
            Operation o2 = ((Sum) arg).getO2().toStand();
            Operation o1_old = o1;
            Operation o2_old = o2;
            while (!((o1 = ((Sum) arg).getO1().toStand()).equals(o1_old) && (o2 = ((Sum) arg).getO2().toStand()).equals(o2_old))) {
                o1_old = o1;
                o2_old = o2;
            }
            //Operation o1 = ((Sum) arg).getO1().toStand();
            //Operation o2 = ((Sum) arg).getO2().toStand();
            if (o1 instanceof Sum) {
                getSumLeaves(o1, leaves);
            } else {
                leaves.add(o1);
            }
            if (o2 instanceof Sum) {
                getSumLeaves(o2, leaves);
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
