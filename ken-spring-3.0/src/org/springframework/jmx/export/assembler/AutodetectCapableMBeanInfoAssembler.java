package org.springframework.jmx.export.assembler;

public abstract interface AutodetectCapableMBeanInfoAssembler
  extends MBeanInfoAssembler
{
  public abstract boolean includeBean(Class<?> paramClass, String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.AutodetectCapableMBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */