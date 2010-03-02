package main;

import graphics.TreeDisplay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;


public class LispFreeWindow extends JFrame {

  Tree tree;
  protected TreeDisplay treeDisplay = new TreeDisplay();
  JSplitPane maininput, output;
  protected JTextArea outputtext = new JTextArea(){{setEditable(false);}};
  protected BottomInput bottomInput = new BottomInput();
  
  public LispFreeWindow(){
    this("Lispy Animator");
  }
  
  protected LispFreeWindow(String title)
  {
    super(title);

    maininput = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    output =  new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    maininput.setDividerLocation(400);
    output.setDividerLocation(200);
    getContentPane().add(maininput);
    output.add(treeDisplay);
    output.add(new JScrollPane(outputtext));
    maininput.add(output);
    maininput.add(new JScrollPane(bottomInput));

    bottomInput.setPlayButtonAction(new ActionListener(){
      public void actionPerformed(ActionEvent e){ step(); }
    });

    bottomInput.setChangeAction(new ActionListener(){
      public void actionPerformed(ActionEvent e){ 
          outputtext.setText(bottomInput.getText().replace('\n', ' '));
          startLayout(); 
      }
    });

    pack();

    tree = getStartingTree();
    treeDisplay.setTree(tree);
  }
  
  protected Tree getStartingTree(){
    return new Tree("hello", Arrays.asList(new Tree[] { new Tree("there", null), new Tree("you", null) }));
  }

  synchronized private void startLayout()
  {
  }

  Tree newkid = null;
  
  protected void step()
  {
    if (outputtext.getText() == null || outputtext.getText().trim().equals("")) {
        outputtext.setText(bottomInput.getText().replace('\n', ' '));
    }
    
    startLayout();
    if (newkid == null)
    	tree.addChild(newkid = new Tree("newkid", null));
    else {
    	tree.removeChildren();
    	newkid = null;
    }
  }


  public static void main(String args[])
  {
    LispFreeWindow m = new LispFreeWindow();
    m.setSize(800, 600);
    m.setVisible(true);
    m.setDefaultCloseOperation(EXIT_ON_CLOSE);
    m.setLocationRelativeTo(null); //Center the window
    
  }
}
