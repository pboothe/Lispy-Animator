package main;

import java.util.*;

public class Automaton extends Tree {
  public Automaton(Iterable<Automaton> a)
  { 
        super(a);
  }

  public Automaton(String data, Iterable<Automaton> as)
  { 
    super(data);
    for (Automaton a : as) 
        addChild(a, false);
  }

  public Automaton(String data)
  { 
        super(data);
  }

    public boolean step()
    {
        Vector<Automaton> kids = new Vector<Automaton>();
        for (Tree t : getChildren()) kids.add((Automaton) t);

        // Leaf case
        if (kids.size() == 0) {
            if (getTreeName().equals("1")) {
                setData("true"); return true;
            } else if (getTreeName().equals("0")) {
                setData("false"); return true;
            } else if (getTreeName().equals("true")) {
                setData("ACCEPT");
                return true;
            }
        } else if (kids.size() == 1) {
            String k = kids.get(0).getTreeName();
            if (!k.equals("true") && !k.equals("false"))
                return kids.get(0).step();
            else if (getTreeName().equals("not")) {
                removeChildren(false);
                setData(k.equals("true") ? "false" : "true");
                return true;
            }
        } else if (kids.size() == 2) {
            String l = kids.get(0).getTreeName();
            String r = kids.get(1).getTreeName();
            if (!l.equals("true") && !l.equals("false")) 
                return kids.get(0).step();
            else if (!r.equals("true") && !r.equals("false")) 
                return kids.get(1).step();
            else if (getTreeName().equals("and")) {
                removeChildren(false);
                setData((l.equals("true") && r.equals("true")) ? "true" : "false");
                return true;
            } else if (getTreeName().equals("or")) {
                removeChildren(false);
                setData((l.equals("true") || r.equals("true")) ? "true" : "false");
                return true;
            }
        }

        return false;
    }

    public String lispy() 
    {
        Vector<Automaton> kids = new Vector<Automaton>();
        for (Tree t : getChildren()) kids.add((Automaton) t);

        String rv = getTreeName();
        for (Automaton a : kids) {
            rv += " " + a.lispy();
        }

        if (kids.size() > 0)
            return "(" + rv + ")";
        else
            return rv;
    }


  // Static parsing crap.
  static int findClose(String code, int index) throws CompilationException
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

  static ArrayList<String> findChunks(String code) throws CompilationException
  {
    ArrayList<String> chunks = new ArrayList<String>();

    int start = 1;
    while (code.charAt(start) == ' ') start++;

    int end = start;
    while (end < code.length()-1 && code.charAt(end) != ')') {
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

  static Automaton parse(String code) throws CompilationException
  {
    if (code == null || code.isEmpty())
       return null;
    
    // Base Case
    if (code.charAt(0) != '(') return new Automaton(code);

    // Recursive Case
    ArrayList<String> chunks = findChunks(code);
    List<Automaton> kids = new LinkedList<Automaton>();
    for (String chunk : chunks) {
      kids.add(parse(chunk));
    }

    if (!kids.isEmpty()) {
        Automaton first = kids.get(0);
        if (first.getChildren() != null && first.getChildren().size() > 0)
            return new Automaton(kids);

        kids.remove(0);
        String word = first.getData();
        return new Automaton(word, kids);
    } else {
        return new Automaton(kids);
    }
  }

}
