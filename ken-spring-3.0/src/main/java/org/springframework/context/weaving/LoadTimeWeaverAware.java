package org.springframework.context.weaving;

import org.springframework.beans.factory.Aware;
import org.springframework.instrument.classloading.LoadTimeWeaver;

public abstract interface LoadTimeWeaverAware
  extends Aware
{
  public abstract void setLoadTimeWeaver(LoadTimeWeaver paramLoadTimeWeaver);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.weaving.LoadTimeWeaverAware
 * JD-Core Version:    0.7.0.1
 */