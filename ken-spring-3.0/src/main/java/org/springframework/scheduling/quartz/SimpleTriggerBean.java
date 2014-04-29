/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import java.text.ParseException;
/*   4:    */ import java.util.Date;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.quartz.JobDataMap;
/*   7:    */ import org.quartz.JobDetail;
/*   8:    */ import org.quartz.SimpleTrigger;
/*   9:    */ import org.springframework.beans.factory.BeanNameAware;
/*  10:    */ import org.springframework.beans.factory.InitializingBean;
/*  11:    */ import org.springframework.core.Constants;
/*  12:    */ 
/*  13:    */ public class SimpleTriggerBean
/*  14:    */   extends SimpleTrigger
/*  15:    */   implements JobDetailAwareTrigger, BeanNameAware, InitializingBean
/*  16:    */ {
/*  17: 65 */   private static final Constants constants = new Constants(SimpleTrigger.class);
/*  18: 68 */   private long startDelay = 0L;
/*  19:    */   private JobDetail jobDetail;
/*  20:    */   private String beanName;
/*  21:    */   
/*  22:    */   public SimpleTriggerBean()
/*  23:    */   {
/*  24: 76 */     setRepeatCount(REPEAT_INDEFINITELY);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setJobDataAsMap(Map jobDataAsMap)
/*  28:    */   {
/*  29: 88 */     getJobDataMap().putAll(jobDataAsMap);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setMisfireInstructionName(String constantName)
/*  33:    */   {
/*  34:103 */     setMisfireInstruction(constants.asNumber(constantName).intValue());
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setTriggerListenerNames(String[] names)
/*  38:    */   {
/*  39:115 */     for (int i = 0; i < names.length; i++) {
/*  40:116 */       addTriggerListener(names[i]);
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setStartDelay(long startDelay)
/*  45:    */   {
/*  46:131 */     this.startDelay = startDelay;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setJobDetail(JobDetail jobDetail)
/*  50:    */   {
/*  51:143 */     this.jobDetail = jobDetail;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public JobDetail getJobDetail()
/*  55:    */   {
/*  56:147 */     return this.jobDetail;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setBeanName(String beanName)
/*  60:    */   {
/*  61:151 */     this.beanName = beanName;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void afterPropertiesSet()
/*  65:    */     throws ParseException
/*  66:    */   {
/*  67:156 */     if (getName() == null) {
/*  68:157 */       setName(this.beanName);
/*  69:    */     }
/*  70:159 */     if (getGroup() == null) {
/*  71:160 */       setGroup("DEFAULT");
/*  72:    */     }
/*  73:162 */     if (getStartTime() == null) {
/*  74:163 */       setStartTime(new Date(System.currentTimeMillis() + this.startDelay));
/*  75:    */     }
/*  76:165 */     if (this.jobDetail != null)
/*  77:    */     {
/*  78:166 */       setJobName(this.jobDetail.getName());
/*  79:167 */       setJobGroup(this.jobDetail.getGroup());
/*  80:    */     }
/*  81:    */   }
/*  82:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.SimpleTriggerBean
 * JD-Core Version:    0.7.0.1
 */