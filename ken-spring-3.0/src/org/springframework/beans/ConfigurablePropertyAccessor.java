package org.springframework.beans;

import org.springframework.core.convert.ConversionService;

public abstract interface ConfigurablePropertyAccessor
  extends PropertyAccessor, PropertyEditorRegistry, TypeConverter
{
  public abstract void setConversionService(ConversionService paramConversionService);
  
  public abstract ConversionService getConversionService();
  
  public abstract void setExtractOldValueForEditor(boolean paramBoolean);
  
  public abstract boolean isExtractOldValueForEditor();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.ConfigurablePropertyAccessor
 * JD-Core Version:    0.7.0.1
 */