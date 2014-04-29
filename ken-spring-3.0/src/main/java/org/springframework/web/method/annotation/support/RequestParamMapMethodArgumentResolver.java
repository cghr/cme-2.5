/*  1:   */ package org.springframework.web.method.annotation.support;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ import java.util.LinkedHashMap;
/*  5:   */ import java.util.Map;
/*  6:   */ import java.util.Map.Entry;
/*  7:   */ import java.util.Set;
/*  8:   */ import org.springframework.core.MethodParameter;
/*  9:   */ import org.springframework.util.LinkedMultiValueMap;
/* 10:   */ import org.springframework.util.MultiValueMap;
/* 11:   */ import org.springframework.util.StringUtils;
/* 12:   */ import org.springframework.web.bind.annotation.RequestParam;
/* 13:   */ import org.springframework.web.bind.support.WebDataBinderFactory;
/* 14:   */ import org.springframework.web.context.request.NativeWebRequest;
/* 15:   */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/* 16:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/* 17:   */ 
/* 18:   */ public class RequestParamMapMethodArgumentResolver
/* 19:   */   implements HandlerMethodArgumentResolver
/* 20:   */ {
/* 21:   */   public boolean supportsParameter(MethodParameter parameter)
/* 22:   */   {
/* 23:49 */     RequestParam requestParamAnnot = (RequestParam)parameter.getParameterAnnotation(RequestParam.class);
/* 24:50 */     if ((requestParamAnnot != null) && 
/* 25:51 */       (Map.class.isAssignableFrom(parameter.getParameterType()))) {
/* 26:52 */       return !StringUtils.hasText(requestParamAnnot.value());
/* 27:   */     }
/* 28:55 */     return false;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/* 32:   */     throws Exception
/* 33:   */   {
/* 34:62 */     Class<?> paramType = parameter.getParameterType();
/* 35:   */     
/* 36:64 */     Map<String, String[]> parameterMap = webRequest.getParameterMap();
/* 37:   */     Iterator localIterator;
/* 38:65 */     if (MultiValueMap.class.isAssignableFrom(paramType))
/* 39:   */     {
/* 40:66 */       MultiValueMap<String, String> result = new LinkedMultiValueMap(parameterMap.size());
/* 41:   */       int j;
/* 42:   */       int i;
/* 43:67 */       for (localIterator = parameterMap.entrySet().iterator(); localIterator.hasNext(); i < j)
/* 44:   */       {
/* 45:67 */         Map.Entry<String, String[]> entry = (Map.Entry)localIterator.next();
/* 46:   */         String[] arrayOfString;
/* 47:68 */         j = (arrayOfString = (String[])entry.getValue()).length;i = 0; continue;String value = arrayOfString[i];
/* 48:69 */         result.add((String)entry.getKey(), value);i++;
/* 49:   */       }
/* 50:72 */       return result;
/* 51:   */     }
/* 52:75 */     Map<String, String> result = new LinkedHashMap(parameterMap.size());
/* 53:76 */     for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
/* 54:77 */       if (((String[])entry.getValue()).length > 0) {
/* 55:78 */         result.put((String)entry.getKey(), ((String[])entry.getValue())[0]);
/* 56:   */       }
/* 57:   */     }
/* 58:81 */     return result;
/* 59:   */   }
/* 60:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.RequestParamMapMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */