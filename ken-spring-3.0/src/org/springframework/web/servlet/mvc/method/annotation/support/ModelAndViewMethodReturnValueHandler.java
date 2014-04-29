/*  1:   */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.MethodParameter;
/*  4:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  5:   */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  6:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  7:   */ import org.springframework.web.servlet.ModelAndView;
/*  8:   */ 
/*  9:   */ public class ModelAndViewMethodReturnValueHandler
/* 10:   */   implements HandlerMethodReturnValueHandler
/* 11:   */ {
/* 12:   */   public boolean supportsReturnType(MethodParameter returnType)
/* 13:   */   {
/* 14:44 */     return ModelAndView.class.isAssignableFrom(returnType.getParameterType());
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/* 18:   */     throws Exception
/* 19:   */   {
/* 20:51 */     if (returnValue != null)
/* 21:   */     {
/* 22:52 */       ModelAndView mav = (ModelAndView)returnValue;
/* 23:53 */       mavContainer.setViewName(mav.getViewName());
/* 24:54 */       if (!mav.isReference()) {
/* 25:55 */         mavContainer.setView(mav.getView());
/* 26:   */       }
/* 27:57 */       mavContainer.addAllAttributes(mav.getModel());
/* 28:   */     }
/* 29:   */     else
/* 30:   */     {
/* 31:60 */       mavContainer.setRequestHandled(true);
/* 32:   */     }
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.ModelAndViewMethodReturnValueHandler
 * JD-Core Version:    0.7.0.1
 */