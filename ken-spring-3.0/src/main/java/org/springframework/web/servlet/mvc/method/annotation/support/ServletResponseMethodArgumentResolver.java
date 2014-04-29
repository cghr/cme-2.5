/*  1:   */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.OutputStream;
/*  5:   */ import java.io.Writer;
/*  6:   */ import java.lang.reflect.Method;
/*  7:   */ import javax.servlet.ServletResponse;
/*  8:   */ import javax.servlet.http.HttpServletResponse;
/*  9:   */ import org.springframework.core.MethodParameter;
/* 10:   */ import org.springframework.web.bind.support.WebDataBinderFactory;
/* 11:   */ import org.springframework.web.context.request.NativeWebRequest;
/* 12:   */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/* 13:   */ import org.springframework.web.method.support.ModelAndViewContainer;
/* 14:   */ 
/* 15:   */ public class ServletResponseMethodArgumentResolver
/* 16:   */   implements HandlerMethodArgumentResolver
/* 17:   */ {
/* 18:   */   public boolean supportsParameter(MethodParameter parameter)
/* 19:   */   {
/* 20:48 */     Class<?> paramType = parameter.getParameterType();
/* 21:   */     
/* 22:   */ 
/* 23:51 */     return (ServletResponse.class.isAssignableFrom(paramType)) || (OutputStream.class.isAssignableFrom(paramType)) || (Writer.class.isAssignableFrom(paramType));
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/* 27:   */     throws IOException
/* 28:   */   {
/* 29:65 */     mavContainer.setRequestHandled(true);
/* 30:   */     
/* 31:67 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 32:68 */     Class<?> paramType = parameter.getParameterType();
/* 33:70 */     if (ServletResponse.class.isAssignableFrom(paramType))
/* 34:   */     {
/* 35:71 */       Object nativeResponse = webRequest.getNativeResponse(paramType);
/* 36:72 */       if (nativeResponse == null) {
/* 37:73 */         throw new IllegalStateException(
/* 38:74 */           "Current response is not of type [" + paramType.getName() + "]: " + response);
/* 39:   */       }
/* 40:76 */       return nativeResponse;
/* 41:   */     }
/* 42:78 */     if (OutputStream.class.isAssignableFrom(paramType)) {
/* 43:79 */       return response.getOutputStream();
/* 44:   */     }
/* 45:81 */     if (Writer.class.isAssignableFrom(paramType)) {
/* 46:82 */       return response.getWriter();
/* 47:   */     }
/* 48:86 */     Method method = parameter.getMethod();
/* 49:87 */     throw new UnsupportedOperationException("Unknown parameter type: " + paramType + " in method: " + method);
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.ServletResponseMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */