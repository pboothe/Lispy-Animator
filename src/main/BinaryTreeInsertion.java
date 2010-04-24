package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JOptionPane;


public class BinaryTreeInsertion extends LispFreeWindow{
  
  final int LEFT = 0;
  final int RIGHT = 1;
  
  public BinaryTreeInsertion(){
    super("Binary Tree Insertion");

    bottomInput.setPlayAllButtonAction(new ActionListener() {
            public void actionPerformed(ActionEvent e){ 
                treeDisplay.setTree(tree = getStartingTree());
            }
        });
  }
  
  protected Tree getStartingTree(){
    return new Tree("", null);
  }
  
  protected void step(){
    if (bottomInput.getText().isEmpty() == false){
      System.out.println(bottomInput.getText());
      String[] numbers = bottomInput.getText().split(",");
      try{
          for(String num : numbers){
              insertNumber(Integer.parseInt(num.trim()), tree);
                
              //try { Thread.sleep(2000); } catch (InterruptedException ie) {}
          }

          tree.setData(tree.getData());

          bottomInput.text.setText("");
      }catch(NumberFormatException nfe){
          JOptionPane.showMessageDialog(this, "Please enter a list of comma seperated numbers");
      }
    }
  }
  
  private void insertNumber(int num, Tree t){
        
    System.out.println("adding num: " + num + " to " + t);
    if (t.getData() == null || t.getData().isEmpty()){
      t.setData(num, false);
      t.addChild(new Tree("", null), false);
      t.addChild(new Tree("", null));
    }else if ( num  < Integer.parseInt(t.getData())){
      insertNumber(num, t.getChild(LEFT));
      System.out.println("About to rebalance " + t);
      rebalance(t);
      System.out.println("done rebalancing " + t);
    }else{
      insertNumber(num, t.getChild(RIGHT));
      System.out.println("About to rebalance " + t);
      rebalance(t);
      System.out.println("done rebalancing " + t);
    }
  }
  
  private void rebalance(Tree root){
    int balance = balanceRatio(root);
    System.out.println("Bal = " + balance);
    if (balance == -2){ //Right side is too heavy
      if(balanceRatio(root.getChild(RIGHT)) == -1){
        System.out.println("leftRotation");
        leftRotation(root);
      }else{
        System.out.println("rightLeft");
        rightLeft(root);
      }
    }else if (balance == 2){
      if (balanceRatio(root.getChild(LEFT)) == 1){
        System.out.println("rightRotation");
        rightRotation(root);
      }else{
        System.out.println("leftRight");
        leftRight(root);
      }
    }
  }

  private void rightRotation(Tree root){
    Tree c = root;
    Tree b = c.getChild(LEFT);
    Tree a = b.getChild(LEFT);
    Tree T0 = a.getChild(LEFT);
    Tree T1 = a.getChild(RIGHT);
    Tree T2 = b.getChild(RIGHT);
    Tree T3 = c.getChild(RIGHT);

    a.removeChildren(false);
    b.removeChildren(false);
    c.removeChildren(false);

    String cdata = c.getData();
    root.setData(b.getData(), false);
    b.setData(cdata, false);
    c = b;
    b = root;

    a.addChild(T0, false);
    a.addChild(T1, false);
    b.addChild(a, false);
    b.addChild(c, false);
    c.addChild(T2, false);
    c.addChild(T3, false);
  }
  
  private void leftRotation(Tree root) {
    Tree a = root;
    Tree b = a.getChild(RIGHT);
    Tree c = b.getChild(RIGHT);

    Tree T0 = a.getChild(LEFT);
    Tree T1 = b.getChild(LEFT);
    Tree T2 = c.getChild(LEFT);
    Tree T3 = c.getChild(RIGHT);
    
    a.removeChildren(false);
    b.removeChildren(false);
    c.removeChildren(false);

    String adata = a.getData();
    a.setData(b.getData(), false);
    b.setData(adata);
    a = b;
    b = root;

    b.addChild(a, false);
    b.addChild(c, false);
    a.addChild(T0, false);
    a.addChild(T1, false);
    c.addChild(T2, false);
    c.addChild(T3, false);
  }
  
  private void leftRight(Tree root){
    Tree c = root;
    Tree a = c.getChild(LEFT);
    Tree b = a.getChild(RIGHT);
    
    Tree T0 = a.getChild(LEFT);
    Tree T1 = b.getChild(LEFT);
    Tree T2 = b.getChild(RIGHT);
    Tree T3 = c.getChild(RIGHT);
    
    a.removeChildren(false);
    b.removeChildren(false);
    c.removeChildren(false);
    
    String cdata = c.getData();
    root.setData(b.getData(), false);
    b.setData(cdata, false);
    c = b;
    b = root;

    b.addChild(a, false);
    b.addChild(c, false);
    a.addChild(T0, false);
    a.addChild(T1, false);
    c.addChild(T2, false);
    c.addChild(T3, false);
  }
  
  private void rightLeft(Tree root){
    Tree a = root;
    Tree c = a.getChild(RIGHT);
    Tree b = c.getChild(LEFT);

    Tree T0 = a.getChild(LEFT);
    Tree T1 = b.getChild(LEFT);
    Tree T2 = b.getChild(RIGHT);
    Tree T3 = c.getChild(RIGHT);
    
    a.removeChildren(false);
    b.removeChildren(false);
    c.removeChildren(false);
    
    String adata = a.getData();
    root.setData(b.getData(), false);
    b.setData(adata, false);
    a = b;
    b = root;

    b.addChild(a, false);
    b.addChild(c, false);
    a.addChild(T0, false);
    a.addChild(T1, false);
    c.addChild(T2, false);
    c.addChild(T3, false);
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
