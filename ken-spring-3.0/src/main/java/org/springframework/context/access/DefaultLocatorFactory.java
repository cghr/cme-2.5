/*  1:   */ package org.springframework.context.access;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.FatalBeanException;
/*  4:   */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*  5:   */ 
/*  6:   */ public class DefaultLocatorFactory
/*  7:   */ {
/*  8:   */   public static BeanFactoryLocator getInstance()
/*  9:   */     throws FatalBeanException
/* 10:   */   {
/* 11:36 */     return ContextSingletonBeanFactoryLocator.getInstance();
/* 12:   */   }
/* 13:   */   
/* 14:   */   public static BeanFactoryLocator getInstance(String selector)
/* 15:   */     throws FatalBeanException
/* 16:   */   {
/* 17:47 */     return ContextSingletonBeanFactoryLocator.getInstance(selector);
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.access.DefaultLocatorFactory
 * JD-Core Version:    0.7.0.1
 */