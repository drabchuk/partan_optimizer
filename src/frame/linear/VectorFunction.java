package frame.linear;

import frame.functions.Function;
import frame.exceptions.*;

/**
 * Created by Денис on 09.06.2016.
 */
public class VectorFunction extends Vector<Function>{

    public VectorFunction(Function... f) {
        x = f;
    }

    public Vector<Double> eval() {
        Double[] res = new Double[getDim()];
        for (int i = 0; i < getDim(); i++) {
            res[i] = x[i].eval();
        }
        return new VectorDouble(res);
    }

    @Override
    public Vector<Function> plus(Vector<Function> that) throws DimensionConflictException {
        return null;
    }

    @Override
    public Vector<Function> minus(Vector<Function> that) throws DimensionConflictException {
        return null;
    }

    @Override
    public Vector<Function> multByConst(Double c) {
        return null;
    }

    @Override
    public Function norm() {
        return null;
    }

    @Override
    public Vector<Function> copy() {
        return new VectorFunction(x.clone());
    }
}
