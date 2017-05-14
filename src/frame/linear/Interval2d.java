package frame.linear;

/**
 * Created by Денис on 09.06.2016.
 */
public class Interval2d {
    public Double x;
    public Double y;

    //null means infinity?
    public Interval2d(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public String view() {
        return "[" + x + ", " + y + "]";
    }
}
