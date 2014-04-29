/*  1:   */ package org.springframework.web.method.annotation.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.MethodParameter;
/*  4:   */ import org.springframework.ui.Model;
/*  5:   */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  6:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  7:   */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  8:   */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  9:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/* 10:   */ 
/* 11:   */ public class ModelMethodProcessor
/* 12:   */   implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler
/* 13:   */ {
/* 14:   */   public boolean supportsParameter(MethodParameter parameter)
/* 15:   */   {
/* 16:41 */     return Model.class.isAssignableFrom(parameter.getParameterType());
/* 17:   */   }
/* 18:   */   
/* 19:   */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/* 20:   */     throws Exception
/* 21:   */   {
/* 22:48 */     return mavContainer.getModel();
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean supportsReturnType(MethodParameter returnType)
/* 26:   */   {
/* 27:52 */     return Model.class.isAssignableFrom(returnType.getParameterType());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/* 31:   */     throws Exception
/* 32:   */   {
/* 33:59 */     if (returnValue == null) {
/* 34:60 */       return;
/* 35:   */     }
/* 36:62 */     if ((returnValue instanceof Model)) {
/* 37:63 */       mavContainer.addAllAttributes(((Model)returnValue).asMap());
/* 38:   */     } else {
/* 39:67 */       throw new UnsupportedOperationException("Unexpected return type: " + 
/* 40:68 */         returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
/* 41:   */     }
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.ModelMethodProcessor
 * JD-Core Version:    0.7.0.1
 */