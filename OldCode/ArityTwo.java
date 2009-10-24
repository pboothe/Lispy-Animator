import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

abstract class ArityTwo extends Expression
{
    Expression left, right;
    String symbol;

    public ArityTwo(String symbol, Expression left, Expression right)
    {
        super(symbol, new Expression[] { left, right });
        this.symbol = symbol;
        this.left = left;
        this.right = right;
    }
}

