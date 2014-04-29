/*  1:   */ package org.springframework.beans.factory.config;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ public class RuntimeBeanNameReference
/*  6:   */   implements BeanReference
/*  7:   */ {
/*  8:   */   private final String beanName;
/*  9:   */   private Object source;
/* 10:   */   
/* 11:   */   public RuntimeBeanNameReference(String beanName)
/* 12:   */   {
/* 13:43 */     Assert.hasText(beanName, "'beanName' must not be empty");
/* 14:44 */     this.beanName = beanName;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String getBeanName()
/* 18:   */   {
/* 19:48 */     return this.beanName;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setSource(Object source)
/* 23:   */   {
/* 24:56 */     this.source = source;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Object getSource()
/* 28:   */   {
/* 29:60 */     return this.source;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean equals(Object other)
/* 33:   */   {
/* 34:66 */     if (this == other) {
/* 35:67 */       return true;
/* 36:   */     }
/* 37:69 */     if (!(other instanceof RuntimeBeanNameReference)) {
/* 38:70 */       return false;
/* 39:   */     }
/* 40:72 */     RuntimeBeanNameReference that = (RuntimeBeanNameReference)other;
/* 41:73 */     return this.beanName.equals(that.beanName);
/* 42:   */   }
/* 43:   */   
/* 44:   */   public int hashCode()
/* 45:   */   {
/* 46:78 */     return this.beanName.hashCode();
/* 47:   */   }
/* 48:   */   
/* 49:   */   public String toString()
/* 50:   */   {
/* 51:83 */     return '<' + getBeanName() + '>';
/* 52:   */   }
/* 53:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.RuntimeBeanNameReference
 * JD-Core Version:    0.7.0.1
 */