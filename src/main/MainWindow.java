package main;

import graphics.TreeDisplay;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class CompilationException extends Exception {
  public CompilationException(String code)
  {
    super("Problem with: " + code);
  }
}


class BottomInput extends JPanel {
  JTextArea text;
  private JButton run = new JButton(">");

  public BottomInput()
  {
    super(new BorderLayout());
    add(text = new JTextArea(), BorderLayout.CENTER);
    text.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {checkSyntax();}
            public void removeUpdate(DocumentEvent e) {checkSyntax();}
            public void changedUpdate(DocumentEvent e) {}
        });
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(run);
    buttonPanel.add(new JButton(">>"));
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void checkSyntax(){
      Stack<Character> stack = new Stack<Character>();
      String code = text.getText();
      for (int i = 0;  i < code.length(); i++){
        try{
            if (code.charAt(i) == '('){
                stack.push('(');
            }else if (code.charAt(i) == ')'){
                if ( stack.pop() != '(' )
                 throw new Exception("Unbalanced!!");
            }
        }catch(Exception e){
            text.setBackground(Color.decode("#FF6347"));
            return;
        }
      }
      text.setBackground(stack.isEmpty() ? Color.WHITE : Color.decode("#FA8072") );
  }

  public String getText(){
    return text.getText();
  }

  void setPlayButtonAction(ActionListener actionListener) {
    run.addActionListener(actionListener);
  }
}
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
      public void actionPerformed(ActionEvent e){
        playButtonPressed();
      }
    });

    pack();
  }

  private void playButtonPressed(){
    
    final String code = bottomInput.getText();
    if (code == null || code.isEmpty() )
      return;

    new Thread(){
        private CompilationException ce = null;
        private Lisp lisp = null;
        
        @Override
        public void run(){
          try{
            lisp =  new Lisp(code);
            outputtext.setText(lisp.root.toString());
          }catch(CompilationException ce){
            this.ce = ce;
          }
          if (ce != null){
            treeDisplay.setMessage(ce.getMessage());
          }else{
            treeDisplay.setTree(lisp.getTree());
          }

          treeDisplay.repaint();
          int count = 0;
          while (true) {
            double d = treeDisplay.adjust();
            if (d != -1 && d < 10)
                break;
            if (count % 10 == 0) treeDisplay.repaint();
          }
          treeDisplay.repaint();
        }
      }.start();
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
                    //I like to imagine that a  buffer is used in the textarea
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
