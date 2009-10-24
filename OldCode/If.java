import java.awt.*;
import java.awt.geom.*;

class If extends Expression
{
    Expression condition, thenExp, elseExp;
    public If(Expression condition, Expression thenExp, Expression elseExp)
    {
        super("if", new Expression[] {condition, thenExp, elseExp});
        this.condition = condition;
        this.thenExp = thenExp;
        this.elseExp = elseExp;
    }

    public Expression evaluate(Lisp l)
    {
        if (condition instanceof Bool) {
            Bool b = (Bool)condition;
            if (b.bool) {
                l.move(thenExp, this);
                return thenExp;
            } else {
                l.move(elseExp, this);
                return elseExp;
            }
        } else {
            Expression newC = condition.evaluate(l);
            l.moveSideways(newC, condition, 
                    newC.getOffset() - condition.getOffset());
            return new If(newC, thenExp, elseExp);
        }
    }

    public Expression replace(String symbol, Expression e, Lisp l) {
        return new If(condition.replace(symbol, e, l), 
                thenExp.replace(symbol, e, l),
                elseExp.replace(symbol, e, l));
    }

    public String toString()
    {
        return "(if " + condition + " " + thenExp + " " + elseExp + ")";
    }

    public Expression dup() 
    { 
        return new If(condition.dup(), thenExp.dup(), elseExp.dup());
    }
}
