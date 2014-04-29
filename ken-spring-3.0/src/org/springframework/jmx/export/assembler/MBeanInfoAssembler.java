package org.springframework.jmx.export.assembler;

import javax.management.JMException;
import javax.management.modelmbean.ModelMBeanInfo;

public abstract interface MBeanInfoAssembler
{
  public abstract ModelMBeanInfo getMBeanInfo(Object paramObject, String paramString)
    throws JMException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.MBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */