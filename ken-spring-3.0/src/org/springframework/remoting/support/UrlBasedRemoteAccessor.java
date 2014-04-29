/*  1:   */ package org.springframework.remoting.support;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.InitializingBean;
/*  4:   */ 
/*  5:   */ public abstract class UrlBasedRemoteAccessor
/*  6:   */   extends RemoteAccessor
/*  7:   */   implements InitializingBean
/*  8:   */ {
/*  9:   */   private String serviceUrl;
/* 10:   */   
/* 11:   */   public void setServiceUrl(String serviceUrl)
/* 12:   */   {
/* 13:38 */     this.serviceUrl = serviceUrl;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getServiceUrl()
/* 17:   */   {
/* 18:45 */     return this.serviceUrl;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void afterPropertiesSet()
/* 22:   */   {
/* 23:50 */     if (getServiceUrl() == null) {
/* 24:51 */       throw new IllegalArgumentException("Property 'serviceUrl' is required");
/* 25:   */     }
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.UrlBasedRemoteAccessor
 * JD-Core Version:    0.7.0.1
 */