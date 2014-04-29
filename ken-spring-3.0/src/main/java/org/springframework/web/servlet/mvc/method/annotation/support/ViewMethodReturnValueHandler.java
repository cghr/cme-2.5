/*  1:   */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.MethodParameter;
/*  4:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  5:   */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  6:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  7:   */ import org.springframework.web.servlet.SmartView;
/*  8:   */ import org.springframework.web.servlet.View;
/*  9:   */ 
/* 10:   */ public class ViewMethodReturnValueHandler
/* 11:   */   implements HandlerMethodReturnValueHandler
/* 12:   */ {
/* 13:   */   public boolean supportsReturnType(MethodParameter returnType)
/* 14:   */   {
/* 15:44 */     return View.class.isAssignableFrom(returnType.getParameterType());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/* 19:   */     throws Exception
/* 20:   */   {
/* 21:51 */     if (returnValue == null) {
/* 22:52 */       return;
/* 23:   */     }
/* 24:54 */     if ((returnValue instanceof View))
/* 25:   */     {
/* 26:55 */       View view = (View)returnValue;
/* 27:56 */       mavContainer.setView(view);
/* 28:57 */       if (((view instanceof SmartView)) && 
/* 29:58 */         (((SmartView)view).isRedirectView())) {
/* 30:59 */         mavContainer.setRedirectModelScenario(true);
/* 31:   */       }
/* 32:   */     }
/* 33:   */     else
/* 34:   */     {
/* 35:65 */       throw new UnsupportedOperationException("Unexpected return type: " + 
/* 36:66 */         returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
/* 37:   */     }
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.ViewMethodReturnValueHandler
 * JD-Core Version:    0.7.0.1
 */