import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

class Symbol extends Expression 
{
    String symbol;
    public Symbol(String symbol) 
    { 
        super(symbol, null); 
        this.symbol = symbol;
    }

    public Expression replace(String symbol, Expression e, Lisp l) 
    { 
        return dup(); 
    }

    public Expression evaluate(Lisp l) { return dup(); }

    public Expression dup() { return new Symbol(symbol); }
    public String toString() { return symbol; }
}
