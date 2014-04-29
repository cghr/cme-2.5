/*  1:   */ package org.springframework.web.servlet.view;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.springframework.beans.BeansException;
/*  5:   */ import org.springframework.context.ApplicationContext;
/*  6:   */ import org.springframework.core.Ordered;
/*  7:   */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*  8:   */ import org.springframework.web.servlet.View;
/*  9:   */ import org.springframework.web.servlet.ViewResolver;
/* 10:   */ 
/* 11:   */ public class BeanNameViewResolver
/* 12:   */   extends WebApplicationObjectSupport
/* 13:   */   implements ViewResolver, Ordered
/* 14:   */ {
/* 15:57 */   private int order = 2147483647;
/* 16:   */   
/* 17:   */   public void setOrder(int order)
/* 18:   */   {
/* 19:61 */     this.order = order;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public int getOrder()
/* 23:   */   {
/* 24:65 */     return this.order;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public View resolveViewName(String viewName, Locale locale)
/* 28:   */     throws BeansException
/* 29:   */   {
/* 30:70 */     ApplicationContext context = getApplicationContext();
/* 31:71 */     if (!context.containsBean(viewName)) {
/* 32:73 */       return null;
/* 33:   */     }
/* 34:75 */     return (View)context.getBean(viewName, View.class);
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.BeanNameViewResolver
 * JD-Core Version:    0.7.0.1
 */