package main;

import graphics.TreeDisplay;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class TreeAutomaton extends JFrame {

  private TreeDisplay treeDisplay = new TreeDisplay();
  JSplitPane maininput, output;
  JTextArea outputtext = new JTextArea(){{setEditable(false);}};
  private BottomInput bottomInput = new BottomInput();

  public TreeAutomaton()
  {
    super("Tree Automaton");

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
                public void run() { while (step()) { 
                        try { Thread.sleep(500); } 
                        catch (InterruptedException ie) {}
                    } 
                }
            }.start();
        }
    });

    bottomInput.setChangeAction(new ActionListener(){
        public void actionPerformed(ActionEvent e){ automaton = null; step(); }
    });

    pack();
  }

  Automaton automaton;
  private boolean step()
  {
    if (automaton == null) {
      outputtext.setText("");

      String code = bottomInput.getText();
      code = code.replace("\n", " ");
      code = code.replace("  ", " ");
      code = code.trim();
      try {
          automaton = Automaton.parse(code);
          treeDisplay.setTree(automaton);
          outputtext.setText(automaton.lispy());
      } catch (CompilationException ce) {
        automaton = null;
      }
      return true;
    } else {
        boolean rv = automaton.step();
        if (rv) {
            outputtext.append(automaton.lispy() + "\n");
        }
        return rv;
    }
  }


  public static void main(String args[])
  {
    TreeAutomaton m = new TreeAutomaton();
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
    if (args.length == 0) {
        m.bottomInput.text.append(
                    "(and (not (and (not (or (or 1 1) (not 1))) (or (and (not 1) (and 0\n"
                    + "0)) (or (or 1 1) (or 1 0))))) (not (and (and (or (not 1) (and 0 1))\n"
                    + "(and (or 0 0) (or 1 1))) (or (or (and 1 0) (and 0 1)) (or (and 1\n"
                    + "1) (or 0 0))))))"
                );

    }
  }
}
