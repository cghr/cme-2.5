package org.springframework.jca.context;

import javax.resource.spi.BootstrapContext;
import org.springframework.beans.factory.Aware;

public abstract interface BootstrapContextAware
  extends Aware
{
  public abstract void setBootstrapContext(BootstrapContext paramBootstrapContext);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.context.BootstrapContextAware
 * JD-Core Version:    0.7.0.1
 */