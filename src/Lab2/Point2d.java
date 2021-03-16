package Lab2;

public class Point2d {
    protected double x;
    protected double y;

    public Point2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point2d(){
        this(0,0);
    }

    public double getX () { return x; }
    public void setX (double x) { this.x = x; }

    public double getY () { return y; }
    public void setY (double y) { this.y = y; }
}
