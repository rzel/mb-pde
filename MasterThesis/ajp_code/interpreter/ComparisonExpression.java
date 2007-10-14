package ajp_code.interpreter;
/**
 * @author  Marcel Birkner
 */
public abstract class ComparisonExpression implements Expression{
    protected Expression expressionA;
    protected Expression expressionB;
    
    public ComparisonExpression(Expression expressionA, Expression expressionB){
        this.expressionA = expressionA;
        this.expressionB = expressionB;
    }
}