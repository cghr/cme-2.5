/*   1:    */ package org.springframework.scheduling.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.concurrent.ScheduledExecutorService;
/*   9:    */ import org.springframework.aop.support.AopUtils;
/*  10:    */ import org.springframework.beans.factory.DisposableBean;
/*  11:    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*  12:    */ import org.springframework.context.ApplicationContext;
/*  13:    */ import org.springframework.context.ApplicationContextAware;
/*  14:    */ import org.springframework.context.ApplicationListener;
/*  15:    */ import org.springframework.context.EmbeddedValueResolverAware;
/*  16:    */ import org.springframework.context.event.ContextRefreshedEvent;
/*  17:    */ import org.springframework.core.Ordered;
/*  18:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  19:    */ import org.springframework.scheduling.TaskScheduler;
/*  20:    */ import org.springframework.scheduling.config.ScheduledTaskRegistrar;
/*  21:    */ import org.springframework.scheduling.support.ScheduledMethodRunnable;
/*  22:    */ import org.springframework.util.Assert;
/*  23:    */ import org.springframework.util.ReflectionUtils;
/*  24:    */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*  25:    */ import org.springframework.util.StringValueResolver;
/*  26:    */ 
/*  27:    */ public class ScheduledAnnotationBeanPostProcessor
/*  28:    */   implements BeanPostProcessor, Ordered, EmbeddedValueResolverAware, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, DisposableBean
/*  29:    */ {
/*  30:    */   private Object scheduler;
/*  31:    */   private StringValueResolver embeddedValueResolver;
/*  32:    */   private ApplicationContext applicationContext;
/*  33:    */   private ScheduledTaskRegistrar registrar;
/*  34: 79 */   private final Map<Runnable, String> cronTasks = new HashMap();
/*  35: 81 */   private final Map<Runnable, Long> fixedDelayTasks = new HashMap();
/*  36: 83 */   private final Map<Runnable, Long> fixedRateTasks = new HashMap();
/*  37:    */   
/*  38:    */   public void setScheduler(Object scheduler)
/*  39:    */   {
/*  40: 92 */     this.scheduler = scheduler;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setEmbeddedValueResolver(StringValueResolver resolver)
/*  44:    */   {
/*  45: 96 */     this.embeddedValueResolver = resolver;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setApplicationContext(ApplicationContext applicationContext)
/*  49:    */   {
/*  50:100 */     this.applicationContext = applicationContext;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int getOrder()
/*  54:    */   {
/*  55:104 */     return 2147483647;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*  59:    */   {
/*  60:109 */     return bean;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Object postProcessAfterInitialization(final Object bean, String beanName)
/*  64:    */   {
/*  65:113 */     Class<?> targetClass = AopUtils.getTargetClass(bean);
/*  66:114 */     ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback()
/*  67:    */     {
/*  68:    */       public void doWith(Method method)
/*  69:    */         throws IllegalArgumentException, IllegalAccessException
/*  70:    */       {
/*  71:116 */         Scheduled annotation = (Scheduled)AnnotationUtils.getAnnotation(method, Scheduled.class);
/*  72:117 */         if (annotation != null)
/*  73:    */         {
/*  74:118 */           Assert.isTrue(Void.TYPE.equals(method.getReturnType()), 
/*  75:119 */             "Only void-returning methods may be annotated with @Scheduled.");
/*  76:120 */           Assert.isTrue(method.getParameterTypes().length == 0, 
/*  77:121 */             "Only no-arg methods may be annotated with @Scheduled.");
/*  78:122 */           Runnable runnable = new ScheduledMethodRunnable(bean, method);
/*  79:123 */           boolean processedSchedule = false;
/*  80:124 */           String errorMessage = "Exactly one of 'cron', 'fixedDelay', or 'fixedRate' is required.";
/*  81:125 */           String cron = annotation.cron();
/*  82:126 */           if (!"".equals(cron))
/*  83:    */           {
/*  84:127 */             processedSchedule = true;
/*  85:128 */             if (ScheduledAnnotationBeanPostProcessor.this.embeddedValueResolver != null) {
/*  86:129 */               cron = ScheduledAnnotationBeanPostProcessor.this.embeddedValueResolver.resolveStringValue(cron);
/*  87:    */             }
/*  88:131 */             ScheduledAnnotationBeanPostProcessor.this.cronTasks.put(runnable, cron);
/*  89:    */           }
/*  90:133 */           long fixedDelay = annotation.fixedDelay();
/*  91:134 */           if (fixedDelay >= 0L)
/*  92:    */           {
/*  93:135 */             Assert.isTrue(!processedSchedule, errorMessage);
/*  94:136 */             processedSchedule = true;
/*  95:137 */             ScheduledAnnotationBeanPostProcessor.this.fixedDelayTasks.put(runnable, Long.valueOf(fixedDelay));
/*  96:    */           }
/*  97:139 */           long fixedRate = annotation.fixedRate();
/*  98:140 */           if (fixedRate >= 0L)
/*  99:    */           {
/* 100:141 */             Assert.isTrue(!processedSchedule, errorMessage);
/* 101:142 */             processedSchedule = true;
/* 102:143 */             ScheduledAnnotationBeanPostProcessor.this.fixedRateTasks.put(runnable, Long.valueOf(fixedRate));
/* 103:    */           }
/* 104:145 */           Assert.isTrue(processedSchedule, errorMessage);
/* 105:    */         }
/* 106:    */       }
/* 107:148 */     });
/* 108:149 */     return bean;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void onApplicationEvent(ContextRefreshedEvent event)
/* 112:    */   {
/* 113:153 */     if (event.getApplicationContext() != this.applicationContext) {
/* 114:154 */       return;
/* 115:    */     }
/* 116:157 */     Map<String, SchedulingConfigurer> configurers = this.applicationContext.getBeansOfType(SchedulingConfigurer.class);
/* 117:159 */     if ((this.cronTasks.isEmpty()) && (this.fixedDelayTasks.isEmpty()) && 
/* 118:160 */       (this.fixedRateTasks.isEmpty()) && (configurers.isEmpty())) {
/* 119:161 */       return;
/* 120:    */     }
/* 121:164 */     this.registrar = new ScheduledTaskRegistrar();
/* 122:165 */     this.registrar.setCronTasks(this.cronTasks);
/* 123:166 */     this.registrar.setFixedDelayTasks(this.fixedDelayTasks);
/* 124:167 */     this.registrar.setFixedRateTasks(this.fixedRateTasks);
/* 125:169 */     if (this.scheduler != null) {
/* 126:170 */       this.registrar.setScheduler(this.scheduler);
/* 127:    */     }
/* 128:173 */     for (SchedulingConfigurer configurer : configurers.values()) {
/* 129:174 */       configurer.configureTasks(this.registrar);
/* 130:    */     }
/* 131:177 */     if (this.registrar.getScheduler() == null)
/* 132:    */     {
/* 133:178 */       Map<String, ? super Object> schedulers = new HashMap();
/* 134:179 */       schedulers.putAll(this.applicationContext.getBeansOfType(TaskScheduler.class));
/* 135:180 */       schedulers.putAll(this.applicationContext.getBeansOfType(ScheduledExecutorService.class));
/* 136:181 */       if (schedulers.size() != 0) {
/* 137:183 */         if (schedulers.size() == 1) {
/* 138:184 */           this.registrar.setScheduler(schedulers.values().iterator().next());
/* 139:185 */         } else if (schedulers.size() >= 2) {
/* 140:186 */           throw new IllegalStateException("More than one TaskScheduler and/or ScheduledExecutorService  exist within the context. Remove all but one of the beans; or implement the SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback. Found the following beans: " + 
/* 141:    */           
/* 142:    */ 
/* 143:189 */             schedulers.keySet());
/* 144:    */         }
/* 145:    */       }
/* 146:    */     }
/* 147:193 */     this.registrar.afterPropertiesSet();
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void destroy()
/* 151:    */     throws Exception
/* 152:    */   {
/* 153:197 */     if (this.registrar != null) {
/* 154:198 */       this.registrar.destroy();
/* 155:    */     }
/* 156:    */   }
/* 157:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */