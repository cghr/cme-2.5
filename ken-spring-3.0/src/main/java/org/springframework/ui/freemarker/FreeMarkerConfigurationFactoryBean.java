/*  1:   */ package org.springframework.ui.freemarker;
/*  2:   */ 
/*  3:   */ import freemarker.template.Configuration;
/*  4:   */ import freemarker.template.TemplateException;
/*  5:   */ import java.io.IOException;
/*  6:   */ import org.springframework.beans.factory.FactoryBean;
/*  7:   */ import org.springframework.beans.factory.InitializingBean;
/*  8:   */ import org.springframework.context.ResourceLoaderAware;
/*  9:   */ 
/* 10:   */ public class FreeMarkerConfigurationFactoryBean
/* 11:   */   extends FreeMarkerConfigurationFactory
/* 12:   */   implements FactoryBean<Configuration>, InitializingBean, ResourceLoaderAware
/* 13:   */ {
/* 14:   */   private Configuration configuration;
/* 15:   */   
/* 16:   */   public void afterPropertiesSet()
/* 17:   */     throws IOException, TemplateException
/* 18:   */   {
/* 19:60 */     this.configuration = createConfiguration();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Configuration getObject()
/* 23:   */   {
/* 24:65 */     return this.configuration;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Class<? extends Configuration> getObjectType()
/* 28:   */   {
/* 29:69 */     return Configuration.class;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean isSingleton()
/* 33:   */   {
/* 34:73 */     return true;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean
 * JD-Core Version:    0.7.0.1
 */