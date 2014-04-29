/*  1:   */ package org.springframework.beans.factory;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.BeansException;
/*  4:   */ 
/*  5:   */ public class BeanNotOfRequiredTypeException
/*  6:   */   extends BeansException
/*  7:   */ {
/*  8:   */   private String beanName;
/*  9:   */   private Class requiredType;
/* 10:   */   private Class actualType;
/* 11:   */   
/* 12:   */   public BeanNotOfRequiredTypeException(String beanName, Class requiredType, Class actualType)
/* 13:   */   {
/* 14:48 */     super("Bean named '" + beanName + "' must be of type [" + requiredType.getName() + "], but was actually of type [" + actualType.getName() + "]");
/* 15:49 */     this.beanName = beanName;
/* 16:50 */     this.requiredType = requiredType;
/* 17:51 */     this.actualType = actualType;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getBeanName()
/* 21:   */   {
/* 22:59 */     return this.beanName;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Class getRequiredType()
/* 26:   */   {
/* 27:66 */     return this.requiredType;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Class getActualType()
/* 31:   */   {
/* 32:73 */     return this.actualType;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.BeanNotOfRequiredTypeException
 * JD-Core Version:    0.7.0.1
 */