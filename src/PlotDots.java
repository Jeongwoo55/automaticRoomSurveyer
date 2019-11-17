import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PlotDots extends JFrame
{
    static int DATATHRESHOLD = 50;
    static int ANGLETHRESHOLD = 5;
    static int DATAMULTIPLIER = 100;
    static int screenHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    static int screenWidth = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());

    private JPanel window;
    private ArrayList<dataPoints> dataArray = new ArrayList<>();
    private int windowHeight;
    private int windowWidth;
    private int counter = 0;

    public PlotDots() throws FileNotFoundException
    {
        makeTheWindow();
        readData();
        convertDataToDots();
        testAngles();
        testDistance();
        makeJLabels();
    }

    public void makeTheWindow()
    {
        window = new JPanel(null);
        getContentPane().add(window);
        window.setLayout(null);
        window.setBackground(Color.white);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Plot of Room");
        setSize(screenWidth, screenHeight);
        setLocation(0,0);
        setResizable(true);

        windowHeight = this.getHeight();
        windowWidth = this.getWidth();
    }

    public void makeJLabels()
    {
        JLabel titleLabel = new JLabel("Automatic Room Surveyer");
        titleLabel.setBounds(5, 0, 400, 20);
        window.add(titleLabel);

        JLabel areaLabel = new JLabel("The area of the room is " + calculateArea() + " centimeters sqaured.");
        areaLabel.setBounds(5,20,400,20);
        window.add(areaLabel);

        JLabel perimeter = new JLabel("The perimeter of the room is " + calculatePerimeter() + " centimeters.");
        perimeter.setBounds(5,40,400,20);
        window.add(perimeter);
    }

    public void readData() throws FileNotFoundException
    {
        Scanner file = new Scanner(new File("data.dat"));
        int i = 0;

        while(file.hasNextDouble())
        {
            double tempData = DATAMULTIPLIER * file.nextDouble();
            if (i == 0)
            {
                dataArray.add(new dataPoints(tempData));
            }
            else
            {
                if (Math.abs(tempData - dataArray.get(dataArray.size()-1).getDistance()) < DATATHRESHOLD)
                {
                    dataArray.add(new dataPoints(tempData));
                }
            }
            ++i;
        }
    }

    private void convertDataToDots()
    {
        for (int i = 0; i < dataArray.size(); i++)
        {
            dataArray.get(i).setDeg(i*(360/dataArray.size()));
        }

        for (int i = 0; i < dataArray.size(); i++)
        {
            System.out.println("(" + dataArray.get(i).getX() + ", " + dataArray.get(i).getY() + ")");
        }
    }

    public void paint(Graphics gp)
    {
        super.paint(gp);
        Graphics2D graphics = (Graphics2D) gp;

        for (int i = 0; i < dataArray.size()-1; i++)
        {
            Line2D line = new Line2D.Double((int)((windowWidth/2) + dataArray.get(i).getX()), (int)((windowHeight/2) - dataArray.get(i).getY()), (int)((windowWidth/2) + dataArray.get(i+1).getX()), (int)((windowHeight/2) - dataArray.get(i+1).getY()));
            graphics.draw(line);
        }
        Line2D line = new Line2D.Double((int)((windowWidth/2) + dataArray.get(dataArray.size()-1).getX()), (int)((windowHeight/2) - dataArray.get(dataArray.size()-1).getY()), (int)((windowWidth/2) + dataArray.get(0).getX()), (int)((windowHeight/2) - dataArray.get(0).getY()));
        graphics.draw(line);
    }

    public double angleCalculation(dataPoints x, dataPoints y, dataPoints z)
    {
        double a = distanceFormula(y, z);
        double b = distanceFormula(x, z);	//goal
        double c = distanceFormula(x, y);
        dataPoints origin = new dataPoints();

        //changes to degrees and rounds to 2 places
        if (x.getDistance() <= y.getDistance() && z.getDistance() <= y.getDistance())
            return Math.round( (Math.toDegrees(Math.acos( (Math.pow(a, 2) + Math.pow(c , 2) - Math.pow(b, 2) ) / (2 * a * c) ) ) ) * 100) / 100.0;
        else
            return 360 - (Math.round( (Math.toDegrees(Math.acos( (Math.pow(a, 2) + Math.pow(c , 2) - Math.pow(b, 2) ) / (2 * a * c) ) ) ) * 100) / 100.0);
    }

    public double distanceFormula(dataPoints x, dataPoints y)
    {
        double rise = Math.abs(x.getY() - y.getY());
        double run = Math.abs(x.getX() - y.getX());

        return Math.sqrt(Math.pow(rise, 2) + Math.pow(run, 2));
    }

    public void testAngles()
    {
        System.out.println("TEST ANGLE MEASURES");

        double angle = angleCalculation(dataArray.get(dataArray.size()-1), dataArray.get(0), dataArray.get(1));
        dataArray.get(0).setAngle(angle);
        if(checkAngleChange(angle))
            makeAngleLabel(angle, dataArray.get(0));
        for (int i = 1; i < dataArray.size() - 1; i++)
        {
            angle = angleCalculation(dataArray.get(i-1), dataArray.get(i), dataArray.get(i+1));
            dataArray.get(i).setAngle(angle);
            if(checkAngleChange(angle))
                makeAngleLabel(angle, dataArray.get(i));
        }
        angle = angleCalculation(dataArray.get(dataArray.size()-2), dataArray.get(dataArray.size()-1), dataArray.get(0));
        dataArray.get(dataArray.size()-1).setAngle(angle);
        if(checkAngleChange(angle))
            makeAngleLabel(angle, dataArray.get(dataArray.size()-1));
    }

    public void makeAngleLabel(double angle, dataPoints point)
    {
        int width = 50;
        int height = 15;
        System.out.println("windowheight = " + windowHeight);
        System.out.println("windowwidth = " + windowWidth);

        JLabel angleLabel = new JLabel();
        angleLabel.setText(angle + "" + (char)176);
//        angleLabel.setHorizontalTextPosition(SwingConstants.LEFT);
//        angleLabel.setVerticalTextPosition(SwingConstants.TOP);
        angleLabel.setBounds((int)((windowWidth/2.0) + point.getX() - (width / 2.0)) + 20, (int)((windowHeight/2.0) - point.getY() - (height / 2.0)) - 20, width, height);
//        angleLabel.setLocation();
//        angleLabel.setHorizontalAlignment(0);
//        angleLabel.setVerticalAlignment(0);
        window.add(angleLabel);
        getContentPane().repaint();
        System.out.println((int)((windowWidth/2.0) + point.getX()) + ", " + (int)((windowHeight/2.0) + point.getY()));
        System.out.println(angle);
    }

//    public void makeAngleLabel(double angle, dataPoints point)
//    {
//        JLabel label = new JLabel();
//        label.setText((char)(65 + counter) + "");
//        label.setHorizontalTextPosition((int)(this.getContentPane().getWidth()/2.0 + point.getX()));
//        label.setVerticalTextPosition((int)(this.getContentPane().getHeight()/2.0 + point.getY()));
//
//        System.out.println(angle);
//    }

    public void makeLineLabel(dataPoints x, dataPoints y)
    {
        int width = 75;
        int height = 15;
        JLabel lineLabel = new JLabel();
        double distance = Math.round(distanceFormula(x, y) * 100) / 100.0;
        lineLabel.setText(distance + "cm ");

        int labelX = (int)(x.getX() + ((y.getX() - x.getX()) / 2));
        int labelY = (int)(y.getY() + ((x.getY() - y.getY()) / 2));
        lineLabel.setBounds((int)((windowWidth/2.0) + labelX - (0.5 * width)) + 10, (int)((windowHeight/2.0) - labelY - (0.5 * height)) - 30, width, height);
        window.add(lineLabel);
        window.repaint();
    }

    public void testDistance()
    {
        dataPoints start = null;
        dataPoints end;
        int i;
        for (i = 0; i < dataArray.size(); i++)
        {
            if (checkAngleChange(dataArray.get(i).getAngle()))
            {
                start = dataArray.get(i);
                break;
            }
            //place for circum
        }
        int startPlace = i;
        for (i = i + 1; i < dataArray.size(); i++)
        {
            if (checkAngleChange(dataArray.get(i).getAngle()))
            {
                end = dataArray.get(i);
                makeLineLabel(start, end);
                start = dataArray.get(i);
            }
        }

        for (i = 0; i < startPlace + 1; i++)
        {
            if (checkAngleChange(dataArray.get(i).getAngle()))
            {
                end = dataArray.get(i);
                makeLineLabel(start, end);
                start = dataArray.get(i);
            }
        }

    }

    public double calculateArea()
    {
        double sum = 0;
        double radians;
        double height;
        double base;
        for (int i = 0; i < dataArray.size()-1; i++)
        {
            radians = Math.abs(dataArray.get(i).getRadians() - dataArray.get(i+1).getRadians());
            height = Math.abs(dataArray.get(i).getDistance() * Math.sin(radians));
            base = dataArray.get(i+1).getDistance();
            sum += (height * base) / 2.0;
        }

        radians = Math.abs(dataArray.get(dataArray.size()-1).getRadians() - dataArray.get(0).getRadians());
        height = Math.abs(dataArray.get(dataArray.size()-1).getDistance() * Math.sin(radians));
        base = dataArray.get(0).getDistance();

        sum += (height * base) / 2.0;

        return Math.round(sum * 100) / 100.0;
    }

    public double calculatePerimeter()
    {
        double sum = 0;
        for (int i = 0; i < dataArray.size()-1; i++)
        {
            sum += distanceFormula(dataArray.get(i), dataArray.get(i+1));
        }
        sum += distanceFormula(dataArray.get(dataArray.size()-1), dataArray.get(0));

        return Math.round(sum * 100) / 100.0;
    }

    public boolean checkAngleChange(double angle)
    {
        return ((angle > 180 + ANGLETHRESHOLD) || (angle < 180 - ANGLETHRESHOLD)) && ((angle > ANGLETHRESHOLD) || (angle < -1 * ANGLETHRESHOLD)) && ((angle > 360 + ANGLETHRESHOLD) || (angle < 360 - ANGLETHRESHOLD));
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        PlotDots plot = new PlotDots();
        plot.setVisible(true);
    }
}
