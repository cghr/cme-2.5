/*  1:   */ package org.springframework.web.method.annotation.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.MethodParameter;
/*  4:   */ import org.springframework.web.bind.support.SessionStatus;
/*  5:   */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  6:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  7:   */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  8:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  9:   */ 
/* 10:   */ public class SessionStatusMethodArgumentResolver
/* 11:   */   implements HandlerMethodArgumentResolver
/* 12:   */ {
/* 13:   */   public boolean supportsParameter(MethodParameter parameter)
/* 14:   */   {
/* 15:36 */     return SessionStatus.class.equals(parameter.getParameterType());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/* 19:   */     throws Exception
/* 20:   */   {
/* 21:43 */     return mavContainer.getSessionStatus();
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.SessionStatusMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */