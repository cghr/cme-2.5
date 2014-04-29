/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.springframework.web.servlet.HandlerInterceptor;
/*  6:   */ import org.springframework.web.servlet.ModelAndView;
/*  7:   */ 
/*  8:   */ public abstract class HandlerInterceptorAdapter
/*  9:   */   implements HandlerInterceptor
/* 10:   */ {
/* 11:   */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 12:   */     throws Exception
/* 13:   */   {
/* 14:39 */     return true;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
/* 18:   */     throws Exception
/* 19:   */   {}
/* 20:   */   
/* 21:   */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/* 22:   */     throws Exception
/* 23:   */   {}
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.HandlerInterceptorAdapter
 * JD-Core Version:    0.7.0.1
 */