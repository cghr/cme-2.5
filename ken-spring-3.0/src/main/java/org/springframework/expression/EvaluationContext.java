package org.springframework.expression;

import java.util.List;

public abstract interface EvaluationContext
{
  public abstract TypedValue getRootObject();
  
  public abstract List<ConstructorResolver> getConstructorResolvers();
  
  public abstract List<MethodResolver> getMethodResolvers();
  
  public abstract List<PropertyAccessor> getPropertyAccessors();
  
  public abstract TypeLocator getTypeLocator();
  
  public abstract TypeConverter getTypeConverter();
  
  public abstract TypeComparator getTypeComparator();
  
  public abstract OperatorOverloader getOperatorOverloader();
  
  public abstract BeanResolver getBeanResolver();
  
  public abstract void setVariable(String paramString, Object paramObject);
  
  public abstract Object lookupVariable(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.EvaluationContext
 * JD-Core Version:    0.7.0.1
 */