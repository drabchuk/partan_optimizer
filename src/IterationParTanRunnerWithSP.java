import frame.functions.Function;
import frame.functions.realizations.Factories.FunctionFactory;
import frame.functions.realizations.Factories.MVFunctionFactory;
import frame.linear.Vector;
import frame.linear.VectorDouble;
import frame.optimization.multyDim.IterativeParTan;
import frame.optimization.multyDim.Optimizer;

import java.util.Scanner;

/**
 * Created by Денис on 10.06.2016.
 */
public class IterationParTanRunnerWithSP {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String fInter = scanner.nextLine();
        FunctionFactory ff = new MVFunctionFactory();

        try {
            Function f = ff.buildFunction(fInter);
            System.out.println(f);

            Optimizer optimizer = new IterativeParTan();

            int length = f.getVarsArray().length;
            Double[] sp = new Double[length];
            System.out.println("Enter starting point");
            for (int i = 0; i < length; i++) {
                sp[i] = scanner.nextDouble();
            }
            VectorDouble start = new VectorDouble(sp);
            Vector<Double> min = optimizer.optimize(f, start);

            System.out.println(min);
            System.out.println("f(min) = " + f.eval());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
