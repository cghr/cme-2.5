package org.springframework.web.servlet.config.annotation;

import java.util.List;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

public abstract interface WebMvcConfigurer
{
  public abstract void addFormatters(FormatterRegistry paramFormatterRegistry);
  
  public abstract void configureMessageConverters(List<HttpMessageConverter<?>> paramList);
  
  public abstract Validator getValidator();
  
  public abstract void addArgumentResolvers(List<HandlerMethodArgumentResolver> paramList);
  
  public abstract void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> paramList);
  
  public abstract void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> paramList);
  
  public abstract void addInterceptors(InterceptorRegistry paramInterceptorRegistry);
  
  public abstract void addViewControllers(ViewControllerRegistry paramViewControllerRegistry);
  
  public abstract void addResourceHandlers(ResourceHandlerRegistry paramResourceHandlerRegistry);
  
  public abstract void configureDefaultServletHandling(DefaultServletHandlerConfigurer paramDefaultServletHandlerConfigurer);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 * JD-Core Version:    0.7.0.1
 */