package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.*;
import main.Tree;

public class TreeDisplay extends Canvas{

    private static final Color BACKGROUND = Color.decode("0xAAffff");
    private static final int PADDING = 20;
    private static final int ROUNDING = 20;
    private static final int DISTANCE = 50;
    private static final double DEG2RAD = (2 * Math.PI)/ 360;

    private Map<Tree, Node> positions;
    private Vector<Vector<Node>> layers;

    private Tree tree;
    private String message = "Enter some code to run!";
    private boolean treechanged = false;
    private boolean laidout = false;

    public TreeDisplay()
    {
        super();
    }

    public void setTree(Tree tree)
    {
        this.tree = tree;
        treechanged = true;
        message = null;
    }

    public void setMessage(String message) 
    {
        this.message = message;
    }

    private void layout(Tree t, double x, double y) 
    {
        positions = new HashMap<Tree,Node>();
        layers = new Vector<Vector<Node>>();
        layers.add(new Vector<Node>());

        Queue<Tree> curr_layer = new LinkedList<Tree>();
        curr_layer.add(t);

        int layer = 0;
        Queue<Tree> next_layer = new LinkedList<Tree>();

        double xpos = x;

        while (!curr_layer.isEmpty() || !next_layer.isEmpty()) {
            if (curr_layer.isEmpty()) {
                layer += 1;
                curr_layer = next_layer;
                next_layer = new LinkedList<Tree>();
                layers.add(new Vector<Node>());
                xpos = x;
            }

            t = curr_layer.remove();
            Node n = new Node(t, xpos, (layer+1)*50);
            xpos += 40;

            layers.get(layer).add(n);
            positions.put(t, n);

            for (Tree kid : t.getKids()) {
                next_layer.add(kid);
            }
        }

        laidout = true;
    }

    @Override
    public void paint(Graphics g1)
    {
        synchronized (this) {
            Graphics2D g = graphicsPrep(g1);
            if (message != null){
                g.drawString(message, getWidth()/2, getHeight()/2);
                return;
            } 

            if (treechanged) {
                treechanged = false;
                layout(tree, getWidth()/2, PADDING);
            }

            for (Node n : positions.values()) {
                n.draw(g);
            }


            for (Node n : positions.values()) {
                for (Tree kid : n.data.getKids()) {
                    Node knode = positions.get(kid);
                    g.drawLine((int)n.x, (int)n.y, (int)knode.x, (int)knode.y);
                }
            }
        }
    }

    public double adjust()
    {
        synchronized (this) {
            if (!laidout) return -1;

            double total = 0;

            for (Node n : positions.values())
                n.fx = 0;

            // Magnets
            for (int i = 1; i < layers.size(); i++) {
                Vector<Node> layer = layers.get(i);

                for (int j = 0; j < layer.size(); j++) {
                    if (j > 0) {
                        double d = layer.get(j).x - layer.get(j-1).x;
                        layer.get(j).fx += 1000.0 / (d*d);
                    }

                    if (j < layer.size()-1) {
                        double d = layer.get(j+1).x - layer.get(j).x;
                        layer.get(j).fx -= 1000.0 / (d*d);
                    }
                }
            }

            // Springs
            for (Node n : positions.values()) {
                for (Tree kidt : n.data.getKids()) {
                    Node kid = positions.get(kidt);

                    // F = -k*d
                    double dx = n.x - kid.x;
                    double dy = n.y - kid.y;

                    double d = Math.sqrt(dx * dx + dy * dy);
                    double F = .01 * d;
                    System.out.println("F=" + F);
                    kid.fx += F * dx / d;
                }
            }

            for (Node n : positions.values()) {
                n.vx = n.vx*.9 + 100*n.fx;
                n.x += .01*n.vx;
                total += Math.abs(100*n.fx);

                System.out.println("" + n.data + " " + n.x);
                if (n.x < 0) { n.x = 0; }
            }

            return total;
        }
    }

    private Graphics2D graphicsPrep(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BACKGROUND);
        g2.fillRect(0, 0, getWidth(), getHeight());
        return g2;
    }
}
