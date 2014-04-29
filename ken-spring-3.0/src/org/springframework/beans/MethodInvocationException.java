/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyChangeEvent;
/*  4:   */ 
/*  5:   */ public class MethodInvocationException
/*  6:   */   extends PropertyAccessException
/*  7:   */ {
/*  8:   */   public static final String ERROR_CODE = "methodInvocation";
/*  9:   */   
/* 10:   */   public MethodInvocationException(PropertyChangeEvent propertyChangeEvent, Throwable cause)
/* 11:   */   {
/* 12:41 */     super(propertyChangeEvent, "Property '" + propertyChangeEvent.getPropertyName() + "' threw exception", cause);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public String getErrorCode()
/* 16:   */   {
/* 17:45 */     return "methodInvocation";
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.MethodInvocationException
 * JD-Core Version:    0.7.0.1
 */