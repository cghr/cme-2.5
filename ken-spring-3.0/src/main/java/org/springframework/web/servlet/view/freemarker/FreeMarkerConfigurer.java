/*   1:    */ package org.springframework.web.servlet.view.freemarker;
/*   2:    */ 
/*   3:    */ import freemarker.cache.ClassTemplateLoader;
/*   4:    */ import freemarker.cache.TemplateLoader;
/*   5:    */ import freemarker.ext.jsp.TaglibFactory;
/*   6:    */ import freemarker.template.Configuration;
/*   7:    */ import freemarker.template.TemplateException;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.util.List;
/*  10:    */ import javax.servlet.ServletContext;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.springframework.beans.factory.InitializingBean;
/*  13:    */ import org.springframework.context.ResourceLoaderAware;
/*  14:    */ import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
/*  15:    */ import org.springframework.web.context.ServletContextAware;
/*  16:    */ 
/*  17:    */ public class FreeMarkerConfigurer
/*  18:    */   extends FreeMarkerConfigurationFactory
/*  19:    */   implements FreeMarkerConfig, InitializingBean, ResourceLoaderAware, ServletContextAware
/*  20:    */ {
/*  21:    */   private Configuration configuration;
/*  22:    */   private TaglibFactory taglibFactory;
/*  23:    */   
/*  24:    */   public void setConfiguration(Configuration configuration)
/*  25:    */   {
/*  26: 94 */     this.configuration = configuration;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setServletContext(ServletContext servletContext)
/*  30:    */   {
/*  31:101 */     this.taglibFactory = new TaglibFactory(servletContext);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void afterPropertiesSet()
/*  35:    */     throws IOException, TemplateException
/*  36:    */   {
/*  37:113 */     if (this.configuration == null) {
/*  38:114 */       this.configuration = createConfiguration();
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders)
/*  43:    */   {
/*  44:124 */     templateLoaders.add(new ClassTemplateLoader(FreeMarkerConfigurer.class, ""));
/*  45:125 */     this.logger.info("ClassTemplateLoader for Spring macros added to FreeMarker configuration");
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Configuration getConfiguration()
/*  49:    */   {
/*  50:133 */     return this.configuration;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public TaglibFactory getTaglibFactory()
/*  54:    */   {
/*  55:140 */     return this.taglibFactory;
/*  56:    */   }
/*  57:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
 * JD-Core Version:    0.7.0.1
 */