package org.springframework.expression;

public abstract interface OperatorOverloader
{
  public abstract boolean overridesOperation(Operation paramOperation, Object paramObject1, Object paramObject2)
    throws EvaluationException;
  
  public abstract Object operate(Operation paramOperation, Object paramObject1, Object paramObject2)
    throws EvaluationException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.OperatorOverloader
 * JD-Core Version:    0.7.0.1
 */