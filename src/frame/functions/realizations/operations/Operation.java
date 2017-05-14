package frame.functions.realizations.operations;

import frame.functions.realizations.operations.realizations.Var;

/**
 * Created by ����� on 05.10.2015.
 */
public interface Operation {
    double eval();
    Operation dif();
    Operation copy();
    Operation simplify();
    Operation toStand();
    boolean isBinary();
    boolean equals(Operation origin);
    Operation partialDerivative(Var v);
}
