package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public abstract interface ObjectNamingStrategy
{
  public abstract ObjectName getObjectName(Object paramObject, String paramString)
    throws MalformedObjectNameException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.naming.ObjectNamingStrategy
 * JD-Core Version:    0.7.0.1
 */