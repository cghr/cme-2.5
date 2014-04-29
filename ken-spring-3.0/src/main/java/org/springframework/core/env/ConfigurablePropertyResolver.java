package org.springframework.core.env;

import org.springframework.core.convert.support.ConfigurableConversionService;

public abstract interface ConfigurablePropertyResolver
  extends PropertyResolver
{
  public abstract ConfigurableConversionService getConversionService();
  
  public abstract void setConversionService(ConfigurableConversionService paramConfigurableConversionService);
  
  public abstract void setPlaceholderPrefix(String paramString);
  
  public abstract void setPlaceholderSuffix(String paramString);
  
  public abstract void setValueSeparator(String paramString);
  
  public abstract void setRequiredProperties(String... paramVarArgs);
  
  public abstract void validateRequiredProperties()
    throws MissingRequiredPropertiesException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.ConfigurablePropertyResolver
 * JD-Core Version:    0.7.0.1
 */