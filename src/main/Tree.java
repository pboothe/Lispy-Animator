package main;

import java.util.*;
import jscheme.*;

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
    public Tree(Object o) {
        if (o != null)
            System.out.println(o.getClass().getName() + " " + o);
        else
            System.out.println("null");

        if (o instanceof Pair) {
            Pair p = (Pair)o;
            this.kids.add(new Tree(p.first));
            this.kids.add(new Tree(p.rest));
        } else if (o != null) {
            this.data = o.toString();
        } else {
            this.data = "";
        }
    }

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

    public int depth()
    {
        int depth = 1;
        if (kids != null) {
            for (Tree kid : kids) {
                int kd = kid.depth();
                if (1 + kd > depth) {
                    depth = 1 + kd;
                }
            }
        }

        return depth;
    }
}
