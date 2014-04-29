/*  1:   */ package org.springframework.ejb.support;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.BeansException;
/*  4:   */ import org.springframework.beans.FatalBeanException;
/*  5:   */ 
/*  6:   */ public abstract class AbstractStatefulSessionBean
/*  7:   */   extends AbstractSessionBean
/*  8:   */ {
/*  9:   */   protected void loadBeanFactory()
/* 10:   */     throws BeansException
/* 11:   */   {
/* 12:59 */     super.loadBeanFactory();
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected void unloadBeanFactory()
/* 16:   */     throws FatalBeanException
/* 17:   */   {
/* 18:72 */     super.unloadBeanFactory();
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.support.AbstractStatefulSessionBean
 * JD-Core Version:    0.7.0.1
 */