package org.springframework.context.annotation;

import org.springframework.instrument.classloading.LoadTimeWeaver;

public abstract interface LoadTimeWeavingConfigurer
{
  public abstract LoadTimeWeaver getLoadTimeWeaver();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.LoadTimeWeavingConfigurer
 * JD-Core Version:    0.7.0.1
 */