/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Properties;
/*   6:    */ import java.util.concurrent.Executor;
/*   7:    */ import javax.sql.DataSource;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.quartz.Scheduler;
/*  10:    */ import org.quartz.SchedulerContext;
/*  11:    */ import org.quartz.SchedulerException;
/*  12:    */ import org.quartz.SchedulerFactory;
/*  13:    */ import org.quartz.impl.RemoteScheduler;
/*  14:    */ import org.quartz.impl.StdSchedulerFactory;
/*  15:    */ import org.quartz.simpl.SimpleThreadPool;
/*  16:    */ import org.quartz.spi.JobFactory;
/*  17:    */ import org.springframework.beans.BeanUtils;
/*  18:    */ import org.springframework.beans.factory.BeanNameAware;
/*  19:    */ import org.springframework.beans.factory.DisposableBean;
/*  20:    */ import org.springframework.beans.factory.FactoryBean;
/*  21:    */ import org.springframework.beans.factory.InitializingBean;
/*  22:    */ import org.springframework.context.ApplicationContext;
/*  23:    */ import org.springframework.context.ApplicationContextAware;
/*  24:    */ import org.springframework.context.SmartLifecycle;
/*  25:    */ import org.springframework.core.io.Resource;
/*  26:    */ import org.springframework.core.io.ResourceLoader;
/*  27:    */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*  28:    */ import org.springframework.scheduling.SchedulingException;
/*  29:    */ import org.springframework.util.CollectionUtils;
/*  30:    */ 
/*  31:    */ public class SchedulerFactoryBean
/*  32:    */   extends SchedulerAccessor
/*  33:    */   implements FactoryBean<Scheduler>, BeanNameAware, ApplicationContextAware, InitializingBean, DisposableBean, SmartLifecycle
/*  34:    */ {
/*  35:    */   public static final String PROP_THREAD_COUNT = "org.quartz.threadPool.threadCount";
/*  36:    */   public static final int DEFAULT_THREAD_COUNT = 10;
/*  37: 96 */   private static final ThreadLocal<ResourceLoader> configTimeResourceLoaderHolder = new ThreadLocal();
/*  38: 99 */   private static final ThreadLocal<Executor> configTimeTaskExecutorHolder = new ThreadLocal();
/*  39:102 */   private static final ThreadLocal<DataSource> configTimeDataSourceHolder = new ThreadLocal();
/*  40:105 */   private static final ThreadLocal<DataSource> configTimeNonTransactionalDataSourceHolder = new ThreadLocal();
/*  41:    */   
/*  42:    */   public static ResourceLoader getConfigTimeResourceLoader()
/*  43:    */   {
/*  44:117 */     return (ResourceLoader)configTimeResourceLoaderHolder.get();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static Executor getConfigTimeTaskExecutor()
/*  48:    */   {
/*  49:130 */     return (Executor)configTimeTaskExecutorHolder.get();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static DataSource getConfigTimeDataSource()
/*  53:    */   {
/*  54:143 */     return (DataSource)configTimeDataSourceHolder.get();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static DataSource getConfigTimeNonTransactionalDataSource()
/*  58:    */   {
/*  59:156 */     return (DataSource)configTimeNonTransactionalDataSourceHolder.get();
/*  60:    */   }
/*  61:    */   
/*  62:160 */   private Class<?> schedulerFactoryClass = StdSchedulerFactory.class;
/*  63:    */   private String schedulerName;
/*  64:    */   private Resource configLocation;
/*  65:    */   private Properties quartzProperties;
/*  66:    */   private Executor taskExecutor;
/*  67:    */   private DataSource dataSource;
/*  68:    */   private DataSource nonTransactionalDataSource;
/*  69:    */   private Map schedulerContextMap;
/*  70:    */   private ApplicationContext applicationContext;
/*  71:    */   private String applicationContextSchedulerContextKey;
/*  72:    */   private JobFactory jobFactory;
/*  73:184 */   private boolean jobFactorySet = false;
/*  74:187 */   private boolean autoStartup = true;
/*  75:189 */   private int startupDelay = 0;
/*  76:191 */   private int phase = 2147483647;
/*  77:193 */   private boolean exposeSchedulerInRepository = false;
/*  78:195 */   private boolean waitForJobsToCompleteOnShutdown = false;
/*  79:    */   private Scheduler scheduler;
/*  80:    */   
/*  81:    */   public void setSchedulerFactoryClass(Class schedulerFactoryClass)
/*  82:    */   {
/*  83:212 */     if ((schedulerFactoryClass == null) || (!SchedulerFactory.class.isAssignableFrom(schedulerFactoryClass))) {
/*  84:213 */       throw new IllegalArgumentException("schedulerFactoryClass must implement [org.quartz.SchedulerFactory]");
/*  85:    */     }
/*  86:215 */     this.schedulerFactoryClass = schedulerFactoryClass;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setSchedulerName(String schedulerName)
/*  90:    */   {
/*  91:226 */     this.schedulerName = schedulerName;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setConfigLocation(Resource configLocation)
/*  95:    */   {
/*  96:237 */     this.configLocation = configLocation;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setQuartzProperties(Properties quartzProperties)
/* 100:    */   {
/* 101:247 */     this.quartzProperties = quartzProperties;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setTaskExecutor(Executor taskExecutor)
/* 105:    */   {
/* 106:264 */     this.taskExecutor = taskExecutor;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setDataSource(DataSource dataSource)
/* 110:    */   {
/* 111:288 */     this.dataSource = dataSource;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setNonTransactionalDataSource(DataSource nonTransactionalDataSource)
/* 115:    */   {
/* 116:302 */     this.nonTransactionalDataSource = nonTransactionalDataSource;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setSchedulerContextAsMap(Map schedulerContextAsMap)
/* 120:    */   {
/* 121:317 */     this.schedulerContextMap = schedulerContextAsMap;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setApplicationContextSchedulerContextKey(String applicationContextSchedulerContextKey)
/* 125:    */   {
/* 126:337 */     this.applicationContextSchedulerContextKey = applicationContextSchedulerContextKey;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setJobFactory(JobFactory jobFactory)
/* 130:    */   {
/* 131:354 */     this.jobFactory = jobFactory;
/* 132:355 */     this.jobFactorySet = true;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setAutoStartup(boolean autoStartup)
/* 136:    */   {
/* 137:364 */     this.autoStartup = autoStartup;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean isAutoStartup()
/* 141:    */   {
/* 142:373 */     return this.autoStartup;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setPhase(int phase)
/* 146:    */   {
/* 147:384 */     this.phase = phase;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public int getPhase()
/* 151:    */   {
/* 152:391 */     return this.phase;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setStartupDelay(int startupDelay)
/* 156:    */   {
/* 157:402 */     this.startupDelay = startupDelay;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void setExposeSchedulerInRepository(boolean exposeSchedulerInRepository)
/* 161:    */   {
/* 162:415 */     this.exposeSchedulerInRepository = exposeSchedulerInRepository;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void setWaitForJobsToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown)
/* 166:    */   {
/* 167:425 */     this.waitForJobsToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void setBeanName(String name)
/* 171:    */   {
/* 172:430 */     if (this.schedulerName == null) {
/* 173:431 */       this.schedulerName = name;
/* 174:    */     }
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void setApplicationContext(ApplicationContext applicationContext)
/* 178:    */   {
/* 179:436 */     this.applicationContext = applicationContext;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public void afterPropertiesSet()
/* 183:    */     throws Exception
/* 184:    */   {
/* 185:445 */     if ((this.dataSource == null) && (this.nonTransactionalDataSource != null)) {
/* 186:446 */       this.dataSource = this.nonTransactionalDataSource;
/* 187:    */     }
/* 188:449 */     if ((this.applicationContext != null) && (this.resourceLoader == null)) {
/* 189:450 */       this.resourceLoader = this.applicationContext;
/* 190:    */     }
/* 191:454 */     SchedulerFactory schedulerFactory = 
/* 192:455 */       (SchedulerFactory)BeanUtils.instantiateClass(this.schedulerFactoryClass);
/* 193:    */     
/* 194:457 */     initSchedulerFactory(schedulerFactory);
/* 195:459 */     if (this.resourceLoader != null) {
/* 196:461 */       configTimeResourceLoaderHolder.set(this.resourceLoader);
/* 197:    */     }
/* 198:463 */     if (this.taskExecutor != null) {
/* 199:465 */       configTimeTaskExecutorHolder.set(this.taskExecutor);
/* 200:    */     }
/* 201:467 */     if (this.dataSource != null) {
/* 202:469 */       configTimeDataSourceHolder.set(this.dataSource);
/* 203:    */     }
/* 204:471 */     if (this.nonTransactionalDataSource != null) {
/* 205:473 */       configTimeNonTransactionalDataSourceHolder.set(this.nonTransactionalDataSource);
/* 206:    */     }
/* 207:    */     try
/* 208:    */     {
/* 209:479 */       this.scheduler = createScheduler(schedulerFactory, this.schedulerName);
/* 210:480 */       populateSchedulerContext();
/* 211:482 */       if ((!this.jobFactorySet) && (!(this.scheduler instanceof RemoteScheduler))) {
/* 212:485 */         this.jobFactory = new AdaptableJobFactory();
/* 213:    */       }
/* 214:487 */       if (this.jobFactory != null)
/* 215:    */       {
/* 216:488 */         if ((this.jobFactory instanceof SchedulerContextAware)) {
/* 217:489 */           ((SchedulerContextAware)this.jobFactory).setSchedulerContext(this.scheduler.getContext());
/* 218:    */         }
/* 219:491 */         this.scheduler.setJobFactory(this.jobFactory);
/* 220:    */       }
/* 221:    */     }
/* 222:    */     finally
/* 223:    */     {
/* 224:496 */       if (this.resourceLoader != null) {
/* 225:497 */         configTimeResourceLoaderHolder.remove();
/* 226:    */       }
/* 227:499 */       if (this.taskExecutor != null) {
/* 228:500 */         configTimeTaskExecutorHolder.remove();
/* 229:    */       }
/* 230:502 */       if (this.dataSource != null) {
/* 231:503 */         configTimeDataSourceHolder.remove();
/* 232:    */       }
/* 233:505 */       if (this.nonTransactionalDataSource != null) {
/* 234:506 */         configTimeNonTransactionalDataSourceHolder.remove();
/* 235:    */       }
/* 236:    */     }
/* 237:510 */     registerListeners();
/* 238:511 */     registerJobsAndTriggers();
/* 239:    */   }
/* 240:    */   
/* 241:    */   private void initSchedulerFactory(SchedulerFactory schedulerFactory)
/* 242:    */     throws SchedulerException, IOException
/* 243:    */   {
/* 244:522 */     if (!(schedulerFactory instanceof StdSchedulerFactory))
/* 245:    */     {
/* 246:523 */       if ((this.configLocation != null) || (this.quartzProperties != null) || 
/* 247:524 */         (this.taskExecutor != null) || (this.dataSource != null)) {
/* 248:525 */         throw new IllegalArgumentException(
/* 249:526 */           "StdSchedulerFactory required for applying Quartz properties: " + schedulerFactory);
/* 250:    */       }
/* 251:529 */       return;
/* 252:    */     }
/* 253:532 */     Properties mergedProps = new Properties();
/* 254:534 */     if (this.resourceLoader != null) {
/* 255:535 */       mergedProps.setProperty("org.quartz.scheduler.classLoadHelper.class", 
/* 256:536 */         ResourceLoaderClassLoadHelper.class.getName());
/* 257:    */     }
/* 258:539 */     if (this.taskExecutor != null)
/* 259:    */     {
/* 260:540 */       mergedProps.setProperty("org.quartz.threadPool.class", 
/* 261:541 */         LocalTaskExecutorThreadPool.class.getName());
/* 262:    */     }
/* 263:    */     else
/* 264:    */     {
/* 265:546 */       mergedProps.setProperty("org.quartz.threadPool.class", SimpleThreadPool.class.getName());
/* 266:547 */       mergedProps.setProperty("org.quartz.threadPool.threadCount", Integer.toString(10));
/* 267:    */     }
/* 268:550 */     if (this.configLocation != null)
/* 269:    */     {
/* 270:551 */       if (this.logger.isInfoEnabled()) {
/* 271:552 */         this.logger.info("Loading Quartz config from [" + this.configLocation + "]");
/* 272:    */       }
/* 273:554 */       PropertiesLoaderUtils.fillProperties(mergedProps, this.configLocation);
/* 274:    */     }
/* 275:557 */     CollectionUtils.mergePropertiesIntoMap(this.quartzProperties, mergedProps);
/* 276:559 */     if (this.dataSource != null) {
/* 277:560 */       mergedProps.put("org.quartz.jobStore.class", LocalDataSourceJobStore.class.getName());
/* 278:    */     }
/* 279:564 */     if (this.schedulerName != null) {
/* 280:565 */       mergedProps.put("org.quartz.scheduler.instanceName", this.schedulerName);
/* 281:    */     }
/* 282:568 */     ((StdSchedulerFactory)schedulerFactory).initialize(mergedProps);
/* 283:    */   }
/* 284:    */   
/* 285:    */   /* Error */
/* 286:    */   protected Scheduler createScheduler(SchedulerFactory schedulerFactory, String schedulerName)
/* 287:    */     throws SchedulerException
/* 288:    */   {
/* 289:    */     // Byte code:
/* 290:    */     //   0: invokestatic 334	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/* 291:    */     //   3: astore_3
/* 292:    */     //   4: aload_3
/* 293:    */     //   5: invokevirtual 340	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/* 294:    */     //   8: astore 4
/* 295:    */     //   10: aload_0
/* 296:    */     //   11: getfield 190	org/springframework/scheduling/quartz/SchedulerFactoryBean:resourceLoader	Lorg/springframework/core/io/ResourceLoader;
/* 297:    */     //   14: ifnull +24 -> 38
/* 298:    */     //   17: aload_0
/* 299:    */     //   18: getfield 190	org/springframework/scheduling/quartz/SchedulerFactoryBean:resourceLoader	Lorg/springframework/core/io/ResourceLoader;
/* 300:    */     //   21: invokeinterface 344 1 0
/* 301:    */     //   26: aload 4
/* 302:    */     //   28: invokevirtual 347	java/lang/Object:equals	(Ljava/lang/Object;)Z
/* 303:    */     //   31: ifne +7 -> 38
/* 304:    */     //   34: iconst_1
/* 305:    */     //   35: goto +4 -> 39
/* 306:    */     //   38: iconst_0
/* 307:    */     //   39: istore 5
/* 308:    */     //   41: iload 5
/* 309:    */     //   43: ifeq +16 -> 59
/* 310:    */     //   46: aload_3
/* 311:    */     //   47: aload_0
/* 312:    */     //   48: getfield 190	org/springframework/scheduling/quartz/SchedulerFactoryBean:resourceLoader	Lorg/springframework/core/io/ResourceLoader;
/* 313:    */     //   51: invokeinterface 344 1 0
/* 314:    */     //   56: invokevirtual 353	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/* 315:    */     //   59: invokestatic 357	org/quartz/impl/SchedulerRepository:getInstance	()Lorg/quartz/impl/SchedulerRepository;
/* 316:    */     //   62: astore 6
/* 317:    */     //   64: aload 6
/* 318:    */     //   66: dup
/* 319:    */     //   67: astore 7
/* 320:    */     //   69: monitorenter
/* 321:    */     //   70: aload_2
/* 322:    */     //   71: ifnull +12 -> 83
/* 323:    */     //   74: aload 6
/* 324:    */     //   76: aload_2
/* 325:    */     //   77: invokevirtual 363	org/quartz/impl/SchedulerRepository:lookup	(Ljava/lang/String;)Lorg/quartz/Scheduler;
/* 326:    */     //   80: goto +4 -> 84
/* 327:    */     //   83: aconst_null
/* 328:    */     //   84: astore 8
/* 329:    */     //   86: aload_1
/* 330:    */     //   87: invokeinterface 367 1 0
/* 331:    */     //   92: astore 9
/* 332:    */     //   94: aload 9
/* 333:    */     //   96: aload 8
/* 334:    */     //   98: if_acmpne +40 -> 138
/* 335:    */     //   101: new 371	java/lang/IllegalStateException
/* 336:    */     //   104: dup
/* 337:    */     //   105: new 251	java/lang/StringBuilder
/* 338:    */     //   108: dup
/* 339:    */     //   109: ldc_w 373
/* 340:    */     //   112: invokespecial 255	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/* 341:    */     //   115: aload_2
/* 342:    */     //   116: invokevirtual 302	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 343:    */     //   119: ldc_w 375
/* 344:    */     //   122: invokevirtual 302	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 345:    */     //   125: ldc_w 377
/* 346:    */     //   128: invokevirtual 302	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 347:    */     //   131: invokevirtual 260	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 348:    */     //   134: invokespecial 379	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/* 349:    */     //   137: athrow
/* 350:    */     //   138: aload_0
/* 351:    */     //   139: getfield 95	org/springframework/scheduling/quartz/SchedulerFactoryBean:exposeSchedulerInRepository	Z
/* 352:    */     //   142: ifne +17 -> 159
/* 353:    */     //   145: invokestatic 357	org/quartz/impl/SchedulerRepository:getInstance	()Lorg/quartz/impl/SchedulerRepository;
/* 354:    */     //   148: aload 9
/* 355:    */     //   150: invokeinterface 380 1 0
/* 356:    */     //   155: invokevirtual 383	org/quartz/impl/SchedulerRepository:remove	(Ljava/lang/String;)Z
/* 357:    */     //   158: pop
/* 358:    */     //   159: aload 9
/* 359:    */     //   161: astore 11
/* 360:    */     //   163: aload 7
/* 361:    */     //   165: monitorexit
/* 362:    */     //   166: iload 5
/* 363:    */     //   168: ifeq +9 -> 177
/* 364:    */     //   171: aload_3
/* 365:    */     //   172: aload 4
/* 366:    */     //   174: invokevirtual 353	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/* 367:    */     //   177: aload 11
/* 368:    */     //   179: areturn
/* 369:    */     //   180: aload 7
/* 370:    */     //   182: monitorexit
/* 371:    */     //   183: athrow
/* 372:    */     //   184: astore 10
/* 373:    */     //   186: iload 5
/* 374:    */     //   188: ifeq +9 -> 197
/* 375:    */     //   191: aload_3
/* 376:    */     //   192: aload 4
/* 377:    */     //   194: invokevirtual 353	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/* 378:    */     //   197: aload 10
/* 379:    */     //   199: athrow
/* 380:    */     // Line number table:
/* 381:    */     //   Java source line #587	-> byte code offset #0
/* 382:    */     //   Java source line #588	-> byte code offset #4
/* 383:    */     //   Java source line #589	-> byte code offset #10
/* 384:    */     //   Java source line #590	-> byte code offset #17
/* 385:    */     //   Java source line #589	-> byte code offset #39
/* 386:    */     //   Java source line #591	-> byte code offset #41
/* 387:    */     //   Java source line #592	-> byte code offset #46
/* 388:    */     //   Java source line #595	-> byte code offset #59
/* 389:    */     //   Java source line #596	-> byte code offset #64
/* 390:    */     //   Java source line #597	-> byte code offset #70
/* 391:    */     //   Java source line #598	-> byte code offset #86
/* 392:    */     //   Java source line #599	-> byte code offset #94
/* 393:    */     //   Java source line #600	-> byte code offset #101
/* 394:    */     //   Java source line #601	-> byte code offset #125
/* 395:    */     //   Java source line #600	-> byte code offset #134
/* 396:    */     //   Java source line #603	-> byte code offset #138
/* 397:    */     //   Java source line #605	-> byte code offset #145
/* 398:    */     //   Java source line #607	-> byte code offset #159
/* 399:    */     //   Java source line #611	-> byte code offset #166
/* 400:    */     //   Java source line #613	-> byte code offset #171
/* 401:    */     //   Java source line #607	-> byte code offset #177
/* 402:    */     //   Java source line #596	-> byte code offset #180
/* 403:    */     //   Java source line #610	-> byte code offset #184
/* 404:    */     //   Java source line #611	-> byte code offset #186
/* 405:    */     //   Java source line #613	-> byte code offset #191
/* 406:    */     //   Java source line #615	-> byte code offset #197
/* 407:    */     // Local variable table:
/* 408:    */     //   start	length	slot	name	signature
/* 409:    */     //   0	200	0	this	SchedulerFactoryBean
/* 410:    */     //   0	200	1	schedulerFactory	SchedulerFactory
/* 411:    */     //   0	200	2	schedulerName	String
/* 412:    */     //   3	189	3	currentThread	Thread
/* 413:    */     //   8	185	4	threadContextClassLoader	java.lang.ClassLoader
/* 414:    */     //   39	148	5	overrideClassLoader	boolean
/* 415:    */     //   62	13	6	repository	org.quartz.impl.SchedulerRepository
/* 416:    */     //   180	1	6	repository	org.quartz.impl.SchedulerRepository
/* 417:    */     //   84	13	8	existingScheduler	Scheduler
/* 418:    */     //   92	68	9	newScheduler	Scheduler
/* 419:    */     //   184	14	10	localObject1	Object
/* 420:    */     //   161	17	11	localScheduler1	Scheduler
/* 421:    */     // Exception table:
/* 422:    */     //   from	to	target	type
/* 423:    */     //   70	166	180	finally
/* 424:    */     //   180	183	180	finally
/* 425:    */     //   59	166	184	finally
/* 426:    */     //   180	184	184	finally
/* 427:    */   }
/* 428:    */   
/* 429:    */   private void populateSchedulerContext()
/* 430:    */     throws SchedulerException
/* 431:    */   {
/* 432:624 */     if (this.schedulerContextMap != null) {
/* 433:625 */       this.scheduler.getContext().putAll(this.schedulerContextMap);
/* 434:    */     }
/* 435:629 */     if (this.applicationContextSchedulerContextKey != null)
/* 436:    */     {
/* 437:630 */       if (this.applicationContext == null) {
/* 438:631 */         throw new IllegalStateException(
/* 439:632 */           "SchedulerFactoryBean needs to be set up in an ApplicationContext to be able to handle an 'applicationContextSchedulerContextKey'");
/* 440:    */       }
/* 441:635 */       this.scheduler.getContext().put(this.applicationContextSchedulerContextKey, this.applicationContext);
/* 442:    */     }
/* 443:    */   }
/* 444:    */   
/* 445:    */   protected void startScheduler(final Scheduler scheduler, final int startupDelay)
/* 446:    */     throws SchedulerException
/* 447:    */   {
/* 448:647 */     if (startupDelay <= 0)
/* 449:    */     {
/* 450:648 */       this.logger.info("Starting Quartz Scheduler now");
/* 451:649 */       scheduler.start();
/* 452:    */     }
/* 453:    */     else
/* 454:    */     {
/* 455:652 */       if (this.logger.isInfoEnabled()) {
/* 456:653 */         this.logger.info("Will start Quartz Scheduler [" + scheduler.getSchedulerName() + 
/* 457:654 */           "] in " + startupDelay + " seconds");
/* 458:    */       }
/* 459:656 */       Thread schedulerThread = new Thread()
/* 460:    */       {
/* 461:    */         public void run()
/* 462:    */         {
/* 463:    */           try
/* 464:    */           {
/* 465:660 */             Thread.sleep(startupDelay * 1000);
/* 466:    */           }
/* 467:    */           catch (InterruptedException localInterruptedException) {}
/* 468:665 */           if (SchedulerFactoryBean.this.logger.isInfoEnabled()) {
/* 469:666 */             SchedulerFactoryBean.this.logger.info("Starting Quartz Scheduler now, after delay of " + startupDelay + " seconds");
/* 470:    */           }
/* 471:    */           try
/* 472:    */           {
/* 473:669 */             scheduler.start();
/* 474:    */           }
/* 475:    */           catch (SchedulerException ex)
/* 476:    */           {
/* 477:672 */             throw new SchedulingException("Could not start Quartz Scheduler after delay", ex);
/* 478:    */           }
/* 479:    */         }
/* 480:675 */       };
/* 481:676 */       schedulerThread.setName("Quartz Scheduler [" + scheduler.getSchedulerName() + "]");
/* 482:677 */       schedulerThread.setDaemon(true);
/* 483:678 */       schedulerThread.start();
/* 484:    */     }
/* 485:    */   }
/* 486:    */   
/* 487:    */   public Scheduler getScheduler()
/* 488:    */   {
/* 489:689 */     return this.scheduler;
/* 490:    */   }
/* 491:    */   
/* 492:    */   public Scheduler getObject()
/* 493:    */   {
/* 494:693 */     return this.scheduler;
/* 495:    */   }
/* 496:    */   
/* 497:    */   public Class<? extends Scheduler> getObjectType()
/* 498:    */   {
/* 499:697 */     return this.scheduler != null ? this.scheduler.getClass() : Scheduler.class;
/* 500:    */   }
/* 501:    */   
/* 502:    */   public boolean isSingleton()
/* 503:    */   {
/* 504:701 */     return true;
/* 505:    */   }
/* 506:    */   
/* 507:    */   public void start()
/* 508:    */     throws SchedulingException
/* 509:    */   {
/* 510:710 */     if (this.scheduler != null) {
/* 511:    */       try
/* 512:    */       {
/* 513:712 */         startScheduler(this.scheduler, this.startupDelay);
/* 514:    */       }
/* 515:    */       catch (SchedulerException ex)
/* 516:    */       {
/* 517:715 */         throw new SchedulingException("Could not start Quartz Scheduler", ex);
/* 518:    */       }
/* 519:    */     }
/* 520:    */   }
/* 521:    */   
/* 522:    */   public void stop()
/* 523:    */     throws SchedulingException
/* 524:    */   {
/* 525:721 */     if (this.scheduler != null) {
/* 526:    */       try
/* 527:    */       {
/* 528:723 */         this.scheduler.standby();
/* 529:    */       }
/* 530:    */       catch (SchedulerException ex)
/* 531:    */       {
/* 532:726 */         throw new SchedulingException("Could not stop Quartz Scheduler", ex);
/* 533:    */       }
/* 534:    */     }
/* 535:    */   }
/* 536:    */   
/* 537:    */   public void stop(Runnable callback)
/* 538:    */     throws SchedulingException
/* 539:    */   {
/* 540:732 */     stop();
/* 541:733 */     callback.run();
/* 542:    */   }
/* 543:    */   
/* 544:    */   public boolean isRunning()
/* 545:    */     throws SchedulingException
/* 546:    */   {
/* 547:737 */     if (this.scheduler != null) {
/* 548:    */       try
/* 549:    */       {
/* 550:739 */         return !this.scheduler.isInStandbyMode();
/* 551:    */       }
/* 552:    */       catch (SchedulerException localSchedulerException)
/* 553:    */       {
/* 554:742 */         return false;
/* 555:    */       }
/* 556:    */     }
/* 557:745 */     return false;
/* 558:    */   }
/* 559:    */   
/* 560:    */   public void destroy()
/* 561:    */     throws SchedulerException
/* 562:    */   {
/* 563:758 */     this.logger.info("Shutting down Quartz Scheduler");
/* 564:759 */     this.scheduler.shutdown(this.waitForJobsToCompleteOnShutdown);
/* 565:    */   }
/* 566:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.SchedulerFactoryBean
 * JD-Core Version:    0.7.0.1
 */