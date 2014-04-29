/*  1:   */ package org.springframework.scheduling.quartz;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import org.quartz.Job;
/*  5:   */ import org.quartz.Scheduler;
/*  6:   */ import org.quartz.SchedulerException;
/*  7:   */ import org.quartz.spi.JobFactory;
/*  8:   */ import org.quartz.spi.TriggerFiredBundle;
/*  9:   */ import org.springframework.util.ReflectionUtils;
/* 10:   */ 
/* 11:   */ public class AdaptableJobFactory
/* 12:   */   implements JobFactory
/* 13:   */ {
/* 14:   */   public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler)
/* 15:   */     throws SchedulerException
/* 16:   */   {
/* 17:47 */     return newJob(bundle);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Job newJob(TriggerFiredBundle bundle)
/* 21:   */     throws SchedulerException
/* 22:   */   {
/* 23:   */     try
/* 24:   */     {
/* 25:55 */       Object jobObject = createJobInstance(bundle);
/* 26:56 */       return adaptJob(jobObject);
/* 27:   */     }
/* 28:   */     catch (Exception ex)
/* 29:   */     {
/* 30:59 */       throw new SchedulerException("Job instantiation failed", ex);
/* 31:   */     }
/* 32:   */   }
/* 33:   */   
/* 34:   */   protected Object createJobInstance(TriggerFiredBundle bundle)
/* 35:   */     throws Exception
/* 36:   */   {
/* 37:73 */     Method getJobDetail = bundle.getClass().getMethod("getJobDetail", new Class[0]);
/* 38:74 */     Object jobDetail = ReflectionUtils.invokeMethod(getJobDetail, bundle);
/* 39:75 */     Method getJobClass = jobDetail.getClass().getMethod("getJobClass", new Class[0]);
/* 40:76 */     Class jobClass = (Class)ReflectionUtils.invokeMethod(getJobClass, jobDetail);
/* 41:77 */     return jobClass.newInstance();
/* 42:   */   }
/* 43:   */   
/* 44:   */   protected Job adaptJob(Object jobObject)
/* 45:   */     throws Exception
/* 46:   */   {
/* 47:90 */     if ((jobObject instanceof Job)) {
/* 48:91 */       return (Job)jobObject;
/* 49:   */     }
/* 50:93 */     if ((jobObject instanceof Runnable)) {
/* 51:94 */       return new DelegatingJob((Runnable)jobObject);
/* 52:   */     }
/* 53:97 */     throw new IllegalArgumentException("Unable to execute job class [" + jobObject.getClass().getName() + 
/* 54:98 */       "]: only [org.quartz.Job] and [java.lang.Runnable] supported.");
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.AdaptableJobFactory
 * JD-Core Version:    0.7.0.1
 */