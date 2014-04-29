/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ public class NullValueInNestedPathException
/*  4:   */   extends InvalidPropertyException
/*  5:   */ {
/*  6:   */   public NullValueInNestedPathException(Class beanClass, String propertyName)
/*  7:   */   {
/*  8:36 */     super(beanClass, propertyName, "Value of nested property '" + propertyName + "' is null");
/*  9:   */   }
/* 10:   */   
/* 11:   */   public NullValueInNestedPathException(Class beanClass, String propertyName, String msg)
/* 12:   */   {
/* 13:46 */     super(beanClass, propertyName, msg);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.NullValueInNestedPathException
 * JD-Core Version:    0.7.0.1
 */