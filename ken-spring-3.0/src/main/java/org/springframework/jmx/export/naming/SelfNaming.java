package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public abstract interface SelfNaming
{
  public abstract ObjectName getObjectName()
    throws MalformedObjectNameException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.naming.SelfNaming
 * JD-Core Version:    0.7.0.1
 */