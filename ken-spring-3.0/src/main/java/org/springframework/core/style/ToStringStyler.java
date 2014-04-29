package org.springframework.core.style;

public abstract interface ToStringStyler
{
  public abstract void styleStart(StringBuilder paramStringBuilder, Object paramObject);
  
  public abstract void styleEnd(StringBuilder paramStringBuilder, Object paramObject);
  
  public abstract void styleField(StringBuilder paramStringBuilder, String paramString, Object paramObject);
  
  public abstract void styleValue(StringBuilder paramStringBuilder, Object paramObject);
  
  public abstract void styleFieldSeparator(StringBuilder paramStringBuilder);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.style.ToStringStyler
 * JD-Core Version:    0.7.0.1
 */