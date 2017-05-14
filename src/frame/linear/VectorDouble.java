package frame.linear;
import frame.exceptions.*;

/**
 * Created by Денис on 09.06.2016.
 */
public class VectorDouble extends Vector<Double> {

    public VectorDouble(Double... x){
        super(x);
    }

    @Override
    public Vector<Double> plus(Vector<Double> that)
            throws DimensionConflictException
    {
        if ( that.getDim() != this.getDim()) {
            throw new DimensionConflictException();
        }

        VectorDouble res = new VectorDouble(x);
        for (int i = 0; i < res.getDim(); i++) {
            Double sum = Double.sum(this.get(i), that.get(i));
            res.set(i, sum);
        }
        return res;
    }

    @Override
    public Vector<Double> minus(Vector<Double> that)
            throws DimensionConflictException
    {
        if ( that.getDim() != this.getDim()) {
            throw new DimensionConflictException();
        }

        VectorDouble res = new VectorDouble(x);
        for (int i = 0; i < res.getDim(); i++) {
            Double sum = Double.sum(this.get(i), - that.get(i));
            res.set(i, sum);
        }
        return res;
    }

    @Override
    public Vector<Double> multByConst(Double c){
        VectorDouble res = new VectorDouble(x);
        for (int i = 0; i < res.getDim(); i++) {
            Double temp = this.get(i) * c;
            res.set(i, temp);
        }
        return res;
    }

    @Override
    public Double norm() {
        double sumSqr = 0.0;
        for (int i = 0; i < x.length; i++) {
            sumSqr += Math.pow(x[i], 2);
        }
        return Math.sqrt(sumSqr);
    }

    @Override
    public Vector<Double> copy() {
        return new VectorDouble(x.clone());
    }
}
