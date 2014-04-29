/*  1:   */ package org.springframework.beans.factory;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.FatalBeanException;
/*  4:   */ 
/*  5:   */ public class FactoryBeanNotInitializedException
/*  6:   */   extends FatalBeanException
/*  7:   */ {
/*  8:   */   public FactoryBeanNotInitializedException()
/*  9:   */   {
/* 10:43 */     super("FactoryBean is not fully initialized yet");
/* 11:   */   }
/* 12:   */   
/* 13:   */   public FactoryBeanNotInitializedException(String msg)
/* 14:   */   {
/* 15:51 */     super(msg);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.FactoryBeanNotInitializedException
 * JD-Core Version:    0.7.0.1
 */