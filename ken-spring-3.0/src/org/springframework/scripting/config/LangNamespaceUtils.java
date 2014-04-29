/*  1:   */ package org.springframework.scripting.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  5:   */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  6:   */ import org.springframework.scripting.support.ScriptFactoryPostProcessor;
/*  7:   */ 
/*  8:   */ public abstract class LangNamespaceUtils
/*  9:   */ {
/* 10:   */   private static final String SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME = "org.springframework.scripting.config.scriptFactoryPostProcessor";
/* 11:   */   
/* 12:   */   public static BeanDefinition registerScriptFactoryPostProcessorIfNecessary(BeanDefinitionRegistry registry)
/* 13:   */   {
/* 14:47 */     BeanDefinition beanDefinition = null;
/* 15:48 */     if (registry.containsBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor"))
/* 16:   */     {
/* 17:49 */       beanDefinition = registry.getBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor");
/* 18:   */     }
/* 19:   */     else
/* 20:   */     {
/* 21:52 */       beanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
/* 22:53 */       registry.registerBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor", beanDefinition);
/* 23:   */     }
/* 24:55 */     return beanDefinition;
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.config.LangNamespaceUtils
 * JD-Core Version:    0.7.0.1
 */