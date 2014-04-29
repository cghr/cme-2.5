package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.env.Environment;

public abstract interface EnvironmentAware
  extends Aware
{
  public abstract void setEnvironment(Environment paramEnvironment);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.EnvironmentAware
 * JD-Core Version:    0.7.0.1
 */