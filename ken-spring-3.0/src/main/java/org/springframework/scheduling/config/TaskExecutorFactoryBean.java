/*   1:    */ package org.springframework.scheduling.config;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.BeanWrapper;
/*   4:    */ import org.springframework.beans.BeanWrapperImpl;
/*   5:    */ import org.springframework.beans.factory.BeanNameAware;
/*   6:    */ import org.springframework.beans.factory.DisposableBean;
/*   7:    */ import org.springframework.beans.factory.FactoryBean;
/*   8:    */ import org.springframework.beans.factory.InitializingBean;
/*   9:    */ import org.springframework.core.JdkVersion;
/*  10:    */ import org.springframework.core.task.TaskExecutor;
/*  11:    */ import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public class TaskExecutorFactoryBean
/*  15:    */   implements FactoryBean<TaskExecutor>, BeanNameAware, InitializingBean, DisposableBean
/*  16:    */ {
/*  17:    */   private String poolSize;
/*  18:    */   private Integer queueCapacity;
/*  19:    */   private Object rejectedExecutionHandler;
/*  20:    */   private Integer keepAliveSeconds;
/*  21:    */   private String beanName;
/*  22:    */   private TaskExecutor target;
/*  23:    */   
/*  24:    */   public void setPoolSize(String poolSize)
/*  25:    */   {
/*  26: 55 */     this.poolSize = poolSize;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setQueueCapacity(int queueCapacity)
/*  30:    */   {
/*  31: 59 */     this.queueCapacity = Integer.valueOf(queueCapacity);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setRejectedExecutionHandler(Object rejectedExecutionHandler)
/*  35:    */   {
/*  36: 63 */     this.rejectedExecutionHandler = rejectedExecutionHandler;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setKeepAliveSeconds(int keepAliveSeconds)
/*  40:    */   {
/*  41: 67 */     this.keepAliveSeconds = Integer.valueOf(keepAliveSeconds);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setBeanName(String beanName)
/*  45:    */   {
/*  46: 71 */     this.beanName = beanName;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void afterPropertiesSet()
/*  50:    */     throws Exception
/*  51:    */   {
/*  52: 76 */     Class<?> executorClass = shouldUseBackport() ? 
/*  53: 77 */       getClass().getClassLoader().loadClass("org.springframework.scheduling.backportconcurrent.ThreadPoolTaskExecutor") : 
/*  54: 78 */       ThreadPoolTaskExecutor.class;
/*  55: 79 */     BeanWrapper bw = new BeanWrapperImpl(executorClass);
/*  56: 80 */     determinePoolSizeRange(bw);
/*  57: 81 */     if (this.queueCapacity != null) {
/*  58: 82 */       bw.setPropertyValue("queueCapacity", this.queueCapacity);
/*  59:    */     }
/*  60: 84 */     if (this.keepAliveSeconds != null) {
/*  61: 85 */       bw.setPropertyValue("keepAliveSeconds", this.keepAliveSeconds);
/*  62:    */     }
/*  63: 87 */     if (this.rejectedExecutionHandler != null) {
/*  64: 88 */       bw.setPropertyValue("rejectedExecutionHandler", this.rejectedExecutionHandler);
/*  65:    */     }
/*  66: 90 */     if (this.beanName != null) {
/*  67: 91 */       bw.setPropertyValue("threadNamePrefix", this.beanName + "-");
/*  68:    */     }
/*  69: 93 */     this.target = ((TaskExecutor)bw.getWrappedInstance());
/*  70: 94 */     if ((this.target instanceof InitializingBean)) {
/*  71: 95 */       ((InitializingBean)this.target).afterPropertiesSet();
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   private boolean shouldUseBackport()
/*  76:    */   {
/*  77:101 */     return (StringUtils.hasText(this.poolSize)) && (this.poolSize.startsWith("0")) && (JdkVersion.getMajorJavaVersion() < 3);
/*  78:    */   }
/*  79:    */   
/*  80:    */   private void determinePoolSizeRange(BeanWrapper bw)
/*  81:    */   {
/*  82:105 */     if (StringUtils.hasText(this.poolSize)) {
/*  83:    */       try
/*  84:    */       {
/*  85:109 */         int separatorIndex = this.poolSize.indexOf('-');
/*  86:    */         int corePoolSize;
/*  87:    */         int maxPoolSize;
/*  88:110 */         if (separatorIndex != -1)
/*  89:    */         {
/*  90:111 */           int corePoolSize = Integer.valueOf(this.poolSize.substring(0, separatorIndex)).intValue();
/*  91:112 */           int maxPoolSize = Integer.valueOf(this.poolSize.substring(separatorIndex + 1, this.poolSize.length())).intValue();
/*  92:113 */           if (corePoolSize > maxPoolSize) {
/*  93:114 */             throw new IllegalArgumentException(
/*  94:115 */               "Lower bound of pool-size range must not exceed the upper bound");
/*  95:    */           }
/*  96:117 */           if (this.queueCapacity == null) {
/*  97:119 */             if (corePoolSize == 0)
/*  98:    */             {
/*  99:122 */               bw.setPropertyValue("allowCoreThreadTimeOut", Boolean.valueOf(true));
/* 100:123 */               corePoolSize = maxPoolSize;
/* 101:    */             }
/* 102:    */             else
/* 103:    */             {
/* 104:127 */               throw new IllegalArgumentException(
/* 105:128 */                 "A non-zero lower bound for the size range requires a queue-capacity value");
/* 106:    */             }
/* 107:    */           }
/* 108:    */         }
/* 109:    */         else
/* 110:    */         {
/* 111:133 */           Integer value = Integer.valueOf(this.poolSize);
/* 112:134 */           corePoolSize = value.intValue();
/* 113:135 */           maxPoolSize = value.intValue();
/* 114:    */         }
/* 115:137 */         bw.setPropertyValue("corePoolSize", Integer.valueOf(corePoolSize));
/* 116:138 */         bw.setPropertyValue("maxPoolSize", Integer.valueOf(maxPoolSize));
/* 117:    */       }
/* 118:    */       catch (NumberFormatException ex)
/* 119:    */       {
/* 120:141 */         throw new IllegalArgumentException("Invalid pool-size value [" + this.poolSize + "]: only single " + 
/* 121:142 */           "maximum integer (e.g. \"5\") and minimum-maximum range (e.g. \"3-5\") are supported", ex);
/* 122:    */       }
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   public TaskExecutor getObject()
/* 127:    */   {
/* 128:149 */     return this.target;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public Class<? extends TaskExecutor> getObjectType()
/* 132:    */   {
/* 133:153 */     if (this.target != null) {
/* 134:154 */       return this.target.getClass();
/* 135:    */     }
/* 136:156 */     return !shouldUseBackport() ? ThreadPoolTaskExecutor.class : TaskExecutor.class;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public boolean isSingleton()
/* 140:    */   {
/* 141:160 */     return true;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public void destroy()
/* 145:    */     throws Exception
/* 146:    */   {
/* 147:165 */     if ((this.target instanceof DisposableBean)) {
/* 148:166 */       ((DisposableBean)this.target).destroy();
/* 149:    */     }
/* 150:    */   }
/* 151:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.config.TaskExecutorFactoryBean
 * JD-Core Version:    0.7.0.1
 */