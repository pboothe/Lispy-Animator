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
import main.Tree;

public class TreeDisplay extends Canvas{

  private static final Color BACKGROUND = Color.decode("0xAAffff");
  private static final int PADDING = 5;
  private static final int ROUNDING = 20;
  private static final int DISTANCE = 50;
  private static final double DEG2RAD = (2 * Math.PI)/ 360;


  private Tree tree;
  private String message = "Enter some code to run!";

  public TreeDisplay(){
    super();
  }

  public void setTree(Tree tree){
    this.tree = tree;
    message = null;
  }
   public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public void paint(Graphics g1){
    Graphics2D g = graphicsPrep(g1);
    if (message != null){
       drawMessage(g);
    }else{
      drawTree(tree,getWidth()/2, getHeight()/2, g);
    }
  }

  private Graphics2D graphicsPrep(Graphics g){
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(BACKGROUND);
    g2.fillRect(0, 0, getWidth(), getHeight());
    return g2;
  }

  private void drawMessage(Graphics2D g){
    //Draw the message in the center of the graph area
    drawNode(message, getWidth()/2,  getHeight()/2, g);
  }

  //Draws a Node with the given text centering around the x,y coordinates
  private void drawNode(String text, int x, int y, Graphics2D g){
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
    x -= (int)bounds.getWidth()/2;
    y -= (int)bounds.getHeight()/2;
    g.drawString(text,x,y);
    g.drawRoundRect(x-PADDING,
                    y+((int)bounds.getY())-PADDING,
                    (int)bounds.getWidth() + 2*PADDING,
                    (int)bounds.getHeight() + 2*PADDING,
                    ROUNDING, ROUNDING);
  }

  private void drawTree(Tree tree, int x, int y, Graphics2D g){
    if (tree.getData() != null){
      drawNode(tree.getData(), x, y, g);
    }else{
      Tree head = tree.getKids().firstElement();
      tree.getKids().remove(0);
      drawNode(head.getData(), x, y, g);
      if (tree.getKids().size() == 1){
        drawTree(tree.getKids().get(0), x, y + DISTANCE, g);
      }else{
        int children = tree.getKids().size();
        double division = 180/children;
        double mid = 0;
        if (children % 2 == 1 ){
          mid = ((children/2) + 1) * division;
        }else{
          mid = ((division*children)/2) + (division/2);
        }
        double offset = 270 - mid;
        //angles = (i*division)+offset
        for (int i = 0; i < children; i++) {
          double angle = (((i+1)*division) + offset);
          int xoffset = (int) (DISTANCE * Math.cos(angle*DEG2RAD));
          int yoffset = (int) (DISTANCE * Math.sin(angle*DEG2RAD));
          drawTree(tree.getKids().get(i),x + xoffset, y - yoffset, g);
        }
      }

    }
  }

}
