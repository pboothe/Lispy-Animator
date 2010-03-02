package main;

import java.util.Vector;

import javax.swing.JOptionPane;


public class BinaryInsertion extends LispFreeWindow{

  public BinaryInsertion(){
    super("Binary Insertion");
  }
  
  protected Tree getStartingTree(){
    return new Tree();
  }
  
  protected void step(){
    if (bottomInput.getText().isEmpty() == false){
      System.out.println(bottomInput.getText());
      String[] numbers = bottomInput.getText().split(",");
      for(String num : numbers){
        try{
          insertNumber(new Integer(num.trim()));
        }catch(NumberFormatException nfe){
          JOptionPane.showMessageDialog(this, "Please enter a list of comma seperated numbers");
        }
      }
      bottomInput.text.setText("");
    }
  }
  
  private void insertNumber(int num){
    
    Tree tree2Insert = new Tree(String.valueOf(num), null);
    
    //Check if the head is empty, if it is, then just make this the head
    if (tree.getData() == null || tree.getData().isEmpty()){
      tree = tree2Insert;
      treeDisplay.setTree(tree2Insert);
      return;
    }
    
    //Replacing the head is kind of a special case
    if (num <= new Integer(tree.getData()) ) {
      tree2Insert.addChild(tree);
      tree = tree2Insert;
      treeDisplay.setTree(tree);
      repaint();
      return;
    }
    
    //Ok now do a normal binary insertion
    Tree current = tree;
    Tree parent = null; //We need to know the parent to insert the new node
    while(!current.getChildren().isEmpty()){
      parent = current;
      current = current.getChildren().firstElement();
      if (new Integer(current.getData()) > num){
        //Insert!!
        //remove children from parent and move them to tree2Insert
        for(Tree t : parent.getChildren()){
          tree2Insert.addChild(t);
        }
        parent.removeChildren();
        parent.addChild(tree2Insert);
        return;
      }
    }
    
    //If we make to here that means that we just add to the tail of the tree
    current.addChild(tree2Insert);
  }
  

  /**
   * @param args
   */
  public static void main(String[] args) {
    BinaryInsertion m = new BinaryInsertion();
    m.setSize(800, 600);
    m.setVisible(true);
    m.setDefaultCloseOperation(EXIT_ON_CLOSE);
    m.setLocationRelativeTo(null); //Center the window
  }

}
