/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  4:   */ 
/*  5:   */ public class GenericBeanDefinition
/*  6:   */   extends AbstractBeanDefinition
/*  7:   */ {
/*  8:   */   private String parentName;
/*  9:   */   
/* 10:   */   public GenericBeanDefinition() {}
/* 11:   */   
/* 12:   */   public GenericBeanDefinition(BeanDefinition original)
/* 13:   */   {
/* 14:64 */     super(original);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void setParentName(String parentName)
/* 18:   */   {
/* 19:69 */     this.parentName = parentName;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String getParentName()
/* 23:   */   {
/* 24:73 */     return this.parentName;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public AbstractBeanDefinition cloneBeanDefinition()
/* 28:   */   {
/* 29:79 */     return new GenericBeanDefinition(this);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean equals(Object other)
/* 33:   */   {
/* 34:84 */     return (this == other) || (((other instanceof GenericBeanDefinition)) && (super.equals(other)));
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String toString()
/* 38:   */   {
/* 39:89 */     return "Generic bean: " + super.toString();
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.GenericBeanDefinition
 * JD-Core Version:    0.7.0.1
 */