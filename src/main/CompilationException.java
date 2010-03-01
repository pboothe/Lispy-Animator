package main;

public class CompilationException extends Exception {
	  public CompilationException(String code)
	  {
	    super("Problem with: " + code);
	  }
}
