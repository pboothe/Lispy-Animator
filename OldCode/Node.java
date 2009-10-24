import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.util.*;

public class Node extends GraphObject
{
    double x,y;
    double width, height;

    Set<Link> links = Collections.synchronizedSet(new HashSet<Link>());
    Set<Node> neighbors = Collections.synchronizedSet(new HashSet<Node>());

    String text;

    public Node(String text, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public void draw(Graphics2D g)
    {
        Font font = Font.getFont("Helvetica");
        FontRenderContext frc = g.getFontRenderContext();
        TextLayout layout = new TextLayout(text, font, frc);

        Rectangle2D bounds = layout.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();


        bounds.setRect(x- width/2 - 3, y - height/2 - 3, width+6, height+6);
        g.setColor(new Color(1f,1f,1f,getAlpha()));
        g.fill(bounds);
        g.setColor(new Color(0f,0f,0f,getAlpha()));
        g.draw(bounds);
        layout.draw(g, (float)(x - width/2), (float)(y-height/2));
    }

    public void addNeighbor(Node neighbor)
    {
        synchronized (neighbors) {
            if (neighbors.contains(neighbor)) return;

            neighbors.add(neighbor);
            neighbor.neighbors.add(this);

            Link l = new Link(this, neighbor);
            links.add(l);
            neighbor.links.add(l);
        }
    }
}
