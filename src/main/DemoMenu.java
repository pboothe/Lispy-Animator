package main;

import javax.swing.JOptionPane;

public class DemoMenu{
	public static void main(String[] args){
		Object[] options = new String[]{"Scheme", "AVL Trees", "Tree Automata"};
		String s = (String)JOptionPane.showInputDialog(
                    null, "The following demos are available",
		    "Demo Menu", JOptionPane.PLAIN_MESSAGE, null,
                    options, "AVL Trees");

		//If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			if (s.equals("Scheme")){
				MainWindow.main(args);
			}else if (s.equals("AVL Trees")){
				BinaryTreeInsertion.main(args);
			}else if (s.equals("Tree Automata")){
				TreeAutomaton.main(args);
			}else{
				System.out.println("DemoMenu is broken");
			}	
		    return;
		}
	}
}
