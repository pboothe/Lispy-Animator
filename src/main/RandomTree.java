package main;

import graphics.TreeDisplay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;


public class RandomTree extends LispFreeWindow {
  boolean shouldrun;

  public RandomTree()
  {
    super("Random Tree");

    bottomInput.setPlayAllButtonAction(new ActionListener(){
      public void actionPerformed(ActionEvent e){ 
        new Thread() { 
            public void run () { 
                shouldrun = true;
                while (shouldrun) {
                    step(); 
                    try {Thread.sleep(1000); } 
                    catch (InterruptedException ie) {}
                }
            } 
        }.start();
      }
    });
  }
  
  protected Tree getStartingTree(){
    return new Tree();
  }

  Random rand = new Random();
  protected void step()
  {
     Vector<Tree> v = tree.preorder();
     Tree t = v.get(rand.nextInt(v.size()));
     t.addChild(new Tree());
  }


  public static void main(String args[])
  {
    RandomTree m = new RandomTree();
    m.setSize(800, 600);
    m.setVisible(true);
    m.setDefaultCloseOperation(EXIT_ON_CLOSE);
    m.setLocationRelativeTo(null); //Center the window
  }
}
