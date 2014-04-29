package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public abstract interface BeanExpressionResolver
{
  public abstract Object evaluate(String paramString, BeanExpressionContext paramBeanExpressionContext)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanExpressionResolver
 * JD-Core Version:    0.7.0.1
 */