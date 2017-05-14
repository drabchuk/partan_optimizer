package frame.optimization.oneDim.optimizer;

import frame.functions.Function;
import frame.linear.Interval2d;
import frame.optimization.Parametrizators.Parametrizer;

/**
 * Created by Денис on 09.06.2016.
 */
public abstract class OneDimOptimizer {

    Interval2d targetInterval = null;
    Parametrizer parametrizer;
    Function f;

    public OneDimOptimizer(Function f, Parametrizer parametrizer) {
        this.f = f;
        this.parametrizer = parametrizer;
        defTargetInterval();
    }

    public void setParametrizer(Parametrizer parametrizer) {
        this.parametrizer = parametrizer;
    }

    public double findMinimum(double accuracy) {
        return targetInterval.x;
    }

    protected double f(double x) {
        parametrizer.setParams(x);
        return f.eval();
    }

    protected void defTargetInterval() {
        targetInterval = defTIbySven(0.0, 1.0);
    }

    private Interval2d defTIbySven(double x0, double d) {
        double x1, x2, x3, tmp;

        x2 = x0;
        x3 = x2 + d;

        if (f(x3) > f(x2)) {
            d = -d;
            x1 = x3;
        } else {
            x1 = x2 - d;
        }

        while (true) {
            x3 = x2 + d;
            if (f(x3) >= f(x2))
                break;
            x1 = x2;
            x2 = x3;
            d *= 2;
        }

        tmp = (x3 + x2) / 2;
        if (f(tmp) >= f(x2)) {
            x3 = tmp;
        } else {
            x1 = x2;
            x2 = tmp;
        }

        if (x1 > x3) {
            tmp = x3;
            x3 = x1;
            x1 = tmp;
        }
        return new Interval2d(x1, x3);
    }

}
