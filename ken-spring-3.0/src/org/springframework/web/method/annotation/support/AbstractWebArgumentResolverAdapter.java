/*   1:    */ package org.springframework.web.method.annotation.support;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.apache.commons.logging.LogFactory;
/*   5:    */ import org.springframework.core.MethodParameter;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ import org.springframework.util.ClassUtils;
/*   8:    */ import org.springframework.web.bind.support.WebArgumentResolver;
/*   9:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  10:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  11:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  12:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  13:    */ 
/*  14:    */ public abstract class AbstractWebArgumentResolverAdapter
/*  15:    */   implements HandlerMethodArgumentResolver
/*  16:    */ {
/*  17: 50 */   private final Log logger = LogFactory.getLog(getClass());
/*  18:    */   private final WebArgumentResolver adaptee;
/*  19:    */   
/*  20:    */   public AbstractWebArgumentResolverAdapter(WebArgumentResolver adaptee)
/*  21:    */   {
/*  22: 58 */     Assert.notNull(adaptee, "'adaptee' must not be null");
/*  23: 59 */     this.adaptee = adaptee;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public boolean supportsParameter(MethodParameter parameter)
/*  27:    */   {
/*  28:    */     try
/*  29:    */     {
/*  30: 68 */       NativeWebRequest webRequest = getWebRequest();
/*  31: 69 */       Object result = this.adaptee.resolveArgument(parameter, webRequest);
/*  32: 70 */       if (result == WebArgumentResolver.UNRESOLVED) {
/*  33: 71 */         return false;
/*  34:    */       }
/*  35: 74 */       return ClassUtils.isAssignableValue(parameter.getParameterType(), result);
/*  36:    */     }
/*  37:    */     catch (Exception ex)
/*  38:    */     {
/*  39: 79 */       this.logger.trace("Error in checking support for parameter [" + parameter + "], message: " + ex.getMessage());
/*  40:    */     }
/*  41: 80 */     return false;
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected abstract NativeWebRequest getWebRequest();
/*  45:    */   
/*  46:    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*  47:    */     throws Exception
/*  48:    */   {
/*  49: 98 */     Class<?> paramType = parameter.getParameterType();
/*  50: 99 */     Object result = this.adaptee.resolveArgument(parameter, webRequest);
/*  51:100 */     if ((result == WebArgumentResolver.UNRESOLVED) || (!ClassUtils.isAssignableValue(paramType, result))) {
/*  52:101 */       throw new IllegalStateException(
/*  53:102 */         "Standard argument type [" + paramType.getName() + "] in method " + parameter.getMethod() + 
/*  54:103 */         "resolved to incompatible value of type [" + (result != null ? result.getClass() : null) + 
/*  55:104 */         "]. Consider declaring the argument type in a less specific fashion.");
/*  56:    */     }
/*  57:106 */     return result;
/*  58:    */   }
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.AbstractWebArgumentResolverAdapter
 * JD-Core Version:    0.7.0.1
 */