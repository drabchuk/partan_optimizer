package frame.linear;

import frame.exceptions.*;

/**
 * Created by Денис on 09.06.2016.
 */
public abstract class Vector<T> {

    T[] x;

    public Vector(T... x) {
        this.x = x;
    }

    public int getDim() {
        return x.length;
    }

    public T get(int i) throws RuntimeException{
        return x[i];
    }

    public void set(int i, T val) throws DimensionConflictException {
        if ( (i < 0) || (i >= x.length)) {
            throw new DimensionConflictException();
        }
        x[i] = val;
    }

    public abstract T norm();
    public abstract Vector<T> plus(Vector<T> that) throws DimensionConflictException;
    public abstract Vector<T> minus(Vector<T> that) throws DimensionConflictException;
    public abstract Vector<T> multByConst(Double c);

    public abstract Vector<T> copy();

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("(");
        for (int i = 0; i < x.length; i++) {
            sb.append(x[i].toString());
            sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
