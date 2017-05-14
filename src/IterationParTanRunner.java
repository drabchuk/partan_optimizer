import frame.functions.Function;
import frame.functions.realizations.Factories.FunctionFactory;
import frame.functions.realizations.Factories.MVFunctionFactory;
import frame.functions.realizations.operations.realizations.Var;
import frame.linear.Vector;
import frame.optimization.multyDim.IterativeParTan;
import frame.optimization.multyDim.ModifiedParTan;
import frame.optimization.multyDim.Optimizer;

import java.util.Scanner;

/**
 * Created by Денис on 10.06.2016.
 */
public class IterationParTanRunner {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String fInter = scanner.nextLine();
        FunctionFactory ff = new MVFunctionFactory();

        try {
            Function f = ff.buildFunction(fInter);
            System.out.println(f);

            Optimizer optimizer = new IterativeParTan();

            Vector<Double> min = optimizer.optimize(f);

            System.out.println(min);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
