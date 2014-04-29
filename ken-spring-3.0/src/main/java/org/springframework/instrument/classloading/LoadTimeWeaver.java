package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;

public abstract interface LoadTimeWeaver
{
  public abstract void addTransformer(ClassFileTransformer paramClassFileTransformer);
  
  public abstract ClassLoader getInstrumentableClassLoader();
  
  public abstract ClassLoader getThrowawayClassLoader();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.LoadTimeWeaver
 * JD-Core Version:    0.7.0.1
 */