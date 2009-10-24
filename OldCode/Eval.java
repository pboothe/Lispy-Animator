import java.awt.*;
import java.awt.geom.*;

class Eval extends Expression
{
    Expression arg;
    public Eval(Expression arg) 
    { 
        super("Eval", new Expression[] { arg });
        this.arg = arg; 
    }

    public Expression replace(String symbol, Expression e, Lisp l)
    {
        return new Eval(arg.replace(symbol, e, l));
    }

    public Expression evaluate(Lisp l)
    {
        if (arg instanceof Quote) {
            return ((Quote)arg).quote;
        } else if (arg instanceof Value) {
            return arg;
        } else {
            return new Eval(arg.evaluate(l));
        }
    }

    public Expression dup() { return new Eval(arg.dup()); }
    public String toString() { return "(eval " + arg + ")"; }
}

