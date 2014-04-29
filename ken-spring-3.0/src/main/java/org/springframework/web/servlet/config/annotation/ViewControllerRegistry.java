/*  1:   */ package org.springframework.web.servlet.config.annotation;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.LinkedHashMap;
/*  5:   */ import java.util.List;
/*  6:   */ import java.util.Map;
/*  7:   */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*  8:   */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*  9:   */ 
/* 10:   */ public class ViewControllerRegistry
/* 11:   */ {
/* 12:39 */   private final List<ViewControllerRegistration> registrations = new ArrayList();
/* 13:41 */   private int order = 1;
/* 14:   */   
/* 15:   */   public ViewControllerRegistration addViewController(String urlPath)
/* 16:   */   {
/* 17:44 */     ViewControllerRegistration registration = new ViewControllerRegistration(urlPath);
/* 18:45 */     this.registrations.add(registration);
/* 19:46 */     return registration;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setOrder(int order)
/* 23:   */   {
/* 24:55 */     this.order = order;
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected AbstractHandlerMapping getHandlerMapping()
/* 28:   */   {
/* 29:62 */     if (this.registrations.isEmpty()) {
/* 30:63 */       return null;
/* 31:   */     }
/* 32:66 */     Map<String, Object> urlMap = new LinkedHashMap();
/* 33:67 */     for (ViewControllerRegistration registration : this.registrations) {
/* 34:68 */       urlMap.put(registration.getUrlPath(), registration.getViewController());
/* 35:   */     }
/* 36:71 */     SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
/* 37:72 */     handlerMapping.setOrder(this.order);
/* 38:73 */     handlerMapping.setUrlMap(urlMap);
/* 39:74 */     return handlerMapping;
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.ViewControllerRegistry
 * JD-Core Version:    0.7.0.1
 */