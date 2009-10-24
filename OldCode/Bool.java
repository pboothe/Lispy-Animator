class Bool extends Value
{
    boolean bool;
    public Bool(boolean bool) 
    { 
        super("" + bool);
        this.bool = bool; 
    }
    public String toString() { return "" + bool; }
    public Expression dup() { return new Bool(bool); }
}

