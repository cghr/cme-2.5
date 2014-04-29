/*  1:   */ package org.springframework.web.servlet.mvc;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.springframework.web.HttpRequestHandler;
/*  6:   */ import org.springframework.web.servlet.HandlerAdapter;
/*  7:   */ import org.springframework.web.servlet.ModelAndView;
/*  8:   */ 
/*  9:   */ public class HttpRequestHandlerAdapter
/* 10:   */   implements HandlerAdapter
/* 11:   */ {
/* 12:   */   public boolean supports(Object handler)
/* 13:   */   {
/* 14:43 */     return handler instanceof HttpRequestHandler;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 18:   */     throws Exception
/* 19:   */   {
/* 20:49 */     ((HttpRequestHandler)handler).handleRequest(request, response);
/* 21:50 */     return null;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public long getLastModified(HttpServletRequest request, Object handler)
/* 25:   */   {
/* 26:54 */     if ((handler instanceof LastModified)) {
/* 27:55 */       return ((LastModified)handler).getLastModified(request);
/* 28:   */     }
/* 29:57 */     return -1L;
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter
 * JD-Core Version:    0.7.0.1
 */