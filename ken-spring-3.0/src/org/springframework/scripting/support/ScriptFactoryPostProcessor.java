/*   1:    */ package org.springframework.scripting.support;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import net.sf.cglib.asm.Type;
/*   8:    */ import net.sf.cglib.core.Signature;
/*   9:    */ import net.sf.cglib.proxy.InterfaceMaker;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.aop.TargetSource;
/*  13:    */ import org.springframework.aop.framework.AopInfrastructureBean;
/*  14:    */ import org.springframework.aop.framework.ProxyFactory;
/*  15:    */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*  16:    */ import org.springframework.beans.BeanUtils;
/*  17:    */ import org.springframework.beans.MutablePropertyValues;
/*  18:    */ import org.springframework.beans.PropertyValue;
/*  19:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  20:    */ import org.springframework.beans.factory.BeanCreationException;
/*  21:    */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*  22:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  23:    */ import org.springframework.beans.factory.BeanFactory;
/*  24:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  25:    */ import org.springframework.beans.factory.DisposableBean;
/*  26:    */ import org.springframework.beans.factory.FactoryBean;
/*  27:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*  28:    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*  29:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  30:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*  31:    */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*  32:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  33:    */ import org.springframework.beans.factory.support.BeanDefinitionValidationException;
/*  34:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*  35:    */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*  36:    */ import org.springframework.context.ResourceLoaderAware;
/*  37:    */ import org.springframework.core.Conventions;
/*  38:    */ import org.springframework.core.Ordered;
/*  39:    */ import org.springframework.core.io.DefaultResourceLoader;
/*  40:    */ import org.springframework.core.io.ResourceLoader;
/*  41:    */ import org.springframework.scripting.ScriptFactory;
/*  42:    */ import org.springframework.scripting.ScriptSource;
/*  43:    */ import org.springframework.util.ClassUtils;
/*  44:    */ import org.springframework.util.ObjectUtils;
/*  45:    */ import org.springframework.util.StringUtils;
/*  46:    */ 
/*  47:    */ public class ScriptFactoryPostProcessor
/*  48:    */   extends InstantiationAwareBeanPostProcessorAdapter
/*  49:    */   implements BeanClassLoaderAware, BeanFactoryAware, ResourceLoaderAware, DisposableBean, Ordered
/*  50:    */ {
/*  51:    */   public static final String INLINE_SCRIPT_PREFIX = "inline:";
/*  52:150 */   public static final String REFRESH_CHECK_DELAY_ATTRIBUTE = Conventions.getQualifiedAttributeName(
/*  53:151 */     ScriptFactoryPostProcessor.class, "refreshCheckDelay");
/*  54:153 */   public static final String PROXY_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(
/*  55:154 */     ScriptFactoryPostProcessor.class, "proxyTargetClass");
/*  56:156 */   public static final String LANGUAGE_ATTRIBUTE = Conventions.getQualifiedAttributeName(
/*  57:157 */     ScriptFactoryPostProcessor.class, "language");
/*  58:    */   private static final String SCRIPT_FACTORY_NAME_PREFIX = "scriptFactory.";
/*  59:    */   private static final String SCRIPTED_OBJECT_NAME_PREFIX = "scriptedObject.";
/*  60:164 */   protected final Log logger = LogFactory.getLog(getClass());
/*  61:166 */   private long defaultRefreshCheckDelay = -1L;
/*  62:168 */   private boolean defaultProxyTargetClass = false;
/*  63:170 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  64:    */   private ConfigurableBeanFactory beanFactory;
/*  65:174 */   private ResourceLoader resourceLoader = new DefaultResourceLoader();
/*  66:176 */   final DefaultListableBeanFactory scriptBeanFactory = new DefaultListableBeanFactory();
/*  67:179 */   private final Map<String, ScriptSource> scriptSourceCache = new HashMap();
/*  68:    */   
/*  69:    */   public void setDefaultRefreshCheckDelay(long defaultRefreshCheckDelay)
/*  70:    */   {
/*  71:190 */     this.defaultRefreshCheckDelay = defaultRefreshCheckDelay;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setDefaultProxyTargetClass(boolean defaultProxyTargetClass)
/*  75:    */   {
/*  76:198 */     this.defaultProxyTargetClass = defaultProxyTargetClass;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  80:    */   {
/*  81:202 */     this.beanClassLoader = classLoader;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  85:    */   {
/*  86:206 */     if (!(beanFactory instanceof ConfigurableBeanFactory)) {
/*  87:207 */       throw new IllegalStateException("ScriptFactoryPostProcessor doesn't work with a BeanFactory which does not implement ConfigurableBeanFactory: " + 
/*  88:208 */         beanFactory.getClass());
/*  89:    */     }
/*  90:210 */     this.beanFactory = ((ConfigurableBeanFactory)beanFactory);
/*  91:    */     
/*  92:    */ 
/*  93:213 */     this.scriptBeanFactory.setParentBeanFactory(this.beanFactory);
/*  94:    */     
/*  95:    */ 
/*  96:216 */     this.scriptBeanFactory.copyConfigurationFrom(this.beanFactory);
/*  97:220 */     for (Iterator<BeanPostProcessor> it = this.scriptBeanFactory.getBeanPostProcessors().iterator(); it.hasNext();) {
/*  98:221 */       if ((it.next() instanceof AopInfrastructureBean)) {
/*  99:222 */         it.remove();
/* 100:    */       }
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setResourceLoader(ResourceLoader resourceLoader)
/* 105:    */   {
/* 106:228 */     this.resourceLoader = resourceLoader;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public int getOrder()
/* 110:    */   {
/* 111:232 */     return -2147483648;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public Class predictBeanType(Class beanClass, String beanName)
/* 115:    */   {
/* 116:238 */     if (!ScriptFactory.class.isAssignableFrom(beanClass)) {
/* 117:239 */       return null;
/* 118:    */     }
/* 119:242 */     BeanDefinition bd = this.beanFactory.getMergedBeanDefinition(beanName);
/* 120:    */     try
/* 121:    */     {
/* 122:245 */       String scriptFactoryBeanName = "scriptFactory." + beanName;
/* 123:246 */       String scriptedObjectBeanName = "scriptedObject." + beanName;
/* 124:247 */       prepareScriptBeans(bd, scriptFactoryBeanName, scriptedObjectBeanName);
/* 125:    */       
/* 126:249 */       ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/* 127:250 */       ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 128:251 */       Class[] interfaces = scriptFactory.getScriptInterfaces();
/* 129:    */       
/* 130:253 */       Class scriptedType = scriptFactory.getScriptedObjectType(scriptSource);
/* 131:254 */       if (scriptedType != null) {
/* 132:255 */         return scriptedType;
/* 133:    */       }
/* 134:256 */       if (!ObjectUtils.isEmpty(interfaces)) {
/* 135:257 */         return interfaces.length == 1 ? interfaces[0] : createCompositeInterface(interfaces);
/* 136:    */       }
/* 137:259 */       if (bd.isSingleton())
/* 138:    */       {
/* 139:260 */         Object bean = this.scriptBeanFactory.getBean(scriptedObjectBeanName);
/* 140:261 */         if (bean != null) {
/* 141:262 */           return bean.getClass();
/* 142:    */         }
/* 143:    */       }
/* 144:    */     }
/* 145:    */     catch (Exception ex)
/* 146:    */     {
/* 147:267 */       if (((ex instanceof BeanCreationException)) && 
/* 148:268 */         ((((BeanCreationException)ex).getMostSpecificCause() instanceof BeanCurrentlyInCreationException)))
/* 149:    */       {
/* 150:269 */         if (this.logger.isTraceEnabled()) {
/* 151:270 */           this.logger.trace("Could not determine scripted object type for bean '" + beanName + "': " + 
/* 152:271 */             ex.getMessage());
/* 153:    */         }
/* 154:    */       }
/* 155:274 */       else if (this.logger.isDebugEnabled()) {
/* 156:275 */         this.logger.debug("Could not determine scripted object type for bean '" + beanName + "'", ex);
/* 157:    */       }
/* 158:    */     }
/* 159:280 */     return null;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public Object postProcessBeforeInstantiation(Class beanClass, String beanName)
/* 163:    */   {
/* 164:286 */     if (!ScriptFactory.class.isAssignableFrom(beanClass)) {
/* 165:287 */       return null;
/* 166:    */     }
/* 167:290 */     BeanDefinition bd = this.beanFactory.getMergedBeanDefinition(beanName);
/* 168:291 */     String scriptFactoryBeanName = "scriptFactory." + beanName;
/* 169:292 */     String scriptedObjectBeanName = "scriptedObject." + beanName;
/* 170:293 */     prepareScriptBeans(bd, scriptFactoryBeanName, scriptedObjectBeanName);
/* 171:    */     
/* 172:295 */     ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/* 173:296 */     ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 174:297 */     boolean isFactoryBean = false;
/* 175:    */     try
/* 176:    */     {
/* 177:299 */       Class scriptedObjectType = scriptFactory.getScriptedObjectType(scriptSource);
/* 178:301 */       if (scriptedObjectType != null) {
/* 179:302 */         isFactoryBean = FactoryBean.class.isAssignableFrom(scriptedObjectType);
/* 180:    */       }
/* 181:    */     }
/* 182:    */     catch (Exception ex)
/* 183:    */     {
/* 184:305 */       throw new BeanCreationException(beanName, "Could not determine scripted object type for " + scriptFactory, 
/* 185:306 */         ex);
/* 186:    */     }
/* 187:309 */     long refreshCheckDelay = resolveRefreshCheckDelay(bd);
/* 188:310 */     if (refreshCheckDelay >= 0L)
/* 189:    */     {
/* 190:311 */       Class[] interfaces = scriptFactory.getScriptInterfaces();
/* 191:312 */       RefreshableScriptTargetSource ts = new RefreshableScriptTargetSource(this.scriptBeanFactory, 
/* 192:313 */         scriptedObjectBeanName, scriptFactory, scriptSource, isFactoryBean);
/* 193:314 */       boolean proxyTargetClass = resolveProxyTargetClass(bd);
/* 194:315 */       String language = (String)bd.getAttribute(LANGUAGE_ATTRIBUTE);
/* 195:316 */       if ((proxyTargetClass) && ((language == null) || (!language.equals("groovy")))) {
/* 196:317 */         throw new BeanDefinitionValidationException(
/* 197:318 */           "Cannot use proxyTargetClass=true with script beans where language is not groovy (found " + 
/* 198:319 */           language + ")");
/* 199:    */       }
/* 200:321 */       ts.setRefreshCheckDelay(refreshCheckDelay);
/* 201:322 */       return createRefreshableProxy(ts, interfaces, proxyTargetClass);
/* 202:    */     }
/* 203:325 */     if (isFactoryBean) {
/* 204:326 */       scriptedObjectBeanName = "&" + scriptedObjectBeanName;
/* 205:    */     }
/* 206:328 */     return this.scriptBeanFactory.getBean(scriptedObjectBeanName);
/* 207:    */   }
/* 208:    */   
/* 209:    */   protected void prepareScriptBeans(BeanDefinition bd, String scriptFactoryBeanName, String scriptedObjectBeanName)
/* 210:    */   {
/* 211:342 */     synchronized (this.scriptBeanFactory)
/* 212:    */     {
/* 213:343 */       if (!this.scriptBeanFactory.containsBeanDefinition(scriptedObjectBeanName))
/* 214:    */       {
/* 215:345 */         this.scriptBeanFactory.registerBeanDefinition(scriptFactoryBeanName, 
/* 216:346 */           createScriptFactoryBeanDefinition(bd));
/* 217:347 */         ScriptFactory scriptFactory = 
/* 218:348 */           (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/* 219:349 */         ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, 
/* 220:350 */           scriptFactory.getScriptSourceLocator());
/* 221:351 */         Class[] interfaces = scriptFactory.getScriptInterfaces();
/* 222:    */         
/* 223:353 */         Class[] scriptedInterfaces = interfaces;
/* 224:354 */         if ((scriptFactory.requiresConfigInterface()) && (!bd.getPropertyValues().isEmpty()))
/* 225:    */         {
/* 226:355 */           Class<?> configInterface = createConfigInterface(bd, interfaces);
/* 227:356 */           scriptedInterfaces = (Class[])ObjectUtils.addObjectToArray(interfaces, configInterface);
/* 228:    */         }
/* 229:359 */         BeanDefinition objectBd = createScriptedObjectBeanDefinition(bd, scriptFactoryBeanName, scriptSource, 
/* 230:360 */           scriptedInterfaces);
/* 231:361 */         long refreshCheckDelay = resolveRefreshCheckDelay(bd);
/* 232:362 */         if (refreshCheckDelay >= 0L) {
/* 233:363 */           objectBd.setScope("prototype");
/* 234:    */         }
/* 235:366 */         this.scriptBeanFactory.registerBeanDefinition(scriptedObjectBeanName, objectBd);
/* 236:    */       }
/* 237:    */     }
/* 238:    */   }
/* 239:    */   
/* 240:    */   protected long resolveRefreshCheckDelay(BeanDefinition beanDefinition)
/* 241:    */   {
/* 242:382 */     long refreshCheckDelay = this.defaultRefreshCheckDelay;
/* 243:383 */     Object attributeValue = beanDefinition.getAttribute(REFRESH_CHECK_DELAY_ATTRIBUTE);
/* 244:384 */     if ((attributeValue instanceof Number)) {
/* 245:385 */       refreshCheckDelay = ((Number)attributeValue).longValue();
/* 246:386 */     } else if ((attributeValue instanceof String)) {
/* 247:387 */       refreshCheckDelay = Long.parseLong((String)attributeValue);
/* 248:388 */     } else if (attributeValue != null) {
/* 249:389 */       throw new BeanDefinitionStoreException("Invalid refresh check delay attribute [" + 
/* 250:390 */         REFRESH_CHECK_DELAY_ATTRIBUTE + "] with value [" + attributeValue + 
/* 251:391 */         "]: needs to be of type Number or String");
/* 252:    */     }
/* 253:393 */     return refreshCheckDelay;
/* 254:    */   }
/* 255:    */   
/* 256:    */   protected boolean resolveProxyTargetClass(BeanDefinition beanDefinition)
/* 257:    */   {
/* 258:397 */     boolean proxyTargetClass = this.defaultProxyTargetClass;
/* 259:398 */     Object attributeValue = beanDefinition.getAttribute(PROXY_TARGET_CLASS_ATTRIBUTE);
/* 260:399 */     if ((attributeValue instanceof Boolean)) {
/* 261:400 */       proxyTargetClass = ((Boolean)attributeValue).booleanValue();
/* 262:401 */     } else if ((attributeValue instanceof String)) {
/* 263:402 */       proxyTargetClass = new Boolean((String)attributeValue).booleanValue();
/* 264:403 */     } else if (attributeValue != null) {
/* 265:404 */       throw new BeanDefinitionStoreException("Invalid refresh check delay attribute [" + 
/* 266:405 */         REFRESH_CHECK_DELAY_ATTRIBUTE + "] with value [" + attributeValue + 
/* 267:406 */         "]: needs to be of type Number or String");
/* 268:    */     }
/* 269:408 */     return proxyTargetClass;
/* 270:    */   }
/* 271:    */   
/* 272:    */   protected BeanDefinition createScriptFactoryBeanDefinition(BeanDefinition bd)
/* 273:    */   {
/* 274:420 */     GenericBeanDefinition scriptBd = new GenericBeanDefinition();
/* 275:421 */     scriptBd.setBeanClassName(bd.getBeanClassName());
/* 276:422 */     scriptBd.getConstructorArgumentValues().addArgumentValues(bd.getConstructorArgumentValues());
/* 277:423 */     return scriptBd;
/* 278:    */   }
/* 279:    */   
/* 280:    */   protected ScriptSource getScriptSource(String beanName, String scriptSourceLocator)
/* 281:    */   {
/* 282:435 */     synchronized (this.scriptSourceCache)
/* 283:    */     {
/* 284:436 */       ScriptSource scriptSource = (ScriptSource)this.scriptSourceCache.get(beanName);
/* 285:437 */       if (scriptSource == null)
/* 286:    */       {
/* 287:438 */         scriptSource = convertToScriptSource(beanName, scriptSourceLocator, this.resourceLoader);
/* 288:439 */         this.scriptSourceCache.put(beanName, scriptSource);
/* 289:    */       }
/* 290:441 */       return scriptSource;
/* 291:    */     }
/* 292:    */   }
/* 293:    */   
/* 294:    */   protected ScriptSource convertToScriptSource(String beanName, String scriptSourceLocator, ResourceLoader resourceLoader)
/* 295:    */   {
/* 296:458 */     if (scriptSourceLocator.startsWith("inline:")) {
/* 297:459 */       return new StaticScriptSource(scriptSourceLocator.substring("inline:".length()), beanName);
/* 298:    */     }
/* 299:461 */     return new ResourceScriptSource(resourceLoader.getResource(scriptSourceLocator));
/* 300:    */   }
/* 301:    */   
/* 302:    */   protected Class createConfigInterface(BeanDefinition bd, Class[] interfaces)
/* 303:    */   {
/* 304:480 */     InterfaceMaker maker = new InterfaceMaker();
/* 305:481 */     PropertyValue[] pvs = bd.getPropertyValues().getPropertyValues();
/* 306:482 */     for (PropertyValue pv : pvs)
/* 307:    */     {
/* 308:483 */       String propertyName = pv.getName();
/* 309:484 */       Class propertyType = BeanUtils.findPropertyType(propertyName, interfaces);
/* 310:485 */       String setterName = "set" + StringUtils.capitalize(propertyName);
/* 311:486 */       Signature signature = new Signature(setterName, Type.VOID_TYPE, new Type[] { Type.getType(propertyType) });
/* 312:487 */       maker.add(signature, new Type[0]);
/* 313:    */     }
/* 314:489 */     if ((bd instanceof AbstractBeanDefinition))
/* 315:    */     {
/* 316:490 */       AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/* 317:491 */       if (abd.getInitMethodName() != null)
/* 318:    */       {
/* 319:492 */         Signature signature = new Signature(abd.getInitMethodName(), Type.VOID_TYPE, new Type[0]);
/* 320:493 */         maker.add(signature, new Type[0]);
/* 321:    */       }
/* 322:495 */       if (abd.getDestroyMethodName() != null)
/* 323:    */       {
/* 324:496 */         Signature signature = new Signature(abd.getDestroyMethodName(), Type.VOID_TYPE, new Type[0]);
/* 325:497 */         maker.add(signature, new Type[0]);
/* 326:    */       }
/* 327:    */     }
/* 328:500 */     return maker.create();
/* 329:    */   }
/* 330:    */   
/* 331:    */   protected Class createCompositeInterface(Class[] interfaces)
/* 332:    */   {
/* 333:513 */     return ClassUtils.createCompositeInterface(interfaces, this.beanClassLoader);
/* 334:    */   }
/* 335:    */   
/* 336:    */   protected BeanDefinition createScriptedObjectBeanDefinition(BeanDefinition bd, String scriptFactoryBeanName, ScriptSource scriptSource, Class[] interfaces)
/* 337:    */   {
/* 338:530 */     GenericBeanDefinition objectBd = new GenericBeanDefinition(bd);
/* 339:531 */     objectBd.setFactoryBeanName(scriptFactoryBeanName);
/* 340:532 */     objectBd.setFactoryMethodName("getScriptedObject");
/* 341:533 */     objectBd.getConstructorArgumentValues().clear();
/* 342:534 */     objectBd.getConstructorArgumentValues().addIndexedArgumentValue(0, scriptSource);
/* 343:535 */     objectBd.getConstructorArgumentValues().addIndexedArgumentValue(1, interfaces);
/* 344:536 */     return objectBd;
/* 345:    */   }
/* 346:    */   
/* 347:    */   protected Object createRefreshableProxy(TargetSource ts, Class[] interfaces, boolean proxyTargetClass)
/* 348:    */   {
/* 349:548 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 350:549 */     proxyFactory.setTargetSource(ts);
/* 351:550 */     ClassLoader classLoader = this.beanClassLoader;
/* 352:552 */     if (interfaces == null) {
/* 353:553 */       interfaces = ClassUtils.getAllInterfacesForClass(ts.getTargetClass(), this.beanClassLoader);
/* 354:    */     }
/* 355:555 */     proxyFactory.setInterfaces(interfaces);
/* 356:556 */     if (proxyTargetClass)
/* 357:    */     {
/* 358:557 */       classLoader = null;
/* 359:558 */       proxyFactory.setProxyTargetClass(proxyTargetClass);
/* 360:    */     }
/* 361:561 */     DelegatingIntroductionInterceptor introduction = new DelegatingIntroductionInterceptor(ts);
/* 362:562 */     introduction.suppressInterface(TargetSource.class);
/* 363:563 */     proxyFactory.addAdvice(introduction);
/* 364:    */     
/* 365:565 */     return proxyFactory.getProxy(classLoader);
/* 366:    */   }
/* 367:    */   
/* 368:    */   public void destroy()
/* 369:    */   {
/* 370:572 */     this.scriptBeanFactory.destroySingletons();
/* 371:    */   }
/* 372:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.support.ScriptFactoryPostProcessor
 * JD-Core Version:    0.7.0.1
 */