class Number extends Value
{
    double num;
    public Number(double num) 
    { 
        super("" + num);
        this.num = num; 
    }

    public String toString() { return "" + num; }
    public Expression dup() { return new Number(num); }
}
