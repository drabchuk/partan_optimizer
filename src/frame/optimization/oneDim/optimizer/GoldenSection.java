package frame.optimization.oneDim.optimizer;


import frame.functions.Function;
import frame.optimization.Parametrizators.Parametrizer;

/**
 * Created by Денис on 09.06.2016.
 */
public class GoldenSection extends OneDimOptimizer {

    public GoldenSection(Function f, Parametrizer parametrizer) {
        super(f, parametrizer);
    }

    @Override
    public double findMinimum(double accuracy) {
        super.defTargetInterval();
        //System.out.println("Golden section method started calculations; accuracy = " + accuracy);
        double a = targetInterval.x, fa = f(a);
        double b = targetInterval.y, fb = f(b);
        double l = b - a;
        //System.out.println("a = " + a + "; b = " + b + "l = " + l);
        double fx1, fx2;

        double x1 = a + 0.382*l; fx1 = f(x1);
        double x2 = a + 0.618*l; fx2 = f(x2);
        //System.out.println("x1 = " + x1 + "; x2 = " + x2);


        while(l > accuracy) {
            //System.out.println("NEXT ITERATION");
            if (fx1 < fx2) {
                b = x2;
                fb = fx2;
                l = b - a;
                x2 = x1;
                fx2 = fx1;
                x1 = a + 0.382 * l; fx1 = f(x1);
            } else {
                a = x1; fa = fx1;
                l = b - a;
                x1 = x2; fx1 = fx2;
                x2 = a + 0.612*l; fx2 = f(x2);
            }
            //System.out.println("a = " + a + "; b = " + b + "; x1 = " + x1 + "; x2 = " + x2);
        }

        //System.out.println("Golden section method executed");
        //System.out.println("x_min = " + x1 + "; f(x_min) = " + f(x1));

        return x1;
    }

}
