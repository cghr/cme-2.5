/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.Reader;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.security.Principal;
/*   8:    */ import java.util.Locale;
/*   9:    */ import javax.servlet.ServletRequest;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import javax.servlet.http.HttpSession;
/*  12:    */ import org.springframework.core.MethodParameter;
/*  13:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  14:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  15:    */ import org.springframework.web.context.request.WebRequest;
/*  16:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  17:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  18:    */ import org.springframework.web.multipart.MultipartRequest;
/*  19:    */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  20:    */ 
/*  21:    */ public class ServletRequestMethodArgumentResolver
/*  22:    */   implements HandlerMethodArgumentResolver
/*  23:    */ {
/*  24:    */   public boolean supportsParameter(MethodParameter parameter)
/*  25:    */   {
/*  26: 59 */     Class<?> paramType = parameter.getParameterType();
/*  27:    */     
/*  28:    */ 
/*  29:    */ 
/*  30:    */ 
/*  31:    */ 
/*  32:    */ 
/*  33:    */ 
/*  34: 67 */     return (WebRequest.class.isAssignableFrom(paramType)) || (ServletRequest.class.isAssignableFrom(paramType)) || (MultipartRequest.class.isAssignableFrom(paramType)) || (HttpSession.class.isAssignableFrom(paramType)) || (Principal.class.isAssignableFrom(paramType)) || (Locale.class.equals(paramType)) || (InputStream.class.isAssignableFrom(paramType)) || (Reader.class.isAssignableFrom(paramType));
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*  38:    */     throws IOException
/*  39:    */   {
/*  40: 75 */     Class<?> paramType = parameter.getParameterType();
/*  41: 76 */     if (WebRequest.class.isAssignableFrom(paramType)) {
/*  42: 77 */       return webRequest;
/*  43:    */     }
/*  44: 80 */     HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  45: 81 */     if ((ServletRequest.class.isAssignableFrom(paramType)) || (MultipartRequest.class.isAssignableFrom(paramType)))
/*  46:    */     {
/*  47: 82 */       Object nativeRequest = webRequest.getNativeRequest(paramType);
/*  48: 83 */       if (nativeRequest == null) {
/*  49: 84 */         throw new IllegalStateException(
/*  50: 85 */           "Current request is not of type [" + paramType.getName() + "]: " + request);
/*  51:    */       }
/*  52: 87 */       return nativeRequest;
/*  53:    */     }
/*  54: 89 */     if (HttpSession.class.isAssignableFrom(paramType)) {
/*  55: 90 */       return request.getSession();
/*  56:    */     }
/*  57: 92 */     if (Principal.class.isAssignableFrom(paramType)) {
/*  58: 93 */       return request.getUserPrincipal();
/*  59:    */     }
/*  60: 95 */     if (Locale.class.equals(paramType)) {
/*  61: 96 */       return RequestContextUtils.getLocale(request);
/*  62:    */     }
/*  63: 98 */     if (InputStream.class.isAssignableFrom(paramType)) {
/*  64: 99 */       return request.getInputStream();
/*  65:    */     }
/*  66:101 */     if (Reader.class.isAssignableFrom(paramType)) {
/*  67:102 */       return request.getReader();
/*  68:    */     }
/*  69:106 */     Method method = parameter.getMethod();
/*  70:107 */     throw new UnsupportedOperationException("Unknown parameter type: " + paramType + " in method: " + method);
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.ServletRequestMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */