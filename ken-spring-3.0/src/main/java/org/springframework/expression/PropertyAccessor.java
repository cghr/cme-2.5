package org.springframework.expression;

public abstract interface PropertyAccessor
{
  public abstract Class[] getSpecificTargetClasses();
  
  public abstract boolean canRead(EvaluationContext paramEvaluationContext, Object paramObject, String paramString)
    throws AccessException;
  
  public abstract TypedValue read(EvaluationContext paramEvaluationContext, Object paramObject, String paramString)
    throws AccessException;
  
  public abstract boolean canWrite(EvaluationContext paramEvaluationContext, Object paramObject, String paramString)
    throws AccessException;
  
  public abstract void write(EvaluationContext paramEvaluationContext, Object paramObject1, String paramString, Object paramObject2)
    throws AccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.PropertyAccessor
 * JD-Core Version:    0.7.0.1
 */