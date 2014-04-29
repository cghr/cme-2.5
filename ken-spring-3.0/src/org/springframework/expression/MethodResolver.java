package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;

public abstract interface MethodResolver
{
  public abstract MethodExecutor resolve(EvaluationContext paramEvaluationContext, Object paramObject, String paramString, List<TypeDescriptor> paramList)
    throws AccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.MethodResolver
 * JD-Core Version:    0.7.0.1
 */