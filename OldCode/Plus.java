class Plus extends BinOp
{
    public Plus(Expression left, Expression right) { super("+", left, right); }
    Number doOp(Number l, Number r) { return new Number(l.num + r.num); }
    BinOp create(Expression l, Expression r) { return new Plus(l, r); }
    public Expression dup() { return new Plus(left.dup(), right.dup()); }
}

