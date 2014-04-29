/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletConfig;
/*  4:   */ import javax.servlet.ServletContext;
/*  5:   */ import org.springframework.beans.BeansException;
/*  6:   */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*  7:   */ import org.springframework.web.context.ServletConfigAware;
/*  8:   */ import org.springframework.web.context.ServletContextAware;
/*  9:   */ 
/* 10:   */ public class ServletContextAwareProcessor
/* 11:   */   implements BeanPostProcessor
/* 12:   */ {
/* 13:   */   private ServletContext servletContext;
/* 14:   */   private ServletConfig servletConfig;
/* 15:   */   
/* 16:   */   public ServletContextAwareProcessor(ServletContext servletContext)
/* 17:   */   {
/* 18:51 */     this(servletContext, null);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public ServletContextAwareProcessor(ServletConfig servletConfig)
/* 22:   */   {
/* 23:58 */     this(null, servletConfig);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public ServletContextAwareProcessor(ServletContext servletContext, ServletConfig servletConfig)
/* 27:   */   {
/* 28:65 */     this.servletContext = servletContext;
/* 29:66 */     this.servletConfig = servletConfig;
/* 30:67 */     if ((servletContext == null) && (servletConfig != null)) {
/* 31:68 */       this.servletContext = servletConfig.getServletContext();
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/* 36:   */     throws BeansException
/* 37:   */   {
/* 38:74 */     if ((this.servletContext != null) && ((bean instanceof ServletContextAware))) {
/* 39:75 */       ((ServletContextAware)bean).setServletContext(this.servletContext);
/* 40:   */     }
/* 41:77 */     if ((this.servletConfig != null) && ((bean instanceof ServletConfigAware))) {
/* 42:78 */       ((ServletConfigAware)bean).setServletConfig(this.servletConfig);
/* 43:   */     }
/* 44:80 */     return bean;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public Object postProcessAfterInitialization(Object bean, String beanName)
/* 48:   */   {
/* 49:84 */     return bean;
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextAwareProcessor
 * JD-Core Version:    0.7.0.1
 */