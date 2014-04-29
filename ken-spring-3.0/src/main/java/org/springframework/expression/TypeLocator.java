package org.springframework.expression;

public abstract interface TypeLocator
{
  public abstract Class<?> findType(String paramString)
    throws EvaluationException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.TypeLocator
 * JD-Core Version:    0.7.0.1
 */