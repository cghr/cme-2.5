/*  1:   */ package org.springframework.jca.context;
/*  2:   */ 
/*  3:   */ import javax.resource.spi.BootstrapContext;
/*  4:   */ import javax.resource.spi.work.WorkManager;
/*  5:   */ import org.springframework.beans.BeansException;
/*  6:   */ import org.springframework.beans.factory.ObjectFactory;
/*  7:   */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  8:   */ import org.springframework.context.support.GenericApplicationContext;
/*  9:   */ import org.springframework.util.Assert;
/* 10:   */ 
/* 11:   */ public class ResourceAdapterApplicationContext
/* 12:   */   extends GenericApplicationContext
/* 13:   */ {
/* 14:   */   private final BootstrapContext bootstrapContext;
/* 15:   */   
/* 16:   */   public ResourceAdapterApplicationContext(BootstrapContext bootstrapContext)
/* 17:   */   {
/* 18:50 */     Assert.notNull(bootstrapContext, "BootstrapContext must not be null");
/* 19:51 */     this.bootstrapContext = bootstrapContext;
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/* 23:   */     throws BeansException
/* 24:   */   {
/* 25:57 */     beanFactory.addBeanPostProcessor(new BootstrapContextAwareProcessor(this.bootstrapContext));
/* 26:58 */     beanFactory.ignoreDependencyInterface(BootstrapContextAware.class);
/* 27:59 */     beanFactory.registerResolvableDependency(BootstrapContext.class, this.bootstrapContext);
/* 28:   */     
/* 29:   */ 
/* 30:62 */     beanFactory.registerResolvableDependency(WorkManager.class, new ObjectFactory()
/* 31:   */     {
/* 32:   */       public WorkManager getObject()
/* 33:   */       {
/* 34:64 */         return ResourceAdapterApplicationContext.this.bootstrapContext.getWorkManager();
/* 35:   */       }
/* 36:   */     });
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.context.ResourceAdapterApplicationContext
 * JD-Core Version:    0.7.0.1
 */