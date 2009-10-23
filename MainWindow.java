import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class TreePanel extends JComponent {
	public void paint(Graphics g)
	{
		g.drawLine(20,30, 60, 90);
	} 
}

class BottomInput extends JPanel {
	public BottomInput()
	{
		super(new BorderLayout());
		add(new JTextArea(), BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(new JButton(">"));
		buttonPanel.add(new JButton(">>"));
		add(buttonPanel, BorderLayout.SOUTH);
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