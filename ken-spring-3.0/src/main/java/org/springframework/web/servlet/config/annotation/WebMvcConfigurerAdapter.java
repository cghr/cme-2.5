/*  1:   */ package org.springframework.web.servlet.config.annotation;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.springframework.format.FormatterRegistry;
/*  5:   */ import org.springframework.http.converter.HttpMessageConverter;
/*  6:   */ import org.springframework.validation.Validator;
/*  7:   */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  8:   */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  9:   */ import org.springframework.web.servlet.HandlerExceptionResolver;
/* 10:   */ 
/* 11:   */ public abstract class WebMvcConfigurerAdapter
/* 12:   */   implements WebMvcConfigurer
/* 13:   */ {
/* 14:   */   public void addFormatters(FormatterRegistry registry) {}
/* 15:   */   
/* 16:   */   public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {}
/* 17:   */   
/* 18:   */   public Validator getValidator()
/* 19:   */   {
/* 20:55 */     return null;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {}
/* 24:   */   
/* 25:   */   public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {}
/* 26:   */   
/* 27:   */   public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}
/* 28:   */   
/* 29:   */   public void addInterceptors(InterceptorRegistry registry) {}
/* 30:   */   
/* 31:   */   public void addViewControllers(ViewControllerRegistry registry) {}
/* 32:   */   
/* 33:   */   public void addResourceHandlers(ResourceHandlerRegistry registry) {}
/* 34:   */   
/* 35:   */   public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {}
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
 * JD-Core Version:    0.7.0.1
 */