/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  4:   */ import org.springframework.beans.factory.config.BeanReference;
/*  5:   */ 
/*  6:   */ public abstract class AbstractComponentDefinition
/*  7:   */   implements ComponentDefinition
/*  8:   */ {
/*  9:   */   public String getDescription()
/* 10:   */   {
/* 11:39 */     return getName();
/* 12:   */   }
/* 13:   */   
/* 14:   */   public BeanDefinition[] getBeanDefinitions()
/* 15:   */   {
/* 16:46 */     return new BeanDefinition[0];
/* 17:   */   }
/* 18:   */   
/* 19:   */   public BeanDefinition[] getInnerBeanDefinitions()
/* 20:   */   {
/* 21:53 */     return new BeanDefinition[0];
/* 22:   */   }
/* 23:   */   
/* 24:   */   public BeanReference[] getBeanReferences()
/* 25:   */   {
/* 26:60 */     return new BeanReference[0];
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String toString()
/* 30:   */   {
/* 31:68 */     return getDescription();
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.AbstractComponentDefinition
 * JD-Core Version:    0.7.0.1
 */