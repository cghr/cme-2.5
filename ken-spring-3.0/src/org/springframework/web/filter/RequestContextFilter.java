/*  1:   */ package org.springframework.web.filter;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.FilterChain;
/*  5:   */ import javax.servlet.ServletException;
/*  6:   */ import javax.servlet.http.HttpServletRequest;
/*  7:   */ import javax.servlet.http.HttpServletResponse;
/*  8:   */ import org.apache.commons.logging.Log;
/*  9:   */ import org.springframework.context.i18n.LocaleContextHolder;
/* 10:   */ import org.springframework.web.context.request.RequestContextHolder;
/* 11:   */ import org.springframework.web.context.request.ServletRequestAttributes;
/* 12:   */ 
/* 13:   */ public class RequestContextFilter
/* 14:   */   extends OncePerRequestFilter
/* 15:   */ {
/* 16:51 */   private boolean threadContextInheritable = false;
/* 17:   */   
/* 18:   */   public void setThreadContextInheritable(boolean threadContextInheritable)
/* 19:   */   {
/* 20:67 */     this.threadContextInheritable = threadContextInheritable;
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/* 24:   */     throws ServletException, IOException
/* 25:   */   {
/* 26:76 */     ServletRequestAttributes attributes = new ServletRequestAttributes(request);
/* 27:77 */     LocaleContextHolder.setLocale(request.getLocale(), this.threadContextInheritable);
/* 28:78 */     RequestContextHolder.setRequestAttributes(attributes, this.threadContextInheritable);
/* 29:79 */     if (this.logger.isDebugEnabled()) {
/* 30:80 */       this.logger.debug("Bound request context to thread: " + request);
/* 31:   */     }
/* 32:   */     try
/* 33:   */     {
/* 34:83 */       filterChain.doFilter(request, response);
/* 35:   */     }
/* 36:   */     finally
/* 37:   */     {
/* 38:86 */       LocaleContextHolder.resetLocaleContext();
/* 39:87 */       RequestContextHolder.resetRequestAttributes();
/* 40:88 */       attributes.requestCompleted();
/* 41:89 */       if (this.logger.isDebugEnabled()) {
/* 42:90 */         this.logger.debug("Cleared thread-bound request context: " + request);
/* 43:   */       }
/* 44:   */     }
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.RequestContextFilter
 * JD-Core Version:    0.7.0.1
 */