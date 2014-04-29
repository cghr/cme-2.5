package org.springframework.core.env;

public abstract interface Environment
  extends PropertyResolver
{
  public abstract String[] getActiveProfiles();
  
  public abstract String[] getDefaultProfiles();
  
  public abstract boolean acceptsProfiles(String... paramVarArgs);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.Environment
 * JD-Core Version:    0.7.0.1
 */