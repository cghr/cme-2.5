/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.TimeZone;
/*   6:    */ import org.quartz.CronTrigger;
/*   7:    */ import org.quartz.JobDataMap;
/*   8:    */ import org.quartz.JobDetail;
/*   9:    */ import org.springframework.beans.factory.BeanNameAware;
/*  10:    */ import org.springframework.beans.factory.InitializingBean;
/*  11:    */ import org.springframework.core.Constants;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ 
/*  14:    */ public class CronTriggerBean
/*  15:    */   extends CronTrigger
/*  16:    */   implements JobDetailAwareTrigger, BeanNameAware, InitializingBean
/*  17:    */ {
/*  18: 66 */   private static final Constants constants = new Constants(CronTrigger.class);
/*  19:    */   private JobDetail jobDetail;
/*  20:    */   private String beanName;
/*  21:    */   private long startDelay;
/*  22:    */   
/*  23:    */   public void setJobDataAsMap(Map jobDataAsMap)
/*  24:    */   {
/*  25: 84 */     getJobDataMap().putAll(jobDataAsMap);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setMisfireInstructionName(String constantName)
/*  29:    */   {
/*  30: 96 */     setMisfireInstruction(constants.asNumber(constantName).intValue());
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setTriggerListenerNames(String[] names)
/*  34:    */   {
/*  35:108 */     for (int i = 0; i < names.length; i++) {
/*  36:109 */       addTriggerListener(names[i]);
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setJobDetail(JobDetail jobDetail)
/*  41:    */   {
/*  42:122 */     this.jobDetail = jobDetail;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setStartDelay(long startDelay)
/*  46:    */   {
/*  47:135 */     Assert.state(startDelay >= 0L, "Start delay cannot be negative.");
/*  48:136 */     this.startDelay = startDelay;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public JobDetail getJobDetail()
/*  52:    */   {
/*  53:140 */     return this.jobDetail;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setBeanName(String beanName)
/*  57:    */   {
/*  58:144 */     this.beanName = beanName;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void afterPropertiesSet()
/*  62:    */     throws Exception
/*  63:    */   {
/*  64:149 */     if (this.startDelay > 0L) {
/*  65:150 */       setStartTime(new Date(System.currentTimeMillis() + this.startDelay));
/*  66:    */     }
/*  67:153 */     if (getName() == null) {
/*  68:154 */       setName(this.beanName);
/*  69:    */     }
/*  70:156 */     if (getGroup() == null) {
/*  71:157 */       setGroup("DEFAULT");
/*  72:    */     }
/*  73:159 */     if (getStartTime() == null) {
/*  74:160 */       setStartTime(new Date());
/*  75:    */     }
/*  76:162 */     if (getTimeZone() == null) {
/*  77:163 */       setTimeZone(TimeZone.getDefault());
/*  78:    */     }
/*  79:165 */     if (this.jobDetail != null)
/*  80:    */     {
/*  81:166 */       setJobName(this.jobDetail.getName());
/*  82:167 */       setJobGroup(this.jobDetail.getGroup());
/*  83:    */     }
/*  84:    */   }
/*  85:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.CronTriggerBean
 * JD-Core Version:    0.7.0.1
 */