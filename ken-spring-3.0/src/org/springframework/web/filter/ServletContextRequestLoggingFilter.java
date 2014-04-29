/*  1:   */ package org.springframework.web.filter;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletContext;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ 
/*  6:   */ public class ServletContextRequestLoggingFilter
/*  7:   */   extends AbstractRequestLoggingFilter
/*  8:   */ {
/*  9:   */   protected void beforeRequest(HttpServletRequest request, String message)
/* 10:   */   {
/* 11:41 */     getServletContext().log(message);
/* 12:   */   }
/* 13:   */   
/* 14:   */   protected void afterRequest(HttpServletRequest request, String message)
/* 15:   */   {
/* 16:49 */     getServletContext().log(message);
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.ServletContextRequestLoggingFilter
 * JD-Core Version:    0.7.0.1
 */