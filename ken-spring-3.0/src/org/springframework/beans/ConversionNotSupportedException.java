/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyChangeEvent;
/*  4:   */ 
/*  5:   */ public class ConversionNotSupportedException
/*  6:   */   extends TypeMismatchException
/*  7:   */ {
/*  8:   */   public ConversionNotSupportedException(PropertyChangeEvent propertyChangeEvent, Class requiredType, Throwable cause)
/*  9:   */   {
/* 10:37 */     super(propertyChangeEvent, requiredType, cause);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public ConversionNotSupportedException(Object value, Class requiredType, Throwable cause)
/* 14:   */   {
/* 15:47 */     super(value, requiredType, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.ConversionNotSupportedException
 * JD-Core Version:    0.7.0.1
 */