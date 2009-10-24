class Apply extends ArityTwo
{
    public Apply(Expression function, Expression argument)
    {
        super("apply", function, argument);
    }

    public Expression replace(String symbol, Expression e, Lisp l)
    {
        return new Apply(left.replace(symbol, e, l), 
                right.replace(symbol, e, l));
    }

    public Expression evaluate(Lisp l)
    {
        if (!(left instanceof Function)) {
            Expression rv = left.evaluate(l);
            l.moveSideways(rv, left, rv.getOffset() - left.getOffset());
            return new Apply(rv, right);
        } else if (!(right instanceof Value)) {
            Expression rv = right.evaluate(l);
            l.moveSideways(rv, right, rv.getOffset() - right.getOffset());
            return new Apply(left, rv);
        } else {
            Function f = (Function)left;
            Expression tmp = f.body.replace(f.arg, right, l);
            Expression ne = tmp.replace(f.name, f, l);
            l.moveScrub(ne, f.body, this);
            return ne;
        }
    }

    public Expression dup()
    {
        return new Apply(left.dup(), right.dup());
    }

    public String toString()
    {
        return "(apply " + left + " " + right + ")";
    }
}
