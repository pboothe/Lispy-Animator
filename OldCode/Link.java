import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class Link extends GraphObject
{
    Node from, to;
    public Link(Node from, Node to)
    {
        this.from = from;
        this.to = to;

        setPriority(Math.min(from.getPriority(), to.getPriority()) - 1);
    }

    public void draw(Graphics2D g)
    {
        g.setColor(new Color(0f,0f,0f,getAlpha()));
        g.draw(new Line2D.Double(from.x, from.y, to.x, to.y));
    }
}
