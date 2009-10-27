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

  private Tree tree;
  private String message = "Enter some code to run!";

  public TreeDisplay(){
    super();
  }

  public void setTree(Tree tree){
    this.tree = tree;
  }


  @Override
  public void paint(Graphics g1){
    Graphics2D g = graphicsPrep(g1);
    if (tree == null){
       drawMessage(g);
    }else{
      drawTree(g);
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
    g.setColor(Color.BLACK);

    //Taken from: http://www.3rd-evolution.de/tkrammer/docs/java_font_size.html
    //To ensure that we got the same font size regardless of OS/DPI/Resolution
    int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
    int fontSize = (int)Math.round(12.0 * screenRes / 72.0);
    Font font = new Font("Arial", Font.PLAIN, fontSize);
    g.setFont(font);

    //Lets get some font metric info
    FontMetrics metrics = g.getFontMetrics();
    Rectangle2D bounds = metrics.getStringBounds(message, g);
    int x  = (getWidth()/2)-((int)bounds.getWidth()/2);
    int y = (getHeight()/2) - ((int)bounds.getHeight()/2);
    g.drawString(message,x,y);
    System.out.println(bounds.getHeight());
    g.drawRoundRect(x-5,y-15,(int)bounds.getWidth()+10,(int)bounds.getHeight(),5,5);
  }

  public void setMessage(String message) {
    tree = null;
    this.message = message;
  }

  private void drawTree(Graphics2D g){
    setMessage(tree.toString());
    drawMessage(g);
  }

}
