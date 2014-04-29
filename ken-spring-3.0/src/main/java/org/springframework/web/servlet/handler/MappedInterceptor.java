/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import org.springframework.util.PathMatcher;
/*  4:   */ import org.springframework.web.context.request.WebRequestInterceptor;
/*  5:   */ import org.springframework.web.servlet.HandlerInterceptor;
/*  6:   */ 
/*  7:   */ public final class MappedInterceptor
/*  8:   */ {
/*  9:   */   private final String[] pathPatterns;
/* 10:   */   private final HandlerInterceptor interceptor;
/* 11:   */   
/* 12:   */   public MappedInterceptor(String[] pathPatterns, HandlerInterceptor interceptor)
/* 13:   */   {
/* 14:44 */     this.pathPatterns = pathPatterns;
/* 15:45 */     this.interceptor = interceptor;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public MappedInterceptor(String[] pathPatterns, WebRequestInterceptor interceptor)
/* 19:   */   {
/* 20:54 */     this.pathPatterns = pathPatterns;
/* 21:55 */     this.interceptor = new WebRequestHandlerInterceptorAdapter(interceptor);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String[] getPathPatterns()
/* 25:   */   {
/* 26:63 */     return this.pathPatterns;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public HandlerInterceptor getInterceptor()
/* 30:   */   {
/* 31:70 */     return this.interceptor;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public boolean matches(String lookupPath, PathMatcher pathMatcher)
/* 35:   */   {
/* 36:79 */     if (this.pathPatterns == null) {
/* 37:80 */       return true;
/* 38:   */     }
/* 39:83 */     for (String pathPattern : this.pathPatterns) {
/* 40:84 */       if (pathMatcher.match(pathPattern, lookupPath)) {
/* 41:85 */         return true;
/* 42:   */       }
/* 43:   */     }
/* 44:88 */     return false;
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.MappedInterceptor
 * JD-Core Version:    0.7.0.1
 */