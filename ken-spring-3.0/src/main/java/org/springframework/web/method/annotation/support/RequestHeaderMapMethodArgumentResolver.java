/*  1:   */ package org.springframework.web.method.annotation.support;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ import java.util.LinkedHashMap;
/*  5:   */ import java.util.Map;
/*  6:   */ import org.springframework.core.MethodParameter;
/*  7:   */ import org.springframework.http.HttpHeaders;
/*  8:   */ import org.springframework.util.LinkedMultiValueMap;
/*  9:   */ import org.springframework.util.MultiValueMap;
/* 10:   */ import org.springframework.web.bind.annotation.RequestHeader;
/* 11:   */ import org.springframework.web.bind.support.WebDataBinderFactory;
/* 12:   */ import org.springframework.web.context.request.NativeWebRequest;
/* 13:   */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/* 14:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/* 15:   */ 
/* 16:   */ public class RequestHeaderMapMethodArgumentResolver
/* 17:   */   implements HandlerMethodArgumentResolver
/* 18:   */ {
/* 19:   */   public boolean supportsParameter(MethodParameter parameter)
/* 20:   */   {
/* 21:50 */     return (parameter.hasParameterAnnotation(RequestHeader.class)) && (Map.class.isAssignableFrom(parameter.getParameterType()));
/* 22:   */   }
/* 23:   */   
/* 24:   */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/* 25:   */     throws Exception
/* 26:   */   {
/* 27:57 */     Class<?> paramType = parameter.getParameterType();
/* 28:59 */     if (MultiValueMap.class.isAssignableFrom(paramType))
/* 29:   */     {
/* 30:   */       MultiValueMap<String, String> result;
/* 31:   */       MultiValueMap<String, String> result;
/* 32:61 */       if (HttpHeaders.class.isAssignableFrom(paramType)) {
/* 33:62 */         result = new HttpHeaders();
/* 34:   */       } else {
/* 35:65 */         result = new LinkedMultiValueMap();
/* 36:   */       }
/* 37:   */       int j;
/* 38:   */       int i;
/* 39:67 */       for (Iterator<String> iterator = webRequest.getHeaderNames(); iterator.hasNext(); i < j)
/* 40:   */       {
/* 41:68 */         String headerName = (String)iterator.next();
/* 42:   */         String[] arrayOfString;
/* 43:69 */         j = (arrayOfString = webRequest.getHeaderValues(headerName)).length;i = 0; continue;String headerValue = arrayOfString[i];
/* 44:70 */         result.add(headerName, headerValue);i++;
/* 45:   */       }
/* 46:73 */       return result;
/* 47:   */     }
/* 48:76 */     Map<String, String> result = new LinkedHashMap();
/* 49:77 */     for (Iterator<String> iterator = webRequest.getHeaderNames(); iterator.hasNext();)
/* 50:   */     {
/* 51:78 */       String headerName = (String)iterator.next();
/* 52:79 */       String headerValue = webRequest.getHeader(headerName);
/* 53:80 */       result.put(headerName, headerValue);
/* 54:   */     }
/* 55:82 */     return result;
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.RequestHeaderMapMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */