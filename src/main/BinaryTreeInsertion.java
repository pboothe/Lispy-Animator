package main;

import java.util.Arrays;
import java.util.Vector;

import javax.swing.JOptionPane;


public class BinaryTreeInsertion extends LispFreeWindow{
  
  final int LEFT = 0;
  final int RIGHT = 1;
  
  public BinaryTreeInsertion(){
    super("Binary Tree Insertion");
  }
  
  protected Tree getStartingTree(){
    return new Tree("",null);
  }
  
  protected void step(){
    if (bottomInput.getText().isEmpty() == false){
      System.out.println(bottomInput.getText());
      String[] numbers = bottomInput.getText().split(",");
      for(String num : numbers){
        try{
          insertNumber(Integer.parseInt(num.trim()), tree);
        }catch(NumberFormatException nfe){
          JOptionPane.showMessageDialog(this, "Please enter a list of comma seperated numbers");
        }
      }
      bottomInput.text.setText("");
    }
  }
  
  private void insertNumber(int num, Tree t){
        
    if (t.getData().isEmpty()){
      t.FIREAWAY = false;
      t.setData(num);
      t.addChild(new Tree("", null));
      t.FIREAWAY = true;
      t.addChild(new Tree("", null));
    }else if ( num  < Integer.parseInt(t.getData())){
      insertNumber(num, t.getChild(LEFT));
      rebalance(t);
    }else{
      insertNumber(num, t.getChild(RIGHT));
      rebalance(t);
    }
  }
  
  private void rebalance(Tree root){
    int balance = balanceRatio(root);
    System.out.println("Bal = " + balance);
    if (balance == -2){ //Right side is too heavy
      if(balanceRatio(root.getChild(RIGHT)) == -1){
        leftRotation(root);
      }else{
        rightLeft(root);
      }
    }else if (balance == 2){
      if (balanceRatio(root.getChild(LEFT)) == 1){
        rightRotation(root);
      }else{
        leftRight(root);
      }
    }
  }

  private void rightRotation(Tree root){
    Tree c = root;
    Tree b = c.getChild(LEFT);
    Tree a = b.getChild(LEFT);
    
    Tree left = a;
    Tree right = new Tree(c.getData(), Arrays.asList(new Tree[]{b.getChild(RIGHT), c.getChild(RIGHT)}));
    root.setData(b.getData());
    root.removeChildren();
    root.addChild(left);
    root.addChild(right);
  }
  
  private void leftRotation(Tree root) {
    Tree a = root;
    Tree b = a.getChild(RIGHT);
    Tree c = b.getChild(RIGHT);
    
    Tree left = new Tree(a.getData(), Arrays.asList(new Tree[]{a.getChild(LEFT), b.getChild(LEFT)}));
    Tree right = c;
    root.setData(b.getData());
    root.removeChildren();
    root.addChild(left);
    root.addChild(right);
  }
  
  private void leftRight(Tree root){
    Tree c = root;
    Tree a = c.getChild(LEFT);
    Tree b = c.getChild(RIGHT);
    
    Tree left = new Tree(a.getData(), Arrays.asList(new Tree[]{a.getChild(LEFT), b.getChild(LEFT)}));
    Tree right = new Tree(c.getData(), Arrays.asList(new Tree[]{b.getChild(RIGHT), c.getChild(RIGHT)}));
    root.setData(b.getData());
    root.removeChildren();
    root.addChild(left);
    root.addChild(right);
  }
  
  private void rightLeft(Tree root){
    Tree a = root;
    Tree c = a.getChild(RIGHT);
    Tree b = c.getChild(LEFT);
    
    Tree left = new Tree(a.getData(), Arrays.asList(new Tree[]{a.getChild(LEFT), b.getChild(LEFT)}));
    Tree right = new Tree(c.getData(), Arrays.asList(new Tree[]{b.getChild(RIGHT), c.getChild(RIGHT)}));
    root.setData(b.getData());
    root.removeChildren();
    root.addChild(left);
    root.addChild(right);
  }
  
  private int balanceRatio(Tree t){
    return height(t.getChild(LEFT)) - height(t.getChild(RIGHT));
  }
  
  private int height(Tree t){
    return t.depth();
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
