import java.util.Scanner;
import frame.functions.Function;
import frame.functions.realizations.Factories.FunctionFactory;
import frame.functions.realizations.Factories.MVFunctionFactory;
import frame.functions.realizations.operations.realizations.Var;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String fInter = scanner.nextLine();
        FunctionFactory ff = new MVFunctionFactory();

        try {
            Function f = ff.buildFunction(fInter);
            System.out.println(f);

            Var[] fVars = f.getVarsArray();
            for (int i = 0; i < fVars.length; i++) {
                System.out.println(fVars[i]);

                Function df = f.partialDerivative(fVars[i]);
                System.out.println("df / d" + fVars[i]
                        + "= " + df);

            }

        } catch (Exception e) {
            System.out.println("Illegal expression");
            e.printStackTrace();
        }

    }
}
