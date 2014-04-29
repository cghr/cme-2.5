/*  1:   */ package org.springframework.web.servlet.config.annotation;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ import javax.servlet.ServletContext;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ import org.springframework.web.HttpRequestHandler;
/*  8:   */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*  9:   */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/* 10:   */ import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
/* 11:   */ 
/* 12:   */ public class DefaultServletHandlerConfigurer
/* 13:   */ {
/* 14:   */   private final ServletContext servletContext;
/* 15:   */   private DefaultServletHttpRequestHandler handler;
/* 16:   */   
/* 17:   */   public DefaultServletHandlerConfigurer(ServletContext servletContext)
/* 18:   */   {
/* 19:54 */     Assert.notNull(servletContext, "A ServletContext is required to configure default servlet handling");
/* 20:55 */     this.servletContext = servletContext;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void enable()
/* 24:   */   {
/* 25:65 */     enable(null);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void enable(String defaultServletName)
/* 29:   */   {
/* 30:74 */     this.handler = new DefaultServletHttpRequestHandler();
/* 31:75 */     this.handler.setDefaultServletName(defaultServletName);
/* 32:76 */     this.handler.setServletContext(this.servletContext);
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected AbstractHandlerMapping getHandlerMapping()
/* 36:   */   {
/* 37:85 */     if (this.handler == null) {
/* 38:86 */       return null;
/* 39:   */     }
/* 40:89 */     Map<String, HttpRequestHandler> urlMap = new HashMap();
/* 41:90 */     urlMap.put("/**", this.handler);
/* 42:   */     
/* 43:92 */     SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
/* 44:93 */     handlerMapping.setOrder(2147483647);
/* 45:94 */     handlerMapping.setUrlMap(urlMap);
/* 46:95 */     return handlerMapping;
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
 * JD-Core Version:    0.7.0.1
 */