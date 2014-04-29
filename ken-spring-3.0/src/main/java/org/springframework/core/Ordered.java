package org.springframework.core;

public abstract interface Ordered
{
  public static final int HIGHEST_PRECEDENCE = -2147483648;
  public static final int LOWEST_PRECEDENCE = 2147483647;
  
  public abstract int getOrder();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.Ordered
 * JD-Core Version:    0.7.0.1
 */