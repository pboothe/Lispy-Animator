import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

abstract public class Expression
{
    Expression subs[];
    String name;

    public Expression(String name, Expression subs[]) 
    { 
        this.name = name;
        if (subs == null)
            this.subs = new Expression[] {};
        else
            this.subs = subs; 
    }

    abstract public Expression replace(String symbol, Expression e, Lisp l);
    abstract public Expression evaluate(Lisp l);

    abstract public Expression dup();

    private final static int max(int a, int b) { return (a > b) ? a : b; }

    boolean widthset = false;
    int width;
    public void setWidth(int width) { widthset = true; this.width = width; }
    public void naturalWidth() { widthset = false; }

    public int getWidth() 
    { 
        if (widthset) return width;

        int width = 0;
        for (Expression e : subs)
            width += e.getWidth();

        return max(width, WIDTH) + SPACING;
    }

    public int getHeight()
    {
        int height = HEIGHT;
        for (Expression e : subs)
            height = max(e.getHeight(), height);

        return height;
    }

    public int getOffset()
    {
        if (subs.length == 0) return 0;
        else if ((subs.length % 2) == 0) {
            int offset = 0;
            for (int i = 0; i < subs.length/2; i++)
                offset += subs[i].getWidth() + SPACING;
            return offset - SPACING/2 - WIDTH/2;
        } else {
            int offset = 0;
            for (int i = 0; i < subs.length/2; i++)
                offset += subs[i].getWidth() + SPACING;
            return offset + subs[subs.length/2].getWidth()/2 - WIDTH/2;
        }
    }

    public int getCenter() { return WIDTH/2; }

    public void draw(Graphics2D g2d, double x, double y)
    {
        drawRounded(g2d, x, y, name);
    }

    public static int WIDTH = 40;
    public static int HEIGHT = 20;
    public static int SPACING = 10;

    public void drawRounded(Graphics2D g2d, double x, double y, String s)
    {
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(x,
                y, 
                WIDTH, 
                HEIGHT, 
                8, 
                8);

        g2d.setColor(Color.white);
        g2d.fill(rect);
        g2d.setColor(Color.black);
        g2d.draw(rect);
        g2d.drawString(s, 
                (float)(x + getCenter() - 3*s.length()), 
                (float)(y + 3*HEIGHT/4));
    }
}
