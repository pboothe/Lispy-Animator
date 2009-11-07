package main;

import java.util.*;
import jscheme.*;

public class Tree {
    private String data = null;
    private Vector<Tree> children = new Vector<Tree>();
    
    Vector<NodeChangeListener> listeners = new Vector<NodeChangeListener>();

    public Tree(String data, Iterable<Tree> kids) 
    {
        setData(data, false);
        if (kids != null) 
            for (Tree kid : kids) {
                //Avoid setting off the listeners
                children.add(kid);
            }
    }
    public Tree(String data) { this(data, null); }
    public Tree(Iterable<Tree> kids) { this(null, kids); }
    public Tree(Object o) {
        setData(o, false);
    }

    public Tree(){ }

    @Override
    public String toString()
    {
        if (getData() != null && getChildren().size() == 0)
            return getData();

        String rv = "";
        for (Tree kid : getChildren()) {
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

    public void setData(Object o){
         setData(o, true);
    }

    private void setData(Object o, boolean fireListener){
         //Check if we already have data, if we do, remove it.
        if (!children.isEmpty())
            removeChildren();

        if (o != null)
            System.out.println(o.getClass().getName() + " " + o);
        else
            System.out.println("null");

        if (o instanceof Pair) {
            Pair p = (Pair)o;
            this.children.add(new Tree(p.first));
            this.children.add(new Tree(p.rest));
        } else if (o != null) {
            this.data = o.toString();
        } else {
            this.data = "";
        }
        if (fireListener)
            fireDataChangedEvent();
    }

    public void setData(String data){
        this.data = data;
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
    public Vector<Tree> getChildren()
    {
        return children;
    }

    public void addChild(Tree child)
    {
        children.add(child);
        fireNodeAddedEvent(child);
    }

    public void removeChildren(){
        children.clear();
        fireChildrenClearedEvent();
    }


    public int depth()
    {
        int depth = 1;
        if (children != null) {
            for (Tree kid : children) {
                int kd = kid.depth();
                if (1 + kd > depth) {
                    depth = 1 + kd;
                }
            }
        }

        return depth;
    }

    public void addNodeChangeListener(NodeChangeListener listener){
        listeners.add(listener);
    }

    public void removeNodeChangeListener(NodeChangeListener listener){
        listeners.remove(listener);
    }

    private void fireNodeAddedEvent(Tree child){
        for (NodeChangeListener nodeChangeListener : listeners) {
            nodeChangeListener.kidAdded(this, child);
        }
    }

    private void fireChildrenClearedEvent(){
        for (NodeChangeListener nodeChangeListener : listeners) {
            nodeChangeListener.childrenRemoved(this);
        }
    }

    private void fireDataChangedEvent() {
        for (NodeChangeListener nodeChangeListener : listeners) {
            nodeChangeListener.dataChanged(this);
        }
    }
}
