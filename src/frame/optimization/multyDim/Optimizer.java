package frame.optimization.multyDim;

import frame.functions.Function;
import frame.linear.Vector;

/**
 * Created by Денис on 09.06.2016.
 */
public interface Optimizer {

    Vector<Double> optimize(Function f);
    Vector<Double> optimize(Function f, Vector<Double> startingPoint);
    Vector<Double> optimize(Function f, Vector<Double> startingPoint, Integer requestsCounter);

}
