/*  1:   */ package org.springframework.transaction.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.aop.config.AopConfigUtils;
/*  5:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  6:   */ import org.springframework.context.annotation.ImportSelector;
/*  7:   */ import org.springframework.context.annotation.ImportSelectorContext;
/*  8:   */ import org.springframework.core.type.AnnotationMetadata;
/*  9:   */ import org.springframework.util.Assert;
/* 10:   */ 
/* 11:   */ public class TransactionManagementConfigurationSelector
/* 12:   */   implements ImportSelector
/* 13:   */ {
/* 14:   */   public String[] selectImports(ImportSelectorContext context)
/* 15:   */   {
/* 16:55 */     AnnotationMetadata importingClassMetadata = context.getImportingClassMetadata();
/* 17:56 */     BeanDefinitionRegistry registry = context.getBeanDefinitionRegistry();
/* 18:   */     
/* 19:58 */     Map<String, Object> enableTx = 
/* 20:59 */       importingClassMetadata.getAnnotationAttributes(EnableTransactionManagement.class.getName());
/* 21:60 */     Assert.notNull(enableTx, 
/* 22:61 */       "@EnableTransactionManagement is not present on importing class " + 
/* 23:62 */       importingClassMetadata.getClassName());
/* 24:64 */     switch ((org.springframework.context.annotation.AdviceMode)enableTx.get("mode"))
/* 25:   */     {
/* 26:   */     case ASPECTJ: 
/* 27:66 */       AopConfigUtils.registerAutoProxyCreatorIfNecessary(registry);
/* 28:67 */       if (((Boolean)enableTx.get("proxyTargetClass")).booleanValue()) {
/* 29:68 */         AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
/* 30:   */       }
/* 31:70 */       return new String[] { ProxyTransactionManagementConfiguration.class.getName() };
/* 32:   */     case PROXY: 
/* 33:72 */       return new String[] { "org.springframework.transaction.aspectj.AspectJTransactionManagementConfiguration" };
/* 34:   */     }
/* 35:74 */     throw new IllegalArgumentException("Unknown AdviceMode " + enableTx.get("mode"));
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.TransactionManagementConfigurationSelector
 * JD-Core Version:    0.7.0.1
 */