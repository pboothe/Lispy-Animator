package main;

import java.util.Arrays;

import javax.swing.JOptionPane;


public class BinaryTreeInsertion extends LispFreeWindow{

  public BinaryTreeInsertion(){
    super("Binary Tree Insertion");
  }
  
  protected Tree getStartingTree(){
    return new Tree("", null);
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
    
    //Binary trees always have 2 children, even in our case
    Tree tree2Insert = new Tree(String.valueOf(num), Arrays.asList(new Tree[]{new Tree("", null), new Tree("", null)}));
    
    //Check if the head is empty, if it is, then just make this the head
    if (tree.getData() == null || tree.getData().isEmpty()){
      tree = tree2Insert;
      treeDisplay.setTree(tree2Insert);
      return;
    }
    
    final int LEFT = 0;
    final int RIGHT = 1;
    
    Tree current = tree;
    while (true){
      if (num < Integer.parseInt(current.getData())){
        Tree left = current.getChild(LEFT);
        if (left.getData().isEmpty()){
          left.setData(num, false);
          left.addChild(new Tree("", null), false);
          left.addChild(new Tree("", null));
          return;
        }else{
          current = left;
        }
      }else{
        Tree right = current.getChild(RIGHT);
        if (right.getData().isEmpty()){
          right.setData(num, false);
          right.addChild(new Tree("", null), false);
          right.addChild(new Tree("", null));
          return;
        }else{
          current = right;
        }
      }
    }
  }
  

  /**
   * @param args
   */
  public static void main(String[] args) {
    BinaryTreeInsertion m = new BinaryTreeInsertion();
    m.setSize(800, 600);
    m.setVisible(true);
    m.setDefaultCloseOperation(EXIT_ON_CLOSE);
    m.setLocationRelativeTo(null); //Center the window
  }

}
