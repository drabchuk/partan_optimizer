package frame.optimization.oneDim.byDirection;

import frame.functions.Function;
import frame.linear.Vector;
import frame.optimization.Parametrizators.LinearParametrizator;
import frame.optimization.oneDim.optimizer.GoldenSection;
import frame.optimization.oneDim.optimizer.OneDimOptimizer;
import static frame.constants.Constants.*;

/**
 * Created by Денис on 09.06.2016.
 */
public class GoldenSectionDirOpt implements DirectionOptimizer {

    @Override
    public Vector<Double> minimizeByDir(Function f, Vector<Double> x0, Vector<Double> direction) {
        double lambda;
        LinearParametrizator parametrizator = new LinearParametrizator(
                f.getVarsArray()
                , x0
                , direction
        );

        // 2.1
        // find lambda
        // min f(x0 - grad * lambda)
        OneDimOptimizer optimizer = new GoldenSection(f, parametrizator);
        lambda = optimizer.findMinimum(STANDARD_ACCURACY);
        Vector<Double> minPoint = x0.plus(direction.multByConst(-lambda));

        return minPoint;
    }
}
