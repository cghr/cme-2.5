/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ public class NotReadablePropertyException
/*  4:   */   extends InvalidPropertyException
/*  5:   */ {
/*  6:   */   public NotReadablePropertyException(Class beanClass, String propertyName)
/*  7:   */   {
/*  8:35 */     super(beanClass, propertyName, "Bean property '" + propertyName + "' is not readable or has an invalid getter method: " + 
/*  9:36 */       "Does the return type of the getter match the parameter type of the setter?");
/* 10:   */   }
/* 11:   */   
/* 12:   */   public NotReadablePropertyException(Class beanClass, String propertyName, String msg)
/* 13:   */   {
/* 14:46 */     super(beanClass, propertyName, msg);
/* 15:   */   }
/* 16:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.NotReadablePropertyException
 * JD-Core Version:    0.7.0.1
 */