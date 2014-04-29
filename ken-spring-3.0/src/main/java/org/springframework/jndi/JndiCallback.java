package org.springframework.jndi;

import javax.naming.Context;
import javax.naming.NamingException;

public abstract interface JndiCallback<T>
{
  public abstract T doInContext(Context paramContext)
    throws NamingException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiCallback
 * JD-Core Version:    0.7.0.1
 */