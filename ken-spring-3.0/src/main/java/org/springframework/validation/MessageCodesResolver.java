package org.springframework.validation;

public abstract interface MessageCodesResolver
{
  public abstract String[] resolveMessageCodes(String paramString1, String paramString2);
  
  public abstract String[] resolveMessageCodes(String paramString1, String paramString2, String paramString3, Class<?> paramClass);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.MessageCodesResolver
 * JD-Core Version:    0.7.0.1
 */