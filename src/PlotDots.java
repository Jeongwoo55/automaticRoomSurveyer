import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PlotDots extends JFrame
{
    private Container window;
    private ArrayList<dataPoints> dataArray = new ArrayList<>();

    public PlotDots() throws FileNotFoundException
    {
        makeTheWindow();
        readData();
        convertDataToDots();
        makeDots();
    }

    public void makeTheWindow()
    {
        window = getContentPane();
        window.setLayout(null);
        window.setBackground(Color.red);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Plot of Room");
        int screenHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        int screenWidth = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        setSize(screenWidth / 2, screenHeight /2);
        setLocation(screenWidth /4, screenHeight / 4);
        setResizable(false);
    }

    public void readData() throws FileNotFoundException
    {
        Scanner file = new Scanner(new File("data.dat"));
        while(file.hasNextDouble())
        {
            double tempData = file.nextDouble();
            if(tempData != 357)
                dataArray.add(new dataPoints(tempData));
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

    public void makeDots()
    {
        JPanel p = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Shape line = new Line2D.Double(3, 3, 303, 303);
                g2.draw(line);
            }
        };
        setTitle("My Shapes");
        p.setVisible(true);
        window.add(p);
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        PlotDots plot = new PlotDots();
        plot.setVisible(true);
    }
}
