package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;

import main.Tree;
import main.TreeChangeListener;

public class TreeDisplay extends JComponent implements TreeChangeListener{

    private static final Color BACKGROUND = Color.decode("0xAAffff");
    private static final int PADDING = 20;

    protected Map<Tree, Node> positions;
    protected Vector<Vector<Node>> layers;

    //This is probably over kill, Dr, Boothe gets to decide.
    private BlockingQueue<Animation> animations = new LinkedBlockingQueue<Animation>();

    private Tree tree;
    private String message = "Enter some code to run!";
    private boolean treechanged = false;
    private boolean laidout = false;

    public TreeDisplay(boolean shouldgo) 
    {
    	super();
    	if (shouldgo) start();
    }
    
    public TreeDisplay()
    {
        this(true);
    }

    public void setTree(Tree tree)
    {
        this.tree = tree;
        treechanged = true;
        message = null;
        laidout = false;
    }

    public void setMessage(String message) 
    {
        this.message = message;
    }

    public void start()
    {
        new Thread() {
            public void run()
            {
                while (true) {
                    adjust();
                    repaint();
                    try { Thread.sleep(1); } catch (InterruptedException ie) {}

                    try {
                        Animation a = animations.poll(1, TimeUnit.MICROSECONDS);
                        if (a != null) a.animate();
                    } catch (InterruptedException e) {}
                }
            }
        }.start();
    }

    protected void layout(Tree t, double x, double y) 
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
	    t.addTreeChangeListener(this);
            Node n = new Node(t, xpos, (layer+1)*50, cachedGraphics);
            xpos += n.width + PADDING;

            layers.get(layer).add(n);
            positions.put(t, n);

            for (Tree kid : t.getChildren()) {
                next_layer.add(kid);
            }
        }

        laidout = true;
    }

    @Override
    public void paint(Graphics g)
    {
        synchronized (this) {
            Graphics2D g2 = graphicsPrep(g);
            if (message != null){
                g.drawString(message, 0, getHeight()/2);
                return;
            } 

            if (treechanged) {
                treechanged = false;
                layout(tree, 0, PADDING);
            }

            for (Node n : positions.values()) {
                for (Tree kid : n.data.getChildren()) {
                    Node knode = positions.get(kid);
                    if (knode == null) continue;
                    
                    knode.drawLine(n, knode, g2);
                }
            }

            for (Node n : positions.values()) {
                n.draw(g2);
            }


            g2.dispose();
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
                        Node r = layer.get(j);
                        Node l = layer.get(j-1);
                        double d = (l.x + l.width/2) - (r.x - r.width/2);
                        layer.get(j).fx += 1000.0 / (d*d);
                    }

                    if (j < layer.size()-1) {
                        Node l = layer.get(j);
                        Node r = layer.get(j+1);
                        double d = (l.x + l.width/2) - (r.x - r.width/2);
                        layer.get(j).fx -= 1000.0 / (d*d);
                    }
                }
            }

            // Springs
            for (Node n : positions.values()) {
                for (Tree kidt : n.data.getChildren()) {
                    Node kid = positions.get(kidt);
                    if (kid == null) continue;
                    // F = -k*d
                    double dx = n.x - kid.x;
                    double dy = n.y - kid.y;

                    double d = Math.sqrt(dx * dx + dy * dy);
                    double F = .01 * d;
                    kid.fx += F * dx / d;
                }
            }

            for (Node n : positions.values()) {
                n.vx = n.vx*.7 + 100*n.fx;
                n.x += .01*n.vx;
                total += Math.abs(100*n.fx);
            }

            // Now correct the layers
            for (Vector<Node> layer : layers) {
                for (int i = 1; i < layer.size(); i++) {
                    Node l = layer.get(i-1);
                    Node n = layer.get(i);
                    if (n.x - n.width/2 < l.x + l.width/2 + PADDING) {
                        n.x = n.width/2 + l.x + l.width/2 + PADDING;
                    }
                }
            }

            return total;
        }
    }

    Graphics cachedGraphics;

    private Graphics2D graphicsPrep(Graphics g1)
    {
        cachedGraphics = g1;
        Graphics2D g = (Graphics2D)g1;
        int depth = tree != null ? tree.depth() : 1;

        double totalpixels = depth * 50 + 40;
        double available = getHeight();
        double scale = available / totalpixels;

        g.scale(scale, scale);
        g.translate(getWidth()/2/scale,0);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        return g;
    }

    /**
     * ADD YOUR ANIMATION ADDING TO QUEUE CODE HERE!!!
     */
    public void kidAdded(final Tree parent, final Tree addedNode){
        dataChanged(parent);
    }

    public void childrenRemoved(final Tree parent){
        dataChanged(parent);
    }

    public void dataChanged(final Tree parent){
    	final Tree superTree = tree;

    	animations.offer(new Animation() {
                    void animate()
                    {
                        // Make the new layout
                        TreeDisplay td = new TreeDisplay(false);
                        td.cachedGraphics = cachedGraphics;
                        td.setTree(superTree);
                        td.layout(superTree, 0, PADDING);
                        
                        for(int i = 0; i < 1000; i++)
                            if (td.adjust() < 10) break;
                       
                        // Fade out all of the removed nodes
                        LinkedList<Tree> removal = new LinkedList<Tree>();
                        for (Tree t : positions.keySet()) {
                            if (!td.positions.containsKey(t))
                                removal.add(t);
                        }

                        if (removal.size() > 0) {
                            for (int i = 0; i < 10; i++) {
                                for (Tree t : removal) {
                                    positions.get(t).setOpacity(1 - i*.1);
                                }
                                repaint();
                                try { Thread.sleep(10); } catch (InterruptedException ie) {}
                            }
                        }

                        // Move stuff from start to end
                        Map<Tree,Node> starts = new HashMap<Tree,Node>();
                        for (Tree t : positions.keySet()) {
                        	Node n = positions.get(t);
                        	starts.put(t, new Node(n.data, n.x, n.y, cachedGraphics));
                        }
                        
                        
                        for (int i = 0; i < 20; i++) {
                        	for (Tree t : positions.keySet()) {
                        		if (starts.get(t) == null) continue;
                        		if (td.positions.get(t) == null) continue;
                        		if (positions.get(t) == null) continue;
                        		
                        		positions.get(t).x = (td.positions.get(t).x - starts.get(t).x) * i / 20.0 + starts.get(t).x;
                        	}
                        	repaint();
                        	try { Thread.sleep(10); } catch (InterruptedException ie) {}
                        }
                     
                        //Fade in the new stuff
                        LinkedList<Tree> added = new LinkedList<Tree>();
                        for (Tree t : td.positions.keySet()) {
                            if (!positions.containsKey(t))
                                added.add(t);
                        }

                        if (added.size() > 0) {
                            for (Tree t : added) {
                                td.positions.get(t).setOpacity(0);
                            }
                        }
                        
                        positions = td.positions;
                        layers = td.layers;
                        
                        if (added.size() > 0) {
                        	for (int i = 1; i <= 10; i++) {
                        		for (Tree t : added) {
                        			positions.get(t).setOpacity(i*.1);
                        		}
                        	
                        		repaint();
                        		try { Thread.sleep(10); } catch (InterruptedException ie) {}
                        	}
                        }
                        
                        repaint();
                    }
                });
    }
}
