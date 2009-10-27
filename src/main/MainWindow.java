package main;

import graphics.TreeDisplay;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

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
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(run);
		buttonPanel.add(new JButton(">>"));
		add(buttonPanel, BorderLayout.SOUTH);
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
    try {
			Lisp lisp = new Lisp(bottomInput.getText());
      treeDisplay.setTree(lisp.getTree());
		} catch (CompilationException ce) {
      treeDisplay.setMessage(ce.getMessage());
		}
    
  }


	public static void main(String args[])
	{
		MainWindow m = new MainWindow();
		m.setSize(800, 600);
		m.setVisible(true);
		m.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}