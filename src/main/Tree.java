package main;

import java.util.*;
import jscheme.*;

public class Tree {
    private String data = null;
    private Vector<Tree> children = new Vector<Tree>();
    
    Vector<TreeChangeListener> listeners = new Vector<TreeChangeListener>();

    public Tree(String data, Iterable<Tree> kids) 
    {
        FIREAWAY = false;
        setData(data);
        if (kids != null) 
            for (Tree kid : kids) {
                //Avoid setting off the listeners
                addChild(kid);
            }
        FIREAWAY = true;
    }
    public Tree(String data) { this(data, null); }
    public Tree(Iterable<Tree> kids) { this(null, kids); }
    public Tree(Object o) {
        FIREAWAY = false;
        setData(o);
        FIREAWAY = true;
    }

    public Tree(){ }

    @Override
    public String toString()
    {
        if (getData() != null && getChildren().size() == 0)
            return getData();

        String rv = "";
        for (Tree kid : getChildren()) {
            if (kid == this)
              continue;
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

    public boolean FIREAWAY = true;

    protected void setData(Object o)
    {
        if (o != null) {
            this.data = o.toString();
        } else {
            this.data = "";
        }

        if (FIREAWAY)
            fireDataChangedEvent();
    }

    public String getTreeName()
    {
        if (getData() != null)
            return getData();
        else
            return "";
    }

    /**
     * @return the kids
     */
    public Vector<Tree> getChildren()
    {
        synchronized (children) {
            Vector<Tree> copy = new Vector<Tree>(children);
            return copy;
        }
    }
    
    /**
     * Returns the child at the given position, null if none is there
     * @param child
     */
    public Tree getChild(int i){
      if (i >= children.size())
        return null;
      return children.get(i);
    }
    
    public void setChild(int position, Tree child){
      synchronized(child){
        children.set(position, child);
      }
      
      synchronized (listeners) {
        for (TreeChangeListener l : listeners) {
            child.addTreeChangeListener(l);
        }
      }
    }
    

    public void addChild(Tree child)
    {
        synchronized (children) {
            children.add(child);
        }

        synchronized (listeners) {
            for (TreeChangeListener l : listeners) {
                child.addTreeChangeListener(l);
            }
        }
        if (FIREAWAY) fireTreeAddedEvent(child);
    }

    public void removeChildren(){
        children.clear();
        if (FIREAWAY) fireChildrenClearedEvent();
    }

    public int depth()
    {
        synchronized(children){
          int depth = 1;
          if (children != null) {
              for (Tree kid : children) {
                  if (kid == this || kid == null){
                    continue;
                  }
                  int kd = kid.depth();
                  if (1 + kd > depth) {
                      depth = 1 + kd;
                  }
              }
          }
          return depth;
        }
    }

    public void addTreeChangeListener(TreeChangeListener listener){
        synchronized (listeners) {
            listeners.add(listener);
        }

        synchronized (children) {
            for (Tree kid : children) {
                if (kid == null) continue;
                kid.addTreeChangeListener(listener);
            }
        }
    }

    public void removeTreeChangeListener(TreeChangeListener listener){
        synchronized (listeners) {
            listeners.remove(listener);
        }

        synchronized (children) {
            for (Tree kid : children) {
                kid.addTreeChangeListener(listener);
            }
        }
    }

    private void fireTreeAddedEvent(Tree child){
        synchronized (listeners) {
            for (TreeChangeListener nodeChangeListener : listeners) {
                nodeChangeListener.kidAdded(this, child);
            }
        }
    }

    private void fireChildrenClearedEvent(){
        synchronized (listeners) {
            for (TreeChangeListener nodeChangeListener : listeners) {
                nodeChangeListener.childrenRemoved(this);
            }
        }
    }

    private void fireDataChangedEvent() {
        synchronized (listeners) {
            for (TreeChangeListener nodeChangeListener : listeners) {
                nodeChangeListener.dataChanged(this);
            }
        }
    }
    
    Vector<Tree> preorder()
    {
    	Vector<Tree> answer = new Vector<Tree>();
    	answer.add(this);
    	for (Tree kid : children)
    		for (Tree t : kid.preorder())
    			answer.add(t);
    	return answer;
    }
    
}
