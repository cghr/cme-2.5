/*   1:    */ package org.springframework.web.servlet.config.annotation;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.LinkedHashMap;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import javax.servlet.ServletContext;
/*   9:    */ import org.springframework.context.ApplicationContext;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ import org.springframework.web.HttpRequestHandler;
/*  12:    */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*  13:    */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*  14:    */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
/*  15:    */ 
/*  16:    */ public class ResourceHandlerRegistry
/*  17:    */ {
/*  18:    */   private final ServletContext servletContext;
/*  19:    */   private final ApplicationContext applicationContext;
/*  20: 57 */   private final List<ResourceHandlerRegistration> registrations = new ArrayList();
/*  21: 59 */   private int order = 2147483646;
/*  22:    */   
/*  23:    */   public ResourceHandlerRegistry(ApplicationContext applicationContext, ServletContext servletContext)
/*  24:    */   {
/*  25: 62 */     Assert.notNull(applicationContext, "ApplicationContext is required");
/*  26: 63 */     this.applicationContext = applicationContext;
/*  27: 64 */     this.servletContext = servletContext;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public ResourceHandlerRegistration addResourceHandler(String... pathPatterns)
/*  31:    */   {
/*  32: 73 */     ResourceHandlerRegistration registration = new ResourceHandlerRegistration(this.applicationContext, pathPatterns);
/*  33: 74 */     this.registrations.add(registration);
/*  34: 75 */     return registration;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ResourceHandlerRegistry setOrder(int order)
/*  38:    */   {
/*  39: 83 */     this.order = order;
/*  40: 84 */     return this;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected AbstractHandlerMapping getHandlerMapping()
/*  44:    */   {
/*  45: 91 */     if (this.registrations.isEmpty()) {
/*  46: 92 */       return null;
/*  47:    */     }
/*  48: 95 */     Map<String, HttpRequestHandler> urlMap = new LinkedHashMap();
/*  49:    */     int j;
/*  50:    */     int i;
/*  51: 96 */     for (Iterator localIterator = this.registrations.iterator(); localIterator.hasNext(); i < j)
/*  52:    */     {
/*  53: 96 */       ResourceHandlerRegistration registration = (ResourceHandlerRegistration)localIterator.next();
/*  54:    */       String[] arrayOfString;
/*  55: 97 */       j = (arrayOfString = registration.getPathPatterns()).length;i = 0; continue;String pathPattern = arrayOfString[i];
/*  56: 98 */       ResourceHttpRequestHandler requestHandler = registration.getRequestHandler();
/*  57: 99 */       requestHandler.setServletContext(this.servletContext);
/*  58:100 */       requestHandler.setApplicationContext(this.applicationContext);
/*  59:101 */       urlMap.put(pathPattern, requestHandler);i++;
/*  60:    */     }
/*  61:105 */     SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
/*  62:106 */     handlerMapping.setOrder(this.order);
/*  63:107 */     handlerMapping.setUrlMap(urlMap);
/*  64:108 */     return handlerMapping;
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
 * JD-Core Version:    0.7.0.1
 */