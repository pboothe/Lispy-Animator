import java.awt.*;
import java.awt.geom.*;

abstract class BinOp extends ArityTwo
{
    public BinOp(String symbol, Expression left, Expression right)
    {
        super(symbol, left, right);
    }

    abstract Number doOp(Number l, Number r);
    abstract BinOp create(Expression left, Expression right);

    public Expression evaluate(Lisp l)
    {
        if (!(left instanceof Number)) {
            Expression rv = left.evaluate(l); 
            Expression created = create(rv, right);
            l.moveSideways(rv, left, rv.getOffset() - left.getOffset());
            return created;
        } else if (!(right instanceof Number)) {
            Expression rv = right.evaluate(l);
            Expression created = create(left, rv);
            l.moveSideways(rv, right, rv.getOffset() - right.getOffset());
            return created;
        } else {
            l.move(left, this);
            l.move(right, this);
            return doOp((Number)left, (Number)right);
        }
    }

    public Expression replace(String symbol, Expression e, Lisp l)
    {
        Expression nl = left.replace(symbol, e, l);
        Expression nr = right.replace(symbol, e, l);
    
        if (nl == left && nr == right)
            return this;
        else
            return create(nl, nr);
    }

    public String toString()
    {
        return "(" + symbol + " " + left + " " + right + ")";
    }
}
