class Quote extends Value
{
    Expression quote;
    public Quote(Expression quote) 
    { 
        super("quote", new Expression[] { quote });
        this.quote = quote; 
    }

    public String toString() { return "'" + quote; }
    public Expression dup() { return new Quote(quote.dup()); }
}
