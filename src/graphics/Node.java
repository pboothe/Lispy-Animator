package graphics;

import java.awt.*;
import java.awt.geom.*;
import main.*;

public class Node {
    private static final int PADDING = 5;
    private static final int ROUNDING = 20;

    private static final Color BACKGROUND = Color.WHITE;
    private static final Color TEXT = Color.BLACK;
    private static final Color BORDER = Color.BLACK;
    private static final Stroke STROKE = new BasicStroke(2);
    
    //Taken from: http://www.3rd-evolution.de/tkrammer/docs/java_font_size.html
    //To ensure that we got the same font size regardless of OS/DPI/Resolution
    private static final int FONTSIZE = (int)Math.round(12.0 * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0);
    private static final Font FONT = new Font("Arial", Font.BOLD, FONTSIZE);

    public static void drawLine(Node start, Node end, Graphics2D g){
        double start_dy = (start.getBounds(g).getHeight()/2) + PADDING;

        double startx = start.x;
        if (start.getBounds(g).getWidth() >  10 ){//Found expeirementally
            startx += (end.x - start.x) * (start_dy/(end.y-start.y));
        }
        double starty = start.y + start_dy;

        double end_dy = (end.getBounds(g).getHeight()/2) + PADDING;
        double endx = end.x;
        if (end.getBounds(g).getWidth() > 10){
            endx += (start.x - end.x)*(end_dy/(end.y - start.y)); //We want dy to be positive
        }
        double endy = end.y - end_dy;

        g.drawLine( (int)startx, (int)starty, (int)endx, (int)endy );
    }

    double x, y;
    double fx;
    double vx=0;
    Tree data;

    public Node(Tree data, double x, double y)
    {
        this.data = data;
        this.x = x;
        this.y = y;
    }

    void draw(Graphics2D g)
    {
        g.setFont(FONT);

        Rectangle2D bounds = getBounds(g);

        g.setStroke(STROKE);
        g.setColor(BACKGROUND);
        g.fillRoundRect((int)(x - (bounds.getWidth()/2)  - PADDING ),
                   (int)(y - (bounds.getHeight()/2) - PADDING ),
                   (int)bounds.getWidth()  + 2*PADDING,
                   (int)bounds.getHeight() + 2*PADDING,
                   ROUNDING, ROUNDING);

        g.setColor(BORDER);
        g.drawRoundRect((int)(x - (bounds.getWidth()/2)  - PADDING ),
                   (int)(y - (bounds.getHeight()/2) - PADDING ),
                   (int)bounds.getWidth()  + 2*PADDING,
                   (int)bounds.getHeight() + 2*PADDING,
                   ROUNDING, ROUNDING);

        g.setColor(TEXT);
        g.drawString(data.getNodeName(), (int)(x - bounds.getWidth()/2),
                                         (int)(y + bounds.getHeight()/2) );

        
    }

    public Rectangle2D getBounds(Graphics g){
        FontMetrics metrics = g.getFontMetrics(FONT);
        return metrics.getStringBounds(data.getNodeName(), g);
    }
}
