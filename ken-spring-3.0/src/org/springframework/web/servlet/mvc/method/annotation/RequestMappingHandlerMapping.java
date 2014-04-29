/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import org.springframework.core.annotation.AnnotationUtils;
/*   5:    */ import org.springframework.stereotype.Controller;
/*   6:    */ import org.springframework.web.bind.annotation.RequestMapping;
/*   7:    */ import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
/*   8:    */ import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
/*   9:    */ import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
/*  10:    */ import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
/*  11:    */ import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
/*  12:    */ import org.springframework.web.servlet.mvc.condition.RequestCondition;
/*  13:    */ import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
/*  14:    */ import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
/*  15:    */ import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
/*  16:    */ 
/*  17:    */ public class RequestMappingHandlerMapping
/*  18:    */   extends RequestMappingInfoHandlerMapping
/*  19:    */ {
/*  20: 45 */   private boolean useSuffixPatternMatch = true;
/*  21:    */   
/*  22:    */   public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch)
/*  23:    */   {
/*  24: 53 */     this.useSuffixPatternMatch = useSuffixPatternMatch;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean useSuffixPatternMatch()
/*  28:    */   {
/*  29: 60 */     return this.useSuffixPatternMatch;
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected boolean isHandler(Class<?> beanType)
/*  33:    */   {
/*  34: 69 */     return AnnotationUtils.findAnnotation(beanType, Controller.class) != null;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType)
/*  38:    */   {
/*  39: 84 */     RequestMappingInfo info = null;
/*  40: 85 */     RequestMapping methodAnnotation = (RequestMapping)AnnotationUtils.findAnnotation(method, RequestMapping.class);
/*  41: 86 */     if (methodAnnotation != null)
/*  42:    */     {
/*  43: 87 */       RequestCondition<?> methodCondition = getCustomMethodCondition(method);
/*  44: 88 */       info = createRequestMappingInfo(methodAnnotation, methodCondition);
/*  45: 89 */       RequestMapping typeAnnotation = (RequestMapping)AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
/*  46: 90 */       if (typeAnnotation != null)
/*  47:    */       {
/*  48: 91 */         RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
/*  49: 92 */         info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
/*  50:    */       }
/*  51:    */     }
/*  52: 95 */     return info;
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected RequestCondition<?> getCustomMethodCondition(Method method)
/*  56:    */   {
/*  57:107 */     return null;
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType)
/*  61:    */   {
/*  62:119 */     return null;
/*  63:    */   }
/*  64:    */   
/*  65:    */   private RequestMappingInfo createRequestMappingInfo(RequestMapping annotation, RequestCondition<?> customCondition)
/*  66:    */   {
/*  67:126 */     return new RequestMappingInfo(
/*  68:127 */       new PatternsRequestCondition(annotation.value(), 
/*  69:128 */       getUrlPathHelper(), getPathMatcher(), this.useSuffixPatternMatch), 
/*  70:129 */       new RequestMethodsRequestCondition(annotation.method()), 
/*  71:130 */       new ParamsRequestCondition(annotation.params()), 
/*  72:131 */       new HeadersRequestCondition(annotation.headers()), 
/*  73:132 */       new ConsumesRequestCondition(annotation.consumes(), annotation.headers()), 
/*  74:133 */       new ProducesRequestCondition(annotation.produces(), annotation.headers()), 
/*  75:134 */       customCondition);
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
 * JD-Core Version:    0.7.0.1
 */