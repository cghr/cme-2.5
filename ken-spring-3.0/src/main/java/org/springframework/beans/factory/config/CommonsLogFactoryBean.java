/*  1:   */ package org.springframework.beans.factory.config;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.apache.commons.logging.LogFactory;
/*  5:   */ import org.springframework.beans.factory.FactoryBean;
/*  6:   */ import org.springframework.beans.factory.InitializingBean;
/*  7:   */ 
/*  8:   */ public class CommonsLogFactoryBean
/*  9:   */   implements FactoryBean<Log>, InitializingBean
/* 10:   */ {
/* 11:   */   private Log log;
/* 12:   */   
/* 13:   */   public void setLogName(String logName)
/* 14:   */   {
/* 15:48 */     this.log = LogFactory.getLog(logName);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void afterPropertiesSet()
/* 19:   */   {
/* 20:53 */     if (this.log == null) {
/* 21:54 */       throw new IllegalArgumentException("'logName' is required");
/* 22:   */     }
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Log getObject()
/* 26:   */   {
/* 27:59 */     return this.log;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Class<? extends Log> getObjectType()
/* 31:   */   {
/* 32:63 */     return this.log != null ? this.log.getClass() : Log.class;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public boolean isSingleton()
/* 36:   */   {
/* 37:67 */     return true;
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.CommonsLogFactoryBean
 * JD-Core Version:    0.7.0.1
 */