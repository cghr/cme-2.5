/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Constructor;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import java.util.Collection;
/*   8:    */ import java.util.LinkedList;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.quartz.Calendar;
/*  14:    */ import org.quartz.JobDetail;
/*  15:    */ import org.quartz.JobListener;
/*  16:    */ import org.quartz.ObjectAlreadyExistsException;
/*  17:    */ import org.quartz.Scheduler;
/*  18:    */ import org.quartz.SchedulerException;
/*  19:    */ import org.quartz.SchedulerListener;
/*  20:    */ import org.quartz.Trigger;
/*  21:    */ import org.quartz.TriggerListener;
/*  22:    */ import org.quartz.spi.ClassLoadHelper;
/*  23:    */ import org.springframework.context.ResourceLoaderAware;
/*  24:    */ import org.springframework.core.io.ResourceLoader;
/*  25:    */ import org.springframework.transaction.PlatformTransactionManager;
/*  26:    */ import org.springframework.transaction.TransactionException;
/*  27:    */ import org.springframework.transaction.TransactionStatus;
/*  28:    */ import org.springframework.transaction.support.DefaultTransactionDefinition;
/*  29:    */ import org.springframework.util.ReflectionUtils;
/*  30:    */ 
/*  31:    */ public abstract class SchedulerAccessor
/*  32:    */   implements ResourceLoaderAware
/*  33:    */ {
/*  34:    */   private static Class<?> jobKeyClass;
/*  35:    */   private static Class<?> triggerKeyClass;
/*  36:    */   
/*  37:    */   static
/*  38:    */   {
/*  39:    */     try
/*  40:    */     {
/*  41: 67 */       jobKeyClass = Class.forName("org.quartz.JobKey");
/*  42: 68 */       triggerKeyClass = Class.forName("org.quartz.TriggerKey");
/*  43:    */     }
/*  44:    */     catch (ClassNotFoundException localClassNotFoundException)
/*  45:    */     {
/*  46: 71 */       jobKeyClass = null;
/*  47: 72 */       triggerKeyClass = null;
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51: 77 */   protected final Log logger = LogFactory.getLog(getClass());
/*  52: 79 */   private boolean overwriteExistingJobs = false;
/*  53:    */   private String[] jobSchedulingDataLocations;
/*  54:    */   private List<JobDetail> jobDetails;
/*  55:    */   private Map<String, Calendar> calendars;
/*  56:    */   private List<Trigger> triggers;
/*  57:    */   private SchedulerListener[] schedulerListeners;
/*  58:    */   private JobListener[] globalJobListeners;
/*  59:    */   private JobListener[] jobListeners;
/*  60:    */   private TriggerListener[] globalTriggerListeners;
/*  61:    */   private TriggerListener[] triggerListeners;
/*  62:    */   private PlatformTransactionManager transactionManager;
/*  63:    */   protected ResourceLoader resourceLoader;
/*  64:    */   
/*  65:    */   public void setOverwriteExistingJobs(boolean overwriteExistingJobs)
/*  66:    */   {
/*  67:110 */     this.overwriteExistingJobs = overwriteExistingJobs;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setJobSchedulingDataLocation(String jobSchedulingDataLocation)
/*  71:    */   {
/*  72:121 */     this.jobSchedulingDataLocations = new String[] { jobSchedulingDataLocation };
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setJobSchedulingDataLocations(String[] jobSchedulingDataLocations)
/*  76:    */   {
/*  77:132 */     this.jobSchedulingDataLocations = jobSchedulingDataLocations;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setJobDetails(JobDetail[] jobDetails)
/*  81:    */   {
/*  82:150 */     this.jobDetails = new ArrayList((Collection)Arrays.asList(jobDetails));
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setCalendars(Map<String, Calendar> calendars)
/*  86:    */   {
/*  87:162 */     this.calendars = calendars;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setTriggers(Trigger[] triggers)
/*  91:    */   {
/*  92:179 */     this.triggers = Arrays.asList(triggers);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setSchedulerListeners(SchedulerListener[] schedulerListeners)
/*  96:    */   {
/*  97:187 */     this.schedulerListeners = schedulerListeners;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setGlobalJobListeners(JobListener[] globalJobListeners)
/* 101:    */   {
/* 102:195 */     this.globalJobListeners = globalJobListeners;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setJobListeners(JobListener[] jobListeners)
/* 106:    */   {
/* 107:207 */     this.jobListeners = jobListeners;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setGlobalTriggerListeners(TriggerListener[] globalTriggerListeners)
/* 111:    */   {
/* 112:215 */     this.globalTriggerListeners = globalTriggerListeners;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setTriggerListeners(TriggerListener[] triggerListeners)
/* 116:    */   {
/* 117:228 */     this.triggerListeners = triggerListeners;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setTransactionManager(PlatformTransactionManager transactionManager)
/* 121:    */   {
/* 122:238 */     this.transactionManager = transactionManager;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setResourceLoader(ResourceLoader resourceLoader)
/* 126:    */   {
/* 127:242 */     this.resourceLoader = resourceLoader;
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected void registerJobsAndTriggers()
/* 131:    */     throws SchedulerException
/* 132:    */   {
/* 133:250 */     TransactionStatus transactionStatus = null;
/* 134:251 */     if (this.transactionManager != null) {
/* 135:252 */       transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
/* 136:    */     }
/* 137:    */     try
/* 138:    */     {
/* 139:    */       Class dataProcessorClass;
/* 140:256 */       if (this.jobSchedulingDataLocations != null)
/* 141:    */       {
/* 142:257 */         ClassLoadHelper clh = new ResourceLoaderClassLoadHelper(this.resourceLoader);
/* 143:258 */         clh.initialize();
/* 144:    */         Object dataProcessor;
/* 145:    */         Method processFileAndScheduleJobs;
/* 146:    */         try
/* 147:    */         {
/* 148:261 */           Class dataProcessorClass = getClass().getClassLoader().loadClass("org.quartz.xml.XMLSchedulingDataProcessor");
/* 149:262 */           this.logger.debug("Using Quartz 1.8 XMLSchedulingDataProcessor");
/* 150:263 */           Object dataProcessor = dataProcessorClass.getConstructor(new Class[] { ClassLoadHelper.class }).newInstance(new Object[] { clh });
/* 151:264 */           Method processFileAndScheduleJobs = dataProcessorClass.getMethod("processFileAndScheduleJobs", new Class[] { String.class, Scheduler.class });
/* 152:265 */           for (String location : this.jobSchedulingDataLocations) {
/* 153:266 */             processFileAndScheduleJobs.invoke(dataProcessor, new Object[] { location, getScheduler() });
/* 154:    */           }
/* 155:    */         }
/* 156:    */         catch (ClassNotFoundException localClassNotFoundException)
/* 157:    */         {
/* 158:271 */           dataProcessorClass = getClass().getClassLoader().loadClass("org.quartz.xml.JobSchedulingDataProcessor");
/* 159:272 */           this.logger.debug("Using Quartz 1.6 JobSchedulingDataProcessor");
/* 160:273 */           dataProcessor = dataProcessorClass.getConstructor(new Class[] { ClassLoadHelper.class, Boolean.TYPE, Boolean.TYPE }).newInstance(new Object[] { clh, Boolean.valueOf(true), Boolean.valueOf(true) });
/* 161:274 */           processFileAndScheduleJobs = dataProcessorClass.getMethod("processFileAndScheduleJobs", new Class[] { String.class, Scheduler.class, Boolean.TYPE });
/* 162:275 */           ??? = (??? = this.jobSchedulingDataLocations).length;??? = 0;
/* 163:    */         }
/* 164:275 */         for (; ??? < ???; ???++)
/* 165:    */         {
/* 166:275 */           String location = ???[???];
/* 167:276 */           processFileAndScheduleJobs.invoke(dataProcessor, new Object[] { location, getScheduler(), Boolean.valueOf(this.overwriteExistingJobs) });
/* 168:    */         }
/* 169:    */       }
/* 170:282 */       if (this.jobDetails != null) {
/* 171:283 */         for (JobDetail jobDetail : this.jobDetails) {
/* 172:284 */           addJobToScheduler(jobDetail);
/* 173:    */         }
/* 174:    */       } else {
/* 175:289 */         this.jobDetails = new LinkedList();
/* 176:    */       }
/* 177:293 */       if (this.calendars != null) {
/* 178:294 */         for (String calendarName : this.calendars.keySet())
/* 179:    */         {
/* 180:295 */           Calendar calendar = (Calendar)this.calendars.get(calendarName);
/* 181:296 */           getScheduler().addCalendar(calendarName, calendar, true, true);
/* 182:    */         }
/* 183:    */       }
/* 184:301 */       if (this.triggers != null) {
/* 185:302 */         for (Trigger trigger : this.triggers) {
/* 186:303 */           addTriggerToScheduler(trigger);
/* 187:    */         }
/* 188:    */       }
/* 189:    */     }
/* 190:    */     catch (Throwable ex)
/* 191:    */     {
/* 192:309 */       if (transactionStatus != null) {
/* 193:    */         try
/* 194:    */         {
/* 195:311 */           this.transactionManager.rollback(transactionStatus);
/* 196:    */         }
/* 197:    */         catch (TransactionException tex)
/* 198:    */         {
/* 199:314 */           this.logger.error("Job registration exception overridden by rollback exception", ex);
/* 200:315 */           throw tex;
/* 201:    */         }
/* 202:    */       }
/* 203:318 */       if ((ex instanceof SchedulerException)) {
/* 204:319 */         throw ((SchedulerException)ex);
/* 205:    */       }
/* 206:321 */       if ((ex instanceof Exception)) {
/* 207:322 */         throw new SchedulerException("Registration of jobs and triggers failed: " + ex.getMessage(), ex);
/* 208:    */       }
/* 209:324 */       throw new SchedulerException("Registration of jobs and triggers failed: " + ex.getMessage());
/* 210:    */     }
/* 211:327 */     if (transactionStatus != null) {
/* 212:328 */       this.transactionManager.commit(transactionStatus);
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   private boolean addJobToScheduler(JobDetail jobDetail)
/* 217:    */     throws SchedulerException
/* 218:    */   {
/* 219:341 */     if ((this.overwriteExistingJobs) || (!jobDetailExists(jobDetail)))
/* 220:    */     {
/* 221:342 */       getScheduler().addJob(jobDetail, true);
/* 222:343 */       return true;
/* 223:    */     }
/* 224:346 */     return false;
/* 225:    */   }
/* 226:    */   
/* 227:    */   private boolean addTriggerToScheduler(Trigger trigger)
/* 228:    */     throws SchedulerException
/* 229:    */   {
/* 230:359 */     boolean triggerExists = triggerExists(trigger);
/* 231:360 */     if ((!triggerExists) || (this.overwriteExistingJobs))
/* 232:    */     {
/* 233:362 */       if ((trigger instanceof JobDetailAwareTrigger))
/* 234:    */       {
/* 235:363 */         JobDetail jobDetail = ((JobDetailAwareTrigger)trigger).getJobDetail();
/* 236:365 */         if ((!this.jobDetails.contains(jobDetail)) && (addJobToScheduler(jobDetail))) {
/* 237:366 */           this.jobDetails.add(jobDetail);
/* 238:    */         }
/* 239:    */       }
/* 240:369 */       if (!triggerExists)
/* 241:    */       {
/* 242:    */         try
/* 243:    */         {
/* 244:371 */           getScheduler().scheduleJob(trigger);
/* 245:    */         }
/* 246:    */         catch (ObjectAlreadyExistsException ex)
/* 247:    */         {
/* 248:374 */           if (this.logger.isDebugEnabled()) {
/* 249:375 */             this.logger.debug("Unexpectedly found existing trigger, assumably due to cluster race condition: " + 
/* 250:376 */               ex.getMessage() + " - can safely be ignored");
/* 251:    */           }
/* 252:378 */           if (!this.overwriteExistingJobs) {
/* 253:    */             break label152;
/* 254:    */           }
/* 255:    */         }
/* 256:379 */         rescheduleJob(trigger);
/* 257:    */       }
/* 258:    */       else
/* 259:    */       {
/* 260:384 */         rescheduleJob(trigger);
/* 261:    */       }
/* 262:    */       label152:
/* 263:386 */       return true;
/* 264:    */     }
/* 265:389 */     return false;
/* 266:    */   }
/* 267:    */   
/* 268:    */   private boolean jobDetailExists(JobDetail jobDetail)
/* 269:    */     throws SchedulerException
/* 270:    */   {
/* 271:396 */     if (jobKeyClass != null) {
/* 272:    */       try
/* 273:    */       {
/* 274:398 */         Method getJobDetail = Scheduler.class.getMethod("getJobDetail", new Class[] { jobKeyClass });
/* 275:399 */         Object key = ReflectionUtils.invokeMethod(JobDetail.class.getMethod("getKey", new Class[0]), jobDetail);
/* 276:400 */         return ReflectionUtils.invokeMethod(getJobDetail, getScheduler(), new Object[] { key }) != null;
/* 277:    */       }
/* 278:    */       catch (NoSuchMethodException ex)
/* 279:    */       {
/* 280:403 */         throw new IllegalStateException("Inconsistent Quartz 2.0 API: " + ex);
/* 281:    */       }
/* 282:    */     }
/* 283:407 */     return getScheduler().getJobDetail(jobDetail.getName(), jobDetail.getGroup()) != null;
/* 284:    */   }
/* 285:    */   
/* 286:    */   private boolean triggerExists(Trigger trigger)
/* 287:    */     throws SchedulerException
/* 288:    */   {
/* 289:413 */     if (triggerKeyClass != null) {
/* 290:    */       try
/* 291:    */       {
/* 292:415 */         Method getTrigger = Scheduler.class.getMethod("getTrigger", new Class[] { triggerKeyClass });
/* 293:416 */         Object key = ReflectionUtils.invokeMethod(Trigger.class.getMethod("getKey", new Class[0]), trigger);
/* 294:417 */         return ReflectionUtils.invokeMethod(getTrigger, getScheduler(), new Object[] { key }) != null;
/* 295:    */       }
/* 296:    */       catch (NoSuchMethodException ex)
/* 297:    */       {
/* 298:420 */         throw new IllegalStateException("Inconsistent Quartz 2.0 API: " + ex);
/* 299:    */       }
/* 300:    */     }
/* 301:424 */     return getScheduler().getTrigger(trigger.getName(), trigger.getGroup()) != null;
/* 302:    */   }
/* 303:    */   
/* 304:    */   private void rescheduleJob(Trigger trigger)
/* 305:    */     throws SchedulerException
/* 306:    */   {
/* 307:430 */     if (triggerKeyClass != null) {
/* 308:    */       try
/* 309:    */       {
/* 310:432 */         Method rescheduleJob = Scheduler.class.getMethod("rescheduleJob", new Class[] { triggerKeyClass, Trigger.class });
/* 311:433 */         Object key = ReflectionUtils.invokeMethod(Trigger.class.getMethod("getKey", new Class[0]), trigger);
/* 312:434 */         ReflectionUtils.invokeMethod(rescheduleJob, getScheduler(), new Object[] { key, trigger });
/* 313:    */       }
/* 314:    */       catch (NoSuchMethodException ex)
/* 315:    */       {
/* 316:437 */         throw new IllegalStateException("Inconsistent Quartz 2.0 API: " + ex);
/* 317:    */       }
/* 318:    */     } else {
/* 319:441 */       getScheduler().rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
/* 320:    */     }
/* 321:    */   }
/* 322:    */   
/* 323:    */   protected void registerListeners()
/* 324:    */     throws SchedulerException
/* 325:    */   {
/* 326:    */     Object target;
/* 327:    */     boolean quartz2;
/* 328:    */     try
/* 329:    */     {
/* 330:453 */       Method getListenerManager = Scheduler.class.getMethod("getListenerManager", new Class[0]);
/* 331:454 */       Object target = ReflectionUtils.invokeMethod(getListenerManager, getScheduler());
/* 332:455 */       quartz2 = true;
/* 333:    */     }
/* 334:    */     catch (NoSuchMethodException localNoSuchMethodException1)
/* 335:    */     {
/* 336:    */       boolean quartz2;
/* 337:458 */       target = getScheduler();
/* 338:459 */       quartz2 = false;
/* 339:    */     }
/* 340:    */     try
/* 341:    */     {
/* 342:    */       Object localObject1;
/* 343:    */       int i;
/* 344:    */       JobListener localJobListener1;
/* 345:463 */       if (this.schedulerListeners != null)
/* 346:    */       {
/* 347:464 */         Method addSchedulerListener = target.getClass().getMethod("addSchedulerListener", new Class[] { SchedulerListener.class });
/* 348:465 */         i = (localObject1 = this.schedulerListeners).length;
/* 349:465 */         for (localJobListener1 = 0; localJobListener1 < i; localJobListener1++)
/* 350:    */         {
/* 351:465 */           SchedulerListener listener = localObject1[localJobListener1];
/* 352:466 */           ReflectionUtils.invokeMethod(addSchedulerListener, target, new Object[] { listener });
/* 353:    */         }
/* 354:    */       }
/* 355:    */       JobListener listener;
/* 356:469 */       if (this.globalJobListeners != null)
/* 357:    */       {
/* 358:470 */         Method addJobListener = target.getClass().getMethod(
/* 359:471 */           quartz2 ? "addJobListener" : "addGlobalJobListener", new Class[] { JobListener.class });
/* 360:472 */         i = (localObject1 = this.globalJobListeners).length;
/* 361:472 */         for (localJobListener1 = 0; localJobListener1 < i; localJobListener1++)
/* 362:    */         {
/* 363:472 */           listener = localObject1[localJobListener1];
/* 364:473 */           ReflectionUtils.invokeMethod(addJobListener, target, new Object[] { listener });
/* 365:    */         }
/* 366:    */       }
/* 367:476 */       if (this.jobListeners != null)
/* 368:    */       {
/* 369:    */         JobListener[] arrayOfJobListener;
/* 370:477 */         localJobListener1 = (arrayOfJobListener = this.jobListeners).length;
/* 371:477 */         for (listener = 0; listener < localJobListener1; listener++)
/* 372:    */         {
/* 373:477 */           JobListener listener = arrayOfJobListener[listener];
/* 374:478 */           if (quartz2) {
/* 375:479 */             throw new IllegalStateException("Non-global JobListeners not supported on Quartz 2 - manually register a Matcher against the Quartz ListenerManager instead");
/* 376:    */           }
/* 377:482 */           getScheduler().addJobListener(listener);
/* 378:    */         }
/* 379:    */       }
/* 380:    */       TriggerListener localTriggerListener1;
/* 381:    */       TriggerListener listener;
/* 382:485 */       if (this.globalTriggerListeners != null)
/* 383:    */       {
/* 384:486 */         Method addTriggerListener = target.getClass().getMethod(
/* 385:487 */           quartz2 ? "addTriggerListener" : "addGlobalTriggerListener", new Class[] { TriggerListener.class });
/* 386:488 */         int j = (localObject1 = this.globalTriggerListeners).length;
/* 387:488 */         for (localTriggerListener1 = 0; localTriggerListener1 < j; localTriggerListener1++)
/* 388:    */         {
/* 389:488 */           listener = localObject1[localTriggerListener1];
/* 390:489 */           ReflectionUtils.invokeMethod(addTriggerListener, target, new Object[] { listener });
/* 391:    */         }
/* 392:    */       }
/* 393:492 */       if (this.triggerListeners != null)
/* 394:    */       {
/* 395:    */         TriggerListener[] arrayOfTriggerListener;
/* 396:493 */         localTriggerListener1 = (arrayOfTriggerListener = this.triggerListeners).length;
/* 397:493 */         for (listener = 0; listener < localTriggerListener1; listener++)
/* 398:    */         {
/* 399:493 */           TriggerListener listener = arrayOfTriggerListener[listener];
/* 400:494 */           if (quartz2) {
/* 401:495 */             throw new IllegalStateException("Non-global TriggerListeners not supported on Quartz 2 - manually register a Matcher against the Quartz ListenerManager instead");
/* 402:    */           }
/* 403:498 */           getScheduler().addTriggerListener(listener);
/* 404:    */         }
/* 405:    */       }
/* 406:    */     }
/* 407:    */     catch (NoSuchMethodException ex)
/* 408:    */     {
/* 409:503 */       throw new IllegalStateException("Expected Quartz API not present: " + ex);
/* 410:    */     }
/* 411:    */   }
/* 412:    */   
/* 413:    */   protected abstract Scheduler getScheduler();
/* 414:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.SchedulerAccessor
 * JD-Core Version:    0.7.0.1
 */