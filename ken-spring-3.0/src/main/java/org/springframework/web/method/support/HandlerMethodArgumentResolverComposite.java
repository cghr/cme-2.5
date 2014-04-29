/*   1:    */ package org.springframework.web.method.support;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.concurrent.ConcurrentHashMap;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.core.MethodParameter;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  13:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  14:    */ 
/*  15:    */ public class HandlerMethodArgumentResolverComposite
/*  16:    */   implements HandlerMethodArgumentResolver
/*  17:    */ {
/*  18: 41 */   protected final Log logger = LogFactory.getLog(getClass());
/*  19: 44 */   private final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList();
/*  20: 47 */   private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap();
/*  21:    */   
/*  22:    */   public List<HandlerMethodArgumentResolver> getResolvers()
/*  23:    */   {
/*  24: 53 */     return Collections.unmodifiableList(this.argumentResolvers);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean supportsParameter(MethodParameter parameter)
/*  28:    */   {
/*  29: 61 */     return getArgumentResolver(parameter) != null;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*  33:    */     throws Exception
/*  34:    */   {
/*  35: 72 */     HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
/*  36: 73 */     Assert.notNull(resolver, "Unknown parameter type [" + parameter.getParameterType().getName() + "]");
/*  37: 74 */     return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
/*  38:    */   }
/*  39:    */   
/*  40:    */   private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter)
/*  41:    */   {
/*  42: 81 */     HandlerMethodArgumentResolver result = (HandlerMethodArgumentResolver)this.argumentResolverCache.get(parameter);
/*  43: 82 */     if (result == null) {
/*  44: 83 */       for (HandlerMethodArgumentResolver methodArgumentResolver : this.argumentResolvers)
/*  45:    */       {
/*  46: 84 */         if (this.logger.isTraceEnabled()) {
/*  47: 85 */           this.logger.trace("Testing if argument resolver [" + methodArgumentResolver + "] supports [" + 
/*  48: 86 */             parameter.getGenericParameterType() + "]");
/*  49:    */         }
/*  50: 88 */         if (methodArgumentResolver.supportsParameter(parameter))
/*  51:    */         {
/*  52: 89 */           result = methodArgumentResolver;
/*  53: 90 */           this.argumentResolverCache.put(parameter, result);
/*  54: 91 */           break;
/*  55:    */         }
/*  56:    */       }
/*  57:    */     }
/*  58: 95 */     return result;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver argumentResolver)
/*  62:    */   {
/*  63:102 */     this.argumentResolvers.add(argumentResolver);
/*  64:103 */     return this;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public HandlerMethodArgumentResolverComposite addResolvers(List<? extends HandlerMethodArgumentResolver> argumentResolvers)
/*  68:    */   {
/*  69:111 */     if (argumentResolvers != null) {
/*  70:112 */       for (HandlerMethodArgumentResolver resolver : argumentResolvers) {
/*  71:113 */         this.argumentResolvers.add(resolver);
/*  72:    */       }
/*  73:    */     }
/*  74:116 */     return this;
/*  75:    */   }
/*  76:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.support.HandlerMethodArgumentResolverComposite
 * JD-Core Version:    0.7.0.1
 */