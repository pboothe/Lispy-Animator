package graphics;

import java.awt.*;
import java.awt.geom.*;
import main.*;

public class Node {
    double x, y;
    double fx;
    double vx=0;
    Tree data;
    String text;

    public Node(Tree data, double x, double y)
    {
        this.data = data;
        this.x = x;
        this.y = y;

        text = data.getNodeName();
    }

    private static final int PADDING = 5;
    private static final int ROUNDING = 20;

    void draw(Graphics2D g)
    {
        g.setColor(Color.BLACK);

        //Taken from: http://www.3rd-evolution.de/tkrammer/docs/java_font_size.html
        //To ensure that we got the same font size regardless of OS/DPI/Resolution
        int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
        int fontSize = (int)Math.round(12.0 * screenRes / 72.0);
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        g.setFont(font);

        //Lets get some font metric info
        FontMetrics metrics = g.getFontMetrics();
        Rectangle2D bounds = metrics.getStringBounds(text, g);
        //Use the bounds to center the text
        double tempx = x - (int)bounds.getWidth()/2;
        double tempy = y - (int)bounds.getHeight()/2;
        g.drawString(text,(int)tempx,(int)tempy);
        g.drawRoundRect((int)tempx-PADDING, (int)tempy+((int)bounds.getY())-PADDING, 
                (int)bounds.getWidth() + 2*PADDING, 
                (int)bounds.getHeight() + 2*PADDING, 
                ROUNDING, ROUNDING); 
    }
}
