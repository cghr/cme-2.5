/*  1:   */ package org.springframework.web.method.annotation.support;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Collection;
/*  5:   */ import org.springframework.core.MethodParameter;
/*  6:   */ import org.springframework.ui.ModelMap;
/*  7:   */ import org.springframework.validation.BindingResult;
/*  8:   */ import org.springframework.validation.Errors;
/*  9:   */ import org.springframework.web.bind.support.WebDataBinderFactory;
/* 10:   */ import org.springframework.web.context.request.NativeWebRequest;
/* 11:   */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/* 12:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/* 13:   */ 
/* 14:   */ public class ErrorsMethodArgumentResolver
/* 15:   */   implements HandlerMethodArgumentResolver
/* 16:   */ {
/* 17:   */   public boolean supportsParameter(MethodParameter parameter)
/* 18:   */   {
/* 19:44 */     Class<?> paramType = parameter.getParameterType();
/* 20:45 */     return Errors.class.isAssignableFrom(paramType);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/* 24:   */     throws Exception
/* 25:   */   {
/* 26:52 */     ModelMap model = mavContainer.getModel();
/* 27:53 */     if (model.size() > 0)
/* 28:   */     {
/* 29:54 */       int lastIndex = model.size() - 1;
/* 30:55 */       String lastKey = (String)new ArrayList((Collection)model.keySet()).get(lastIndex);
/* 31:56 */       if (lastKey.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
/* 32:57 */         return model.get(lastKey);
/* 33:   */       }
/* 34:   */     }
/* 35:61 */     throw new IllegalStateException(
/* 36:62 */       "An Errors/BindingResult argument is expected to be immediately after the model attribute argument in the controller method signature: " + 
/* 37:63 */       parameter.getMethod());
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.ErrorsMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */