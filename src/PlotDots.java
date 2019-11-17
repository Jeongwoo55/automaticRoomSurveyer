import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PlotDots extends JFrame
{
    static int THRESHOLD = 30;
    static int screenHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    static int screenWidth = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());

    private JPanel window;
    private ArrayList<dataPoints> dataArray = new ArrayList<>();

    public PlotDots() throws FileNotFoundException
    {
        makeTheWindow();
        readData();
        convertDataToDots();
//        makeDots();
        testAngles();
    }

    public void makeTheWindow()
    {
        window = new JPanel();
        getContentPane().add(window);
        window.setLayout(null);
        window.setBackground(Color.white);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Plot of Room");
        setSize(screenWidth / 2, screenHeight /2);
        setLocation(screenWidth /4, screenHeight / 4);
        setResizable(false);
    }

    public void readData() throws FileNotFoundException
    {
        Scanner file = new Scanner(new File("data.dat"));
        int i = 0;

        while(file.hasNextDouble())
        {
            double tempData = file.nextDouble();
            if (i == 0)
            {
                dataArray.add(new dataPoints(tempData));
            }
            else
            {
                if (Math.abs(tempData - dataArray.get(dataArray.size()-1).getDistance()) < THRESHOLD)
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

//    public void makeDots()
//    {
//        JPanel p = new JPanel() {
//            @Override
//            public void paintComponent(Graphics g) {
//                Graphics2D g2 = (Graphics2D) g;
//                Shape line = new Line2D.Double(3, 3, 303, 303);
//                g2.draw(line);
//            }
//        };
//        setTitle("My Shapes");
//        p.setVisible(true);
//        this.getContentPane().add(p);
//    }

    public void paint(Graphics gp)
    {
        super.paint(gp);
        Graphics2D graphics = (Graphics2D) gp;

        for (int i = 0; i < dataArray.size()-1; i++)
        {
            Line2D line = new Line2D.Double((screenWidth /4) + dataArray.get(i).getX(), (screenHeight /4) + dataArray.get(i).getY(), (screenWidth /4) + dataArray.get(i+1).getX(), (screenHeight /4) + dataArray.get(i+1).getY());
            graphics.draw(line);
        }
        Line2D line = new Line2D.Double((screenWidth /4) + dataArray.get(dataArray.size()-1).getX(), (screenHeight /4) + dataArray.get(dataArray.size()-1).getY(), (screenWidth /4) + dataArray.get(0).getX(), (screenHeight /4) + dataArray.get(0).getY());
        graphics.draw(line);
    }

    public double angleCalculation(dataPoints x, dataPoints y, dataPoints z)
    {
        double a = distanceFormula(y, z);
        double b = distanceFormula(x, z);	//goal
        double c = distanceFormula(x, y);

        //changes to degrees and rounds to 2 places
        return Math.round( (Math.toDegrees(Math.acos( (Math.pow(a, 2) + Math.pow(c , 2) - Math.pow(b, 2) ) / (2 * a * c) ) ) ) * 100) / 100.0;
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
        System.out.println(angle);
        for (int i = 1; i < dataArray.size() - 1; i++)
        {
            angle = angleCalculation(dataArray.get(i-1), dataArray.get(i), dataArray.get(i+1));
            System.out.println(angle);
        }
        angle = angleCalculation(dataArray.get(dataArray.size()-2), dataArray.get(dataArray.size()-1), dataArray.get(0));
        System.out.println(angle);

    }

    public static void main(String[] args) throws FileNotFoundException
    {
        PlotDots plot = new PlotDots();
        plot.setVisible(true);
    }
}
