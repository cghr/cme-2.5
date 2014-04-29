package org.springframework.core;

public abstract interface AliasRegistry
{
  public abstract void registerAlias(String paramString1, String paramString2);
  
  public abstract void removeAlias(String paramString);
  
  public abstract boolean isAlias(String paramString);
  
  public abstract String[] getAliases(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.AliasRegistry
 * JD-Core Version:    0.7.0.1
 */