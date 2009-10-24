class Times extends BinOp
{
    public Times(Expression left, Expression right) { super("*", left, right); }
    Number doOp(Number l, Number r) { return new Number(l.num * r.num); }
    BinOp create(Expression l, Expression r) { return new Times(l, r); }
    public Expression dup() { return new Times(left.dup(), right.dup()); }
}
