package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;

public abstract interface Expression
{
  public abstract Object getValue()
    throws EvaluationException;
  
  public abstract Object getValue(Object paramObject)
    throws EvaluationException;
  
  public abstract <T> T getValue(Class<T> paramClass)
    throws EvaluationException;
  
  public abstract <T> T getValue(Object paramObject, Class<T> paramClass)
    throws EvaluationException;
  
  public abstract Object getValue(EvaluationContext paramEvaluationContext)
    throws EvaluationException;
  
  public abstract Object getValue(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract <T> T getValue(EvaluationContext paramEvaluationContext, Class<T> paramClass)
    throws EvaluationException;
  
  public abstract <T> T getValue(EvaluationContext paramEvaluationContext, Object paramObject, Class<T> paramClass)
    throws EvaluationException;
  
  public abstract Class getValueType()
    throws EvaluationException;
  
  public abstract Class getValueType(Object paramObject)
    throws EvaluationException;
  
  public abstract Class getValueType(EvaluationContext paramEvaluationContext)
    throws EvaluationException;
  
  public abstract Class getValueType(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract TypeDescriptor getValueTypeDescriptor()
    throws EvaluationException;
  
  public abstract TypeDescriptor getValueTypeDescriptor(Object paramObject)
    throws EvaluationException;
  
  public abstract TypeDescriptor getValueTypeDescriptor(EvaluationContext paramEvaluationContext)
    throws EvaluationException;
  
  public abstract TypeDescriptor getValueTypeDescriptor(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract boolean isWritable(EvaluationContext paramEvaluationContext)
    throws EvaluationException;
  
  public abstract boolean isWritable(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract boolean isWritable(Object paramObject)
    throws EvaluationException;
  
  public abstract void setValue(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract void setValue(Object paramObject1, Object paramObject2)
    throws EvaluationException;
  
  public abstract void setValue(EvaluationContext paramEvaluationContext, Object paramObject1, Object paramObject2)
    throws EvaluationException;
  
  public abstract String getExpressionString();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.Expression
 * JD-Core Version:    0.7.0.1
 */