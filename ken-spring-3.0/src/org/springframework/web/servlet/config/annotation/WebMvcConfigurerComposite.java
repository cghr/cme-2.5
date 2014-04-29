/*   1:    */ package org.springframework.web.servlet.config.annotation;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.springframework.format.FormatterRegistry;
/*  10:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  11:    */ import org.springframework.validation.Validator;
/*  12:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  13:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  14:    */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*  15:    */ 
/*  16:    */ class WebMvcConfigurerComposite
/*  17:    */   implements WebMvcConfigurer
/*  18:    */ {
/*  19: 39 */   private final List<WebMvcConfigurer> delegates = new ArrayList();
/*  20:    */   
/*  21:    */   public void addWebMvcConfigurers(List<WebMvcConfigurer> configurers)
/*  22:    */   {
/*  23: 42 */     if (configurers != null) {
/*  24: 43 */       this.delegates.addAll(configurers);
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void addFormatters(FormatterRegistry registry)
/*  29:    */   {
/*  30: 48 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  31: 49 */       delegate.addFormatters(registry);
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
/*  36:    */   {
/*  37: 54 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  38: 55 */       delegate.configureMessageConverters(converters);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*  43:    */   {
/*  44: 60 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  45: 61 */       delegate.addArgumentResolvers(argumentResolvers);
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/*  50:    */   {
/*  51: 66 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  52: 67 */       delegate.addReturnValueHandlers(returnValueHandlers);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/*  57:    */   {
/*  58: 72 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  59: 73 */       delegate.configureHandlerExceptionResolvers(exceptionResolvers);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void addInterceptors(InterceptorRegistry registry)
/*  64:    */   {
/*  65: 78 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  66: 79 */       delegate.addInterceptors(registry);
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void addViewControllers(ViewControllerRegistry registry)
/*  71:    */   {
/*  72: 84 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  73: 85 */       delegate.addViewControllers(registry);
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void addResourceHandlers(ResourceHandlerRegistry registry)
/*  78:    */   {
/*  79: 90 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  80: 91 */       delegate.addResourceHandlers(registry);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
/*  85:    */   {
/*  86: 96 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  87: 97 */       delegate.configureDefaultServletHandling(configurer);
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public Validator getValidator()
/*  92:    */   {
/*  93:102 */     Map<WebMvcConfigurer, Validator> validators = new HashMap();
/*  94:103 */     for (WebMvcConfigurer delegate : this.delegates)
/*  95:    */     {
/*  96:104 */       Validator validator = delegate.getValidator();
/*  97:105 */       if (validator != null) {
/*  98:106 */         validators.put(delegate, validator);
/*  99:    */       }
/* 100:    */     }
/* 101:109 */     if (validators.size() == 0) {
/* 102:110 */       return null;
/* 103:    */     }
/* 104:112 */     if (validators.size() == 1) {
/* 105:113 */       return (Validator)validators.values().iterator().next();
/* 106:    */     }
/* 107:116 */     throw new IllegalStateException(
/* 108:117 */       "Multiple custom validators provided from [" + validators.keySet() + "]");
/* 109:    */   }
/* 110:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.WebMvcConfigurerComposite
 * JD-Core Version:    0.7.0.1
 */