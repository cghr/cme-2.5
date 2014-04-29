package org.springframework.jmx.export;

import javax.management.ObjectName;

public abstract interface MBeanExporterListener
{
  public abstract void mbeanRegistered(ObjectName paramObjectName);
  
  public abstract void mbeanUnregistered(ObjectName paramObjectName);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.MBeanExporterListener
 * JD-Core Version:    0.7.0.1
 */