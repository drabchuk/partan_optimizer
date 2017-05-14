package frame.optimization.Parametrizators;

import frame.functions.realizations.operations.realizations.Var;
import frame.linear.Vector;

/**
 * Created by Денис on 09.06.2016.
 */
public class LinearParametrizator implements Parametrizer {

    private Double lambda;
    private Var[] vars;
    private Vector<Double> point;
    private Vector<Double> direction;

    public LinearParametrizator(Var[] vars, Vector<Double> point, Vector<Double> direction) {
        this.vars = vars;
        this.point = point;
        this.direction = direction;
    }

    @Override
    public void setParams(Double... values) {
        lambda = values[0];
        for (int i = 0; i < vars.length; i++) {
            vars[i].setValue(point.get(i) - direction.get(i) * lambda);
        }
    }

    public void setLambda(Double lambda) {
        this.lambda = lambda;
    }
}
