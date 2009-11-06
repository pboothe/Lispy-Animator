package main;

import java.util.*;

public class Tree {
    private String data = null;
    private Vector<Tree> kids = new Vector<Tree>();

    public Tree(String data, Iterable<Tree> kids) 
    {
        this.data = data; 
        if (kids != null) 
            for (Tree kid : kids) {
                this.kids.add(kid);
            }
    }
    public Tree(String data) { this(data, null); }
    public Tree(Iterable<Tree> kids) { this(null, kids); }

    public Tree(){ }

    @Override
    public String toString()
    {
        if (getData() != null && getKids().size() == 0)
            return getData();

        String rv = "";
        for (Tree kid : getKids()) {
            rv += " " + kid;
        }

        if (getData() == null)
            return "(" + rv.trim() + ")";
        else
            return "(" + getData() + " " + rv.trim() + ")";
    }

    /**
     * @return the data
     */
    public String getData() 
    {
        return data;
    }

    public String getNodeName()
    {
        if (getData() != null)
            return getData();
        else
            return "β";
    }

    /**
     * @return the kids
     */
    public Vector<Tree> getKids() 
    {
        return kids;
    }

    public void addChild(Tree child)
    {
        kids.add(child);
    }
}
