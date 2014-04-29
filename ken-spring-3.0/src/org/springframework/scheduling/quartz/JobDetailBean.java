/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import org.quartz.Job;
/*   5:    */ import org.quartz.JobDataMap;
/*   6:    */ import org.quartz.JobDetail;
/*   7:    */ import org.springframework.beans.factory.BeanNameAware;
/*   8:    */ import org.springframework.beans.factory.InitializingBean;
/*   9:    */ import org.springframework.context.ApplicationContext;
/*  10:    */ import org.springframework.context.ApplicationContextAware;
/*  11:    */ 
/*  12:    */ public class JobDetailBean
/*  13:    */   extends JobDetail
/*  14:    */   implements BeanNameAware, ApplicationContextAware, InitializingBean
/*  15:    */ {
/*  16:    */   private Class actualJobClass;
/*  17:    */   private String beanName;
/*  18:    */   private ApplicationContext applicationContext;
/*  19:    */   private String applicationContextJobDataKey;
/*  20:    */   
/*  21:    */   public void setJobClass(Class jobClass)
/*  22:    */   {
/*  23: 68 */     if ((jobClass != null) && (!Job.class.isAssignableFrom(jobClass)))
/*  24:    */     {
/*  25: 69 */       super.setJobClass(DelegatingJob.class);
/*  26: 70 */       this.actualJobClass = jobClass;
/*  27:    */     }
/*  28:    */     else
/*  29:    */     {
/*  30: 73 */       super.setJobClass(jobClass);
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public Class getJobClass()
/*  35:    */   {
/*  36: 83 */     return this.actualJobClass != null ? this.actualJobClass : super.getJobClass();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setJobDataAsMap(Map jobDataAsMap)
/*  40:    */   {
/*  41: 98 */     getJobDataMap().putAll(jobDataAsMap);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setJobListenerNames(String[] names)
/*  45:    */   {
/*  46:110 */     for (int i = 0; i < names.length; i++) {
/*  47:111 */       addJobListener(names[i]);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setBeanName(String beanName)
/*  52:    */   {
/*  53:116 */     this.beanName = beanName;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setApplicationContext(ApplicationContext applicationContext)
/*  57:    */   {
/*  58:120 */     this.applicationContext = applicationContext;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setApplicationContextJobDataKey(String applicationContextJobDataKey)
/*  62:    */   {
/*  63:140 */     this.applicationContextJobDataKey = applicationContextJobDataKey;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void afterPropertiesSet()
/*  67:    */   {
/*  68:145 */     if (getName() == null) {
/*  69:146 */       setName(this.beanName);
/*  70:    */     }
/*  71:148 */     if (getGroup() == null) {
/*  72:149 */       setGroup("DEFAULT");
/*  73:    */     }
/*  74:151 */     if (this.applicationContextJobDataKey != null)
/*  75:    */     {
/*  76:152 */       if (this.applicationContext == null) {
/*  77:153 */         throw new IllegalStateException(
/*  78:154 */           "JobDetailBean needs to be set up in an ApplicationContext to be able to handle an 'applicationContextJobDataKey'");
/*  79:    */       }
/*  80:157 */       getJobDataMap().put(this.applicationContextJobDataKey, this.applicationContext);
/*  81:    */     }
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.JobDetailBean
 * JD-Core Version:    0.7.0.1
 */