/*  1:   */ package org.springframework.web.servlet.config.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
/*  5:   */ 
/*  6:   */ public class ViewControllerRegistration
/*  7:   */ {
/*  8:   */   private final String urlPath;
/*  9:   */   private String viewName;
/* 10:   */   
/* 11:   */   public ViewControllerRegistration(String urlPath)
/* 12:   */   {
/* 13:41 */     Assert.notNull(urlPath, "A URL path is required to create a view controller.");
/* 14:42 */     this.urlPath = urlPath;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void setViewName(String viewName)
/* 18:   */   {
/* 19:51 */     this.viewName = viewName;
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected String getUrlPath()
/* 23:   */   {
/* 24:58 */     return this.urlPath;
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected Object getViewController()
/* 28:   */   {
/* 29:65 */     ParameterizableViewController controller = new ParameterizableViewController();
/* 30:66 */     controller.setViewName(this.viewName);
/* 31:67 */     return controller;
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.ViewControllerRegistration
 * JD-Core Version:    0.7.0.1
 */