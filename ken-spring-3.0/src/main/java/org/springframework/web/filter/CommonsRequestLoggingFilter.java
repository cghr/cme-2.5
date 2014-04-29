/*  1:   */ package org.springframework.web.filter;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import org.apache.commons.logging.Log;
/*  5:   */ 
/*  6:   */ public class CommonsRequestLoggingFilter
/*  7:   */   extends AbstractRequestLoggingFilter
/*  8:   */ {
/*  9:   */   protected void beforeRequest(HttpServletRequest request, String message)
/* 10:   */   {
/* 11:41 */     if (this.logger.isDebugEnabled()) {
/* 12:42 */       this.logger.debug(message);
/* 13:   */     }
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected void afterRequest(HttpServletRequest request, String message)
/* 17:   */   {
/* 18:51 */     if (this.logger.isDebugEnabled()) {
/* 19:52 */       this.logger.debug(message);
/* 20:   */     }
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.CommonsRequestLoggingFilter
 * JD-Core Version:    0.7.0.1
 */