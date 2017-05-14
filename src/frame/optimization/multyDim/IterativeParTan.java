package frame.optimization.multyDim;

import static frame.constants.Constants.*;

import frame.functions.Function;
import frame.functions.realizations.operations.realizations.Var;
import frame.linear.Vector;
import frame.linear.VectorDouble;
import frame.linear.VectorFunction;
import frame.optimization.oneDim.byDirection.DirectionOptimizer;
import frame.optimization.oneDim.byDirection.GoldenSectionDirOpt;

/**
 * Created by Денис on 09.06.2016.
 */
public class IterativeParTan implements Optimizer {

    Var[] vars;
    Function f;
    VectorFunction grad;
    DirectionOptimizer directionOptimizer = new GoldenSectionDirOpt();

    @Override
    public Vector<Double> optimize(Function f) {
        this.f = f;
        vars = f.getVarsArray();//Det variables array
        grad = f.grad();//Gradient of entire function

        //set start point as 0
        Double[] dump = new Double[f.getVarsArray().length];
        for (int i = 0; i < dump.length; i++) {
            dump[i] = 0.0;
        }
        Vector<Double> startingPoint = new VectorDouble(dump);

        Vector<Double> approx = methodMainAlgorithm(f, startingPoint);

        return approx;
    }

    @Override
    public Vector<Double> optimize(Function f, Vector<Double> startingPoint) {
        return null;
    }

    @Override
    public Vector<Double> optimize(Function f, Vector<Double> x0, Integer requestsCounter) {
        requestsCounter = 0;
        return null;
    }

    private Vector<Double> methodMainAlgorithm(Function fun, Vector<Double> startingPoint) {


        Vector<Double> x0 = startingPoint;

        Vector<Double> iterationStart = startingPoint;

        Vector<Double> approx;

        // first iteration
        int counter = 0;
        do {
            Vector<Double> iterationEnd = findParallelTangent(iterationStart);
            Vector<Double> iterationDir = iterationEnd.minus(iterationStart);
            approx = directionOptimizer.minimizeByDir(f, iterationStart, iterationDir);
            iterationStart = approx;
            Vector<Double> gradA = grad(approx);
            double gradNorm = grad(approx).norm();
            System.out.println("grad norm: " + gradNorm);
        } while (grad(approx).norm() > STANDARD_ACCURACY && counter++ < 20);

        return approx;
    }

    private Vector<Double> findParallelTangent(Vector<Double> point) {

        Vector<Double> from = point.copy();
        Vector<Double> to = point.copy();
        for (int i = 0; i < vars.length; i++) {
            to = directionOptimizer.minimizeByDir(f, from, grad(from));
            from = to;
        }
        return to;
    }

    private Vector<Double> grad(Vector<Double> point) {
        setVars(point);
        return grad.eval();
    }

    private void setVars(Vector<Double> point) {
        for (int i = 0; i < vars.length; i++) {
            vars[i].setValue(point.get(i));
        }
    }

}
