package frame.functions.realizations.Factories;

import frame.functions.Function;

/**
 * Created by Денис on 09.06.2016.
 */
public abstract class FunctionFactory {

    public abstract Function buildFunction(String interpretation) throws Exception;

}
