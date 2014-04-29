package org.springframework.expression;

public abstract interface TypeComparator
{
  public abstract int compare(Object paramObject1, Object paramObject2)
    throws EvaluationException;
  
  public abstract boolean canCompare(Object paramObject1, Object paramObject2);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.TypeComparator
 * JD-Core Version:    0.7.0.1
 */