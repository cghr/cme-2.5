package org.springframework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.springframework.beans.BeansException;

public abstract interface ListableBeanFactory
  extends BeanFactory
{
  public abstract boolean containsBeanDefinition(String paramString);
  
  public abstract int getBeanDefinitionCount();
  
  public abstract String[] getBeanDefinitionNames();
  
  public abstract String[] getBeanNamesForType(Class<?> paramClass);
  
  public abstract String[] getBeanNamesForType(Class<?> paramClass, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract <T> Map<String, T> getBeansOfType(Class<T> paramClass)
    throws BeansException;
  
  public abstract <T> Map<String, T> getBeansOfType(Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2)
    throws BeansException;
  
  public abstract Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> paramClass)
    throws BeansException;
  
  public abstract <A extends Annotation> A findAnnotationOnBean(String paramString, Class<A> paramClass);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.ListableBeanFactory
 * JD-Core Version:    0.7.0.1
 */