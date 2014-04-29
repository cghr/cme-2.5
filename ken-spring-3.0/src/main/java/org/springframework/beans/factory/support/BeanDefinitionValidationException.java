/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.FatalBeanException;
/*  4:   */ 
/*  5:   */ public class BeanDefinitionValidationException
/*  6:   */   extends FatalBeanException
/*  7:   */ {
/*  8:   */   public BeanDefinitionValidationException(String msg)
/*  9:   */   {
/* 10:35 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public BeanDefinitionValidationException(String msg, Throwable cause)
/* 14:   */   {
/* 15:45 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionValidationException
 * JD-Core Version:    0.7.0.1
 */