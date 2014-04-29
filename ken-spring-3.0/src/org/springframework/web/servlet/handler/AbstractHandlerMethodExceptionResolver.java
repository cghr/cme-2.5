/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.springframework.web.method.HandlerMethod;
/*  6:   */ import org.springframework.web.servlet.ModelAndView;
/*  7:   */ 
/*  8:   */ public class AbstractHandlerMethodExceptionResolver
/*  9:   */   extends AbstractHandlerExceptionResolver
/* 10:   */ {
/* 11:   */   protected boolean shouldApplyTo(HttpServletRequest request, Object handler)
/* 12:   */   {
/* 13:41 */     if (handler == null) {
/* 14:42 */       return super.shouldApplyTo(request, handler);
/* 15:   */     }
/* 16:44 */     if ((handler instanceof HandlerMethod))
/* 17:   */     {
/* 18:45 */       HandlerMethod handlerMethod = (HandlerMethod)handler;
/* 19:46 */       handler = handlerMethod.getBean();
/* 20:47 */       return super.shouldApplyTo(request, handler);
/* 21:   */     }
/* 22:50 */     return false;
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected final ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/* 26:   */   {
/* 27:59 */     return doResolveHandlerMethodException(request, response, (HandlerMethod)handler, ex);
/* 28:   */   }
/* 29:   */   
/* 30:   */   protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex)
/* 31:   */   {
/* 32:80 */     return null;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver
 * JD-Core Version:    0.7.0.1
 */