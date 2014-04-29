/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  5:   */ import org.springframework.beans.factory.annotation.Autowired;
/*  6:   */ import org.springframework.context.weaving.AspectJWeavingEnabler;
/*  7:   */ import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
/*  8:   */ import org.springframework.core.type.AnnotationMetadata;
/*  9:   */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/* 10:   */ import org.springframework.util.Assert;
/* 11:   */ 
/* 12:   */ @Configuration
/* 13:   */ public class LoadTimeWeavingConfiguration
/* 14:   */   implements ImportAware, BeanClassLoaderAware
/* 15:   */ {
/* 16:   */   private Map<String, Object> enableLTW;
/* 17:   */   @Autowired(required=false)
/* 18:   */   private LoadTimeWeavingConfigurer ltwConfigurer;
/* 19:   */   private ClassLoader beanClassLoader;
/* 20:   */   
/* 21:   */   public void setImportMetadata(AnnotationMetadata importMetadata)
/* 22:   */   {
/* 23:57 */     this.enableLTW = importMetadata.getAnnotationAttributes(EnableLoadTimeWeaving.class.getName(), false);
/* 24:58 */     Assert.notNull(this.enableLTW, 
/* 25:59 */       "@EnableLoadTimeWeaving is not present on importing class " + 
/* 26:60 */       importMetadata.getClassName());
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/* 30:   */   {
/* 31:64 */     this.beanClassLoader = beanClassLoader;
/* 32:   */   }
/* 33:   */   
/* 34:   */   @Bean(name={"loadTimeWeaver"})
/* 35:   */   @Role(2)
/* 36:   */   public LoadTimeWeaver loadTimeWeaver()
/* 37:   */   {
/* 38:70 */     LoadTimeWeaver loadTimeWeaver = null;
/* 39:72 */     if (this.ltwConfigurer != null) {
/* 40:74 */       loadTimeWeaver = this.ltwConfigurer.getLoadTimeWeaver();
/* 41:   */     }
/* 42:77 */     if (loadTimeWeaver == null) {
/* 43:79 */       loadTimeWeaver = new DefaultContextLoadTimeWeaver(this.beanClassLoader);
/* 44:   */     }
/* 45:82 */     switch ((EnableLoadTimeWeaving.AspectJWeaving)this.enableLTW.get("aspectjWeaving"))
/* 46:   */     {
/* 47:   */     case DISABLED: 
/* 48:   */       break;
/* 49:   */     case ENABLED: 
/* 50:87 */       if (this.beanClassLoader.getResource("META-INF/aop.xml") == null) {
/* 51:   */         break;
/* 52:   */       }
/* 53:   */     case AUTODETECT: 
/* 54:93 */       AspectJWeavingEnabler.enableAspectJWeaving(loadTimeWeaver, this.beanClassLoader);
/* 55:   */     }
/* 56:96 */     return loadTimeWeaver;
/* 57:   */   }
/* 58:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.LoadTimeWeavingConfiguration
 * JD-Core Version:    0.7.0.1
 */