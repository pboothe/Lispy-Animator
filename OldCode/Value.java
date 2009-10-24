import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

abstract public class Value extends Expression
{
    public Value(String s) { super(s, null); }
    public Value(String s, Expression e[]) { super(s, e); }

    public Expression replace(String symbol, Expression e, Lisp l) 
    { 
        return this;
    }

    public Expression evaluate(Lisp l) { return this; }
}
