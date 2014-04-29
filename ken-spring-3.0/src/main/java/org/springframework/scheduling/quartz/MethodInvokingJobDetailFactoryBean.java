/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.apache.commons.logging.LogFactory;
/*   6:    */ import org.quartz.JobDataMap;
/*   7:    */ import org.quartz.JobDetail;
/*   8:    */ import org.quartz.JobExecutionContext;
/*   9:    */ import org.quartz.JobExecutionException;
/*  10:    */ import org.quartz.StatefulJob;
/*  11:    */ import org.springframework.beans.BeanUtils;
/*  12:    */ import org.springframework.beans.BeanWrapper;
/*  13:    */ import org.springframework.beans.PropertyAccessorFactory;
/*  14:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  15:    */ import org.springframework.beans.factory.BeanFactory;
/*  16:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  17:    */ import org.springframework.beans.factory.BeanNameAware;
/*  18:    */ import org.springframework.beans.factory.FactoryBean;
/*  19:    */ import org.springframework.beans.factory.InitializingBean;
/*  20:    */ import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
/*  21:    */ import org.springframework.util.Assert;
/*  22:    */ import org.springframework.util.ClassUtils;
/*  23:    */ import org.springframework.util.MethodInvoker;
/*  24:    */ 
/*  25:    */ public class MethodInvokingJobDetailFactoryBean
/*  26:    */   extends ArgumentConvertingMethodInvoker
/*  27:    */   implements FactoryBean<JobDetail>, BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, InitializingBean
/*  28:    */ {
/*  29:    */   private static Class<?> jobDetailImplClass;
/*  30:    */   private String name;
/*  31:    */   
/*  32:    */   static
/*  33:    */   {
/*  34:    */     try
/*  35:    */     {
/*  36: 85 */       jobDetailImplClass = Class.forName("org.quartz.impl.JobDetailImpl");
/*  37:    */     }
/*  38:    */     catch (ClassNotFoundException localClassNotFoundException)
/*  39:    */     {
/*  40: 88 */       jobDetailImplClass = null;
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44: 95 */   private String group = "DEFAULT";
/*  45: 97 */   private boolean concurrent = true;
/*  46:    */   private String targetBeanName;
/*  47:    */   private String[] jobListenerNames;
/*  48:    */   private String beanName;
/*  49:105 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  50:    */   private BeanFactory beanFactory;
/*  51:    */   private JobDetail jobDetail;
/*  52:    */   
/*  53:    */   public void setName(String name)
/*  54:    */   {
/*  55:118 */     this.name = name;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setGroup(String group)
/*  59:    */   {
/*  60:128 */     this.group = group;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setConcurrent(boolean concurrent)
/*  64:    */   {
/*  65:140 */     this.concurrent = concurrent;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setTargetBeanName(String targetBeanName)
/*  69:    */   {
/*  70:152 */     this.targetBeanName = targetBeanName;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setJobListenerNames(String[] names)
/*  74:    */   {
/*  75:164 */     this.jobListenerNames = names;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setBeanName(String beanName)
/*  79:    */   {
/*  80:168 */     this.beanName = beanName;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  84:    */   {
/*  85:172 */     this.beanClassLoader = classLoader;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  89:    */   {
/*  90:176 */     this.beanFactory = beanFactory;
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected Class resolveClassName(String className)
/*  94:    */     throws ClassNotFoundException
/*  95:    */   {
/*  96:181 */     return ClassUtils.forName(className, this.beanClassLoader);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void afterPropertiesSet()
/* 100:    */     throws ClassNotFoundException, NoSuchMethodException
/* 101:    */   {
/* 102:186 */     prepare();
/* 103:    */     
/* 104:    */ 
/* 105:189 */     String name = this.name != null ? this.name : this.beanName;
/* 106:    */     
/* 107:    */ 
/* 108:192 */     Class jobClass = this.concurrent ? MethodInvokingJob.class : StatefulMethodInvokingJob.class;
/* 109:    */     BeanWrapper bw;
/* 110:195 */     if (jobDetailImplClass != null)
/* 111:    */     {
/* 112:197 */       Object jobDetail = BeanUtils.instantiate(jobDetailImplClass);
/* 113:198 */       bw = PropertyAccessorFactory.forBeanPropertyAccess(jobDetail);
/* 114:199 */       bw.setPropertyValue("name", name);
/* 115:200 */       bw.setPropertyValue("group", this.group);
/* 116:201 */       bw.setPropertyValue("jobClass", jobClass);
/* 117:202 */       bw.setPropertyValue("durability", Boolean.valueOf(true));
/* 118:203 */       ((JobDataMap)bw.getPropertyValue("jobDataMap")).put("methodInvoker", this);
/* 119:    */     }
/* 120:    */     else
/* 121:    */     {
/* 122:207 */       this.jobDetail = new JobDetail(name, this.group, jobClass);
/* 123:208 */       this.jobDetail.setVolatility(true);
/* 124:209 */       this.jobDetail.setDurability(true);
/* 125:210 */       this.jobDetail.getJobDataMap().put("methodInvoker", this);
/* 126:    */     }
/* 127:214 */     if (this.jobListenerNames != null)
/* 128:    */     {
/* 129:    */       String[] arrayOfString;
/* 130:215 */       BeanWrapper localBeanWrapper1 = (arrayOfString = this.jobListenerNames).length;
/* 131:215 */       for (bw = 0; bw < localBeanWrapper1; bw++)
/* 132:    */       {
/* 133:215 */         String jobListenerName = arrayOfString[bw];
/* 134:216 */         if (jobDetailImplClass != null) {
/* 135:217 */           throw new IllegalStateException("Non-global JobListeners not supported on Quartz 2 - manually register a Matcher against the Quartz ListenerManager instead");
/* 136:    */         }
/* 137:220 */         this.jobDetail.addJobListener(jobListenerName);
/* 138:    */       }
/* 139:    */     }
/* 140:224 */     postProcessJobDetail(this.jobDetail);
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected void postProcessJobDetail(JobDetail jobDetail) {}
/* 144:    */   
/* 145:    */   public Class getTargetClass()
/* 146:    */   {
/* 147:241 */     Class targetClass = super.getTargetClass();
/* 148:242 */     if ((targetClass == null) && (this.targetBeanName != null))
/* 149:    */     {
/* 150:243 */       Assert.state(this.beanFactory != null, "BeanFactory must be set when using 'targetBeanName'");
/* 151:244 */       targetClass = this.beanFactory.getType(this.targetBeanName);
/* 152:    */     }
/* 153:246 */     return targetClass;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public Object getTargetObject()
/* 157:    */   {
/* 158:254 */     Object targetObject = super.getTargetObject();
/* 159:255 */     if ((targetObject == null) && (this.targetBeanName != null))
/* 160:    */     {
/* 161:256 */       Assert.state(this.beanFactory != null, "BeanFactory must be set when using 'targetBeanName'");
/* 162:257 */       targetObject = this.beanFactory.getBean(this.targetBeanName);
/* 163:    */     }
/* 164:259 */     return targetObject;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public JobDetail getObject()
/* 168:    */   {
/* 169:264 */     return this.jobDetail;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public Class<? extends JobDetail> getObjectType()
/* 173:    */   {
/* 174:268 */     return this.jobDetail != null ? this.jobDetail.getClass() : JobDetail.class;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public boolean isSingleton()
/* 178:    */   {
/* 179:272 */     return true;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public static class MethodInvokingJob
/* 183:    */     extends QuartzJobBean
/* 184:    */   {
/* 185:282 */     protected static final Log logger = LogFactory.getLog(MethodInvokingJob.class);
/* 186:    */     private MethodInvoker methodInvoker;
/* 187:    */     
/* 188:    */     public void setMethodInvoker(MethodInvoker methodInvoker)
/* 189:    */     {
/* 190:290 */       this.methodInvoker = methodInvoker;
/* 191:    */     }
/* 192:    */     
/* 193:    */     protected void executeInternal(JobExecutionContext context)
/* 194:    */       throws JobExecutionException
/* 195:    */     {
/* 196:    */       try
/* 197:    */       {
/* 198:299 */         context.setResult(this.methodInvoker.invoke());
/* 199:    */       }
/* 200:    */       catch (InvocationTargetException ex)
/* 201:    */       {
/* 202:302 */         if ((ex.getTargetException() instanceof JobExecutionException)) {
/* 203:304 */           throw ((JobExecutionException)ex.getTargetException());
/* 204:    */         }
/* 205:308 */         throw new JobMethodInvocationFailedException(this.methodInvoker, ex.getTargetException());
/* 206:    */       }
/* 207:    */       catch (Exception ex)
/* 208:    */       {
/* 209:313 */         throw new JobMethodInvocationFailedException(this.methodInvoker, ex);
/* 210:    */       }
/* 211:    */     }
/* 212:    */   }
/* 213:    */   
/* 214:    */   public static class StatefulMethodInvokingJob
/* 215:    */     extends MethodInvokingJobDetailFactoryBean.MethodInvokingJob
/* 216:    */     implements StatefulJob
/* 217:    */   {}
/* 218:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean
 * JD-Core Version:    0.7.0.1
 */