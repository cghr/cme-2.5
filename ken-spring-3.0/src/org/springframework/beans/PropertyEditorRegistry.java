package org.springframework.beans;

import java.beans.PropertyEditor;

public abstract interface PropertyEditorRegistry
{
  public abstract void registerCustomEditor(Class<?> paramClass, PropertyEditor paramPropertyEditor);
  
  public abstract void registerCustomEditor(Class<?> paramClass, String paramString, PropertyEditor paramPropertyEditor);
  
  public abstract PropertyEditor findCustomEditor(Class<?> paramClass, String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyEditorRegistry
 * JD-Core Version:    0.7.0.1
 */