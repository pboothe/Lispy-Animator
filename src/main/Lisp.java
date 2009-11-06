package main;

import java.util.*;


public class Lisp {
  String reservedwords[] = { "+", "-", "*", "/", "<", ">", "if", "fun" };

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
        if (first.getKids() != null && first.getKids().size() > 0)
            return new Tree(kids);

        kids.remove(0);
        String word = first.getData();
        if (word.equals("fun")) {
            // (fun name arg1 arg2 [...] body) ; note that args are optional
            if (kids.size() <= 1) 
                throw new CompilationException(code);
            if (kids.get(1).getKids() == null)
                throw new CompilationException(code);

            return new Tree("fun", kids);
        } else if (word.equals("if")) {
            // (if cond then else)
            if (kids.size() != 3) 
                throw new CompilationException(code);
            return new Tree("if", kids);
        } else {
            return new Tree(word, kids);
        }
    } else {
        return new Tree(kids);
    }
  }


  public String toString()
  {
    return "Lisp(" + root + ")";
  }
}
