class LessThan extends ArityTwo
{
    public LessThan(Expression left, Expression right)
    {
        super("<", left, right);
    }

    public Expression evaluate(Lisp l)
    {
        if (!(left instanceof Number)) {
            Expression newleft = left.evaluate(l);
            l.moveSideways(newleft, left, 
                    newleft.getOffset() - left.getOffset());
            return new LessThan(newleft, right);
        } else if (!(right instanceof Number)) {
            Expression newright = right.evaluate(l);
            l.moveSideways(newright, right, 
                    newright.getOffset() - right.getOffset());
            return new LessThan(left, newright);
        } else {
            l.move(left, this);
            l.move(right, this);
            return new Bool(((Number)left).num < ((Number)right).num);
        }
    }

    public Expression replace(String symbol, Expression e, Lisp l)
    {
        return new LessThan(left.replace(symbol, e, l), 
                right.replace(symbol, e, l));
    }

    public Expression dup() { return new LessThan(left.dup(), right.dup()); }
    
    public String toString()
    {
        return "(< " + left + " " + right + ")";
    }
}


