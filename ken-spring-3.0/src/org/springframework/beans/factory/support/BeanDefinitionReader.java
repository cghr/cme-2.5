package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract interface BeanDefinitionReader
{
  public abstract BeanDefinitionRegistry getRegistry();
  
  public abstract ResourceLoader getResourceLoader();
  
  public abstract ClassLoader getBeanClassLoader();
  
  public abstract BeanNameGenerator getBeanNameGenerator();
  
  public abstract int loadBeanDefinitions(Resource paramResource)
    throws BeanDefinitionStoreException;
  
  public abstract int loadBeanDefinitions(Resource... paramVarArgs)
    throws BeanDefinitionStoreException;
  
  public abstract int loadBeanDefinitions(String paramString)
    throws BeanDefinitionStoreException;
  
  public abstract int loadBeanDefinitions(String... paramVarArgs)
    throws BeanDefinitionStoreException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionReader
 * JD-Core Version:    0.7.0.1
 */