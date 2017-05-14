package frame.functions.realizations;

import frame.functions.Function;
import frame.exceptions.*;
import frame.functions.realizations.operations.Operation;
import frame.functions.realizations.operations.realizations.Const;
import frame.functions.realizations.operations.realizations.Var;
import frame.linear.VectorFunction;

import java.util.HashMap;

/**
 * Created by Денис on 26.05.2016.
 */
public class FunctionMV implements Function {

    private Operation func;
    private HashMap<String, Var> vars = new HashMap<>();

    public FunctionMV(
              Operation func
            , HashMap<String, Var> vars
    ) {
        this.func = func;
        this.vars = vars;
    }

    @Override
    public double eval() {
        return func.eval();
    }

    @Override
    public void setVariable(String name, double val) throws NoSuchVariableException {
        if (!vars.containsKey(name)) {
            throw new NoSuchVariableException();
        }
        vars.get(name).setValue(val);
    }

    //Must be immutable
    @Override
    public Var[] getVarsArray() {
        Var[] out = new Var[vars.values().size()];
        vars.values().toArray(out);
        return out;
    }

    @Override
    public Function partialDerivative(Var v) {
        FunctionMV f = new FunctionMV(func.partialDerivative(v).simplify(), vars);
        f.simplify();
        return f;
    }

    @Override
    public String toString() {
        return func.toString();
    }

    private void simplify() {
        func = func.simplify();
    }

    private void toStand() {
        Operation old_func = new Const(0);
        while (!func.equals(old_func)){
            old_func = func;
            //func = func.simplify();
            func = func.toStand();
        }
    }

    @Override
    public VectorFunction grad() {
        Var[] varsArray = getVarsArray();
        Function[] res = new Function[varsArray.length];
        for (int i = 0; i < varsArray.length; i++) {
            res[i] = this.partialDerivative(varsArray[i]);
        }
        return new VectorFunction(res);
    }

}
