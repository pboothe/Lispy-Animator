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

class Tree {
	String data;
	public Tree(String data)
	{
		this.data = data;
	}
	
	public Tree() {}
	
	Vector<Tree> kids = new Vector<Tree>();
	
	public String toString() 
	{
		if (data != null)
			return data;
		
		String rv = "";
		for (Tree kid : kids) {
			rv += " " + kid;
		}
		return "(" + rv.trim() + ")";
	}
}

class Lisp {
	Tree root = new Tree();
	public Lisp(String code) throws CompilationException
	{
		root = parse(code);
	}
	
	int findClose(String code, int index) throws CompilationException
	{
		int count = 1;
		index++;
		while (count > 0 && index < code.length()) {
			switch (code.charAt(index)) {
				case '(': count++; break;
				case ')': count--; break;
				default: break;
			}
			index++;
		}
		if (count != 0) throw new CompilationException(code);
		
		return index;
	}
	
	ArrayList<String> findChunks(String code) throws CompilationException 
	{
		ArrayList<String> chunks = new ArrayList<String>();
		
		int start = 1;
		while (code.charAt(start) == ' ') start++;
		
		int end = start;
		while (end < code.length()-1 && code.charAt(end) != ')') {
			System.out.println(code + " " + start + " "+ end);
			if (code.charAt(start) == '(') {
				end = findClose(code, start);
				chunks.add(code.substring(start, end));
			} else {
				while (code.charAt(end) != ' '
						&& code.charAt(end) != ')'
						&& code.charAt(end) != '(' ) {
					end++;
				}
				
				if (start != end) {
					String chunk = code.substring(start, end);
					chunk = chunk.trim();
					
					if (!chunk.isEmpty())
						chunks.add(chunk);
				}
			}
			
			while (end < code.length() && code.charAt(end) == ' ') 
				end++;
			start = end;
		}
		if (end != code.length()-1)
			throw new CompilationException(code);
		if (code.charAt(0) == '(' && code.charAt(end) != ')')
			throw new CompilationException(code); 
		
		return chunks;
	}
	
	Tree parse(String code) throws CompilationException
	{
		// Base Case
		if (code.charAt(0) != '(') return new Tree(code);
		
		// Recursive Case
		ArrayList<String> chunks = findChunks(code);
		Tree tree = new Tree();
		for (String chunk : chunks) {
			tree.kids.add(parse(chunk));
		}
		return tree;
	}
	
	public String toString()
	{
		return "Lisp(" + root + ")";
	}
}

class TreePanel extends JComponent {
	public void paint(Graphics g)
	{
		g.drawLine(20,30, 60, 90);
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
		System.out.println("This is the code: " + code);
		try {
			Lisp l = new Lisp(code);
			System.out.println("Code = " + l);
		} catch (CompilationException ce) {
			System.out.println(ce.getMessage());
		} 
	}
}

public class MainWindow extends JFrame {
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
		output.add(new TreePanel());
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