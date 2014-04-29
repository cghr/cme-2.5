/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ public class BeanEntry
/*  4:   */   implements ParseState.Entry
/*  5:   */ {
/*  6:   */   private String beanDefinitionName;
/*  7:   */   
/*  8:   */   public BeanEntry(String beanDefinitionName)
/*  9:   */   {
/* 10:35 */     this.beanDefinitionName = beanDefinitionName;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public String toString()
/* 14:   */   {
/* 15:41 */     return "Bean '" + this.beanDefinitionName + "'";
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.BeanEntry
 * JD-Core Version:    0.7.0.1
 */