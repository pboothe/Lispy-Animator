package main;

import java.util.Vector;

public class Tree {
	String data;
	public Tree(String data)
	{
		this.data = data;
	}

	public Tree() {}

	Vector<Tree> kids = new Vector<Tree>();

  @Override
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