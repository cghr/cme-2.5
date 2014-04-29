package org.springframework.context;

public abstract interface MessageSourceResolvable
{
  public abstract String[] getCodes();
  
  public abstract Object[] getArguments();
  
  public abstract String getDefaultMessage();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.MessageSourceResolvable
 * JD-Core Version:    0.7.0.1
 */