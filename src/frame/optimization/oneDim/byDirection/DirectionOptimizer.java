package frame.optimization.oneDim.byDirection;

import frame.functions.Function;
import frame.linear.Vector;

/**
 * Created by Денис on 09.06.2016.
 */
public interface DirectionOptimizer {

    Vector<Double> minimizeByDir(
            Function f
            , Vector<Double> point
            , Vector<Double> direction
    );

}
