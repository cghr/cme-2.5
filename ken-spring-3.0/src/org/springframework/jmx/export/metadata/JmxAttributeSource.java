package org.springframework.jmx.export.metadata;

import java.lang.reflect.Method;

public abstract interface JmxAttributeSource
{
  public abstract ManagedResource getManagedResource(Class<?> paramClass)
    throws InvalidMetadataException;
  
  public abstract ManagedAttribute getManagedAttribute(Method paramMethod)
    throws InvalidMetadataException;
  
  public abstract ManagedMetric getManagedMetric(Method paramMethod)
    throws InvalidMetadataException;
  
  public abstract ManagedOperation getManagedOperation(Method paramMethod)
    throws InvalidMetadataException;
  
  public abstract ManagedOperationParameter[] getManagedOperationParameters(Method paramMethod)
    throws InvalidMetadataException;
  
  public abstract ManagedNotification[] getManagedNotifications(Class<?> paramClass)
    throws InvalidMetadataException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.metadata.JmxAttributeSource
 * JD-Core Version:    0.7.0.1
 */