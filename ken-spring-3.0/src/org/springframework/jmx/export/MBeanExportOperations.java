package org.springframework.jmx.export;

import javax.management.ObjectName;

public abstract interface MBeanExportOperations
{
  public abstract ObjectName registerManagedResource(Object paramObject)
    throws MBeanExportException;
  
  public abstract void registerManagedResource(Object paramObject, ObjectName paramObjectName)
    throws MBeanExportException;
  
  public abstract void unregisterManagedResource(ObjectName paramObjectName);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.MBeanExportOperations
 * JD-Core Version:    0.7.0.1
 */