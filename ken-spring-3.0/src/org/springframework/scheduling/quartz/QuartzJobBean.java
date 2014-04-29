/*  1:   */ package org.springframework.scheduling.quartz;
/*  2:   */ 
/*  3:   */ import org.quartz.Job;
/*  4:   */ import org.quartz.JobExecutionContext;
/*  5:   */ import org.quartz.JobExecutionException;
/*  6:   */ import org.quartz.Scheduler;
/*  7:   */ import org.quartz.SchedulerException;
/*  8:   */ import org.springframework.beans.BeanWrapper;
/*  9:   */ import org.springframework.beans.MutablePropertyValues;
/* 10:   */ import org.springframework.beans.PropertyAccessorFactory;
/* 11:   */ 
/* 12:   */ public abstract class QuartzJobBean
/* 13:   */   implements Job
/* 14:   */ {
/* 15:   */   public final void execute(JobExecutionContext context)
/* 16:   */     throws JobExecutionException
/* 17:   */   {
/* 18:   */     try
/* 19:   */     {
/* 20:77 */       BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
/* 21:78 */       MutablePropertyValues pvs = new MutablePropertyValues();
/* 22:79 */       pvs.addPropertyValues(context.getScheduler().getContext());
/* 23:80 */       pvs.addPropertyValues(context.getMergedJobDataMap());
/* 24:81 */       bw.setPropertyValues(pvs, true);
/* 25:   */     }
/* 26:   */     catch (SchedulerException ex)
/* 27:   */     {
/* 28:84 */       throw new JobExecutionException(ex);
/* 29:   */     }
/* 30:86 */     executeInternal(context);
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected abstract void executeInternal(JobExecutionContext paramJobExecutionContext)
/* 34:   */     throws JobExecutionException;
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.QuartzJobBean
 * JD-Core Version:    0.7.0.1
 */