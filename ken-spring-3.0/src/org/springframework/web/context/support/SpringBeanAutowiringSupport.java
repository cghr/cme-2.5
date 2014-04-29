/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import javax.servlet.ServletContext;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.apache.commons.logging.LogFactory;
/*   6:    */ import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.ClassUtils;
/*   9:    */ import org.springframework.web.context.ContextLoader;
/*  10:    */ import org.springframework.web.context.WebApplicationContext;
/*  11:    */ 
/*  12:    */ public abstract class SpringBeanAutowiringSupport
/*  13:    */ {
/*  14: 58 */   private static final Log logger = LogFactory.getLog(SpringBeanAutowiringSupport.class);
/*  15:    */   
/*  16:    */   public SpringBeanAutowiringSupport()
/*  17:    */   {
/*  18: 68 */     processInjectionBasedOnCurrentContext(this);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static void processInjectionBasedOnCurrentContext(Object target)
/*  22:    */   {
/*  23: 80 */     Assert.notNull(target, "Target object must not be null");
/*  24: 81 */     WebApplicationContext cc = ContextLoader.getCurrentWebApplicationContext();
/*  25: 82 */     if (cc != null)
/*  26:    */     {
/*  27: 83 */       AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
/*  28: 84 */       bpp.setBeanFactory(cc.getAutowireCapableBeanFactory());
/*  29: 85 */       bpp.processInjection(target);
/*  30:    */     }
/*  31: 88 */     else if (logger.isDebugEnabled())
/*  32:    */     {
/*  33: 89 */       logger.debug("Current WebApplicationContext is not available for processing of " + 
/*  34: 90 */         ClassUtils.getShortName(target.getClass()) + ": " + 
/*  35: 91 */         "Make sure this class gets constructed in a Spring web application. Proceeding without injection.");
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static void processInjectionBasedOnServletContext(Object target, ServletContext servletContext)
/*  40:    */   {
/*  41:106 */     Assert.notNull(target, "Target object must not be null");
/*  42:107 */     WebApplicationContext cc = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
/*  43:108 */     AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
/*  44:109 */     bpp.setBeanFactory(cc.getAutowireCapableBeanFactory());
/*  45:110 */     bpp.processInjection(target);
/*  46:    */   }
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.SpringBeanAutowiringSupport
 * JD-Core Version:    0.7.0.1
 */