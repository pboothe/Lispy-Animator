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
	public BottomInput()
	{
		super(new BorderLayout());
		add(text = new JTextArea(), BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		JButton run = new JButton(">");
		buttonPanel.add(run);
		buttonPanel.add(new JButton(">>"));
		add(buttonPanel, BorderLayout.SOUTH);
		
		//Action Listeners
		run.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				runButtonPressed(e);
			}
		});
	}
	
	private void runButtonPressed(ActionEvent e){
		String code = text.getText();
		try {
			Lisp l = new Lisp(code);
		} catch (CompilationException ce) {
			System.out.println(ce.getMessage());
		} 
	}
}

public class MainWindow extends JFrame {

  private TreeDisplay treeDisplay = new TreeDisplay();

	JSplitPane maininput, output;
	JTextArea outputtext = new JTextArea(){{setEditable(false);}};
	
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
		maininput.add(new JScrollPane(new BottomInput()));
		pack();
	}


	public static void main(String args[])
	{
		MainWindow m = new MainWindow();
		m.setSize(800, 600);
		m.setVisible(true);
		m.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}