import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class DisplayCanvas extends Canvas
{
    BufferStrategy bs;
    PriorityQueue<GraphObject> drawable;

    public DisplayCanvas(GraphObject go[])
    {
        drawable = new PriorityQueue<GraphObject>(Arrays.asList(go));
    }

    public void replace(Node from, Node to)
    {
        final double EPSILON = .1;
        while (Math.abs(from.x-to.x) + Math.abs(from.y - to.y) < EPSILON)
        {
        }
    }

    public void redraw()
    {
        if (bs == null) bs = getBufferStrategy();

        Graphics2D g2 = (Graphics2D)bs.getDrawGraphics();
        g2.clearRect(0, 0, getWidth(), getHeight());

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.black);

        synchronized (drawable) {
            for (GraphObject o : drawable) {
                o.draw(g2);
            }
        }

        g2.dispose();
        bs.show();
    }
}

