import java.awt.*;
import java.awt.geom.*;

class Function extends Value
{
    String name;
    String arg;
    Expression body;

    public Function(String name, String arg, Expression body)
    {
        super("fun", new Expression[] { new Symbol(name), new Symbol(arg), body });
        this.name = name;
        this.arg = arg;
        this.body = body;
    }

    public Expression replace(String symbol, Expression e, Lisp l)
    {
        if (symbol.equals(arg))
            return dup();
        else
            return new Function(name, arg, body.replace(symbol, e, l));
    }

    public Expression dup() { return new Function(name, arg, body.dup()); }

    public String toString()
    {
        return "(fun (" + name + " " + arg + ") " + body + ")";
    }
}

