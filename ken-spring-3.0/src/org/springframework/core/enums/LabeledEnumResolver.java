package org.springframework.core.enums;

import java.util.Map;
import java.util.Set;

@Deprecated
public abstract interface LabeledEnumResolver
{
  public abstract Set getLabeledEnumSet(Class paramClass)
    throws IllegalArgumentException;
  
  public abstract Map getLabeledEnumMap(Class paramClass)
    throws IllegalArgumentException;
  
  public abstract LabeledEnum getLabeledEnumByCode(Class paramClass, Comparable paramComparable)
    throws IllegalArgumentException;
  
  public abstract LabeledEnum getLabeledEnumByLabel(Class paramClass, String paramString)
    throws IllegalArgumentException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.LabeledEnumResolver
 * JD-Core Version:    0.7.0.1
 */