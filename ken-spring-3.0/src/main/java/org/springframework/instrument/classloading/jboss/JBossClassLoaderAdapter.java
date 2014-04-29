package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;

abstract interface JBossClassLoaderAdapter
{
  public abstract void addTransformer(ClassFileTransformer paramClassFileTransformer);
  
  public abstract ClassLoader getInstrumentableClassLoader();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.jboss.JBossClassLoaderAdapter
 * JD-Core Version:    0.7.0.1
 */