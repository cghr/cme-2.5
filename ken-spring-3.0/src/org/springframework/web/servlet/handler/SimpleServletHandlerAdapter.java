/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import javax.servlet.Servlet;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import javax.servlet.http.HttpServletResponse;
/*  6:   */ import org.springframework.web.servlet.HandlerAdapter;
/*  7:   */ import org.springframework.web.servlet.ModelAndView;
/*  8:   */ 
/*  9:   */ public class SimpleServletHandlerAdapter
/* 10:   */   implements HandlerAdapter
/* 11:   */ {
/* 12:   */   public boolean supports(Object handler)
/* 13:   */   {
/* 14:57 */     return handler instanceof Servlet;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 18:   */     throws Exception
/* 19:   */   {
/* 20:63 */     ((Servlet)handler).service(request, response);
/* 21:64 */     return null;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public long getLastModified(HttpServletRequest request, Object handler)
/* 25:   */   {
/* 26:68 */     return -1L;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.SimpleServletHandlerAdapter
 * JD-Core Version:    0.7.0.1
 */