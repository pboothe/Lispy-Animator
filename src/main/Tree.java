package main;

import java.util.Vector;

public class Tree {
    private String data = null;
    private Vector<Tree> kids = new Vector<Tree>();

    public Tree(String data)
    {
            this.data = data;
    }

    public Tree(){ }

    

    @Override
    public String toString()
    {
        if (getData() != null)
            return getData();

        String rv = "";
        for (Tree kid : getKids()) {
            rv += " " + kid;
        }
        return "(" + rv.trim() + ")";
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
