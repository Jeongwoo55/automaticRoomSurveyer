import javax.swing.*;
import java.awt.*;

public class PlotDots extends JFrame
{
    private Container window;

    public PlotDots()
    {
        makeTheWindow();
    }

    public void makeTheWindow()
    {
        window = getContentPane();
        window.setLayout(null);
        window.setBackground(Color.white);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Plot of Room");
        int screenHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        int screenWidth = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        setSize(screenWidth / 2, screenHeight /2);
        setLocation(screenWidth /4, screenHeight / 4);
        setResizable(false);
    }

    public static void main(String[] args)
    {
        PlotDots plot = new PlotDots();
        plot.setVisible(true);
    }
}
