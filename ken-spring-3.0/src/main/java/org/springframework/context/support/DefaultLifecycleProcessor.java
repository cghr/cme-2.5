/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.LinkedHashMap;
/*   8:    */ import java.util.LinkedHashSet;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Map.Entry;
/*  12:    */ import java.util.Set;
/*  13:    */ import java.util.concurrent.CountDownLatch;
/*  14:    */ import java.util.concurrent.TimeUnit;
/*  15:    */ import org.apache.commons.logging.Log;
/*  16:    */ import org.apache.commons.logging.LogFactory;
/*  17:    */ import org.springframework.beans.factory.BeanFactory;
/*  18:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  19:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  20:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  21:    */ import org.springframework.context.ApplicationContextException;
/*  22:    */ import org.springframework.context.Lifecycle;
/*  23:    */ import org.springframework.context.LifecycleProcessor;
/*  24:    */ import org.springframework.context.Phased;
/*  25:    */ import org.springframework.context.SmartLifecycle;
/*  26:    */ import org.springframework.util.Assert;
/*  27:    */ 
/*  28:    */ public class DefaultLifecycleProcessor
/*  29:    */   implements LifecycleProcessor, BeanFactoryAware
/*  30:    */ {
/*  31: 53 */   private final Log logger = LogFactory.getLog(getClass());
/*  32: 55 */   private volatile long timeoutPerShutdownPhase = 30000L;
/*  33:    */   private volatile boolean running;
/*  34:    */   private volatile ConfigurableListableBeanFactory beanFactory;
/*  35:    */   
/*  36:    */   public void setTimeoutPerShutdownPhase(long timeoutPerShutdownPhase)
/*  37:    */   {
/*  38: 68 */     this.timeoutPerShutdownPhase = timeoutPerShutdownPhase;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  42:    */   {
/*  43: 72 */     Assert.isInstanceOf(ConfigurableListableBeanFactory.class, beanFactory);
/*  44: 73 */     this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void start()
/*  48:    */   {
/*  49: 89 */     startBeans(false);
/*  50: 90 */     this.running = true;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void stop()
/*  54:    */   {
/*  55:103 */     stopBeans();
/*  56:104 */     this.running = false;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void onRefresh()
/*  60:    */   {
/*  61:108 */     startBeans(true);
/*  62:109 */     this.running = true;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void onClose()
/*  66:    */   {
/*  67:113 */     stopBeans();
/*  68:114 */     this.running = false;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public boolean isRunning()
/*  72:    */   {
/*  73:118 */     return this.running;
/*  74:    */   }
/*  75:    */   
/*  76:    */   private void startBeans(boolean autoStartupOnly)
/*  77:    */   {
/*  78:125 */     Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
/*  79:126 */     Map<Integer, LifecycleGroup> phases = new HashMap();
/*  80:    */     Lifecycle lifecycle;
/*  81:127 */     for (Map.Entry<String, ? extends Lifecycle> entry : lifecycleBeans.entrySet())
/*  82:    */     {
/*  83:128 */       lifecycle = (Lifecycle)entry.getValue();
/*  84:129 */       if ((!autoStartupOnly) || (((lifecycle instanceof SmartLifecycle)) && (((SmartLifecycle)lifecycle).isAutoStartup())))
/*  85:    */       {
/*  86:130 */         int phase = getPhase(lifecycle);
/*  87:131 */         LifecycleGroup group = (LifecycleGroup)phases.get(Integer.valueOf(phase));
/*  88:132 */         if (group == null)
/*  89:    */         {
/*  90:133 */           group = new LifecycleGroup(phase, this.timeoutPerShutdownPhase, lifecycleBeans);
/*  91:134 */           phases.put(Integer.valueOf(phase), group);
/*  92:    */         }
/*  93:136 */         group.add((String)entry.getKey(), lifecycle);
/*  94:    */       }
/*  95:    */     }
/*  96:139 */     if (phases.size() > 0)
/*  97:    */     {
/*  98:140 */       List<Integer> keys = new ArrayList((Collection)phases.keySet());
/*  99:141 */       Collections.sort(keys);
/* 100:142 */       for (Integer key : keys) {
/* 101:143 */         ((LifecycleGroup)phases.get(key)).start();
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   private void doStart(Map<String, ? extends Lifecycle> lifecycleBeans, String beanName)
/* 107:    */   {
/* 108:155 */     Lifecycle bean = (Lifecycle)lifecycleBeans.remove(beanName);
/* 109:156 */     if ((bean != null) && (!equals(bean)))
/* 110:    */     {
/* 111:157 */       String[] dependenciesForBean = this.beanFactory.getDependenciesForBean(beanName);
/* 112:158 */       for (String dependency : dependenciesForBean) {
/* 113:159 */         doStart(lifecycleBeans, dependency);
/* 114:    */       }
/* 115:161 */       if (!bean.isRunning())
/* 116:    */       {
/* 117:162 */         if (this.logger.isDebugEnabled()) {
/* 118:163 */           this.logger.debug("Starting bean '" + beanName + "' of type [" + bean.getClass() + "]");
/* 119:    */         }
/* 120:    */         try
/* 121:    */         {
/* 122:166 */           bean.start();
/* 123:    */         }
/* 124:    */         catch (Throwable ex)
/* 125:    */         {
/* 126:169 */           throw new ApplicationContextException("Failed to start bean '" + beanName + "'", ex);
/* 127:    */         }
/* 128:171 */         if (this.logger.isDebugEnabled()) {
/* 129:172 */           this.logger.debug("Successfully started bean '" + beanName + "'");
/* 130:    */         }
/* 131:    */       }
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   private void stopBeans()
/* 136:    */   {
/* 137:179 */     Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
/* 138:180 */     Map<Integer, LifecycleGroup> phases = new HashMap();
/* 139:    */     Lifecycle lifecycle;
/* 140:181 */     for (Map.Entry<String, Lifecycle> entry : lifecycleBeans.entrySet())
/* 141:    */     {
/* 142:182 */       lifecycle = (Lifecycle)entry.getValue();
/* 143:183 */       int shutdownOrder = getPhase(lifecycle);
/* 144:184 */       LifecycleGroup group = (LifecycleGroup)phases.get(Integer.valueOf(shutdownOrder));
/* 145:185 */       if (group == null)
/* 146:    */       {
/* 147:186 */         group = new LifecycleGroup(shutdownOrder, this.timeoutPerShutdownPhase, lifecycleBeans);
/* 148:187 */         phases.put(Integer.valueOf(shutdownOrder), group);
/* 149:    */       }
/* 150:189 */       group.add((String)entry.getKey(), lifecycle);
/* 151:    */     }
/* 152:191 */     if (phases.size() > 0)
/* 153:    */     {
/* 154:192 */       List<Integer> keys = new ArrayList((Collection)phases.keySet());
/* 155:193 */       Collections.sort(keys, Collections.reverseOrder());
/* 156:194 */       for (Integer key : keys) {
/* 157:195 */         ((LifecycleGroup)phases.get(key)).stop();
/* 158:    */       }
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   private void doStop(Map<String, ? extends Lifecycle> lifecycleBeans, final String beanName, final CountDownLatch latch, final Set<String> countDownBeanNames)
/* 163:    */   {
/* 164:209 */     Lifecycle bean = (Lifecycle)lifecycleBeans.remove(beanName);
/* 165:210 */     if (bean != null)
/* 166:    */     {
/* 167:211 */       String[] dependentBeans = this.beanFactory.getDependentBeans(beanName);
/* 168:212 */       for (String dependentBean : dependentBeans) {
/* 169:213 */         doStop(lifecycleBeans, dependentBean, latch, countDownBeanNames);
/* 170:    */       }
/* 171:    */       try
/* 172:    */       {
/* 173:216 */         if (bean.isRunning())
/* 174:    */         {
/* 175:217 */           if ((bean instanceof SmartLifecycle))
/* 176:    */           {
/* 177:218 */             if (this.logger.isDebugEnabled()) {
/* 178:219 */               this.logger.debug("Asking bean '" + beanName + "' of type [" + bean.getClass() + "] to stop");
/* 179:    */             }
/* 180:221 */             countDownBeanNames.add(beanName);
/* 181:222 */             ((SmartLifecycle)bean).stop(new Runnable()
/* 182:    */             {
/* 183:    */               public void run()
/* 184:    */               {
/* 185:224 */                 latch.countDown();
/* 186:225 */                 countDownBeanNames.remove(beanName);
/* 187:226 */                 if (DefaultLifecycleProcessor.this.logger.isDebugEnabled()) {
/* 188:227 */                   DefaultLifecycleProcessor.this.logger.debug("Bean '" + beanName + "' completed its stop procedure");
/* 189:    */                 }
/* 190:    */               }
/* 191:    */             });
/* 192:    */           }
/* 193:    */           else
/* 194:    */           {
/* 195:233 */             if (this.logger.isDebugEnabled()) {
/* 196:234 */               this.logger.debug("Stopping bean '" + beanName + "' of type [" + bean.getClass() + "]");
/* 197:    */             }
/* 198:236 */             bean.stop();
/* 199:237 */             if (this.logger.isDebugEnabled()) {
/* 200:238 */               this.logger.debug("Successfully stopped bean '" + beanName + "'");
/* 201:    */             }
/* 202:    */           }
/* 203:    */         }
/* 204:242 */         else if ((bean instanceof SmartLifecycle)) {
/* 205:244 */           latch.countDown();
/* 206:    */         }
/* 207:    */       }
/* 208:    */       catch (Throwable ex)
/* 209:    */       {
/* 210:248 */         if (this.logger.isWarnEnabled()) {
/* 211:249 */           this.logger.warn("Failed to stop bean '" + beanName + "'", ex);
/* 212:    */         }
/* 213:    */       }
/* 214:    */     }
/* 215:    */   }
/* 216:    */   
/* 217:    */   protected Map<String, Lifecycle> getLifecycleBeans()
/* 218:    */   {
/* 219:264 */     Map<String, Lifecycle> beans = new LinkedHashMap();
/* 220:265 */     String[] beanNames = this.beanFactory.getBeanNamesForType(Lifecycle.class, false, false);
/* 221:266 */     for (String beanName : beanNames)
/* 222:    */     {
/* 223:267 */       String beanNameToRegister = BeanFactoryUtils.transformedBeanName(beanName);
/* 224:268 */       boolean isFactoryBean = this.beanFactory.isFactoryBean(beanNameToRegister);
/* 225:269 */       String beanNameToCheck = isFactoryBean ? "&" + beanName : beanName;
/* 226:270 */       if (((this.beanFactory.containsSingleton(beanNameToRegister)) && (
/* 227:271 */         (!isFactoryBean) || (Lifecycle.class.isAssignableFrom(this.beanFactory.getType(beanNameToCheck))))) || 
/* 228:272 */         (SmartLifecycle.class.isAssignableFrom(this.beanFactory.getType(beanNameToCheck))))
/* 229:    */       {
/* 230:273 */         Lifecycle bean = (Lifecycle)this.beanFactory.getBean(beanNameToCheck, Lifecycle.class);
/* 231:274 */         if (bean != this) {
/* 232:275 */           beans.put(beanNameToRegister, bean);
/* 233:    */         }
/* 234:    */       }
/* 235:    */     }
/* 236:279 */     return beans;
/* 237:    */   }
/* 238:    */   
/* 239:    */   protected int getPhase(Lifecycle bean)
/* 240:    */   {
/* 241:292 */     return (bean instanceof Phased) ? ((Phased)bean).getPhase() : 0;
/* 242:    */   }
/* 243:    */   
/* 244:    */   private class LifecycleGroup
/* 245:    */   {
/* 246:302 */     private final List<DefaultLifecycleProcessor.LifecycleGroupMember> members = new ArrayList();
/* 247:304 */     private Map<String, ? extends Lifecycle> lifecycleBeans = DefaultLifecycleProcessor.this.getLifecycleBeans();
/* 248:    */     private volatile int smartMemberCount;
/* 249:    */     private final int phase;
/* 250:    */     private final long timeout;
/* 251:    */     
/* 252:    */     public LifecycleGroup(long phase, Map<String, ? extends Lifecycle> arg4)
/* 253:    */     {
/* 254:313 */       this.phase = phase;
/* 255:314 */       this.timeout = timeout;
/* 256:315 */       this.lifecycleBeans = lifecycleBeans;
/* 257:    */     }
/* 258:    */     
/* 259:    */     public void add(String name, Lifecycle bean)
/* 260:    */     {
/* 261:319 */       if ((bean instanceof SmartLifecycle)) {
/* 262:320 */         this.smartMemberCount += 1;
/* 263:    */       }
/* 264:322 */       this.members.add(new DefaultLifecycleProcessor.LifecycleGroupMember(DefaultLifecycleProcessor.this, name, bean));
/* 265:    */     }
/* 266:    */     
/* 267:    */     public void start()
/* 268:    */     {
/* 269:326 */       if (this.members.isEmpty()) {
/* 270:327 */         return;
/* 271:    */       }
/* 272:329 */       if (DefaultLifecycleProcessor.this.logger.isInfoEnabled()) {
/* 273:330 */         DefaultLifecycleProcessor.this.logger.info("Starting beans in phase " + this.phase);
/* 274:    */       }
/* 275:332 */       Collections.sort(this.members);
/* 276:333 */       for (DefaultLifecycleProcessor.LifecycleGroupMember member : this.members) {
/* 277:334 */         if (this.lifecycleBeans.containsKey(DefaultLifecycleProcessor.LifecycleGroupMember.access$1(member))) {
/* 278:335 */           DefaultLifecycleProcessor.this.doStart(this.lifecycleBeans, DefaultLifecycleProcessor.LifecycleGroupMember.access$1(member));
/* 279:    */         }
/* 280:    */       }
/* 281:    */     }
/* 282:    */     
/* 283:    */     public void stop()
/* 284:    */     {
/* 285:341 */       if (this.members.isEmpty()) {
/* 286:342 */         return;
/* 287:    */       }
/* 288:344 */       if (DefaultLifecycleProcessor.this.logger.isInfoEnabled()) {
/* 289:345 */         DefaultLifecycleProcessor.this.logger.info("Stopping beans in phase " + this.phase);
/* 290:    */       }
/* 291:347 */       Collections.sort(this.members, Collections.reverseOrder());
/* 292:348 */       CountDownLatch latch = new CountDownLatch(this.smartMemberCount);
/* 293:349 */       Set<String> countDownBeanNames = Collections.synchronizedSet(new LinkedHashSet());
/* 294:350 */       for (DefaultLifecycleProcessor.LifecycleGroupMember member : this.members) {
/* 295:351 */         if (this.lifecycleBeans.containsKey(DefaultLifecycleProcessor.LifecycleGroupMember.access$1(member))) {
/* 296:352 */           DefaultLifecycleProcessor.this.doStop(this.lifecycleBeans, DefaultLifecycleProcessor.LifecycleGroupMember.access$1(member), latch, countDownBeanNames);
/* 297:354 */         } else if ((DefaultLifecycleProcessor.LifecycleGroupMember.access$2(member) instanceof SmartLifecycle)) {
/* 298:356 */           latch.countDown();
/* 299:    */         }
/* 300:    */       }
/* 301:    */       try
/* 302:    */       {
/* 303:360 */         latch.await(this.timeout, TimeUnit.MILLISECONDS);
/* 304:361 */         if ((latch.getCount() > 0L) && (!countDownBeanNames.isEmpty()) && (DefaultLifecycleProcessor.this.logger.isWarnEnabled())) {
/* 305:362 */           DefaultLifecycleProcessor.this.logger.warn("Failed to shut down " + countDownBeanNames.size() + " bean" + (
/* 306:363 */             countDownBeanNames.size() > 1 ? "s" : "") + " with phase value " + 
/* 307:364 */             this.phase + " within timeout of " + this.timeout + ": " + countDownBeanNames);
/* 308:    */         }
/* 309:    */       }
/* 310:    */       catch (InterruptedException localInterruptedException)
/* 311:    */       {
/* 312:368 */         Thread.currentThread().interrupt();
/* 313:    */       }
/* 314:    */     }
/* 315:    */   }
/* 316:    */   
/* 317:    */   private class LifecycleGroupMember
/* 318:    */     implements Comparable<LifecycleGroupMember>
/* 319:    */   {
/* 320:    */     private final String name;
/* 321:    */     private final Lifecycle bean;
/* 322:    */     
/* 323:    */     LifecycleGroupMember(String name, Lifecycle bean)
/* 324:    */     {
/* 325:384 */       this.name = name;
/* 326:385 */       this.bean = bean;
/* 327:    */     }
/* 328:    */     
/* 329:    */     public int compareTo(LifecycleGroupMember other)
/* 330:    */     {
/* 331:389 */       int thisOrder = DefaultLifecycleProcessor.this.getPhase(this.bean);
/* 332:390 */       int otherOrder = DefaultLifecycleProcessor.this.getPhase(other.bean);
/* 333:391 */       return thisOrder < otherOrder ? -1 : thisOrder == otherOrder ? 0 : 1;
/* 334:    */     }
/* 335:    */   }
/* 336:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.DefaultLifecycleProcessor
 * JD-Core Version:    0.7.0.1
 */