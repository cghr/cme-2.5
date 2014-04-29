/*  1:   */ package org.springframework.web.method.annotation.support;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  5:   */ import org.springframework.core.MethodParameter;
/*  6:   */ import org.springframework.web.bind.ServletRequestBindingException;
/*  7:   */ import org.springframework.web.bind.annotation.RequestHeader;
/*  8:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  9:   */ 
/* 10:   */ public class RequestHeaderMethodArgumentResolver
/* 11:   */   extends AbstractNamedValueMethodArgumentResolver
/* 12:   */ {
/* 13:   */   public RequestHeaderMethodArgumentResolver(ConfigurableBeanFactory beanFactory)
/* 14:   */   {
/* 15:52 */     super(beanFactory);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean supportsParameter(MethodParameter parameter)
/* 19:   */   {
/* 20:57 */     return (parameter.hasParameterAnnotation(RequestHeader.class)) && (!Map.class.isAssignableFrom(parameter.getParameterType()));
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/* 24:   */   {
/* 25:62 */     RequestHeader annotation = (RequestHeader)parameter.getParameterAnnotation(RequestHeader.class);
/* 26:63 */     return new RequestHeaderNamedValueInfo(annotation, null);
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request)
/* 30:   */     throws Exception
/* 31:   */   {
/* 32:68 */     String[] headerValues = request.getHeaderValues(name);
/* 33:69 */     if (headerValues != null) {
/* 34:70 */       return headerValues.length == 1 ? headerValues[0] : headerValues;
/* 35:   */     }
/* 36:73 */     return null;
/* 37:   */   }
/* 38:   */   
/* 39:   */   protected void handleMissingValue(String headerName, MethodParameter param)
/* 40:   */     throws ServletRequestBindingException
/* 41:   */   {
/* 42:79 */     String paramType = param.getParameterType().getName();
/* 43:80 */     throw new ServletRequestBindingException(
/* 44:81 */       "Missing header '" + headerName + "' for method parameter type [" + paramType + "]");
/* 45:   */   }
/* 46:   */   
/* 47:   */   private static class RequestHeaderNamedValueInfo
/* 48:   */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/* 49:   */   {
/* 50:   */     private RequestHeaderNamedValueInfo(RequestHeader annotation)
/* 51:   */     {
/* 52:87 */       super(annotation.required(), annotation.defaultValue());
/* 53:   */     }
/* 54:   */   }
/* 55:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.RequestHeaderMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */