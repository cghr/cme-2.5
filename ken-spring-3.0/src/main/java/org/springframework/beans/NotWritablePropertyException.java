/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ public class NotWritablePropertyException
/*  4:   */   extends InvalidPropertyException
/*  5:   */ {
/*  6:29 */   private String[] possibleMatches = null;
/*  7:   */   
/*  8:   */   public NotWritablePropertyException(Class beanClass, String propertyName)
/*  9:   */   {
/* 10:39 */     super(beanClass, propertyName, "Bean property '" + propertyName + "' is not writable or has an invalid setter method: " + 
/* 11:40 */       "Does the return type of the getter match the parameter type of the setter?");
/* 12:   */   }
/* 13:   */   
/* 14:   */   public NotWritablePropertyException(Class beanClass, String propertyName, String msg)
/* 15:   */   {
/* 16:50 */     super(beanClass, propertyName, msg);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public NotWritablePropertyException(Class beanClass, String propertyName, String msg, Throwable cause)
/* 20:   */   {
/* 21:61 */     super(beanClass, propertyName, msg, cause);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public NotWritablePropertyException(Class beanClass, String propertyName, String msg, String[] possibleMatches)
/* 25:   */   {
/* 26:73 */     super(beanClass, propertyName, msg);
/* 27:74 */     this.possibleMatches = possibleMatches;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String[] getPossibleMatches()
/* 31:   */   {
/* 32:83 */     return this.possibleMatches;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.NotWritablePropertyException
 * JD-Core Version:    0.7.0.1
 */