/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import org.quartz.JobDataMap;
/*   5:    */ import org.quartz.SchedulerContext;
/*   6:    */ import org.quartz.spi.TriggerFiredBundle;
/*   7:    */ import org.springframework.beans.BeanWrapper;
/*   8:    */ import org.springframework.beans.MutablePropertyValues;
/*   9:    */ import org.springframework.beans.PropertyAccessorFactory;
/*  10:    */ import org.springframework.util.ReflectionUtils;
/*  11:    */ 
/*  12:    */ public class SpringBeanJobFactory
/*  13:    */   extends AdaptableJobFactory
/*  14:    */   implements SchedulerContextAware
/*  15:    */ {
/*  16:    */   private String[] ignoredUnknownProperties;
/*  17:    */   private SchedulerContext schedulerContext;
/*  18:    */   
/*  19:    */   public void setIgnoredUnknownProperties(String[] ignoredUnknownProperties)
/*  20:    */   {
/*  21: 63 */     this.ignoredUnknownProperties = ignoredUnknownProperties;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setSchedulerContext(SchedulerContext schedulerContext)
/*  25:    */   {
/*  26: 67 */     this.schedulerContext = schedulerContext;
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected Object createJobInstance(TriggerFiredBundle bundle)
/*  30:    */     throws Exception
/*  31:    */   {
/*  32: 77 */     Object job = super.createJobInstance(bundle);
/*  33: 78 */     BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(job);
/*  34: 79 */     if (isEligibleForPropertyPopulation(bw.getWrappedInstance()))
/*  35:    */     {
/*  36: 80 */       MutablePropertyValues pvs = new MutablePropertyValues();
/*  37: 81 */       if (this.schedulerContext != null) {
/*  38: 82 */         pvs.addPropertyValues(this.schedulerContext);
/*  39:    */       }
/*  40: 84 */       pvs.addPropertyValues(getJobDetailDataMap(bundle));
/*  41: 85 */       pvs.addPropertyValues(getTriggerDataMap(bundle));
/*  42: 86 */       if (this.ignoredUnknownProperties != null)
/*  43:    */       {
/*  44: 87 */         for (String propName : this.ignoredUnknownProperties) {
/*  45: 88 */           if ((pvs.contains(propName)) && (!bw.isWritableProperty(propName))) {
/*  46: 89 */             pvs.removePropertyValue(propName);
/*  47:    */           }
/*  48:    */         }
/*  49: 92 */         bw.setPropertyValues(pvs);
/*  50:    */       }
/*  51:    */       else
/*  52:    */       {
/*  53: 95 */         bw.setPropertyValues(pvs, true);
/*  54:    */       }
/*  55:    */     }
/*  56: 98 */     return job;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected boolean isEligibleForPropertyPopulation(Object jobObject)
/*  60:    */   {
/*  61:110 */     return !(jobObject instanceof QuartzJobBean);
/*  62:    */   }
/*  63:    */   
/*  64:    */   private JobDataMap getJobDetailDataMap(TriggerFiredBundle bundle)
/*  65:    */     throws Exception
/*  66:    */   {
/*  67:115 */     Method getJobDetail = bundle.getClass().getMethod("getJobDetail", new Class[0]);
/*  68:116 */     Object jobDetail = ReflectionUtils.invokeMethod(getJobDetail, bundle);
/*  69:117 */     Method getJobDataMap = jobDetail.getClass().getMethod("getJobDataMap", new Class[0]);
/*  70:118 */     return (JobDataMap)ReflectionUtils.invokeMethod(getJobDataMap, jobDetail);
/*  71:    */   }
/*  72:    */   
/*  73:    */   private JobDataMap getTriggerDataMap(TriggerFiredBundle bundle)
/*  74:    */     throws Exception
/*  75:    */   {
/*  76:123 */     Method getTrigger = bundle.getClass().getMethod("getTrigger", new Class[0]);
/*  77:124 */     Object trigger = ReflectionUtils.invokeMethod(getTrigger, bundle);
/*  78:125 */     Method getJobDataMap = trigger.getClass().getMethod("getJobDataMap", new Class[0]);
/*  79:126 */     return (JobDataMap)ReflectionUtils.invokeMethod(getJobDataMap, trigger);
/*  80:    */   }
/*  81:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.SpringBeanJobFactory
 * JD-Core Version:    0.7.0.1
 */