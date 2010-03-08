package main;

import graphics.TreeDisplay;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class MainWindow extends JFrame {

  private TreeDisplay treeDisplay = new TreeDisplay();
  JSplitPane maininput, output;
  JTextArea outputtext = new JTextArea(){{setEditable(false);}};
  private BottomInput bottomInput = new BottomInput();

  public MainWindow()
  {
    super("Lispy Animator");

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

    bottomInput.setPlayAllButtonAction(new ActionListener(){
      public void actionPerformed(ActionEvent e){ 
        new Thread() {
            public void run() {
                while (step()) {
                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException ie) {}
                }
            }  
        }.start();
      }
    });

    bottomInput.setChangeAction(new ActionListener(){
      public void actionPerformed(ActionEvent e){ 
          outputtext.setText(bottomInput.getText().replace('\n', ' '));
          startLayout(); 
      }
    });

    pack();
  }

  Lisp lisp = null;
  synchronized private void startLayout()
  {
      String code = bottomInput.getText().trim();
      try {
          lisp = new Lisp(code);
          treeDisplay.setTree(lisp.root);
          System.out.println(lisp.toString());
      } catch (CompilationException ce) {
        treeDisplay.setMessage("Problem with: " + code);
        System.out.println("Problem with: " + code);
      }
  }

  private boolean step()
  {
    if (outputtext.getText() == null || outputtext.getText().trim().equals("")) {
        startLayout();
    }

    boolean rv = lisp.step();
    outputtext.append("\n" + lisp);
    return rv;
  }


  public static void main(String args[])
  {
    MainWindow m = new MainWindow();
    m.setSize(800, 600);
    m.setVisible(true);
    m.setDefaultCloseOperation(EXIT_ON_CLOSE);
    m.setLocationRelativeTo(null); //Center the window

    for (int i = 0; i <args.length; i++) {
       if (!args[i].startsWith("-")){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(args[i])));
                String in;
                while( (in = reader.readLine()) != null){
                    m.bottomInput.text.append(in);
                    //I like to imagine that a buffer is used in the textarea
                    //So I'm avoiding concatination.
                    m.bottomInput.text.append("\n");
                }
            } catch (FileNotFoundException ex) {
                System.err.println("File: '" + args[i] +"'  not found.");
            } catch (IOException ioe){
                System.err.println("Error reading: " + args[i]);
            }
       }
    }
  }
}
