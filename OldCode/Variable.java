class Variable extends Value
{
    String name;
    public Variable(String name) 
    { 
        super(name);
        this.name = name; 
    }

    public Expression replace(String symbol, Expression e, Lisp l) 
    {
        if (symbol.equals(name)) {
            Expression n = e.dup();

            l.grow(this, n);
            l.move(n, e, this);
            return n;
        } else
            return this;
    }

    public Expression dup() { return new Variable(name); }
    public String toString() { return name; }
}
