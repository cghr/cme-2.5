package org.springframework.asm.commons;

import org.springframework.asm.Label;

public abstract interface TableSwitchGenerator
{
  public abstract void generateCase(int paramInt, Label paramLabel);
  
  public abstract void generateDefault();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.commons.TableSwitchGenerator
 * JD-Core Version:    0.7.0.1
 */