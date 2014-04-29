/*  1:   */ package org.springframework.web.method.annotation.support;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  4:   */ import org.springframework.core.MethodParameter;
/*  5:   */ import org.springframework.web.bind.ServletRequestBindingException;
/*  6:   */ import org.springframework.web.bind.annotation.CookieValue;
/*  7:   */ 
/*  8:   */ public abstract class AbstractCookieValueMethodArgumentResolver
/*  9:   */   extends AbstractNamedValueMethodArgumentResolver
/* 10:   */ {
/* 11:   */   public AbstractCookieValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory)
/* 12:   */   {
/* 13:48 */     super(beanFactory);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean supportsParameter(MethodParameter parameter)
/* 17:   */   {
/* 18:52 */     return parameter.hasParameterAnnotation(CookieValue.class);
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/* 22:   */   {
/* 23:57 */     CookieValue annotation = (CookieValue)parameter.getParameterAnnotation(CookieValue.class);
/* 24:58 */     return new CookieValueNamedValueInfo(annotation, null);
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected void handleMissingValue(String cookieName, MethodParameter param)
/* 28:   */     throws ServletRequestBindingException
/* 29:   */   {
/* 30:63 */     String paramType = param.getParameterType().getName();
/* 31:64 */     throw new ServletRequestBindingException(
/* 32:65 */       "Missing cookie named '" + cookieName + "' for method parameter type [" + paramType + "]");
/* 33:   */   }
/* 34:   */   
/* 35:   */   private static class CookieValueNamedValueInfo
/* 36:   */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/* 37:   */   {
/* 38:   */     private CookieValueNamedValueInfo(CookieValue annotation)
/* 39:   */     {
/* 40:71 */       super(annotation.required(), annotation.defaultValue());
/* 41:   */     }
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.AbstractCookieValueMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */