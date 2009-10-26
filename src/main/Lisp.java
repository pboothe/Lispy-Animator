package main;

import java.util.ArrayList;


public class Lisp {
	Tree root = new Tree();
	public Lisp(String code) throws CompilationException
	{
		root = parse(code);
	}

  public Tree getTree(){
    return root;
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