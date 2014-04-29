/*   1:    */ package org.springframework.web.servlet.view.velocity;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.velocity.app.VelocityEngine;
/*   7:    */ import org.apache.velocity.exception.VelocityException;
/*   8:    */ import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
/*   9:    */ import org.springframework.beans.factory.InitializingBean;
/*  10:    */ import org.springframework.context.ResourceLoaderAware;
/*  11:    */ import org.springframework.ui.velocity.VelocityEngineFactory;
/*  12:    */ import org.springframework.web.context.ServletContextAware;
/*  13:    */ 
/*  14:    */ public class VelocityConfigurer
/*  15:    */   extends VelocityEngineFactory
/*  16:    */   implements VelocityConfig, InitializingBean, ResourceLoaderAware, ServletContextAware
/*  17:    */ {
/*  18:    */   private static final String SPRING_MACRO_RESOURCE_LOADER_NAME = "springMacro";
/*  19:    */   private static final String SPRING_MACRO_RESOURCE_LOADER_CLASS = "springMacro.resource.loader.class";
/*  20:    */   private static final String SPRING_MACRO_LIBRARY = "org/springframework/web/servlet/view/velocity/spring.vm";
/*  21:    */   private VelocityEngine velocityEngine;
/*  22:    */   private ServletContext servletContext;
/*  23:    */   
/*  24:    */   public void setVelocityEngine(VelocityEngine velocityEngine)
/*  25:    */   {
/*  26:104 */     this.velocityEngine = velocityEngine;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setServletContext(ServletContext servletContext)
/*  30:    */   {
/*  31:108 */     this.servletContext = servletContext;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void afterPropertiesSet()
/*  35:    */     throws IOException, VelocityException
/*  36:    */   {
/*  37:118 */     if (this.velocityEngine == null) {
/*  38:119 */       this.velocityEngine = createVelocityEngine();
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected void postProcessVelocityEngine(VelocityEngine velocityEngine)
/*  43:    */   {
/*  44:130 */     velocityEngine.setApplicationAttribute(ServletContext.class.getName(), this.servletContext);
/*  45:131 */     velocityEngine.setProperty(
/*  46:132 */       "springMacro.resource.loader.class", ClasspathResourceLoader.class.getName());
/*  47:133 */     velocityEngine.addProperty(
/*  48:134 */       "resource.loader", "springMacro");
/*  49:135 */     velocityEngine.addProperty(
/*  50:136 */       "velocimacro.library", "org/springframework/web/servlet/view/velocity/spring.vm");
/*  51:138 */     if (this.logger.isInfoEnabled()) {
/*  52:139 */       this.logger.info("ClasspathResourceLoader with name 'springMacro' added to configured VelocityEngine");
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public VelocityEngine getVelocityEngine()
/*  57:    */   {
/*  58:145 */     return this.velocityEngine;
/*  59:    */   }
/*  60:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.velocity.VelocityConfigurer
 * JD-Core Version:    0.7.0.1
 */