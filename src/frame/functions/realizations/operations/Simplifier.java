package frame.functions.realizations.operations;

/**
 * Created by Денис on 30.05.2016.
 */
public abstract class Simplifier {

    public static Operation simplifyWhileNotEquals(Operation o) {
        Operation old;
        do {
            old = o;
            o = o.simplify();
        } while(!old.equals(o));
        return o;
    }

}
