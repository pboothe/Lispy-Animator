import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class Lisp extends Canvas
{
    volatile Expression expression;
    BufferStrategy bs;

    public Lisp(Expression e)
    {
        expression = e;
    }

    volatile Hashtable<Expression, Point2D> positions;
    volatile Hashtable<Expression, Collection<Expression>> links;

    public void redraw()
    {
        double scale = ((double)getWidth()) / ((double)expression.getWidth()+20);
        Graphics2D g2 = (Graphics2D)bs.getDrawGraphics();
        g2.clearRect(0, 0, getWidth(), getHeight());
        g2.scale(scale, scale);

        Hashtable<Expression, Collection<Expression>> l = (Hashtable<Expression, Collection<Expression>>)links.clone();
        Hashtable<Expression, Point2D> p = (Hashtable<Expression, Point2D>)positions.clone();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.black);

        for (Expression from : l.keySet())
            for (Expression to : l.get(from)) {
                Point2D fr = p.get(from);
                Point2D tr = p.get(to);
                if (fr != null && tr != null)
                    g2.draw(new Line2D.Double(fr.getX() + from.getCenter(),
                            fr.getY() + from.HEIGHT/2.0,
                            tr.getX() + to.getCenter(),
                            tr.getY() + from.HEIGHT/2.0));
            }

        for (Expression e : p.keySet()) {
            Point2D r = p.get(e);
            if (r == null || e == null) continue;
            e.draw(g2, r.getX(), r.getY());
        }

        g2.dispose();
        bs.show();
    }

    public void layout(Expression e, 
            Hashtable<Expression, Point2D> p, 
            Hashtable<Expression, Collection<Expression>> l,
            double x, double y)
    {
        Expression kids[] = e.subs;
        
        p.put(e, new Point2D.Double(x + e.getOffset(), y));
        l.put(e, Arrays.asList(kids));

        double pos = x;
        for (Expression kid : kids) {
            layout(kid, p, l, pos, y + Expression.SPACING + Expression.HEIGHT);
            pos += kid.getWidth() + Expression.SPACING;
        }
    }
    
    public void relayout()
    {
        Hashtable<Expression, Point2D> p = new Hashtable<Expression, Point2D>();
        Hashtable<Expression, Collection<Expression>> l = 
                new Hashtable<Expression, Collection<Expression>>();

        layout(expression, p, l, 10, 10);

        positions = p;
        links = l;
    }

    public void longpause()
    {
        try { Thread.sleep(1000); } catch (InterruptedException ie) {}
    }

    public void pause()
    {
        try { Thread.sleep(10); } catch (InterruptedException ie) {}
    }

    public void move(Expression exp, Expression start, Expression end)
    {
        move(exp, positions.get(start), positions.get(end));
    }

    public void moveScrub(Expression exp, Expression start, Expression end)
    {
        Point2D s = positions.get(start);
        Point2D e = positions.get(end);

        scrub(start);
        move(exp, s, e);
    }
    
    public void move(Expression start, Expression end) 
    {
        Point2D ep = positions.get(end);
        move(start, ep);
    }

    public void moveSideways(Expression e, Expression start, int dist)
    {
        Point2D sp = positions.get(start);
        if (dist != 0) {
            move(e, sp, new Point2D.Double(sp.getX() + dist, sp.getY()));
        }
    }

    public void move(Expression start, Point2D ep)
    {
        Point2D sp = positions.get(start);
        move(start, sp, ep);
    }

    public void move(Expression start, Point2D sp, Point2D ep)
    {
        double dx = ep.getX() - sp.getX();
        double dy = ep.getY() - sp.getY();

        double dist = Math.sqrt(dx*dx + dy*dy);

        double fx = dx = dx/dist;
        double fy = dy = dy/dist;

        layout(start, positions, links, 
            sp.getX() - start.getOffset(), sp.getY());

        double distcovered = 0;
        for (int i = 0; distcovered < dist/2 + 1; i ++)
        {
            Point2D curpos = positions.get(start);
            layout(start, positions, links, 
                    fx*distcovered + sp.getX() - start.getOffset(), 
                    fy*distcovered + sp.getY());
            redraw();
            pause();
            distcovered += Math.sqrt(dx*dx + dy*dy);
            dx += fx;
            dy += fy;
        }

        for (int i = 0; distcovered < dist; i ++)
        {
            layout(start, positions, links, 
                    fx*distcovered + sp.getX() - start.getOffset(), 
                    fy*distcovered + sp.getY());
            redraw();
            pause();
            distcovered += Math.sqrt(dx*dx + dy*dy);
            dx -= fx;
            dy -= fy;
        }

        layout(start, positions, links, ep.getX()-start.getOffset(), ep.getY());
        redraw();
        pause();
    }

    public void scrub(Expression e)
    {
        if (positions.containsKey(e)) { positions.remove(e); }
        if (links.containsKey(e)) { links.remove(e); }

        for (Expression sub : e.subs)
            scrub(sub);
    }

    public void shrink(Expression fr, Expression to)
    {
        while (fr.getWidth() > to.getWidth()) {
            fr.setWidth(fr.getWidth() - 2);
            relayout();

            redraw();
            pause();
        }

        fr.setWidth(to.getWidth());
        relayout();
        redraw();
        pause();
    }

    public void grow(Expression fr, Expression to)
    {
        while (fr.getWidth() < to.getWidth()) {
            fr.setWidth(fr.getWidth() + 20);
            relayout();
            redraw();
            pause();
        }

        fr.setWidth(to.getWidth());
        relayout();
        redraw();
        pause();
    }

    public void resize(Expression fr, Expression to)
    {
        if (fr.getWidth() < to.getWidth()) 
            grow(fr, to);
        else 
            shrink(fr, to);
    }

    static int findmidpoint(String s, int start)
    {
        int parcount = 0;
        for (int i = start; i < s.length()-1; i++) {
            if (s.charAt(i) == '(')
                parcount++;
            else if (s.charAt(i) == ')')
                parcount--;
            else if (parcount == 0 && s.charAt(i) == ' ') {
                return i;
            }
        }

        throw new RuntimeException("Could not find the midpoint of '" + s + "'");
    }

    public void animate()
    {
        Expression prev = null;
        while (!expression.toString().equals("" + prev)) {

            prev = expression;
            expression = expression.evaluate(this);
            Point2D p = positions.get(prev);
            scrub(prev);

            move(expression, 
                p,
                new Point2D.Double(expression.getOffset()+10, 10));

            relayout();
            redraw();
            
            longpause();
        }
        System.out.println(expression);
    }

    public static Expression parse(String s)
    {
        s = s.trim();
        if (s.startsWith("(apply ")) {
            int start = "(apply ".length();
            int midpoint = findmidpoint(s, start);
            return new Apply(parse(s.substring(start, midpoint)), 
                    parse(s.substring(midpoint, s.length()-1)));
        } else if (s.startsWith("(* ")) {
            int start = "(* ".length();
            int midpoint = findmidpoint(s, start);
            return new Times(parse(s.substring(start, midpoint)), 
                    parse(s.substring(midpoint, s.length()-1)));
        } else if (s.startsWith("(+ ")) {
            int start = "(+ ".length();
            int midpoint = findmidpoint(s, start);
            return new Plus(parse(s.substring(start, midpoint)), 
                    parse(s.substring(midpoint, s.length()-1)));
        } else if (s.startsWith("(< ")) {
            int start = "(< ".length();
            int midpoint = findmidpoint(s, start);
            return new LessThan(parse(s.substring(start, midpoint)), 
                    parse(s.substring(midpoint, s.length()-1)));
        } else if (s.startsWith("(fun ")) {
            int start = "(fun ".length();
            int midpoint1 = findmidpoint(s, start);
            int midpoint2 = findmidpoint(s, midpoint1+1);
            return new Function(s.substring(start, midpoint1).trim(), 
                    s.substring(midpoint1, midpoint2).trim(),
                    parse(s.substring(midpoint2, s.length()-1)));
        } else if (s.startsWith("(if ")) {
            int start = "(if ".length();
            int midpoint1 = findmidpoint(s, start);
            int midpoint2 = findmidpoint(s, midpoint1+1);
            return new If(parse(s.substring(start, midpoint1)), 
                    parse(s.substring(midpoint1, midpoint2)),
                    parse(s.substring(midpoint2, s.length()-1)));
        } else if (s.startsWith("'")) {
            return new Quote(parse(s.substring(1)));
        } else {
            try {
                return new Number(Double.parseDouble(s));
            } catch (NumberFormatException nfe) {
                if (s.equals("true"))
                    return new Bool(true);
                else if (s.equals("false"))
                    return new Bool(false);
                else
                    return new Variable(s);
            }
        }
    }

    public static void main(String args[]) throws AWTException
    {
        Expression e = parse("(apply (fun fact n (if (< n 2) 1 (* n (apply fact (+ n -1))))) 6)");
        //Expression e = parse("(apply (fun fib n (if (< n 2) n (+ (apply fib (+ n -1)) (apply fib (+ n -2))))) 4)");

        //Expression e = parse("(apply (fun f n (if (< n 1) (apply f 1) (apply f 0))) 1)");
        
        //Expression e = parse("(apply (fun f x x) 5)");

        final Frame frame = new Frame("Watch your program execute...");

        final Lisp l = new Lisp(e);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setDoubleBuffered(true);

        frame.add(l);
        l.setSize(900, 700);
        frame.pack();

        l.createBufferStrategy(2);
        l.bs = l.getBufferStrategy();

        l.relayout();
        l.redraw();

        frame.setVisible(true);

        l.animate();
    }
}
