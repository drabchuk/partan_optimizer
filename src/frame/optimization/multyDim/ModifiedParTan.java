package frame.optimization.multyDim;

import frame.functions.Function;
import frame.functions.realizations.operations.realizations.Var;
import frame.linear.Vector;
import frame.linear.VectorDouble;
import frame.linear.VectorFunction;
import frame.optimization.oneDim.byDirection.DirectionOptimizer;
import frame.optimization.oneDim.byDirection.GoldenSectionDirOpt;

/**
 * Created by Денис on 10.06.2016.
 */
public class ModifiedParTan implements Optimizer {
    Var[] vars;
    Function f;
    VectorFunction grad;
    DirectionOptimizer directionOptimizer = new GoldenSectionDirOpt();

    @Override
    public Vector<Double> optimize(Function f) {
        this.f = f;
        vars = f.getVarsArray();//Det variables array
        grad = f.grad();//Gradient of entire function

        //sey start point as 0
        Double[] dump = new Double[f.getVarsArray().length];
        for (int i = 0; i < dump.length; i++) {
            dump[i] = 0.0;
        }
        Vector<Double> startingPoint = new VectorDouble(dump);
        methodMainAlgorithm(f, startingPoint);
        Var[] vars = f.getVarsArray();
        Double[] min = new Double[vars.length];
        for (int i = 0; i < vars.length; i++) {
            min[i] = vars[i].eval();
        }
        return new VectorDouble(min);
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

    private void methodMainAlgorithm(Function fun, Vector<Double> startingPoint) {


        Vector<Double> x0 = startingPoint;

        Vector<Double> iterationStart = startingPoint;

        // first iteration
        Vector<Double> iterationEnd = findParallelTangent(iterationStart);

        Vector<Double> iterationDir = iterationEnd.minus(iterationStart);

        Vector<Double> approx = directionOptimizer.minimizeByDir(f, iterationEnd, iterationDir);

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
