package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import main.Tree;

public class TreeDisplay extends Canvas{

  private Tree tree;
  private String message = "Enter some code to be run!";

  public TreeDisplay(){
    super();
  }

  public void setTree(Tree tree){
    this.tree = tree;
  }


  @Override
  public void paint(Graphics g){
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    if (tree == null){
       drawMessage(g2);
    }else{
      drawTree((Graphics2D)g);
    }
  }

  public void drawMessage(Graphics2D g){
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
    //Yes I know that this isn't the center
    g.setColor(Color.BLACK);
    g.drawString(message,getWidth()/2,getHeight()/2);
  }

  public void setMessage(String message) {
    tree = null;
    this.message = message;
  }

   private void drawTree(Graphics2D g){
     System.out.println(tree);
  }
}
