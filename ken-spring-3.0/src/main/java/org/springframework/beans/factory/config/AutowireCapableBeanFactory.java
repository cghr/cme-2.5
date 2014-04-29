package org.springframework.beans.factory.config;

import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;

public abstract interface AutowireCapableBeanFactory
  extends BeanFactory
{
  public static final int AUTOWIRE_NO = 0;
  public static final int AUTOWIRE_BY_NAME = 1;
  public static final int AUTOWIRE_BY_TYPE = 2;
  public static final int AUTOWIRE_CONSTRUCTOR = 3;
  @Deprecated
  public static final int AUTOWIRE_AUTODETECT = 4;
  
  public abstract <T> T createBean(Class<T> paramClass)
    throws BeansException;
  
  public abstract void autowireBean(Object paramObject)
    throws BeansException;
  
  public abstract Object configureBean(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object resolveDependency(DependencyDescriptor paramDependencyDescriptor, String paramString)
    throws BeansException;
  
  public abstract Object createBean(Class paramClass, int paramInt, boolean paramBoolean)
    throws BeansException;
  
  public abstract Object autowire(Class paramClass, int paramInt, boolean paramBoolean)
    throws BeansException;
  
  public abstract void autowireBeanProperties(Object paramObject, int paramInt, boolean paramBoolean)
    throws BeansException;
  
  public abstract void applyBeanPropertyValues(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object initializeBean(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object applyBeanPostProcessorsBeforeInitialization(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object applyBeanPostProcessorsAfterInitialization(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object resolveDependency(DependencyDescriptor paramDependencyDescriptor, String paramString, Set<String> paramSet, TypeConverter paramTypeConverter)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.AutowireCapableBeanFactory
 * JD-Core Version:    0.7.0.1
 */