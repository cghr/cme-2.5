/*  1:   */ package org.springframework.web.servlet.config.annotation;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.springframework.web.context.request.WebRequestInterceptor;
/*  6:   */ import org.springframework.web.servlet.HandlerInterceptor;
/*  7:   */ import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
/*  8:   */ 
/*  9:   */ public class InterceptorRegistry
/* 10:   */ {
/* 11:37 */   private final List<InterceptorRegistration> registrations = new ArrayList();
/* 12:   */   
/* 13:   */   public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor)
/* 14:   */   {
/* 15:46 */     InterceptorRegistration registration = new InterceptorRegistration(interceptor);
/* 16:47 */     this.registrations.add(registration);
/* 17:48 */     return registration;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public InterceptorRegistration addWebRequestInterceptor(WebRequestInterceptor interceptor)
/* 21:   */   {
/* 22:58 */     WebRequestHandlerInterceptorAdapter adapted = new WebRequestHandlerInterceptorAdapter(interceptor);
/* 23:59 */     InterceptorRegistration registration = new InterceptorRegistration(adapted);
/* 24:60 */     this.registrations.add(registration);
/* 25:61 */     return registration;
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected List<Object> getInterceptors()
/* 29:   */   {
/* 30:68 */     List<Object> interceptors = new ArrayList();
/* 31:69 */     for (InterceptorRegistration registration : this.registrations) {
/* 32:70 */       interceptors.add(registration.getInterceptor());
/* 33:   */     }
/* 34:72 */     return interceptors;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.InterceptorRegistry
 * JD-Core Version:    0.7.0.1
 */