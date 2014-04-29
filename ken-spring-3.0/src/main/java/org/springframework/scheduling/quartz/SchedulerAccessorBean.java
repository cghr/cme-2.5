/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import org.quartz.Scheduler;
/*   4:    */ import org.quartz.SchedulerException;
/*   5:    */ import org.quartz.impl.SchedulerRepository;
/*   6:    */ import org.springframework.beans.factory.BeanFactory;
/*   7:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*   8:    */ import org.springframework.beans.factory.InitializingBean;
/*   9:    */ import org.springframework.beans.factory.ListableBeanFactory;
/*  10:    */ 
/*  11:    */ public class SchedulerAccessorBean
/*  12:    */   extends SchedulerAccessor
/*  13:    */   implements BeanFactoryAware, InitializingBean
/*  14:    */ {
/*  15:    */   private String schedulerName;
/*  16:    */   private Scheduler scheduler;
/*  17:    */   private BeanFactory beanFactory;
/*  18:    */   
/*  19:    */   public void setSchedulerName(String schedulerName)
/*  20:    */   {
/*  21: 58 */     this.schedulerName = schedulerName;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setScheduler(Scheduler scheduler)
/*  25:    */   {
/*  26: 65 */     this.scheduler = scheduler;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Scheduler getScheduler()
/*  30:    */   {
/*  31: 73 */     return this.scheduler;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  35:    */   {
/*  36: 77 */     this.beanFactory = beanFactory;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void afterPropertiesSet()
/*  40:    */     throws SchedulerException
/*  41:    */   {
/*  42: 82 */     if (this.scheduler == null) {
/*  43: 83 */       if (this.schedulerName != null) {
/*  44: 84 */         this.scheduler = findScheduler(this.schedulerName);
/*  45:    */       } else {
/*  46: 87 */         throw new IllegalStateException("No Scheduler specified");
/*  47:    */       }
/*  48:    */     }
/*  49: 90 */     registerListeners();
/*  50: 91 */     registerJobsAndTriggers();
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected Scheduler findScheduler(String schedulerName)
/*  54:    */     throws SchedulerException
/*  55:    */   {
/*  56: 95 */     if ((this.beanFactory instanceof ListableBeanFactory))
/*  57:    */     {
/*  58: 96 */       ListableBeanFactory lbf = (ListableBeanFactory)this.beanFactory;
/*  59: 97 */       String[] beanNames = lbf.getBeanNamesForType(Scheduler.class);
/*  60: 98 */       for (int i = 0; i < beanNames.length; i++)
/*  61:    */       {
/*  62: 99 */         Scheduler schedulerBean = (Scheduler)lbf.getBean(beanNames[i]);
/*  63:100 */         if (schedulerName.equals(schedulerBean.getSchedulerName())) {
/*  64:101 */           return schedulerBean;
/*  65:    */         }
/*  66:    */       }
/*  67:    */     }
/*  68:105 */     Scheduler schedulerInRepo = SchedulerRepository.getInstance().lookup(schedulerName);
/*  69:106 */     if (schedulerInRepo == null) {
/*  70:107 */       throw new IllegalStateException("No Scheduler named '" + schedulerName + "' found");
/*  71:    */     }
/*  72:109 */     return schedulerInRepo;
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.SchedulerAccessorBean
 * JD-Core Version:    0.7.0.1
 */