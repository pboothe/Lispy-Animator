package main;

import java.util.*;


public class Lisp {
  private static final String FUN = "fun";
  private static final String IF = "if";
  String reservedwords[] = { "+", "-", "*", "/", "<", ">", "=", "^", IF, FUN};

  Tree root = new Tree();
  public Lisp(String code) throws CompilationException
  {
    root = parse(code.trim());
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

  boolean isReserved(String word)
  {
      for (String reserved : reservedwords) {
          if (reserved.equals(word)) 
              return true;
      }

      return false;
  }
  
  Tree parse(String code) throws CompilationException
  {
    if (code == null || code.isEmpty())
       return null;
    
    // Base Case
    if (code.charAt(0) != '(') return new Tree(code);

    // Recursive Case
    ArrayList<String> chunks = findChunks(code);
    List<Tree> kids = new LinkedList<Tree>();
    for (String chunk : chunks) {
      kids.add(parse(chunk));
    }

    if (!kids.isEmpty() && isReserved(kids.get(0).getData())) {
        // Now we need to make special Trees based on what kind of special thing 
        // this is
        Tree first = kids.get(0);
        if (first.getChildren() != null && first.getChildren().size() > 0)
            return new Tree(kids);

        kids.remove(0);
        String word = first.getData();
        if (word.equals(FUN)) {
            // (FUN (arg1 arg2 [...]) body) ; note that args are optional
            if (kids.size() <= 1) 
                throw new CompilationException(code);
            if (kids.get(1).getChildren() == null)
                throw new CompilationException(code);

            return new Tree(FUN, kids);
        } else if (word.equals(IF)) {
            // (if cond then else)
            if (kids.size() != 3) 
                throw new CompilationException(code);
            return new Tree(IF, kids);
        } else {
            return new Tree(word, kids);
        }
    } else {
        return new Tree(kids);
    }
  }

  private static void replace(Tree code, String data, Tree replacement)
  {
      System.out.println("Trying to replace" + data + " with " + replacement + " in " + code);
      if (code.getData().equals(data)) {
          System.out.println("Replacing " + data + " with " + replacement);
          code.removeChildren(false);
          for (Tree t : replacement.getChildren()) {
              code.addChild(new Tree(t), false);
          }
          code.setData(replacement.getData());
      } else if (code.getData().equals(FUN)) {
          //Shadowing
          Vector<Tree> fn = code.getChildren();
          Vector<Tree> args = fn.get(0).getChildren();

          for (Tree t : args) {
              if (t.getData().equals(data)) {
                  return;
              }
          }

          // If we made it here, the replacement term is not in the args
          replace(fn.get(1), data, replacement);
      } else {
          for (Tree t : code.getChildren()) {
              replace(t, data, replacement);
          }
      }
  }

  public boolean step() { return step(root); }
  private boolean step(Tree code) {
    if (code.getData().equals(FUN)) return false;

    if (code.getData().equals("") && code.getChildren().size() >= 1 && code.getChild(0).getData().equals(FUN)) { 
        // This is an APPLY -- make sure it works.
        Tree fn = code.getChild(0);
        Vector<Tree> fnargs = fn.getChildren();
        Tree body = fnargs.get(1);
        fnargs = fnargs.get(0).getChildren();

        Vector<Tree> inargs = code.getChildren();
        inargs.remove(0);
        for (Tree t : inargs) {
            if (step(t)) return true;
        }

        if (fnargs.size() != inargs.size()) {
            return false;
        }

        for (int i = 0; i < fnargs.size(); i++) {
            replace(body, fnargs.get(i).getData(), inargs.get(i));
        }

        code.removeChildren(false);
        for (Tree t : body.getChildren()) {
            code.addChild(t, false);
        }
        code.setData(body.getData());
        
        return true;
    } else if (code.getData().equals(IF)) {
        // IF
        Vector<Tree> ifargs = code.getChildren();
        if (step(ifargs.get(0))) return true;

        if (ifargs.get(0).getData().equals("0")) { // "0" is our only false value
            System.out.println("FALSE IF");
            code.removeChildren(false);

            for (Tree kkid : ifargs.get(2).getChildren() ) {
                code.addChild(kkid, false);
            }

            code.setData(ifargs.get(2).getData());
        } else {
            System.out.println("TRUE IF: " + ifargs.get(0));
            code.removeChildren(false);

            for (Tree kkid : ifargs.get(1).getChildren() ) {
                code.addChild(kkid, false);
            }

            code.setData(ifargs.get(1).getData());
        }
        return true;
    }

    for (Tree kid : code.getChildren()) {
        if (step(kid)) { 
            System.out.println("stepped " + kid);
            return true; 
        }
    }

    if (code.getData().equals("<")) {
        Vector<Tree> ltargs = code.getChildren();
        Double left = Double.parseDouble(ltargs.get(0).getData());
        Double right = Double.parseDouble(ltargs.get(1).getData());

        code.removeChildren(false);
        if (left < right) {
            code.setData("1");
        } else {
            code.setData("0");
        }

        return true;
    } else if (code.getData().equals(">")) {
        Vector<Tree> ltargs = code.getChildren();
        Double left = Double.parseDouble(ltargs.get(0).getData());
        Double right = Double.parseDouble(ltargs.get(1).getData());

        code.removeChildren(false);
        if (left > right) {
            code.setData("1");
        } else {
            code.setData("0");
        }

        return true;
    } else if (code.getData().equals("=")) {
        Vector<Tree> ltargs = code.getChildren();
        Double left = Double.parseDouble(ltargs.get(0).getData());
        Double right = Double.parseDouble(ltargs.get(1).getData());

        code.removeChildren(false);
        if (left.equals(right)) {
            code.setData("1");
        } else {
            code.setData("0");
        }

        return true;
    } else if (code.getData().equals("+")) {
        Vector<Tree> ltargs = code.getChildren();
        Double left = Double.parseDouble(ltargs.get(0).getData());
        Double right = Double.parseDouble(ltargs.get(1).getData());

        code.removeChildren(false);
        code.setData("" + (left + right));

        return true;
    } else if (code.getData().equals("-")) {
        Vector<Tree> ltargs = code.getChildren();
        Double left = Double.parseDouble(ltargs.get(0).getData());
        Double right = Double.parseDouble(ltargs.get(1).getData());

        code.removeChildren(false);
        code.setData("" + (left - right));

        return true;
    } else if (code.getData().equals("*")) {
        Vector<Tree> ltargs = code.getChildren();
        Double left = Double.parseDouble(ltargs.get(0).getData());
        Double right = Double.parseDouble(ltargs.get(1).getData());

        code.removeChildren(false);
        code.setData("" + (left * right));

        return true;
    } else if (code.getData().equals("/")) {
        Vector<Tree> ltargs = code.getChildren();
        Double left = Double.parseDouble(ltargs.get(0).getData());
        Double right = Double.parseDouble(ltargs.get(1).getData());

        code.removeChildren(false);
        code.setData(""  +(left / right));

        return true;
    } else if (code.getData().equals("^")) {
        Vector<Tree> ltargs = code.getChildren();
        Double left = Double.parseDouble(ltargs.get(0).getData());
        Double right = Double.parseDouble(ltargs.get(1).getData());

        code.removeChildren(false);
        code.setData(""  + Math.pow(left, right));

        return true;
    }

    System.out.println("Couldn't step: " + code.getData());
    return false;
  }

  public String toString()
  {
    return "Lisp(" + root + ")";
  }
}
