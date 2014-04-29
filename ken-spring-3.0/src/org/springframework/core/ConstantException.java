/*  1:   */ package org.springframework.core;
/*  2:   */ 
/*  3:   */ public class ConstantException
/*  4:   */   extends IllegalArgumentException
/*  5:   */ {
/*  6:   */   public ConstantException(String className, String field, String message)
/*  7:   */   {
/*  8:36 */     super("Field '" + field + "' " + message + " in class [" + className + "]");
/*  9:   */   }
/* 10:   */   
/* 11:   */   public ConstantException(String className, String namePrefix, Object value)
/* 12:   */   {
/* 13:46 */     super("No '" + namePrefix + "' field with value '" + value + "' found in class [" + className + "]");
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.ConstantException
 * JD-Core Version:    0.7.0.1
 */