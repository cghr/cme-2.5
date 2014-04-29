package org.springframework.expression.spel;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;

public abstract interface SpelNode
{
  public abstract Object getValue(ExpressionState paramExpressionState)
    throws EvaluationException;
  
  public abstract TypedValue getTypedValue(ExpressionState paramExpressionState)
    throws EvaluationException;
  
  public abstract boolean isWritable(ExpressionState paramExpressionState)
    throws EvaluationException;
  
  public abstract void setValue(ExpressionState paramExpressionState, Object paramObject)
    throws EvaluationException;
  
  public abstract String toStringAST();
  
  public abstract int getChildCount();
  
  public abstract SpelNode getChild(int paramInt);
  
  public abstract Class<?> getObjectClass(Object paramObject);
  
  public abstract int getStartPosition();
  
  public abstract int getEndPosition();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.SpelNode
 * JD-Core Version:    0.7.0.1
 */