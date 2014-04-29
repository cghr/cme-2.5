/*  1:   */ package com.kentropy.util;
/*  2:   */ 
/*  3:   */ import org.apache.log4j.Logger;
/*  4:   */ import org.springframework.beans.BeansException;
/*  5:   */ import org.springframework.context.ApplicationContext;
/*  6:   */ import org.springframework.context.ApplicationContextAware;
/*  7:   */ 
/*  8:   */ public class SpringApplicationContext
/*  9:   */   implements ApplicationContextAware
/* 10:   */ {
/* 11:   */   private static ApplicationContext appContext;
/* 12:14 */   Logger log = Logger.getLogger(SpringApplicationContext.class);
/* 13:   */   
/* 14:   */   public void setApplicationContext(ApplicationContext context)
/* 15:   */     throws BeansException
/* 16:   */   {
/* 17:20 */     appContext = context;
/* 18:21 */     this.log.info("==> Loaded AppContext to SpringApplicationContext");
/* 19:22 */     this.log.info(appContext);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public static Object getBean(String beanId)
/* 23:   */   {
/* 24:33 */     return appContext.getBean(beanId);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public static ApplicationContext getApplicationContext()
/* 28:   */   {
/* 29:38 */     return appContext;
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.SpringApplicationContext
 * JD-Core Version:    0.7.0.1
 */