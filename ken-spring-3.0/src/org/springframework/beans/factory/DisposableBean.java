package org.springframework.beans.factory;

public abstract interface DisposableBean
{
  public abstract void destroy()
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.DisposableBean
 * JD-Core Version:    0.7.0.1
 */