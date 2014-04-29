package org.springframework.jdbc.core.namedparam;

public abstract interface SqlParameterSource
{
  public static final int TYPE_UNKNOWN = -2147483648;
  
  public abstract boolean hasValue(String paramString);
  
  public abstract Object getValue(String paramString)
    throws IllegalArgumentException;
  
  public abstract int getSqlType(String paramString);
  
  public abstract String getTypeName(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.SqlParameterSource
 * JD-Core Version:    0.7.0.1
 */