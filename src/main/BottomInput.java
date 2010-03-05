package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BottomInput extends JPanel {
	private static final long serialVersionUID = 1L;
	JTextArea text;
	  private JButton step = new JButton(">");
	  private JButton run = new JButton(">>");

	  public BottomInput()
	  {
	    super(new BorderLayout());
	    add(text = new JTextArea(), BorderLayout.CENTER);
	    text.getDocument().addDocumentListener(new DocumentListener() {
	            public void insertUpdate(DocumentEvent e) { checkSyntax(); }
	            public void removeUpdate(DocumentEvent e) { checkSyntax(); }
	            public void changedUpdate(DocumentEvent e) { checkSyntax(); }
	        });
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.add(step);
	    buttonPanel.add(run);
	    add(buttonPanel, BorderLayout.SOUTH);
	  }

	  private boolean checkSyntax(){
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
	            return false;
	        }
	      }

	      text.setBackground(stack.isEmpty() ? Color.WHITE : Color.decode("#FA8072") );
	      if (stack.isEmpty()) { 
	          return true;
	      } else {
	          return false;
	      }
	  }

	  public String getText(){
	    return text.getText();
	  }

	  void setPlayButtonAction(ActionListener actionListener) {
	    step.addActionListener(actionListener);
	  }

	  void setPlayAllButtonAction(ActionListener actionListener) {
	    run.addActionListener(actionListener);
	  }

	  void setChangeAction(final ActionListener actionListener) {
	      text.getDocument().addDocumentListener(new DocumentListener() {
	            public void insertUpdate(DocumentEvent e) { if (checkSyntax()) actionListener.actionPerformed(null); }
	            public void removeUpdate(DocumentEvent e) { if (checkSyntax()) actionListener.actionPerformed(null); }
	            public void changedUpdate(DocumentEvent e) { }
	          });
	  }

}
