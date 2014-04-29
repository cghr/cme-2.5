/*   1:    */ package org.springframework.web.servlet.config.annotation;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.springframework.beans.factory.annotation.Autowired;
/*   5:    */ import org.springframework.context.annotation.Configuration;
/*   6:    */ import org.springframework.format.FormatterRegistry;
/*   7:    */ import org.springframework.http.converter.HttpMessageConverter;
/*   8:    */ import org.springframework.validation.Validator;
/*   9:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  10:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  11:    */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*  12:    */ 
/*  13:    */ @Configuration
/*  14:    */ public class DelegatingWebMvcConfiguration
/*  15:    */   extends WebMvcConfigurationSupport
/*  16:    */ {
/*  17: 48 */   private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();
/*  18:    */   
/*  19:    */   @Autowired(required=false)
/*  20:    */   public void setConfigurers(List<WebMvcConfigurer> configurers)
/*  21:    */   {
/*  22: 52 */     if ((configurers == null) || (configurers.isEmpty())) {
/*  23: 53 */       return;
/*  24:    */     }
/*  25: 55 */     this.configurers.addWebMvcConfigurers(configurers);
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected final void addInterceptors(InterceptorRegistry registry)
/*  29:    */   {
/*  30: 60 */     this.configurers.addInterceptors(registry);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected final void addViewControllers(ViewControllerRegistry registry)
/*  34:    */   {
/*  35: 65 */     this.configurers.addViewControllers(registry);
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected final void addResourceHandlers(ResourceHandlerRegistry registry)
/*  39:    */   {
/*  40: 70 */     this.configurers.addResourceHandlers(registry);
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected final void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
/*  44:    */   {
/*  45: 75 */     this.configurers.configureDefaultServletHandling(configurer);
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected final void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*  49:    */   {
/*  50: 80 */     this.configurers.addArgumentResolvers(argumentResolvers);
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected final void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/*  54:    */   {
/*  55: 85 */     this.configurers.addReturnValueHandlers(returnValueHandlers);
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected final void configureMessageConverters(List<HttpMessageConverter<?>> converters)
/*  59:    */   {
/*  60: 90 */     this.configurers.configureMessageConverters(converters);
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected final void addFormatters(FormatterRegistry registry)
/*  64:    */   {
/*  65: 95 */     this.configurers.addFormatters(registry);
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected final Validator getValidator()
/*  69:    */   {
/*  70:100 */     return this.configurers.getValidator();
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected final void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/*  74:    */   {
/*  75:105 */     this.configurers.configureHandlerExceptionResolvers(exceptionResolvers);
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration
 * JD-Core Version:    0.7.0.1
 */