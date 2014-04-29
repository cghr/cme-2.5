/*   1:    */ package org.springframework.jmx.access;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.lang.reflect.Array;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.net.MalformedURLException;
/*   8:    */ import java.util.Arrays;
/*   9:    */ import java.util.Collection;
/*  10:    */ import java.util.HashMap;
/*  11:    */ import java.util.Map;
/*  12:    */ import javax.management.Attribute;
/*  13:    */ import javax.management.InstanceNotFoundException;
/*  14:    */ import javax.management.IntrospectionException;
/*  15:    */ import javax.management.JMException;
/*  16:    */ import javax.management.JMX;
/*  17:    */ import javax.management.MBeanAttributeInfo;
/*  18:    */ import javax.management.MBeanException;
/*  19:    */ import javax.management.MBeanInfo;
/*  20:    */ import javax.management.MBeanOperationInfo;
/*  21:    */ import javax.management.MBeanServerConnection;
/*  22:    */ import javax.management.MBeanServerInvocationHandler;
/*  23:    */ import javax.management.MalformedObjectNameException;
/*  24:    */ import javax.management.ObjectName;
/*  25:    */ import javax.management.OperationsException;
/*  26:    */ import javax.management.ReflectionException;
/*  27:    */ import javax.management.RuntimeErrorException;
/*  28:    */ import javax.management.RuntimeMBeanException;
/*  29:    */ import javax.management.RuntimeOperationsException;
/*  30:    */ import javax.management.openmbean.CompositeData;
/*  31:    */ import javax.management.openmbean.TabularData;
/*  32:    */ import javax.management.remote.JMXServiceURL;
/*  33:    */ import org.aopalliance.intercept.MethodInterceptor;
/*  34:    */ import org.aopalliance.intercept.MethodInvocation;
/*  35:    */ import org.apache.commons.logging.Log;
/*  36:    */ import org.apache.commons.logging.LogFactory;
/*  37:    */ import org.springframework.beans.BeanUtils;
/*  38:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  39:    */ import org.springframework.beans.factory.DisposableBean;
/*  40:    */ import org.springframework.beans.factory.InitializingBean;
/*  41:    */ import org.springframework.core.CollectionFactory;
/*  42:    */ import org.springframework.core.GenericCollectionTypeResolver;
/*  43:    */ import org.springframework.core.MethodParameter;
/*  44:    */ import org.springframework.jmx.support.JmxUtils;
/*  45:    */ import org.springframework.jmx.support.ObjectNameManager;
/*  46:    */ import org.springframework.util.ClassUtils;
/*  47:    */ import org.springframework.util.ReflectionUtils;
/*  48:    */ 
/*  49:    */ public class MBeanClientInterceptor
/*  50:    */   implements MethodInterceptor, BeanClassLoaderAware, InitializingBean, DisposableBean
/*  51:    */ {
/*  52: 92 */   protected final Log logger = LogFactory.getLog(getClass());
/*  53:    */   private MBeanServerConnection server;
/*  54:    */   private JMXServiceURL serviceUrl;
/*  55:    */   private Map<String, ?> environment;
/*  56:    */   private String agentId;
/*  57:102 */   private boolean connectOnStartup = true;
/*  58:104 */   private boolean refreshOnConnectFailure = false;
/*  59:    */   private ObjectName objectName;
/*  60:108 */   private boolean useStrictCasing = true;
/*  61:    */   private Class managementInterface;
/*  62:112 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  63:114 */   private final ConnectorDelegate connector = new ConnectorDelegate();
/*  64:    */   private MBeanServerConnection serverToUse;
/*  65:    */   private MBeanServerInvocationHandler invocationHandler;
/*  66:    */   private Map<String, MBeanAttributeInfo> allowedAttributes;
/*  67:    */   private Map<MethodCacheKey, MBeanOperationInfo> allowedOperations;
/*  68:124 */   private final Map<Method, String[]> signatureCache = new HashMap();
/*  69:126 */   private final Object preparationMonitor = new Object();
/*  70:    */   
/*  71:    */   public void setServer(MBeanServerConnection server)
/*  72:    */   {
/*  73:134 */     this.server = server;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setServiceUrl(String url)
/*  77:    */     throws MalformedURLException
/*  78:    */   {
/*  79:141 */     this.serviceUrl = new JMXServiceURL(url);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setEnvironment(Map<String, ?> environment)
/*  83:    */   {
/*  84:149 */     this.environment = environment;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public Map<String, ?> getEnvironment()
/*  88:    */   {
/*  89:160 */     return this.environment;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setAgentId(String agentId)
/*  93:    */   {
/*  94:172 */     this.agentId = agentId;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setConnectOnStartup(boolean connectOnStartup)
/*  98:    */   {
/*  99:181 */     this.connectOnStartup = connectOnStartup;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setRefreshOnConnectFailure(boolean refreshOnConnectFailure)
/* 103:    */   {
/* 104:191 */     this.refreshOnConnectFailure = refreshOnConnectFailure;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setObjectName(Object objectName)
/* 108:    */     throws MalformedObjectNameException
/* 109:    */   {
/* 110:199 */     this.objectName = ObjectNameManager.getInstance(objectName);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setUseStrictCasing(boolean useStrictCasing)
/* 114:    */   {
/* 115:210 */     this.useStrictCasing = useStrictCasing;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setManagementInterface(Class managementInterface)
/* 119:    */   {
/* 120:219 */     this.managementInterface = managementInterface;
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected final Class getManagementInterface()
/* 124:    */   {
/* 125:227 */     return this.managementInterface;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/* 129:    */   {
/* 130:231 */     this.beanClassLoader = beanClassLoader;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void afterPropertiesSet()
/* 134:    */   {
/* 135:240 */     if ((this.server != null) && (this.refreshOnConnectFailure)) {
/* 136:241 */       throw new IllegalArgumentException("'refreshOnConnectFailure' does not work when setting a 'server' reference. Prefer 'serviceUrl' etc instead.");
/* 137:    */     }
/* 138:244 */     if (this.connectOnStartup) {
/* 139:245 */       prepare();
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void prepare()
/* 144:    */   {
/* 145:254 */     synchronized (this.preparationMonitor)
/* 146:    */     {
/* 147:255 */       if (this.server != null)
/* 148:    */       {
/* 149:256 */         this.serverToUse = this.server;
/* 150:    */       }
/* 151:    */       else
/* 152:    */       {
/* 153:259 */         this.serverToUse = null;
/* 154:260 */         this.serverToUse = this.connector.connect(this.serviceUrl, this.environment, this.agentId);
/* 155:    */       }
/* 156:262 */       this.invocationHandler = null;
/* 157:263 */       if (this.useStrictCasing)
/* 158:    */       {
/* 159:266 */         if (JmxUtils.isMXBeanSupportAvailable()) {
/* 160:267 */           this.invocationHandler = 
/* 161:268 */             new MBeanServerInvocationHandler(this.serverToUse, this.objectName, 
/* 162:269 */             (this.managementInterface != null) && (JMX.isMXBeanInterface(this.managementInterface)));
/* 163:    */         } else {
/* 164:272 */           this.invocationHandler = new MBeanServerInvocationHandler(this.serverToUse, this.objectName);
/* 165:    */         }
/* 166:    */       }
/* 167:    */       else {
/* 168:278 */         retrieveMBeanInfo();
/* 169:    */       }
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   private void retrieveMBeanInfo()
/* 174:    */     throws MBeanInfoRetrievalException
/* 175:    */   {
/* 176:    */     try
/* 177:    */     {
/* 178:289 */       MBeanInfo info = this.serverToUse.getMBeanInfo(this.objectName);
/* 179:    */       
/* 180:291 */       MBeanAttributeInfo[] attributeInfo = info.getAttributes();
/* 181:292 */       this.allowedAttributes = new HashMap(attributeInfo.length);
/* 182:293 */       for (MBeanAttributeInfo infoEle : attributeInfo) {
/* 183:294 */         this.allowedAttributes.put(infoEle.getName(), infoEle);
/* 184:    */       }
/* 185:297 */       MBeanOperationInfo[] operationInfo = info.getOperations();
/* 186:298 */       this.allowedOperations = new HashMap(operationInfo.length);
/* 187:299 */       for (MBeanOperationInfo infoEle : operationInfo)
/* 188:    */       {
/* 189:300 */         Class[] paramTypes = JmxUtils.parameterInfoToTypes(infoEle.getSignature(), this.beanClassLoader);
/* 190:301 */         this.allowedOperations.put(new MethodCacheKey(infoEle.getName(), paramTypes), infoEle);
/* 191:    */       }
/* 192:    */     }
/* 193:    */     catch (ClassNotFoundException ex)
/* 194:    */     {
/* 195:305 */       throw new MBeanInfoRetrievalException("Unable to locate class specified in method signature", ex);
/* 196:    */     }
/* 197:    */     catch (IntrospectionException ex)
/* 198:    */     {
/* 199:308 */       throw new MBeanInfoRetrievalException("Unable to obtain MBean info for bean [" + this.objectName + "]", ex);
/* 200:    */     }
/* 201:    */     catch (InstanceNotFoundException ex)
/* 202:    */     {
/* 203:312 */       throw new MBeanInfoRetrievalException("Unable to obtain MBean info for bean [" + this.objectName + 
/* 204:313 */         "]: it is likely that this bean was unregistered during the proxy creation process", 
/* 205:314 */         ex);
/* 206:    */     }
/* 207:    */     catch (ReflectionException ex)
/* 208:    */     {
/* 209:317 */       throw new MBeanInfoRetrievalException("Unable to read MBean info for bean [ " + this.objectName + "]", ex);
/* 210:    */     }
/* 211:    */     catch (IOException ex)
/* 212:    */     {
/* 213:320 */       throw new MBeanInfoRetrievalException("An IOException occurred when communicating with the MBeanServer. It is likely that you are communicating with a remote MBeanServer. Check the inner exception for exact details.", 
/* 214:    */       
/* 215:322 */         ex);
/* 216:    */     }
/* 217:    */   }
/* 218:    */   
/* 219:    */   protected boolean isPrepared()
/* 220:    */   {
/* 221:331 */     synchronized (this.preparationMonitor)
/* 222:    */     {
/* 223:332 */       return this.serverToUse != null;
/* 224:    */     }
/* 225:    */   }
/* 226:    */   
/* 227:    */   public Object invoke(MethodInvocation invocation)
/* 228:    */     throws Throwable
/* 229:    */   {
/* 230:347 */     synchronized (this.preparationMonitor)
/* 231:    */     {
/* 232:348 */       if (!isPrepared()) {
/* 233:349 */         prepare();
/* 234:    */       }
/* 235:    */     }
/* 236:    */     try
/* 237:    */     {
/* 238:353 */       return doInvoke(invocation);
/* 239:    */     }
/* 240:    */     catch (MBeanConnectFailureException ex)
/* 241:    */     {
/* 242:356 */       return handleConnectFailure(invocation, ex);
/* 243:    */     }
/* 244:    */     catch (IOException ex)
/* 245:    */     {
/* 246:359 */       return handleConnectFailure(invocation, ex);
/* 247:    */     }
/* 248:    */   }
/* 249:    */   
/* 250:    */   protected Object handleConnectFailure(MethodInvocation invocation, Exception ex)
/* 251:    */     throws Throwable
/* 252:    */   {
/* 253:376 */     if (this.refreshOnConnectFailure)
/* 254:    */     {
/* 255:377 */       String msg = "Could not connect to JMX server - retrying";
/* 256:378 */       if (this.logger.isDebugEnabled()) {
/* 257:379 */         this.logger.warn(msg, ex);
/* 258:381 */       } else if (this.logger.isWarnEnabled()) {
/* 259:382 */         this.logger.warn(msg);
/* 260:    */       }
/* 261:384 */       prepare();
/* 262:385 */       return doInvoke(invocation);
/* 263:    */     }
/* 264:388 */     throw ex;
/* 265:    */   }
/* 266:    */   
/* 267:    */   protected Object doInvoke(MethodInvocation invocation)
/* 268:    */     throws Throwable
/* 269:    */   {
/* 270:401 */     Method method = invocation.getMethod();
/* 271:    */     try
/* 272:    */     {
/* 273:403 */       Object result = null;
/* 274:404 */       if (this.invocationHandler != null)
/* 275:    */       {
/* 276:405 */         result = this.invocationHandler.invoke(invocation.getThis(), method, invocation.getArguments());
/* 277:    */       }
/* 278:    */       else
/* 279:    */       {
/* 280:408 */         PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 281:409 */         if (pd != null) {
/* 282:410 */           result = invokeAttribute(pd, invocation);
/* 283:    */         } else {
/* 284:413 */           result = invokeOperation(method, invocation.getArguments());
/* 285:    */         }
/* 286:    */       }
/* 287:416 */       return convertResultValueIfNecessary(result, new MethodParameter(method, -1));
/* 288:    */     }
/* 289:    */     catch (MBeanException ex)
/* 290:    */     {
/* 291:419 */       throw ex.getTargetException();
/* 292:    */     }
/* 293:    */     catch (RuntimeMBeanException ex)
/* 294:    */     {
/* 295:422 */       throw ex.getTargetException();
/* 296:    */     }
/* 297:    */     catch (RuntimeErrorException ex)
/* 298:    */     {
/* 299:425 */       throw ex.getTargetError();
/* 300:    */     }
/* 301:    */     catch (RuntimeOperationsException ex)
/* 302:    */     {
/* 303:429 */       RuntimeException rex = ex.getTargetException();
/* 304:430 */       if ((rex instanceof RuntimeMBeanException)) {
/* 305:431 */         throw ((RuntimeMBeanException)rex).getTargetException();
/* 306:    */       }
/* 307:433 */       if ((rex instanceof RuntimeErrorException)) {
/* 308:434 */         throw ((RuntimeErrorException)rex).getTargetError();
/* 309:    */       }
/* 310:437 */       throw rex;
/* 311:    */     }
/* 312:    */     catch (OperationsException ex)
/* 313:    */     {
/* 314:441 */       if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 315:442 */         throw ex;
/* 316:    */       }
/* 317:445 */       throw new InvalidInvocationException(ex.getMessage());
/* 318:    */     }
/* 319:    */     catch (JMException ex)
/* 320:    */     {
/* 321:449 */       if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 322:450 */         throw ex;
/* 323:    */       }
/* 324:453 */       throw new InvocationFailureException("JMX access failed", ex);
/* 325:    */     }
/* 326:    */     catch (IOException ex)
/* 327:    */     {
/* 328:457 */       if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 329:458 */         throw ex;
/* 330:    */       }
/* 331:461 */       throw new MBeanConnectFailureException("I/O failure during JMX access", ex);
/* 332:    */     }
/* 333:    */   }
/* 334:    */   
/* 335:    */   private Object invokeAttribute(PropertyDescriptor pd, MethodInvocation invocation)
/* 336:    */     throws JMException, IOException
/* 337:    */   {
/* 338:469 */     String attributeName = JmxUtils.getAttributeName(pd, this.useStrictCasing);
/* 339:470 */     MBeanAttributeInfo inf = (MBeanAttributeInfo)this.allowedAttributes.get(attributeName);
/* 340:473 */     if (inf == null) {
/* 341:474 */       throw new InvalidInvocationException(
/* 342:475 */         "Attribute '" + pd.getName() + "' is not exposed on the management interface");
/* 343:    */     }
/* 344:477 */     if (invocation.getMethod().equals(pd.getReadMethod()))
/* 345:    */     {
/* 346:478 */       if (inf.isReadable()) {
/* 347:479 */         return this.serverToUse.getAttribute(this.objectName, attributeName);
/* 348:    */       }
/* 349:482 */       throw new InvalidInvocationException("Attribute '" + attributeName + "' is not readable");
/* 350:    */     }
/* 351:485 */     if (invocation.getMethod().equals(pd.getWriteMethod()))
/* 352:    */     {
/* 353:486 */       if (inf.isWritable())
/* 354:    */       {
/* 355:487 */         this.serverToUse.setAttribute(this.objectName, new Attribute(attributeName, invocation.getArguments()[0]));
/* 356:488 */         return null;
/* 357:    */       }
/* 358:491 */       throw new InvalidInvocationException("Attribute '" + attributeName + "' is not writable");
/* 359:    */     }
/* 360:495 */     throw new IllegalStateException(
/* 361:496 */       "Method [" + invocation.getMethod() + "] is neither a bean property getter nor a setter");
/* 362:    */   }
/* 363:    */   
/* 364:    */   private Object invokeOperation(Method method, Object[] args)
/* 365:    */     throws JMException, IOException
/* 366:    */   {
/* 367:508 */     MethodCacheKey key = new MethodCacheKey(method.getName(), method.getParameterTypes());
/* 368:509 */     MBeanOperationInfo info = (MBeanOperationInfo)this.allowedOperations.get(key);
/* 369:510 */     if (info == null) {
/* 370:511 */       throw new InvalidInvocationException("Operation '" + method.getName() + 
/* 371:512 */         "' is not exposed on the management interface");
/* 372:    */     }
/* 373:514 */     String[] signature = (String[])null;
/* 374:515 */     synchronized (this.signatureCache)
/* 375:    */     {
/* 376:516 */       signature = (String[])this.signatureCache.get(method);
/* 377:517 */       if (signature == null)
/* 378:    */       {
/* 379:518 */         signature = JmxUtils.getMethodSignature(method);
/* 380:519 */         this.signatureCache.put(method, signature);
/* 381:    */       }
/* 382:    */     }
/* 383:522 */     return this.serverToUse.invoke(this.objectName, method.getName(), args, signature);
/* 384:    */   }
/* 385:    */   
/* 386:    */   protected Object convertResultValueIfNecessary(Object result, MethodParameter parameter)
/* 387:    */   {
/* 388:534 */     Class targetClass = parameter.getParameterType();
/* 389:    */     try
/* 390:    */     {
/* 391:536 */       if (result == null) {
/* 392:537 */         return null;
/* 393:    */       }
/* 394:539 */       if (ClassUtils.isAssignableValue(targetClass, result)) {
/* 395:540 */         return result;
/* 396:    */       }
/* 397:542 */       if ((result instanceof CompositeData))
/* 398:    */       {
/* 399:543 */         Method fromMethod = targetClass.getMethod("from", new Class[] { CompositeData.class });
/* 400:544 */         return ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { result });
/* 401:    */       }
/* 402:546 */       if ((result instanceof CompositeData[]))
/* 403:    */       {
/* 404:547 */         CompositeData[] array = (CompositeData[])result;
/* 405:548 */         if (targetClass.isArray()) {
/* 406:549 */           return convertDataArrayToTargetArray(array, targetClass);
/* 407:    */         }
/* 408:551 */         if (Collection.class.isAssignableFrom(targetClass))
/* 409:    */         {
/* 410:552 */           Class elementType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
/* 411:553 */           if (elementType != null) {
/* 412:554 */             return convertDataArrayToTargetCollection(array, targetClass, elementType);
/* 413:    */           }
/* 414:    */         }
/* 415:    */       }
/* 416:    */       else
/* 417:    */       {
/* 418:558 */         if ((result instanceof TabularData))
/* 419:    */         {
/* 420:559 */           Method fromMethod = targetClass.getMethod("from", new Class[] { TabularData.class });
/* 421:560 */           return ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { result });
/* 422:    */         }
/* 423:562 */         if ((result instanceof TabularData[]))
/* 424:    */         {
/* 425:563 */           TabularData[] array = (TabularData[])result;
/* 426:564 */           if (targetClass.isArray()) {
/* 427:565 */             return convertDataArrayToTargetArray(array, targetClass);
/* 428:    */           }
/* 429:567 */           if (Collection.class.isAssignableFrom(targetClass))
/* 430:    */           {
/* 431:568 */             Class elementType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
/* 432:569 */             if (elementType != null) {
/* 433:570 */               return convertDataArrayToTargetCollection(array, targetClass, elementType);
/* 434:    */             }
/* 435:    */           }
/* 436:    */         }
/* 437:    */       }
/* 438:574 */       throw new InvocationFailureException(
/* 439:575 */         "Incompatible result value [" + result + "] for target type [" + targetClass.getName() + "]");
/* 440:    */     }
/* 441:    */     catch (NoSuchMethodException localNoSuchMethodException)
/* 442:    */     {
/* 443:578 */       throw new InvocationFailureException(
/* 444:579 */         "Could not obtain 'from(CompositeData)' / 'from(TabularData)' method on target type [" + 
/* 445:580 */         targetClass.getName() + "] for conversion of MXBean data structure [" + result + "]");
/* 446:    */     }
/* 447:    */   }
/* 448:    */   
/* 449:    */   private Object convertDataArrayToTargetArray(Object[] array, Class targetClass)
/* 450:    */     throws NoSuchMethodException
/* 451:    */   {
/* 452:585 */     Class targetType = targetClass.getComponentType();
/* 453:586 */     Method fromMethod = targetType.getMethod("from", new Class[] { array.getClass().getComponentType() });
/* 454:587 */     Object resultArray = Array.newInstance(targetType, array.length);
/* 455:588 */     for (int i = 0; i < array.length; i++) {
/* 456:589 */       Array.set(resultArray, i, ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { array[i] }));
/* 457:    */     }
/* 458:591 */     return resultArray;
/* 459:    */   }
/* 460:    */   
/* 461:    */   private Collection convertDataArrayToTargetCollection(Object[] array, Class collectionType, Class elementType)
/* 462:    */     throws NoSuchMethodException
/* 463:    */   {
/* 464:598 */     Method fromMethod = elementType.getMethod("from", new Class[] { array.getClass().getComponentType() });
/* 465:599 */     Collection resultColl = CollectionFactory.createCollection(collectionType, Array.getLength(array));
/* 466:600 */     for (int i = 0; i < array.length; i++) {
/* 467:601 */       resultColl.add(ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { array[i] }));
/* 468:    */     }
/* 469:603 */     return resultColl;
/* 470:    */   }
/* 471:    */   
/* 472:    */   public void destroy()
/* 473:    */   {
/* 474:608 */     this.connector.close();
/* 475:    */   }
/* 476:    */   
/* 477:    */   private static class MethodCacheKey
/* 478:    */   {
/* 479:    */     private final String name;
/* 480:    */     private final Class[] parameterTypes;
/* 481:    */     
/* 482:    */     public MethodCacheKey(String name, Class[] parameterTypes)
/* 483:    */     {
/* 484:628 */       this.name = name;
/* 485:629 */       this.parameterTypes = (parameterTypes != null ? parameterTypes : new Class[0]);
/* 486:    */     }
/* 487:    */     
/* 488:    */     public boolean equals(Object other)
/* 489:    */     {
/* 490:634 */       if (other == this) {
/* 491:635 */         return true;
/* 492:    */       }
/* 493:637 */       MethodCacheKey otherKey = (MethodCacheKey)other;
/* 494:638 */       return (this.name.equals(otherKey.name)) && (Arrays.equals(this.parameterTypes, otherKey.parameterTypes));
/* 495:    */     }
/* 496:    */     
/* 497:    */     public int hashCode()
/* 498:    */     {
/* 499:643 */       return this.name.hashCode();
/* 500:    */     }
/* 501:    */   }
/* 502:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.access.MBeanClientInterceptor
 * JD-Core Version:    0.7.0.1
 */