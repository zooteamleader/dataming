package data.mining.entity;

import lombok.Data;

@Data
public class Point {
    private double x;
    private double y;

    public Point(){}

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return
                "("+x +
                        ", " + y +")"
                ;
    }

    public static boolean getIsSame(Point p1, Point p2){
        if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) return true;
        return false;
    }

    public static double calculateDistance(Point p1, Point p2){
        double xDistance = p1.getX() - p2.getX();
        double yDistance = p1.getY() - p2.getY();
        double tmp = xDistance * xDistance + yDistance * yDistance;
        return Math.sqrt(tmp);
    }

    public static double calculateMHDDistance(Point p1, Point p2){
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

}

