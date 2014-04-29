/*  1:   */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.MethodParameter;
/*  4:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  5:   */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  6:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  7:   */ 
/*  8:   */ public class ViewNameMethodReturnValueHandler
/*  9:   */   implements HandlerMethodReturnValueHandler
/* 10:   */ {
/* 11:   */   public boolean supportsReturnType(MethodParameter returnType)
/* 12:   */   {
/* 13:44 */     Class<?> paramType = returnType.getParameterType();
/* 14:45 */     return (Void.TYPE.equals(paramType)) || (String.class.equals(paramType));
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/* 18:   */     throws Exception
/* 19:   */   {
/* 20:52 */     if (returnValue == null) {
/* 21:53 */       return;
/* 22:   */     }
/* 23:55 */     if ((returnValue instanceof String))
/* 24:   */     {
/* 25:56 */       String viewName = (String)returnValue;
/* 26:57 */       mavContainer.setViewName(viewName);
/* 27:58 */       if (isRedirectViewName(viewName)) {
/* 28:59 */         mavContainer.setRedirectModelScenario(true);
/* 29:   */       }
/* 30:   */     }
/* 31:   */     else
/* 32:   */     {
/* 33:64 */       throw new UnsupportedOperationException("Unexpected return type: " + 
/* 34:65 */         returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
/* 35:   */     }
/* 36:   */   }
/* 37:   */   
/* 38:   */   protected boolean isRedirectViewName(String viewName)
/* 39:   */   {
/* 40:76 */     return viewName.startsWith("redirect:");
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.ViewNameMethodReturnValueHandler
 * JD-Core Version:    0.7.0.1
 */