/*  1:   */ package org.springframework.context.access;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.BeanFactory;
/*  4:   */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*  5:   */ import org.springframework.context.ApplicationContext;
/*  6:   */ import org.springframework.context.ConfigurableApplicationContext;
/*  7:   */ 
/*  8:   */ public class ContextBeanFactoryReference
/*  9:   */   implements BeanFactoryReference
/* 10:   */ {
/* 11:   */   private ApplicationContext applicationContext;
/* 12:   */   
/* 13:   */   public ContextBeanFactoryReference(ApplicationContext applicationContext)
/* 14:   */   {
/* 15:47 */     this.applicationContext = applicationContext;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public BeanFactory getFactory()
/* 19:   */   {
/* 20:52 */     if (this.applicationContext == null) {
/* 21:53 */       throw new IllegalStateException(
/* 22:54 */         "ApplicationContext owned by this BeanFactoryReference has been released");
/* 23:   */     }
/* 24:56 */     return this.applicationContext;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void release()
/* 28:   */   {
/* 29:60 */     if (this.applicationContext != null)
/* 30:   */     {
/* 31:64 */       synchronized (this)
/* 32:   */       {
/* 33:65 */         ApplicationContext savedCtx = this.applicationContext;
/* 34:66 */         this.applicationContext = null;
/* 35:   */       }
/* 36:   */       ApplicationContext savedCtx;
/* 37:69 */       if ((savedCtx != null) && ((savedCtx instanceof ConfigurableApplicationContext))) {
/* 38:70 */         ((ConfigurableApplicationContext)savedCtx).close();
/* 39:   */       }
/* 40:   */     }
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.access.ContextBeanFactoryReference
 * JD-Core Version:    0.7.0.1
 */