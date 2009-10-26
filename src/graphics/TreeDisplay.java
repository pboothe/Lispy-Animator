package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import main.Tree;

public class TreeDisplay extends Canvas{

  private Tree tree;

  public TreeDisplay(){
    super();
  }

  public void setTree(Tree tree){
    this.tree = tree;
  }


  @Override
  public void paint(Graphics g){
    if (tree == null){
      //TODO: Make this a nice little message to run some lisp code
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, getWidth(), getHeight());
      //Yes I know that this isn't the center
      g.setColor(Color.BLACK);
      g.drawString("Enter some Lisp code to be run",
                    getWidth()/2,
                    getHeight()/2);
    }else{
      drawTree((Graphics2D)g);
    }
  }

  private void drawTree(Graphics2D g){
    
  }
}
