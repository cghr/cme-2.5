package org.springframework.expression;

public abstract interface BeanResolver
{
  public abstract Object resolve(EvaluationContext paramEvaluationContext, String paramString)
    throws AccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.BeanResolver
 * JD-Core Version:    0.7.0.1
 */