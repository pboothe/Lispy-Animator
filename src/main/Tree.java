package main;

import java.util.*;
import jscheme.*;

public class Tree {
    protected Tree parent = null;
    private String data = null;
    private Vector<Tree> children = new Vector<Tree>();
    
    Set<TreeChangeListener> listeners = new HashSet<TreeChangeListener>();

    public Tree() {}
    public Tree(Object data, Iterable<Tree> kids) 
    {
        setData(data, false);
        if (kids != null) 
            for (Tree kid : kids) {
                //Avoid setting off the listeners
                addChild(kid, false);
            }
    }
    public Tree(Iterable<Tree> kids) { this(null, kids); }
    public Tree(Object o) {
        this(o, null);
    }

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

    protected void setData(Object o) { setData(o, true); }
    protected void setData(Object o, boolean fire)
    {
        if (o != null) {
            this.data = o.toString();
        } else {
            this.data = "";
        }

        if (fire) fireDataChangedEvent();
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
    

    public void addChild(Tree child) { addChild(child, true); }
    public void addChild(Tree child, boolean fire)
    {
        synchronized (children) {
            child.parent = this;
            children.add(child);
            depthcache = -1;
            Tree t = parent;
            while (t != null && t.depthcache != -1) {
                t.depthcache = -1;
                t = t.parent;
            }
        }

        synchronized (listeners) {
            for (TreeChangeListener l : listeners) {
                child.addTreeChangeListener(l);
            }
        }
        if (fire) fireTreeAddedEvent(child);
    }

    public void removeChildren(){ removeChildren(true); }
    public void removeChildren(boolean fire){
        synchronized (children) { 
            for (Tree t : children) t.parent = null;
            children.clear(); 
        }

        if (fire) fireChildrenClearedEvent();
    }

    int depthcache = -1;
    public int depth()
    {
        synchronized(children){
          if (depthcache != -1) return depthcache;
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
          return depthcache = depth;
        }
    }

    public void addTreeChangeListener(TreeChangeListener listener){
        addTreeChangeListener(listener, new HashSet<Tree>());
    }

    public void addTreeChangeListener(TreeChangeListener listener, Set<Tree> heard){
        synchronized (listeners) {
            listeners.add(listener);
        }

        synchronized (children) {
            for (Tree kid : children) {
                if (kid == null || heard.contains(kid)) continue;
                heard.add(kid);
                kid.addTreeChangeListener(listener, heard);
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
