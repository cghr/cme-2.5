/*  1:   */ package org.springframework.web.servlet.mvc;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.springframework.web.servlet.HandlerAdapter;
/*  6:   */ import org.springframework.web.servlet.ModelAndView;
/*  7:   */ 
/*  8:   */ public class SimpleControllerHandlerAdapter
/*  9:   */   implements HandlerAdapter
/* 10:   */ {
/* 11:   */   public boolean supports(Object handler)
/* 12:   */   {
/* 13:42 */     return handler instanceof Controller;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 17:   */     throws Exception
/* 18:   */   {
/* 19:48 */     return ((Controller)handler).handleRequest(request, response);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public long getLastModified(HttpServletRequest request, Object handler)
/* 23:   */   {
/* 24:52 */     if ((handler instanceof LastModified)) {
/* 25:53 */       return ((LastModified)handler).getLastModified(request);
/* 26:   */     }
/* 27:55 */     return -1L;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter
 * JD-Core Version:    0.7.0.1
 */