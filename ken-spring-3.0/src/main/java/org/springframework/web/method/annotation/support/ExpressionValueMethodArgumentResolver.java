/*  1:   */ package org.springframework.web.method.annotation.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletException;
/*  4:   */ import org.springframework.beans.factory.annotation.Value;
/*  5:   */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  6:   */ import org.springframework.core.MethodParameter;
/*  7:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  8:   */ 
/*  9:   */ public class ExpressionValueMethodArgumentResolver
/* 10:   */   extends AbstractNamedValueMethodArgumentResolver
/* 11:   */ {
/* 12:   */   public ExpressionValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory)
/* 13:   */   {
/* 14:48 */     super(beanFactory);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public boolean supportsParameter(MethodParameter parameter)
/* 18:   */   {
/* 19:52 */     return parameter.hasParameterAnnotation(Value.class);
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/* 23:   */   {
/* 24:57 */     Value annotation = (Value)parameter.getParameterAnnotation(Value.class);
/* 25:58 */     return new ExpressionValueNamedValueInfo(annotation, null);
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest)
/* 29:   */     throws Exception
/* 30:   */   {
/* 31:65 */     return null;
/* 32:   */   }
/* 33:   */   
/* 34:   */   protected void handleMissingValue(String name, MethodParameter parameter)
/* 35:   */     throws ServletException
/* 36:   */   {
/* 37:70 */     throw new UnsupportedOperationException("@Value is never required: " + parameter.getMethod());
/* 38:   */   }
/* 39:   */   
/* 40:   */   private static class ExpressionValueNamedValueInfo
/* 41:   */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/* 42:   */   {
/* 43:   */     private ExpressionValueNamedValueInfo(Value annotation)
/* 44:   */     {
/* 45:76 */       super(false, annotation.value());
/* 46:   */     }
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.ExpressionValueMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */