import java.util.*;

public class dataPoints
{
    private double xVal;
    private double yVal;
    private double distance;
    private double radians;
    private double angle;

    public dataPoints()
    {
        distance = 0;
        xVal = 0;
        yVal = 0;
    }

    public dataPoints(double dist)
    {
        distance = dist;
    }

    public double getX()
    {
        return xVal;
    }

    public double getY()
    {
        return yVal;
    }

    public double getDistance()
    {
        return distance;
    }

    public double getRadians()
    {
        return radians;
    }

    public void setDeg(double deg)
    {
        radians = Math.toRadians(deg);

        xVal = distance * Math.cos(radians);
        yVal = distance * Math.sin(radians);
    }

    public void setAngle(double ang)
    {
        angle = ang;
    }

    public double getAngle()
    {
        return angle;
    }

}