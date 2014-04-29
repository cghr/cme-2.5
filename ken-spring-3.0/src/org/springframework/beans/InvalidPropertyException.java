/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ public class InvalidPropertyException
/*  4:   */   extends FatalBeanException
/*  5:   */ {
/*  6:   */   private Class beanClass;
/*  7:   */   private String propertyName;
/*  8:   */   
/*  9:   */   public InvalidPropertyException(Class beanClass, String propertyName, String msg)
/* 10:   */   {
/* 11:40 */     this(beanClass, propertyName, msg, null);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public InvalidPropertyException(Class beanClass, String propertyName, String msg, Throwable cause)
/* 15:   */   {
/* 16:51 */     super("Invalid property '" + propertyName + "' of bean class [" + beanClass.getName() + "]: " + msg, cause);
/* 17:52 */     this.beanClass = beanClass;
/* 18:53 */     this.propertyName = propertyName;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Class getBeanClass()
/* 22:   */   {
/* 23:60 */     return this.beanClass;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String getPropertyName()
/* 27:   */   {
/* 28:67 */     return this.propertyName;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.InvalidPropertyException
 * JD-Core Version:    0.7.0.1
 */