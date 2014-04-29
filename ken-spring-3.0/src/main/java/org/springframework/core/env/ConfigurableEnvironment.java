package org.springframework.core.env;

import java.util.Map;

public abstract interface ConfigurableEnvironment
  extends Environment, ConfigurablePropertyResolver
{
  public abstract void setActiveProfiles(String... paramVarArgs);
  
  public abstract void addActiveProfile(String paramString);
  
  public abstract void setDefaultProfiles(String... paramVarArgs);
  
  public abstract MutablePropertySources getPropertySources();
  
  public abstract Map<String, Object> getSystemEnvironment();
  
  public abstract Map<String, Object> getSystemProperties();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.ConfigurableEnvironment
 * JD-Core Version:    0.7.0.1
 */