package org.springframework.expression;

public abstract interface ConstructorExecutor
{
  public abstract TypedValue execute(EvaluationContext paramEvaluationContext, Object... paramVarArgs)
    throws AccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.ConstructorExecutor
 * JD-Core Version:    0.7.0.1
 */