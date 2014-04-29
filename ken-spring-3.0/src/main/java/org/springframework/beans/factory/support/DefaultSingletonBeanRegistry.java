/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.LinkedHashMap;
/*   9:    */ import java.util.LinkedHashSet;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Map.Entry;
/*  12:    */ import java.util.Set;
/*  13:    */ import java.util.concurrent.ConcurrentHashMap;
/*  14:    */ import org.apache.commons.logging.Log;
/*  15:    */ import org.apache.commons.logging.LogFactory;
/*  16:    */ import org.springframework.beans.factory.BeanCreationException;
/*  17:    */ import org.springframework.beans.factory.BeanCreationNotAllowedException;
/*  18:    */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*  19:    */ import org.springframework.beans.factory.DisposableBean;
/*  20:    */ import org.springframework.beans.factory.ObjectFactory;
/*  21:    */ import org.springframework.beans.factory.config.SingletonBeanRegistry;
/*  22:    */ import org.springframework.core.SimpleAliasRegistry;
/*  23:    */ import org.springframework.util.Assert;
/*  24:    */ import org.springframework.util.StringUtils;
/*  25:    */ 
/*  26:    */ public class DefaultSingletonBeanRegistry
/*  27:    */   extends SimpleAliasRegistry
/*  28:    */   implements SingletonBeanRegistry
/*  29:    */ {
/*  30: 79 */   protected static final Object NULL_OBJECT = new Object();
/*  31: 83 */   protected final Log logger = LogFactory.getLog(getClass());
/*  32: 86 */   private final Map<String, Object> singletonObjects = new ConcurrentHashMap();
/*  33: 89 */   private final Map<String, ObjectFactory> singletonFactories = new HashMap();
/*  34: 92 */   private final Map<String, Object> earlySingletonObjects = new HashMap();
/*  35: 95 */   private final Set<String> registeredSingletons = new LinkedHashSet(16);
/*  36: 98 */   private final Set<String> singletonsCurrentlyInCreation = Collections.synchronizedSet(new HashSet());
/*  37:101 */   private final Set<String> inCreationCheckExclusions = new HashSet();
/*  38:    */   private Set<Exception> suppressedExceptions;
/*  39:107 */   private boolean singletonsCurrentlyInDestruction = false;
/*  40:110 */   private final Map<String, Object> disposableBeans = new LinkedHashMap();
/*  41:113 */   private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap();
/*  42:116 */   private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap();
/*  43:119 */   private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap();
/*  44:    */   
/*  45:    */   public void registerSingleton(String beanName, Object singletonObject)
/*  46:    */     throws IllegalStateException
/*  47:    */   {
/*  48:123 */     Assert.notNull(beanName, "'beanName' must not be null");
/*  49:124 */     synchronized (this.singletonObjects)
/*  50:    */     {
/*  51:125 */       Object oldObject = this.singletonObjects.get(beanName);
/*  52:126 */       if (oldObject != null) {
/*  53:127 */         throw new IllegalStateException("Could not register object [" + singletonObject + 
/*  54:128 */           "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
/*  55:    */       }
/*  56:130 */       addSingleton(beanName, singletonObject);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected void addSingleton(String beanName, Object singletonObject)
/*  61:    */   {
/*  62:141 */     synchronized (this.singletonObjects)
/*  63:    */     {
/*  64:142 */       this.singletonObjects.put(beanName, singletonObject != null ? singletonObject : NULL_OBJECT);
/*  65:143 */       this.singletonFactories.remove(beanName);
/*  66:144 */       this.earlySingletonObjects.remove(beanName);
/*  67:145 */       this.registeredSingletons.add(beanName);
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected void addSingletonFactory(String beanName, ObjectFactory singletonFactory)
/*  72:    */   {
/*  73:158 */     Assert.notNull(singletonFactory, "Singleton factory must not be null");
/*  74:159 */     synchronized (this.singletonObjects)
/*  75:    */     {
/*  76:160 */       if (!this.singletonObjects.containsKey(beanName))
/*  77:    */       {
/*  78:161 */         this.singletonFactories.put(beanName, singletonFactory);
/*  79:162 */         this.earlySingletonObjects.remove(beanName);
/*  80:163 */         this.registeredSingletons.add(beanName);
/*  81:    */       }
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public Object getSingleton(String beanName)
/*  86:    */   {
/*  87:169 */     return getSingleton(beanName, true);
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected Object getSingleton(String beanName, boolean allowEarlyReference)
/*  91:    */   {
/*  92:181 */     Object singletonObject = this.singletonObjects.get(beanName);
/*  93:182 */     if (singletonObject == null) {
/*  94:183 */       synchronized (this.singletonObjects)
/*  95:    */       {
/*  96:184 */         singletonObject = this.earlySingletonObjects.get(beanName);
/*  97:185 */         if ((singletonObject == null) && (allowEarlyReference))
/*  98:    */         {
/*  99:186 */           ObjectFactory singletonFactory = (ObjectFactory)this.singletonFactories.get(beanName);
/* 100:187 */           if (singletonFactory != null)
/* 101:    */           {
/* 102:188 */             singletonObject = singletonFactory.getObject();
/* 103:189 */             this.earlySingletonObjects.put(beanName, singletonObject);
/* 104:190 */             this.singletonFactories.remove(beanName);
/* 105:    */           }
/* 106:    */         }
/* 107:    */       }
/* 108:    */     }
/* 109:195 */     return singletonObject != NULL_OBJECT ? singletonObject : null;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public Object getSingleton(String beanName, ObjectFactory singletonFactory)
/* 113:    */   {
/* 114:207 */     Assert.notNull(beanName, "'beanName' must not be null");
/* 115:208 */     synchronized (this.singletonObjects)
/* 116:    */     {
/* 117:209 */       Object singletonObject = this.singletonObjects.get(beanName);
/* 118:210 */       if (singletonObject == null)
/* 119:    */       {
/* 120:211 */         if (this.singletonsCurrentlyInDestruction) {
/* 121:212 */           throw new BeanCreationNotAllowedException(beanName, 
/* 122:213 */             "Singleton bean creation not allowed while the singletons of this factory are in destruction (Do not request a bean from a BeanFactory in a destroy method implementation!)");
/* 123:    */         }
/* 124:216 */         if (this.logger.isDebugEnabled()) {
/* 125:217 */           this.logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
/* 126:    */         }
/* 127:219 */         beforeSingletonCreation(beanName);
/* 128:220 */         boolean recordSuppressedExceptions = this.suppressedExceptions == null;
/* 129:221 */         if (recordSuppressedExceptions) {
/* 130:222 */           this.suppressedExceptions = new LinkedHashSet();
/* 131:    */         }
/* 132:    */         try
/* 133:    */         {
/* 134:225 */           singletonObject = singletonFactory.getObject();
/* 135:    */         }
/* 136:    */         catch (BeanCreationException ex)
/* 137:    */         {
/* 138:228 */           if (recordSuppressedExceptions) {
/* 139:229 */             for (Exception suppressedException : this.suppressedExceptions) {
/* 140:230 */               ex.addRelatedCause(suppressedException);
/* 141:    */             }
/* 142:    */           }
/* 143:233 */           throw ex;
/* 144:    */         }
/* 145:    */         finally
/* 146:    */         {
/* 147:236 */           if (recordSuppressedExceptions) {
/* 148:237 */             this.suppressedExceptions = null;
/* 149:    */           }
/* 150:239 */           afterSingletonCreation(beanName);
/* 151:    */         }
/* 152:241 */         addSingleton(beanName, singletonObject);
/* 153:    */       }
/* 154:243 */       return singletonObject != NULL_OBJECT ? singletonObject : null;
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected void onSuppressedException(Exception ex)
/* 159:    */   {
/* 160:253 */     synchronized (this.singletonObjects)
/* 161:    */     {
/* 162:254 */       if (this.suppressedExceptions != null) {
/* 163:255 */         this.suppressedExceptions.add(ex);
/* 164:    */       }
/* 165:    */     }
/* 166:    */   }
/* 167:    */   
/* 168:    */   protected void removeSingleton(String beanName)
/* 169:    */   {
/* 170:267 */     synchronized (this.singletonObjects)
/* 171:    */     {
/* 172:268 */       this.singletonObjects.remove(beanName);
/* 173:269 */       this.singletonFactories.remove(beanName);
/* 174:270 */       this.earlySingletonObjects.remove(beanName);
/* 175:271 */       this.registeredSingletons.remove(beanName);
/* 176:    */     }
/* 177:    */   }
/* 178:    */   
/* 179:    */   public boolean containsSingleton(String beanName)
/* 180:    */   {
/* 181:276 */     return this.singletonObjects.containsKey(beanName);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public String[] getSingletonNames()
/* 185:    */   {
/* 186:280 */     synchronized (this.singletonObjects)
/* 187:    */     {
/* 188:281 */       return StringUtils.toStringArray(this.registeredSingletons);
/* 189:    */     }
/* 190:    */   }
/* 191:    */   
/* 192:    */   public int getSingletonCount()
/* 193:    */   {
/* 194:286 */     synchronized (this.singletonObjects)
/* 195:    */     {
/* 196:287 */       return this.registeredSingletons.size();
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   protected void beforeSingletonCreation(String beanName)
/* 201:    */   {
/* 202:299 */     if ((!this.inCreationCheckExclusions.contains(beanName)) && (!this.singletonsCurrentlyInCreation.add(beanName))) {
/* 203:300 */       throw new BeanCurrentlyInCreationException(beanName);
/* 204:    */     }
/* 205:    */   }
/* 206:    */   
/* 207:    */   protected void afterSingletonCreation(String beanName)
/* 208:    */   {
/* 209:311 */     if ((!this.inCreationCheckExclusions.contains(beanName)) && (!this.singletonsCurrentlyInCreation.remove(beanName))) {
/* 210:312 */       throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
/* 211:    */     }
/* 212:    */   }
/* 213:    */   
/* 214:    */   public final void setCurrentlyInCreation(String beanName, boolean inCreation)
/* 215:    */   {
/* 216:317 */     if (!inCreation) {
/* 217:318 */       this.inCreationCheckExclusions.add(beanName);
/* 218:    */     } else {
/* 219:320 */       this.inCreationCheckExclusions.remove(beanName);
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   public final boolean isSingletonCurrentlyInCreation(String beanName)
/* 224:    */   {
/* 225:330 */     return this.singletonsCurrentlyInCreation.contains(beanName);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public void registerDisposableBean(String beanName, DisposableBean bean)
/* 229:    */   {
/* 230:344 */     synchronized (this.disposableBeans)
/* 231:    */     {
/* 232:345 */       this.disposableBeans.put(beanName, bean);
/* 233:    */     }
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void registerContainedBean(String containedBeanName, String containingBeanName)
/* 237:    */   {
/* 238:359 */     synchronized (this.containedBeanMap)
/* 239:    */     {
/* 240:360 */       Set<String> containedBeans = (Set)this.containedBeanMap.get(containingBeanName);
/* 241:361 */       if (containedBeans == null)
/* 242:    */       {
/* 243:362 */         containedBeans = new LinkedHashSet(8);
/* 244:363 */         this.containedBeanMap.put(containingBeanName, containedBeans);
/* 245:    */       }
/* 246:365 */       containedBeans.add(containedBeanName);
/* 247:    */     }
/* 248:367 */     registerDependentBean(containedBeanName, containingBeanName);
/* 249:    */   }
/* 250:    */   
/* 251:    */   public void registerDependentBean(String beanName, String dependentBeanName)
/* 252:    */   {
/* 253:377 */     String canonicalName = canonicalName(beanName);
/* 254:378 */     synchronized (this.dependentBeanMap)
/* 255:    */     {
/* 256:379 */       Set<String> dependentBeans = (Set)this.dependentBeanMap.get(canonicalName);
/* 257:380 */       if (dependentBeans == null)
/* 258:    */       {
/* 259:381 */         dependentBeans = new LinkedHashSet(8);
/* 260:382 */         this.dependentBeanMap.put(canonicalName, dependentBeans);
/* 261:    */       }
/* 262:384 */       dependentBeans.add(dependentBeanName);
/* 263:    */     }
/* 264:386 */     synchronized (this.dependenciesForBeanMap)
/* 265:    */     {
/* 266:387 */       Set<String> dependenciesForBean = (Set)this.dependenciesForBeanMap.get(dependentBeanName);
/* 267:388 */       if (dependenciesForBean == null)
/* 268:    */       {
/* 269:389 */         dependenciesForBean = new LinkedHashSet(8);
/* 270:390 */         this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
/* 271:    */       }
/* 272:392 */       dependenciesForBean.add(canonicalName);
/* 273:    */     }
/* 274:    */   }
/* 275:    */   
/* 276:    */   protected boolean hasDependentBean(String beanName)
/* 277:    */   {
/* 278:401 */     return this.dependentBeanMap.containsKey(beanName);
/* 279:    */   }
/* 280:    */   
/* 281:    */   public String[] getDependentBeans(String beanName)
/* 282:    */   {
/* 283:410 */     Set<String> dependentBeans = (Set)this.dependentBeanMap.get(beanName);
/* 284:411 */     if (dependentBeans == null) {
/* 285:412 */       return new String[0];
/* 286:    */     }
/* 287:414 */     return StringUtils.toStringArray(dependentBeans);
/* 288:    */   }
/* 289:    */   
/* 290:    */   public String[] getDependenciesForBean(String beanName)
/* 291:    */   {
/* 292:424 */     Set<String> dependenciesForBean = (Set)this.dependenciesForBeanMap.get(beanName);
/* 293:425 */     if (dependenciesForBean == null) {
/* 294:426 */       return new String[0];
/* 295:    */     }
/* 296:428 */     return (String[])dependenciesForBean.toArray(new String[dependenciesForBean.size()]);
/* 297:    */   }
/* 298:    */   
/* 299:    */   public void destroySingletons()
/* 300:    */   {
/* 301:432 */     if (this.logger.isInfoEnabled()) {
/* 302:433 */       this.logger.info("Destroying singletons in " + this);
/* 303:    */     }
/* 304:435 */     synchronized (this.singletonObjects)
/* 305:    */     {
/* 306:436 */       this.singletonsCurrentlyInDestruction = true;
/* 307:    */     }
/* 308:439 */     synchronized (this.disposableBeans)
/* 309:    */     {
/* 310:440 */       String[] disposableBeanNames = StringUtils.toStringArray((Collection)this.disposableBeans.keySet());
/* 311:441 */       for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
/* 312:442 */         destroySingleton(disposableBeanNames[i]);
/* 313:    */       }
/* 314:    */     }
/* 315:446 */     this.containedBeanMap.clear();
/* 316:447 */     this.dependentBeanMap.clear();
/* 317:448 */     this.dependenciesForBeanMap.clear();
/* 318:450 */     synchronized (this.singletonObjects)
/* 319:    */     {
/* 320:451 */       this.singletonObjects.clear();
/* 321:452 */       this.singletonFactories.clear();
/* 322:453 */       this.earlySingletonObjects.clear();
/* 323:454 */       this.registeredSingletons.clear();
/* 324:455 */       this.singletonsCurrentlyInDestruction = false;
/* 325:    */     }
/* 326:    */   }
/* 327:    */   
/* 328:    */   public void destroySingleton(String beanName)
/* 329:    */   {
/* 330:467 */     removeSingleton(beanName);
/* 331:    */     DisposableBean disposableBean;
/* 332:471 */     synchronized (this.disposableBeans)
/* 333:    */     {
/* 334:472 */       disposableBean = (DisposableBean)this.disposableBeans.remove(beanName);
/* 335:    */     }
/* 336:    */     DisposableBean disposableBean;
/* 337:474 */     destroyBean(beanName, disposableBean);
/* 338:    */   }
/* 339:    */   
/* 340:    */   protected void destroyBean(String beanName, DisposableBean bean)
/* 341:    */   {
/* 342:485 */     Set<String> dependencies = (Set)this.dependentBeanMap.remove(beanName);
/* 343:486 */     if (dependencies != null)
/* 344:    */     {
/* 345:487 */       if (this.logger.isDebugEnabled()) {
/* 346:488 */         this.logger.debug("Retrieved dependent beans for bean '" + beanName + "': " + dependencies);
/* 347:    */       }
/* 348:490 */       for (String dependentBeanName : dependencies) {
/* 349:491 */         destroySingleton(dependentBeanName);
/* 350:    */       }
/* 351:    */     }
/* 352:496 */     if (bean != null) {
/* 353:    */       try
/* 354:    */       {
/* 355:498 */         bean.destroy();
/* 356:    */       }
/* 357:    */       catch (Throwable ex)
/* 358:    */       {
/* 359:501 */         this.logger.error("Destroy method on bean with name '" + beanName + "' threw an exception", ex);
/* 360:    */       }
/* 361:    */     }
/* 362:506 */     Set<String> containedBeans = (Set)this.containedBeanMap.remove(beanName);
/* 363:507 */     if (containedBeans != null) {
/* 364:508 */       for (String containedBeanName : containedBeans) {
/* 365:509 */         destroySingleton(containedBeanName);
/* 366:    */       }
/* 367:    */     }
/* 368:514 */     synchronized (this.dependentBeanMap)
/* 369:    */     {
/* 370:515 */       for (Object it = this.dependentBeanMap.entrySet().iterator(); ((Iterator)it).hasNext();)
/* 371:    */       {
/* 372:516 */         Map.Entry<String, Set<String>> entry = (Map.Entry)((Iterator)it).next();
/* 373:517 */         Set<String> dependenciesToClean = (Set)entry.getValue();
/* 374:518 */         dependenciesToClean.remove(beanName);
/* 375:519 */         if (dependenciesToClean.isEmpty()) {
/* 376:520 */           ((Iterator)it).remove();
/* 377:    */         }
/* 378:    */       }
/* 379:    */     }
/* 380:526 */     this.dependenciesForBeanMap.remove(beanName);
/* 381:    */   }
/* 382:    */   
/* 383:    */   protected final Object getSingletonMutex()
/* 384:    */   {
/* 385:537 */     return this.singletonObjects;
/* 386:    */   }
/* 387:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
 * JD-Core Version:    0.7.0.1
 */