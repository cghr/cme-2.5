/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ import org.springframework.web.context.request.WebRequestInterceptor;
/*  7:   */ import org.springframework.web.servlet.HandlerInterceptor;
/*  8:   */ import org.springframework.web.servlet.ModelAndView;
/*  9:   */ 
/* 10:   */ public class WebRequestHandlerInterceptorAdapter
/* 11:   */   implements HandlerInterceptor
/* 12:   */ {
/* 13:   */   private final WebRequestInterceptor requestInterceptor;
/* 14:   */   
/* 15:   */   public WebRequestHandlerInterceptorAdapter(WebRequestInterceptor requestInterceptor)
/* 16:   */   {
/* 17:46 */     Assert.notNull(requestInterceptor, "WebRequestInterceptor must not be null");
/* 18:47 */     this.requestInterceptor = requestInterceptor;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 22:   */     throws Exception
/* 23:   */   {
/* 24:54 */     this.requestInterceptor.preHandle(new DispatcherServletWebRequest(request, response));
/* 25:55 */     return true;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
/* 29:   */     throws Exception
/* 30:   */   {
/* 31:61 */     this.requestInterceptor.postHandle(new DispatcherServletWebRequest(request, response), 
/* 32:62 */       (modelAndView != null) && (!modelAndView.wasCleared()) ? modelAndView.getModelMap() : null);
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/* 36:   */     throws Exception
/* 37:   */   {
/* 38:68 */     this.requestInterceptor.afterCompletion(new DispatcherServletWebRequest(request, response), ex);
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter
 * JD-Core Version:    0.7.0.1
 */