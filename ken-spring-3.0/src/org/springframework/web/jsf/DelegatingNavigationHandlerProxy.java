/*   1:    */ package org.springframework.web.jsf;
/*   2:    */ 
/*   3:    */ import javax.faces.application.NavigationHandler;
/*   4:    */ import javax.faces.context.FacesContext;
/*   5:    */ import org.springframework.beans.factory.BeanFactory;
/*   6:    */ import org.springframework.web.context.WebApplicationContext;
/*   7:    */ 
/*   8:    */ public class DelegatingNavigationHandlerProxy
/*   9:    */   extends NavigationHandler
/*  10:    */ {
/*  11:    */   public static final String DEFAULT_TARGET_BEAN_NAME = "jsfNavigationHandler";
/*  12:    */   private NavigationHandler originalNavigationHandler;
/*  13:    */   
/*  14:    */   public DelegatingNavigationHandlerProxy() {}
/*  15:    */   
/*  16:    */   public DelegatingNavigationHandlerProxy(NavigationHandler originalNavigationHandler)
/*  17:    */   {
/*  18: 95 */     this.originalNavigationHandler = originalNavigationHandler;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void handleNavigation(FacesContext facesContext, String fromAction, String outcome)
/*  22:    */   {
/*  23:110 */     NavigationHandler handler = getDelegate(facesContext);
/*  24:111 */     if ((handler instanceof DecoratingNavigationHandler)) {
/*  25:112 */       ((DecoratingNavigationHandler)handler).handleNavigation(
/*  26:113 */         facesContext, fromAction, outcome, this.originalNavigationHandler);
/*  27:    */     } else {
/*  28:116 */       handler.handleNavigation(facesContext, fromAction, outcome);
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected NavigationHandler getDelegate(FacesContext facesContext)
/*  33:    */   {
/*  34:130 */     String targetBeanName = getTargetBeanName(facesContext);
/*  35:131 */     return (NavigationHandler)getBeanFactory(facesContext).getBean(targetBeanName, NavigationHandler.class);
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected String getTargetBeanName(FacesContext facesContext)
/*  39:    */   {
/*  40:141 */     return "jsfNavigationHandler";
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected BeanFactory getBeanFactory(FacesContext facesContext)
/*  44:    */   {
/*  45:154 */     return getWebApplicationContext(facesContext);
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected WebApplicationContext getWebApplicationContext(FacesContext facesContext)
/*  49:    */   {
/*  50:165 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.jsf.DelegatingNavigationHandlerProxy
 * JD-Core Version:    0.7.0.1
 */