/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.aop.config.AopConfigUtils;
/*  5:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  6:   */ import org.springframework.core.type.AnnotationMetadata;
/*  7:   */ 
/*  8:   */ public class AspectJAutoProxyConfigurationSelector
/*  9:   */   implements ImportSelector
/* 10:   */ {
/* 11:   */   public String[] selectImports(ImportSelectorContext context)
/* 12:   */   {
/* 13:41 */     BeanDefinitionRegistry registry = context.getBeanDefinitionRegistry();
/* 14:42 */     AnnotationMetadata importingClassMetadata = context.getImportingClassMetadata();
/* 15:   */     
/* 16:44 */     Map<String, Object> enableAJAutoProxy = 
/* 17:45 */       importingClassMetadata.getAnnotationAttributes(EnableAspectJAutoProxy.class.getName());
/* 18:   */     
/* 19:47 */     AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
/* 20:49 */     if (((Boolean)enableAJAutoProxy.get("proxyTargetClass")).booleanValue()) {
/* 21:50 */       AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
/* 22:   */     }
/* 23:53 */     return new String[0];
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.AspectJAutoProxyConfigurationSelector
 * JD-Core Version:    0.7.0.1
 */