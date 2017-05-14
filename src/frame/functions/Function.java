package frame.functions;

import frame.functions.realizations.operations.realizations.Var;
import frame.exceptions.*;
import frame.linear.VectorFunction;

/**
 * Created by Денис on 26.05.2016.
 */
public interface Function {
    double eval();
    Function partialDerivative(Var v);
    void setVariable(String name, double val) throws NoSuchVariableException;
    Var[] getVarsArray();
    VectorFunction grad();
}
