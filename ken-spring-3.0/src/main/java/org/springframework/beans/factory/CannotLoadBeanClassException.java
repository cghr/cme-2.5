/*  1:   */ package org.springframework.beans.factory;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.FatalBeanException;
/*  4:   */ 
/*  5:   */ public class CannotLoadBeanClassException
/*  6:   */   extends FatalBeanException
/*  7:   */ {
/*  8:   */   private String resourceDescription;
/*  9:   */   private String beanName;
/* 10:   */   private String beanClassName;
/* 11:   */   
/* 12:   */   public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName, ClassNotFoundException cause)
/* 13:   */   {
/* 14:49 */     super("Cannot find class [" + beanClassName + "] for bean with name '" + beanName + "' defined in " + resourceDescription, cause);
/* 15:50 */     this.resourceDescription = resourceDescription;
/* 16:51 */     this.beanName = beanName;
/* 17:52 */     this.beanClassName = beanClassName;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName, LinkageError cause)
/* 21:   */   {
/* 22:67 */     super("Error loading class [" + beanClassName + "] for bean with name '" + beanName + "' defined in " + resourceDescription + ": problem with class file or dependent class", cause);
/* 23:68 */     this.resourceDescription = resourceDescription;
/* 24:69 */     this.beanName = beanName;
/* 25:70 */     this.beanClassName = beanClassName;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String getResourceDescription()
/* 29:   */   {
/* 30:79 */     return this.resourceDescription;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String getBeanName()
/* 34:   */   {
/* 35:86 */     return this.beanName;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String getBeanClassName()
/* 39:   */   {
/* 40:93 */     return this.beanClassName;
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.CannotLoadBeanClassException
 * JD-Core Version:    0.7.0.1
 */