/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.lang.reflect.Array;
/*   5:    */ import java.lang.reflect.GenericArrayType;
/*   6:    */ import java.lang.reflect.ParameterizedType;
/*   7:    */ import java.lang.reflect.Type;
/*   8:    */ import java.util.List;
/*   9:    */ import org.springframework.core.MethodParameter;
/*  10:    */ import org.springframework.http.HttpEntity;
/*  11:    */ import org.springframework.http.HttpHeaders;
/*  12:    */ import org.springframework.http.HttpInputMessage;
/*  13:    */ import org.springframework.http.ResponseEntity;
/*  14:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  15:    */ import org.springframework.http.server.ServletServerHttpRequest;
/*  16:    */ import org.springframework.http.server.ServletServerHttpResponse;
/*  17:    */ import org.springframework.util.Assert;
/*  18:    */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*  19:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  20:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  21:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  22:    */ 
/*  23:    */ public class HttpEntityMethodProcessor
/*  24:    */   extends AbstractMessageConverterMethodProcessor
/*  25:    */ {
/*  26:    */   public HttpEntityMethodProcessor(List<HttpMessageConverter<?>> messageConverters)
/*  27:    */   {
/*  28: 56 */     super(messageConverters);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean supportsParameter(MethodParameter parameter)
/*  32:    */   {
/*  33: 60 */     Class<?> parameterType = parameter.getParameterType();
/*  34: 61 */     return HttpEntity.class.equals(parameterType);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public boolean supportsReturnType(MethodParameter returnType)
/*  38:    */   {
/*  39: 65 */     Class<?> parameterType = returnType.getParameterType();
/*  40: 66 */     return (HttpEntity.class.equals(parameterType)) || (ResponseEntity.class.equals(parameterType));
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*  44:    */     throws IOException, HttpMediaTypeNotSupportedException
/*  45:    */   {
/*  46: 75 */     HttpInputMessage inputMessage = createInputMessage(webRequest);
/*  47: 76 */     Class<?> paramType = getHttpEntityType(parameter);
/*  48:    */     
/*  49: 78 */     Object body = readWithMessageConverters(webRequest, parameter, paramType);
/*  50: 79 */     return new HttpEntity(body, inputMessage.getHeaders());
/*  51:    */   }
/*  52:    */   
/*  53:    */   private Class<?> getHttpEntityType(MethodParameter parameter)
/*  54:    */   {
/*  55: 83 */     Assert.isAssignable(HttpEntity.class, parameter.getParameterType());
/*  56: 84 */     ParameterizedType type = (ParameterizedType)parameter.getGenericParameterType();
/*  57: 85 */     if (type.getActualTypeArguments().length == 1)
/*  58:    */     {
/*  59: 86 */       Type typeArgument = type.getActualTypeArguments()[0];
/*  60: 87 */       if ((typeArgument instanceof Class)) {
/*  61: 88 */         return (Class)typeArgument;
/*  62:    */       }
/*  63: 90 */       if ((typeArgument instanceof GenericArrayType))
/*  64:    */       {
/*  65: 91 */         Type componentType = ((GenericArrayType)typeArgument).getGenericComponentType();
/*  66: 92 */         if ((componentType instanceof Class))
/*  67:    */         {
/*  68: 94 */           Object array = Array.newInstance((Class)componentType, 0);
/*  69: 95 */           return array.getClass();
/*  70:    */         }
/*  71:    */       }
/*  72:    */     }
/*  73: 99 */     throw new IllegalArgumentException("HttpEntity parameter (" + parameter.getParameterName() + ") " + 
/*  74:100 */       "in method " + parameter.getMethod() + "is not parameterized");
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*  78:    */     throws Exception
/*  79:    */   {
/*  80:108 */     mavContainer.setRequestHandled(true);
/*  81:110 */     if (returnValue == null) {
/*  82:111 */       return;
/*  83:    */     }
/*  84:114 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/*  85:115 */     ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
/*  86:    */     
/*  87:117 */     Assert.isInstanceOf(HttpEntity.class, returnValue);
/*  88:118 */     HttpEntity<?> responseEntity = (HttpEntity)returnValue;
/*  89:119 */     if ((responseEntity instanceof ResponseEntity)) {
/*  90:120 */       outputMessage.setStatusCode(((ResponseEntity)responseEntity).getStatusCode());
/*  91:    */     }
/*  92:123 */     HttpHeaders entityHeaders = responseEntity.getHeaders();
/*  93:124 */     if (!entityHeaders.isEmpty()) {
/*  94:125 */       outputMessage.getHeaders().putAll(entityHeaders);
/*  95:    */     }
/*  96:128 */     Object body = responseEntity.getBody();
/*  97:129 */     if (body != null) {
/*  98:130 */       writeWithMessageConverters(body, returnType, inputMessage, outputMessage);
/*  99:    */     } else {
/* 100:134 */       outputMessage.getBody();
/* 101:    */     }
/* 102:    */   }
/* 103:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.HttpEntityMethodProcessor
 * JD-Core Version:    0.7.0.1
 */