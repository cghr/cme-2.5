/*  1:   */ package org.springframework.jca.context;
/*  2:   */ 
/*  3:   */ import javax.resource.spi.BootstrapContext;
/*  4:   */ import org.springframework.beans.BeansException;
/*  5:   */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*  6:   */ 
/*  7:   */ class BootstrapContextAwareProcessor
/*  8:   */   implements BeanPostProcessor
/*  9:   */ {
/* 10:   */   private final BootstrapContext bootstrapContext;
/* 11:   */   
/* 12:   */   public BootstrapContextAwareProcessor(BootstrapContext bootstrapContext)
/* 13:   */   {
/* 14:45 */     this.bootstrapContext = bootstrapContext;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/* 18:   */     throws BeansException
/* 19:   */   {
/* 20:50 */     if ((this.bootstrapContext != null) && ((bean instanceof BootstrapContextAware))) {
/* 21:51 */       ((BootstrapContextAware)bean).setBootstrapContext(this.bootstrapContext);
/* 22:   */     }
/* 23:53 */     return bean;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Object postProcessAfterInitialization(Object bean, String beanName)
/* 27:   */     throws BeansException
/* 28:   */   {
/* 29:57 */     return bean;
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.context.BootstrapContextAwareProcessor
 * JD-Core Version:    0.7.0.1
 */