package frame.optimization.oneDim.optimizer;

import frame.functions.Function;
import frame.optimization.Parametrizators.Parametrizer;

/**
 * Created by Денис on 09.06.2016.
 */
public class DSKPowel extends OneDimOptimizer {

    public DSKPowel(Function f, Parametrizer parametrizer) {
        super(f, parametrizer);
    }

    @Override
    public void setParametrizer(Parametrizer parametrizer) {
        super.setParametrizer(parametrizer);
    }

    @Override
    public double findMinimum(double accuracy) {
        return super.findMinimum(accuracy);
    }

    @Override
    protected double f(double x) {
        return super.f(x);
    }
}
