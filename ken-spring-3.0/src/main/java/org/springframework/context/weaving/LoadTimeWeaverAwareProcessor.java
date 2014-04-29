/*   1:    */ package org.springframework.context.weaving;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.BeansException;
/*   4:    */ import org.springframework.beans.factory.BeanFactory;
/*   5:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*   6:    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*   7:    */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ 
/*  10:    */ public class LoadTimeWeaverAwareProcessor
/*  11:    */   implements BeanPostProcessor, BeanFactoryAware
/*  12:    */ {
/*  13:    */   private LoadTimeWeaver loadTimeWeaver;
/*  14:    */   private BeanFactory beanFactory;
/*  15:    */   
/*  16:    */   public LoadTimeWeaverAwareProcessor() {}
/*  17:    */   
/*  18:    */   public LoadTimeWeaverAwareProcessor(LoadTimeWeaver loadTimeWeaver)
/*  19:    */   {
/*  20: 69 */     this.loadTimeWeaver = loadTimeWeaver;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public LoadTimeWeaverAwareProcessor(BeanFactory beanFactory)
/*  24:    */   {
/*  25: 80 */     this.beanFactory = beanFactory;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  29:    */   {
/*  30: 85 */     this.beanFactory = beanFactory;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*  34:    */     throws BeansException
/*  35:    */   {
/*  36: 90 */     if ((bean instanceof LoadTimeWeaverAware))
/*  37:    */     {
/*  38: 91 */       LoadTimeWeaver ltw = this.loadTimeWeaver;
/*  39: 92 */       if (ltw == null)
/*  40:    */       {
/*  41: 93 */         Assert.state(this.beanFactory != null, 
/*  42: 94 */           "BeanFactory required if no LoadTimeWeaver explicitly specified");
/*  43: 95 */         ltw = (LoadTimeWeaver)this.beanFactory.getBean(
/*  44: 96 */           "loadTimeWeaver", LoadTimeWeaver.class);
/*  45:    */       }
/*  46: 98 */       ((LoadTimeWeaverAware)bean).setLoadTimeWeaver(ltw);
/*  47:    */     }
/*  48:100 */     return bean;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Object postProcessAfterInitialization(Object bean, String name)
/*  52:    */   {
/*  53:104 */     return bean;
/*  54:    */   }
/*  55:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.weaving.LoadTimeWeaverAwareProcessor
 * JD-Core Version:    0.7.0.1
 */