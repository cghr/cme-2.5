/*  1:   */ package org.springframework.web.servlet.mvc.annotation;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import org.springframework.ui.ExtendedModelMap;
/*  5:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  6:   */ import org.springframework.web.servlet.ModelAndView;
/*  7:   */ 
/*  8:   */ public abstract interface ModelAndViewResolver
/*  9:   */ {
/* 10:54 */   public static final ModelAndView UNRESOLVED = new ModelAndView();
/* 11:   */   
/* 12:   */   public abstract ModelAndView resolveModelAndView(Method paramMethod, Class paramClass, Object paramObject, ExtendedModelMap paramExtendedModelMap, NativeWebRequest paramNativeWebRequest);
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver
 * JD-Core Version:    0.7.0.1
 */