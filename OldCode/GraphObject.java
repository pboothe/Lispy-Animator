import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public abstract class GraphObject implements Comparable
{
    public abstract void draw(Graphics2D g2d);

    int priority = 10;
    public int getPriority() { return priority; } 
    public void setPriority(int p) { priority = p; } 

    float alpha = 1.0f;
    public void setAlpha(float alpha) { this.alpha = alpha; }
    public float getAlpha() { return alpha; }

    public int compareTo(Object o)
    {
        if (o instanceof GraphObject)
            return getPriority() - ((GraphObject)o).getPriority();
        else
            return hashCode() - o.hashCode();
    }
}
