/*  1:   */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.MethodParameter;
/*  4:   */ import org.springframework.ui.ModelMap;
/*  5:   */ import org.springframework.validation.DataBinder;
/*  6:   */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  7:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  8:   */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  9:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/* 10:   */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/* 11:   */ import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
/* 12:   */ 
/* 13:   */ public class RedirectAttributesMethodArgumentResolver
/* 14:   */   implements HandlerMethodArgumentResolver
/* 15:   */ {
/* 16:   */   public boolean supportsParameter(MethodParameter parameter)
/* 17:   */   {
/* 18:48 */     return RedirectAttributes.class.isAssignableFrom(parameter.getParameterType());
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/* 22:   */     throws Exception
/* 23:   */   {
/* 24:55 */     DataBinder dataBinder = binderFactory.createBinder(webRequest, null, null);
/* 25:56 */     ModelMap redirectAttributes = new RedirectAttributesModelMap(dataBinder);
/* 26:57 */     mavContainer.setRedirectModel(redirectAttributes);
/* 27:58 */     return redirectAttributes;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.RedirectAttributesMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */