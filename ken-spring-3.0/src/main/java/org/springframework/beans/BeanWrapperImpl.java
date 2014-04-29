/*    1:     */ package org.springframework.beans;
/*    2:     */ 
/*    3:     */ import java.beans.PropertyChangeEvent;
/*    4:     */ import java.beans.PropertyDescriptor;
/*    5:     */ import java.lang.reflect.Array;
/*    6:     */ import java.lang.reflect.InvocationTargetException;
/*    7:     */ import java.lang.reflect.Method;
/*    8:     */ import java.lang.reflect.Modifier;
/*    9:     */ import java.security.AccessControlContext;
/*   10:     */ import java.security.AccessController;
/*   11:     */ import java.security.PrivilegedAction;
/*   12:     */ import java.security.PrivilegedActionException;
/*   13:     */ import java.security.PrivilegedExceptionAction;
/*   14:     */ import java.util.ArrayList;
/*   15:     */ import java.util.Collection;
/*   16:     */ import java.util.HashMap;
/*   17:     */ import java.util.Iterator;
/*   18:     */ import java.util.List;
/*   19:     */ import java.util.Map;
/*   20:     */ import java.util.Set;
/*   21:     */ import org.apache.commons.logging.Log;
/*   22:     */ import org.apache.commons.logging.LogFactory;
/*   23:     */ import org.springframework.core.CollectionFactory;
/*   24:     */ import org.springframework.core.GenericCollectionTypeResolver;
/*   25:     */ import org.springframework.core.MethodParameter;
/*   26:     */ import org.springframework.core.convert.ConversionException;
/*   27:     */ import org.springframework.core.convert.ConverterNotFoundException;
/*   28:     */ import org.springframework.core.convert.Property;
/*   29:     */ import org.springframework.core.convert.TypeDescriptor;
/*   30:     */ import org.springframework.util.Assert;
/*   31:     */ import org.springframework.util.ObjectUtils;
/*   32:     */ import org.springframework.util.StringUtils;
/*   33:     */ 
/*   34:     */ public class BeanWrapperImpl
/*   35:     */   extends AbstractPropertyAccessor
/*   36:     */   implements BeanWrapper
/*   37:     */ {
/*   38:  93 */   private static final Log logger = LogFactory.getLog(BeanWrapperImpl.class);
/*   39:     */   private Object object;
/*   40:  99 */   private String nestedPath = "";
/*   41:     */   private Object rootObject;
/*   42:     */   private TypeConverterDelegate typeConverterDelegate;
/*   43:     */   private AccessControlContext acc;
/*   44:     */   private CachedIntrospectionResults cachedIntrospectionResults;
/*   45:     */   private Map<String, BeanWrapperImpl> nestedBeanWrappers;
/*   46: 121 */   private boolean autoGrowNestedPaths = false;
/*   47: 123 */   private int autoGrowCollectionLimit = 2147483647;
/*   48:     */   
/*   49:     */   public BeanWrapperImpl()
/*   50:     */   {
/*   51: 132 */     this(true);
/*   52:     */   }
/*   53:     */   
/*   54:     */   public BeanWrapperImpl(boolean registerDefaultEditors)
/*   55:     */   {
/*   56: 142 */     if (registerDefaultEditors) {
/*   57: 143 */       registerDefaultEditors();
/*   58:     */     }
/*   59: 145 */     this.typeConverterDelegate = new TypeConverterDelegate(this);
/*   60:     */   }
/*   61:     */   
/*   62:     */   public BeanWrapperImpl(Object object)
/*   63:     */   {
/*   64: 153 */     registerDefaultEditors();
/*   65: 154 */     setWrappedInstance(object);
/*   66:     */   }
/*   67:     */   
/*   68:     */   public BeanWrapperImpl(Class<?> clazz)
/*   69:     */   {
/*   70: 162 */     registerDefaultEditors();
/*   71: 163 */     setWrappedInstance(BeanUtils.instantiateClass(clazz));
/*   72:     */   }
/*   73:     */   
/*   74:     */   public BeanWrapperImpl(Object object, String nestedPath, Object rootObject)
/*   75:     */   {
/*   76: 174 */     registerDefaultEditors();
/*   77: 175 */     setWrappedInstance(object, nestedPath, rootObject);
/*   78:     */   }
/*   79:     */   
/*   80:     */   private BeanWrapperImpl(Object object, String nestedPath, BeanWrapperImpl superBw)
/*   81:     */   {
/*   82: 186 */     setWrappedInstance(object, nestedPath, superBw.getWrappedInstance());
/*   83: 187 */     setExtractOldValueForEditor(superBw.isExtractOldValueForEditor());
/*   84: 188 */     setAutoGrowNestedPaths(superBw.isAutoGrowNestedPaths());
/*   85: 189 */     setAutoGrowCollectionLimit(superBw.getAutoGrowCollectionLimit());
/*   86: 190 */     setConversionService(superBw.getConversionService());
/*   87: 191 */     setSecurityContext(superBw.acc);
/*   88:     */   }
/*   89:     */   
/*   90:     */   public void setWrappedInstance(Object object)
/*   91:     */   {
/*   92: 205 */     setWrappedInstance(object, "", null);
/*   93:     */   }
/*   94:     */   
/*   95:     */   public void setWrappedInstance(Object object, String nestedPath, Object rootObject)
/*   96:     */   {
/*   97: 216 */     Assert.notNull(object, "Bean object must not be null");
/*   98: 217 */     this.object = object;
/*   99: 218 */     this.nestedPath = (nestedPath != null ? nestedPath : "");
/*  100: 219 */     this.rootObject = (!"".equals(this.nestedPath) ? rootObject : object);
/*  101: 220 */     this.nestedBeanWrappers = null;
/*  102: 221 */     this.typeConverterDelegate = new TypeConverterDelegate(this, object);
/*  103: 222 */     setIntrospectionClass(object.getClass());
/*  104:     */   }
/*  105:     */   
/*  106:     */   public final Object getWrappedInstance()
/*  107:     */   {
/*  108: 226 */     return this.object;
/*  109:     */   }
/*  110:     */   
/*  111:     */   public final Class getWrappedClass()
/*  112:     */   {
/*  113: 230 */     return this.object != null ? this.object.getClass() : null;
/*  114:     */   }
/*  115:     */   
/*  116:     */   public final String getNestedPath()
/*  117:     */   {
/*  118: 237 */     return this.nestedPath;
/*  119:     */   }
/*  120:     */   
/*  121:     */   public final Object getRootInstance()
/*  122:     */   {
/*  123: 245 */     return this.rootObject;
/*  124:     */   }
/*  125:     */   
/*  126:     */   public final Class getRootClass()
/*  127:     */   {
/*  128: 253 */     return this.rootObject != null ? this.rootObject.getClass() : null;
/*  129:     */   }
/*  130:     */   
/*  131:     */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths)
/*  132:     */   {
/*  133: 264 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*  134:     */   }
/*  135:     */   
/*  136:     */   public boolean isAutoGrowNestedPaths()
/*  137:     */   {
/*  138: 271 */     return this.autoGrowNestedPaths;
/*  139:     */   }
/*  140:     */   
/*  141:     */   public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit)
/*  142:     */   {
/*  143: 279 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*  144:     */   }
/*  145:     */   
/*  146:     */   public int getAutoGrowCollectionLimit()
/*  147:     */   {
/*  148: 286 */     return this.autoGrowCollectionLimit;
/*  149:     */   }
/*  150:     */   
/*  151:     */   public void setSecurityContext(AccessControlContext acc)
/*  152:     */   {
/*  153: 294 */     this.acc = acc;
/*  154:     */   }
/*  155:     */   
/*  156:     */   public AccessControlContext getSecurityContext()
/*  157:     */   {
/*  158: 302 */     return this.acc;
/*  159:     */   }
/*  160:     */   
/*  161:     */   protected void setIntrospectionClass(Class clazz)
/*  162:     */   {
/*  163: 311 */     if ((this.cachedIntrospectionResults != null) && 
/*  164: 312 */       (!clazz.equals(this.cachedIntrospectionResults.getBeanClass()))) {
/*  165: 313 */       this.cachedIntrospectionResults = null;
/*  166:     */     }
/*  167:     */   }
/*  168:     */   
/*  169:     */   private CachedIntrospectionResults getCachedIntrospectionResults()
/*  170:     */   {
/*  171: 322 */     Assert.state(this.object != null, "BeanWrapper does not hold a bean instance");
/*  172: 323 */     if (this.cachedIntrospectionResults == null) {
/*  173: 324 */       this.cachedIntrospectionResults = CachedIntrospectionResults.forClass(getWrappedClass());
/*  174:     */     }
/*  175: 326 */     return this.cachedIntrospectionResults;
/*  176:     */   }
/*  177:     */   
/*  178:     */   public PropertyDescriptor[] getPropertyDescriptors()
/*  179:     */   {
/*  180: 331 */     return getCachedIntrospectionResults().getPropertyDescriptors();
/*  181:     */   }
/*  182:     */   
/*  183:     */   public PropertyDescriptor getPropertyDescriptor(String propertyName)
/*  184:     */     throws BeansException
/*  185:     */   {
/*  186: 335 */     PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
/*  187: 336 */     if (pd == null) {
/*  188: 337 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  189: 338 */         "No property '" + propertyName + "' found");
/*  190:     */     }
/*  191: 340 */     return pd;
/*  192:     */   }
/*  193:     */   
/*  194:     */   protected PropertyDescriptor getPropertyDescriptorInternal(String propertyName)
/*  195:     */     throws BeansException
/*  196:     */   {
/*  197: 352 */     Assert.notNull(propertyName, "Property name must not be null");
/*  198: 353 */     BeanWrapperImpl nestedBw = getBeanWrapperForPropertyPath(propertyName);
/*  199: 354 */     return nestedBw.getCachedIntrospectionResults().getPropertyDescriptor(getFinalPath(nestedBw, propertyName));
/*  200:     */   }
/*  201:     */   
/*  202:     */   public Class getPropertyType(String propertyName)
/*  203:     */     throws BeansException
/*  204:     */   {
/*  205:     */     try
/*  206:     */     {
/*  207: 360 */       PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
/*  208: 361 */       if (pd != null) {
/*  209: 362 */         return pd.getPropertyType();
/*  210:     */       }
/*  211: 366 */       Object value = getPropertyValue(propertyName);
/*  212: 367 */       if (value != null) {
/*  213: 368 */         return value.getClass();
/*  214:     */       }
/*  215: 372 */       Class editorType = guessPropertyTypeFromEditors(propertyName);
/*  216: 373 */       if (editorType != null) {
/*  217: 374 */         return editorType;
/*  218:     */       }
/*  219:     */     }
/*  220:     */     catch (InvalidPropertyException localInvalidPropertyException) {}
/*  221: 381 */     return null;
/*  222:     */   }
/*  223:     */   
/*  224:     */   public TypeDescriptor getPropertyTypeDescriptor(String propertyName)
/*  225:     */     throws BeansException
/*  226:     */   {
/*  227:     */     try
/*  228:     */     {
/*  229: 386 */       BeanWrapperImpl nestedBw = getBeanWrapperForPropertyPath(propertyName);
/*  230: 387 */       String finalPath = getFinalPath(nestedBw, propertyName);
/*  231: 388 */       PropertyTokenHolder tokens = getPropertyNameTokens(finalPath);
/*  232: 389 */       PropertyDescriptor pd = nestedBw.getCachedIntrospectionResults().getPropertyDescriptor(tokens.actualName);
/*  233: 390 */       if (pd != null) {
/*  234: 391 */         if (tokens.keys != null)
/*  235:     */         {
/*  236: 392 */           if ((pd.getReadMethod() != null) || (pd.getWriteMethod() != null)) {
/*  237: 393 */             return TypeDescriptor.nested(property(pd), tokens.keys.length);
/*  238:     */           }
/*  239:     */         }
/*  240: 396 */         else if ((pd.getReadMethod() != null) || (pd.getWriteMethod() != null)) {
/*  241: 397 */           return new TypeDescriptor(property(pd));
/*  242:     */         }
/*  243:     */       }
/*  244:     */     }
/*  245:     */     catch (InvalidPropertyException localInvalidPropertyException) {}
/*  246: 405 */     return null;
/*  247:     */   }
/*  248:     */   
/*  249:     */   public boolean isReadableProperty(String propertyName)
/*  250:     */   {
/*  251:     */     try
/*  252:     */     {
/*  253: 410 */       PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
/*  254: 411 */       if (pd != null)
/*  255:     */       {
/*  256: 412 */         if (pd.getReadMethod() != null) {
/*  257: 413 */           return true;
/*  258:     */         }
/*  259:     */       }
/*  260:     */       else
/*  261:     */       {
/*  262: 418 */         getPropertyValue(propertyName);
/*  263: 419 */         return true;
/*  264:     */       }
/*  265:     */     }
/*  266:     */     catch (InvalidPropertyException localInvalidPropertyException) {}
/*  267: 425 */     return false;
/*  268:     */   }
/*  269:     */   
/*  270:     */   public boolean isWritableProperty(String propertyName)
/*  271:     */   {
/*  272:     */     try
/*  273:     */     {
/*  274: 430 */       PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
/*  275: 431 */       if (pd != null)
/*  276:     */       {
/*  277: 432 */         if (pd.getWriteMethod() != null) {
/*  278: 433 */           return true;
/*  279:     */         }
/*  280:     */       }
/*  281:     */       else
/*  282:     */       {
/*  283: 438 */         getPropertyValue(propertyName);
/*  284: 439 */         return true;
/*  285:     */       }
/*  286:     */     }
/*  287:     */     catch (InvalidPropertyException localInvalidPropertyException) {}
/*  288: 445 */     return false;
/*  289:     */   }
/*  290:     */   
/*  291:     */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
/*  292:     */     throws TypeMismatchException
/*  293:     */   {
/*  294:     */     try
/*  295:     */     {
/*  296: 451 */       return this.typeConverterDelegate.convertIfNecessary(value, requiredType, methodParam);
/*  297:     */     }
/*  298:     */     catch (ConverterNotFoundException ex)
/*  299:     */     {
/*  300: 454 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/*  301:     */     }
/*  302:     */     catch (ConversionException ex)
/*  303:     */     {
/*  304: 457 */       throw new TypeMismatchException(value, requiredType, ex);
/*  305:     */     }
/*  306:     */     catch (IllegalStateException ex)
/*  307:     */     {
/*  308: 460 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/*  309:     */     }
/*  310:     */     catch (IllegalArgumentException ex)
/*  311:     */     {
/*  312: 463 */       throw new TypeMismatchException(value, requiredType, ex);
/*  313:     */     }
/*  314:     */   }
/*  315:     */   
/*  316:     */   private Object convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<?> requiredType, TypeDescriptor td)
/*  317:     */     throws TypeMismatchException
/*  318:     */   {
/*  319:     */     try
/*  320:     */     {
/*  321: 470 */       return this.typeConverterDelegate.convertIfNecessary(propertyName, oldValue, newValue, requiredType, td);
/*  322:     */     }
/*  323:     */     catch (ConverterNotFoundException ex)
/*  324:     */     {
/*  325: 473 */       PropertyChangeEvent pce = 
/*  326: 474 */         new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
/*  327: 475 */       throw new ConversionNotSupportedException(pce, td.getType(), ex);
/*  328:     */     }
/*  329:     */     catch (ConversionException ex)
/*  330:     */     {
/*  331: 478 */       PropertyChangeEvent pce = 
/*  332: 479 */         new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
/*  333: 480 */       throw new TypeMismatchException(pce, requiredType, ex);
/*  334:     */     }
/*  335:     */     catch (IllegalStateException ex)
/*  336:     */     {
/*  337: 483 */       PropertyChangeEvent pce = 
/*  338: 484 */         new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
/*  339: 485 */       throw new ConversionNotSupportedException(pce, requiredType, ex);
/*  340:     */     }
/*  341:     */     catch (IllegalArgumentException ex)
/*  342:     */     {
/*  343: 488 */       PropertyChangeEvent pce = 
/*  344: 489 */         new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
/*  345: 490 */       throw new TypeMismatchException(pce, requiredType, ex);
/*  346:     */     }
/*  347:     */   }
/*  348:     */   
/*  349:     */   public Object convertForProperty(Object value, String propertyName)
/*  350:     */     throws TypeMismatchException
/*  351:     */   {
/*  352: 505 */     PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(propertyName);
/*  353: 506 */     if (pd == null) {
/*  354: 507 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  355: 508 */         "No property '" + propertyName + "' found");
/*  356:     */     }
/*  357: 510 */     return convertForProperty(propertyName, null, value, pd);
/*  358:     */   }
/*  359:     */   
/*  360:     */   private Object convertForProperty(String propertyName, Object oldValue, Object newValue, PropertyDescriptor pd)
/*  361:     */     throws TypeMismatchException
/*  362:     */   {
/*  363: 515 */     GenericTypeAwarePropertyDescriptor gpd = (GenericTypeAwarePropertyDescriptor)pd;
/*  364: 516 */     gpd.getBeanClass();
/*  365: 517 */     return convertIfNecessary(propertyName, oldValue, newValue, pd.getPropertyType(), new TypeDescriptor(property(pd)));
/*  366:     */   }
/*  367:     */   
/*  368:     */   private String getFinalPath(BeanWrapper bw, String nestedPath)
/*  369:     */   {
/*  370: 532 */     if (bw == this) {
/*  371: 533 */       return nestedPath;
/*  372:     */     }
/*  373: 535 */     return nestedPath.substring(PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(nestedPath) + 1);
/*  374:     */   }
/*  375:     */   
/*  376:     */   protected BeanWrapperImpl getBeanWrapperForPropertyPath(String propertyPath)
/*  377:     */   {
/*  378: 544 */     int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
/*  379: 546 */     if (pos > -1)
/*  380:     */     {
/*  381: 547 */       String nestedProperty = propertyPath.substring(0, pos);
/*  382: 548 */       String nestedPath = propertyPath.substring(pos + 1);
/*  383: 549 */       BeanWrapperImpl nestedBw = getNestedBeanWrapper(nestedProperty);
/*  384: 550 */       return nestedBw.getBeanWrapperForPropertyPath(nestedPath);
/*  385:     */     }
/*  386: 553 */     return this;
/*  387:     */   }
/*  388:     */   
/*  389:     */   private BeanWrapperImpl getNestedBeanWrapper(String nestedProperty)
/*  390:     */   {
/*  391: 566 */     if (this.nestedBeanWrappers == null) {
/*  392: 567 */       this.nestedBeanWrappers = new HashMap();
/*  393:     */     }
/*  394: 570 */     PropertyTokenHolder tokens = getPropertyNameTokens(nestedProperty);
/*  395: 571 */     String canonicalName = tokens.canonicalName;
/*  396: 572 */     Object propertyValue = getPropertyValue(tokens);
/*  397: 573 */     if (propertyValue == null) {
/*  398: 574 */       if (this.autoGrowNestedPaths) {
/*  399: 575 */         propertyValue = setDefaultValue(tokens);
/*  400:     */       } else {
/*  401: 578 */         throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + canonicalName);
/*  402:     */       }
/*  403:     */     }
/*  404: 583 */     BeanWrapperImpl nestedBw = (BeanWrapperImpl)this.nestedBeanWrappers.get(canonicalName);
/*  405: 584 */     if ((nestedBw == null) || (nestedBw.getWrappedInstance() != propertyValue))
/*  406:     */     {
/*  407: 585 */       if (logger.isTraceEnabled()) {
/*  408: 586 */         logger.trace("Creating new nested BeanWrapper for property '" + canonicalName + "'");
/*  409:     */       }
/*  410: 588 */       nestedBw = newNestedBeanWrapper(propertyValue, this.nestedPath + canonicalName + ".");
/*  411:     */       
/*  412: 590 */       copyDefaultEditorsTo(nestedBw);
/*  413: 591 */       copyCustomEditorsTo(nestedBw, canonicalName);
/*  414: 592 */       this.nestedBeanWrappers.put(canonicalName, nestedBw);
/*  415:     */     }
/*  416: 595 */     else if (logger.isTraceEnabled())
/*  417:     */     {
/*  418: 596 */       logger.trace("Using cached nested BeanWrapper for property '" + canonicalName + "'");
/*  419:     */     }
/*  420: 599 */     return nestedBw;
/*  421:     */   }
/*  422:     */   
/*  423:     */   private Object setDefaultValue(String propertyName)
/*  424:     */   {
/*  425: 603 */     PropertyTokenHolder tokens = new PropertyTokenHolder(null);
/*  426: 604 */     tokens.actualName = propertyName;
/*  427: 605 */     tokens.canonicalName = propertyName;
/*  428: 606 */     return setDefaultValue(tokens);
/*  429:     */   }
/*  430:     */   
/*  431:     */   private Object setDefaultValue(PropertyTokenHolder tokens)
/*  432:     */   {
/*  433: 610 */     PropertyValue pv = createDefaultPropertyValue(tokens);
/*  434: 611 */     setPropertyValue(tokens, pv);
/*  435: 612 */     return getPropertyValue(tokens);
/*  436:     */   }
/*  437:     */   
/*  438:     */   private PropertyValue createDefaultPropertyValue(PropertyTokenHolder tokens)
/*  439:     */   {
/*  440: 616 */     Class<?> type = getPropertyTypeDescriptor(tokens.canonicalName).getType();
/*  441: 617 */     if (type == null) {
/*  442: 618 */       throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + tokens.canonicalName, 
/*  443: 619 */         "Could not determine property type for auto-growing a default value");
/*  444:     */     }
/*  445: 621 */     Object defaultValue = newValue(type, tokens.canonicalName);
/*  446: 622 */     return new PropertyValue(tokens.canonicalName, defaultValue);
/*  447:     */   }
/*  448:     */   
/*  449:     */   private Object newValue(Class<?> type, String name)
/*  450:     */   {
/*  451:     */     try
/*  452:     */     {
/*  453: 627 */       if (type.isArray())
/*  454:     */       {
/*  455: 628 */         Class<?> componentType = type.getComponentType();
/*  456: 630 */         if (componentType.isArray())
/*  457:     */         {
/*  458: 631 */           Object array = Array.newInstance(componentType, 1);
/*  459: 632 */           Array.set(array, 0, Array.newInstance(componentType.getComponentType(), 0));
/*  460: 633 */           return array;
/*  461:     */         }
/*  462: 636 */         return Array.newInstance(componentType, 0);
/*  463:     */       }
/*  464: 639 */       if (Collection.class.isAssignableFrom(type)) {
/*  465: 640 */         return CollectionFactory.createCollection(type, 16);
/*  466:     */       }
/*  467: 642 */       if (Map.class.isAssignableFrom(type)) {
/*  468: 643 */         return CollectionFactory.createMap(type, 16);
/*  469:     */       }
/*  470: 646 */       return type.newInstance();
/*  471:     */     }
/*  472:     */     catch (Exception ex)
/*  473:     */     {
/*  474: 651 */       throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + name, 
/*  475: 652 */         "Could not instantiate property type [" + type.getName() + "] to auto-grow nested property path: " + ex);
/*  476:     */     }
/*  477:     */   }
/*  478:     */   
/*  479:     */   protected BeanWrapperImpl newNestedBeanWrapper(Object object, String nestedPath)
/*  480:     */   {
/*  481: 665 */     return new BeanWrapperImpl(object, nestedPath, this);
/*  482:     */   }
/*  483:     */   
/*  484:     */   private PropertyTokenHolder getPropertyNameTokens(String propertyName)
/*  485:     */   {
/*  486: 674 */     PropertyTokenHolder tokens = new PropertyTokenHolder(null);
/*  487: 675 */     String actualName = null;
/*  488: 676 */     List<String> keys = new ArrayList(2);
/*  489: 677 */     int searchIndex = 0;
/*  490: 678 */     while (searchIndex != -1)
/*  491:     */     {
/*  492: 679 */       int keyStart = propertyName.indexOf("[", searchIndex);
/*  493: 680 */       searchIndex = -1;
/*  494: 681 */       if (keyStart != -1)
/*  495:     */       {
/*  496: 682 */         int keyEnd = propertyName.indexOf("]", keyStart + "[".length());
/*  497: 683 */         if (keyEnd != -1)
/*  498:     */         {
/*  499: 684 */           if (actualName == null) {
/*  500: 685 */             actualName = propertyName.substring(0, keyStart);
/*  501:     */           }
/*  502: 687 */           String key = propertyName.substring(keyStart + "[".length(), keyEnd);
/*  503: 688 */           if (((key.startsWith("'")) && (key.endsWith("'"))) || ((key.startsWith("\"")) && (key.endsWith("\"")))) {
/*  504: 689 */             key = key.substring(1, key.length() - 1);
/*  505:     */           }
/*  506: 691 */           keys.add(key);
/*  507: 692 */           searchIndex = keyEnd + "]".length();
/*  508:     */         }
/*  509:     */       }
/*  510:     */     }
/*  511: 696 */     tokens.actualName = (actualName != null ? actualName : propertyName);
/*  512: 697 */     tokens.canonicalName = tokens.actualName;
/*  513: 698 */     if (!keys.isEmpty())
/*  514:     */     {
/*  515: 699 */       PropertyTokenHolder tmp218_217 = tokens;
/*  516:     */       
/*  517:     */ 
/*  518: 702 */       tmp218_217.canonicalName = (tmp218_217.canonicalName + "[" + StringUtils.collectionToDelimitedString(keys, "][") + "]");
/*  519: 703 */       tokens.keys = StringUtils.toStringArray(keys);
/*  520:     */     }
/*  521: 705 */     return tokens;
/*  522:     */   }
/*  523:     */   
/*  524:     */   public Object getPropertyValue(String propertyName)
/*  525:     */     throws BeansException
/*  526:     */   {
/*  527: 715 */     BeanWrapperImpl nestedBw = getBeanWrapperForPropertyPath(propertyName);
/*  528: 716 */     PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedBw, propertyName));
/*  529: 717 */     return nestedBw.getPropertyValue(tokens);
/*  530:     */   }
/*  531:     */   
/*  532:     */   private Object getPropertyValue(PropertyTokenHolder tokens)
/*  533:     */     throws BeansException
/*  534:     */   {
/*  535: 721 */     String propertyName = tokens.canonicalName;
/*  536: 722 */     String actualName = tokens.actualName;
/*  537: 723 */     PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
/*  538: 724 */     if ((pd == null) || (pd.getReadMethod() == null)) {
/*  539: 725 */       throw new NotReadablePropertyException(getRootClass(), this.nestedPath + propertyName);
/*  540:     */     }
/*  541: 727 */     final Method readMethod = pd.getReadMethod();
/*  542:     */     try
/*  543:     */     {
/*  544: 729 */       if ((!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) && (!readMethod.isAccessible())) {
/*  545: 730 */         if (System.getSecurityManager() != null) {
/*  546: 731 */           AccessController.doPrivileged(new PrivilegedAction()
/*  547:     */           {
/*  548:     */             public Object run()
/*  549:     */             {
/*  550: 733 */               readMethod.setAccessible(true);
/*  551: 734 */               return null;
/*  552:     */             }
/*  553:     */           });
/*  554:     */         } else {
/*  555: 739 */           readMethod.setAccessible(true);
/*  556:     */         }
/*  557:     */       }
/*  558:     */       Object value;
/*  559: 744 */       if (System.getSecurityManager() != null) {
/*  560:     */         try
/*  561:     */         {
/*  562: 746 */           value = AccessController.doPrivileged(new PrivilegedExceptionAction()
/*  563:     */           {
/*  564:     */             public Object run()
/*  565:     */               throws Exception
/*  566:     */             {
/*  567: 748 */               return readMethod.invoke(BeanWrapperImpl.this.object, null);
/*  568:     */             }
/*  569: 750 */           }, this.acc);
/*  570:     */         }
/*  571:     */         catch (PrivilegedActionException pae)
/*  572:     */         {
/*  573:     */           Object value;
/*  574: 753 */           throw pae.getException();
/*  575:     */         }
/*  576:     */       } else {
/*  577: 757 */         value = readMethod.invoke(this.object, null);
/*  578:     */       }
/*  579: 760 */       if (tokens.keys != null)
/*  580:     */       {
/*  581: 761 */         if (value == null) {
/*  582: 762 */           if (this.autoGrowNestedPaths) {
/*  583: 763 */             value = setDefaultValue(tokens.actualName);
/*  584:     */           } else {
/*  585: 766 */             throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, 
/*  586: 767 */               "Cannot access indexed value of property referenced in indexed property path '" + 
/*  587: 768 */               propertyName + "': returned null");
/*  588:     */           }
/*  589:     */         }
/*  590: 771 */         String indexedPropertyName = tokens.actualName;
/*  591: 773 */         for (int i = 0; i < tokens.keys.length; i++)
/*  592:     */         {
/*  593: 774 */           String key = tokens.keys[i];
/*  594: 775 */           if (value == null) {
/*  595: 776 */             throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, 
/*  596: 777 */               "Cannot access indexed value of property referenced in indexed property path '" + 
/*  597: 778 */               propertyName + "': returned null");
/*  598:     */           }
/*  599: 780 */           if (value.getClass().isArray())
/*  600:     */           {
/*  601: 781 */             int index = Integer.parseInt(key);
/*  602: 782 */             value = growArrayIfNecessary(value, index, indexedPropertyName);
/*  603: 783 */             value = Array.get(value, index);
/*  604:     */           }
/*  605: 785 */           else if ((value instanceof List))
/*  606:     */           {
/*  607: 786 */             int index = Integer.parseInt(key);
/*  608: 787 */             List list = (List)value;
/*  609: 788 */             growCollectionIfNecessary(list, index, indexedPropertyName, pd, i + 1);
/*  610: 789 */             value = list.get(index);
/*  611:     */           }
/*  612: 791 */           else if ((value instanceof Set))
/*  613:     */           {
/*  614: 793 */             Set set = (Set)value;
/*  615: 794 */             int index = Integer.parseInt(key);
/*  616: 795 */             if ((index < 0) || (index >= set.size())) {
/*  617: 796 */               throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  618: 797 */                 "Cannot get element with index " + index + " from Set of size " + 
/*  619: 798 */                 set.size() + ", accessed using property path '" + propertyName + "'");
/*  620:     */             }
/*  621: 800 */             Iterator it = set.iterator();
/*  622: 801 */             for (int j = 0; it.hasNext(); j++)
/*  623:     */             {
/*  624: 802 */               Object elem = it.next();
/*  625: 803 */               if (j == index)
/*  626:     */               {
/*  627: 804 */                 value = elem;
/*  628: 805 */                 break;
/*  629:     */               }
/*  630:     */             }
/*  631:     */           }
/*  632: 809 */           else if ((value instanceof Map))
/*  633:     */           {
/*  634: 810 */             Map map = (Map)value;
/*  635: 811 */             Class<?> mapKeyType = GenericCollectionTypeResolver.getMapKeyReturnType(pd.getReadMethod(), i + 1);
/*  636:     */             
/*  637:     */ 
/*  638: 814 */             TypeDescriptor typeDescriptor = mapKeyType != null ? TypeDescriptor.valueOf(mapKeyType) : TypeDescriptor.valueOf(Object.class);
/*  639: 815 */             Object convertedMapKey = convertIfNecessary(null, null, key, mapKeyType, typeDescriptor);
/*  640: 816 */             value = map.get(convertedMapKey);
/*  641:     */           }
/*  642:     */           else
/*  643:     */           {
/*  644: 819 */             throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  645: 820 */               "Property referenced in indexed property path '" + propertyName + 
/*  646: 821 */               "' is neither an array nor a List nor a Set nor a Map; returned value was [" + value + "]");
/*  647:     */           }
/*  648: 823 */           indexedPropertyName = indexedPropertyName + "[" + key + "]";
/*  649:     */         }
/*  650:     */       }
/*  651: 826 */       return value;
/*  652:     */     }
/*  653:     */     catch (IndexOutOfBoundsException ex)
/*  654:     */     {
/*  655: 829 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  656: 830 */         "Index of out of bounds in property path '" + propertyName + "'", ex);
/*  657:     */     }
/*  658:     */     catch (NumberFormatException ex)
/*  659:     */     {
/*  660: 833 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  661: 834 */         "Invalid index in property path '" + propertyName + "'", ex);
/*  662:     */     }
/*  663:     */     catch (TypeMismatchException ex)
/*  664:     */     {
/*  665: 837 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  666: 838 */         "Invalid index in property path '" + propertyName + "'", ex);
/*  667:     */     }
/*  668:     */     catch (InvocationTargetException ex)
/*  669:     */     {
/*  670: 841 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  671: 842 */         "Getter for property '" + actualName + "' threw exception", ex);
/*  672:     */     }
/*  673:     */     catch (Exception ex)
/*  674:     */     {
/*  675: 845 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  676: 846 */         "Illegal attempt to get property '" + actualName + "' threw exception", ex);
/*  677:     */     }
/*  678:     */   }
/*  679:     */   
/*  680:     */   private Object growArrayIfNecessary(Object array, int index, String name)
/*  681:     */   {
/*  682: 851 */     if (!this.autoGrowNestedPaths) {
/*  683: 852 */       return array;
/*  684:     */     }
/*  685: 854 */     int length = Array.getLength(array);
/*  686: 855 */     if ((index >= length) && (index < this.autoGrowCollectionLimit))
/*  687:     */     {
/*  688: 856 */       Class<?> componentType = array.getClass().getComponentType();
/*  689: 857 */       Object newArray = Array.newInstance(componentType, index + 1);
/*  690: 858 */       System.arraycopy(array, 0, newArray, 0, length);
/*  691: 859 */       for (int i = length; i < Array.getLength(newArray); i++) {
/*  692: 860 */         Array.set(newArray, i, newValue(componentType, name));
/*  693:     */       }
/*  694: 863 */       setPropertyValue(name, newArray);
/*  695: 864 */       return getPropertyValue(name);
/*  696:     */     }
/*  697: 867 */     return array;
/*  698:     */   }
/*  699:     */   
/*  700:     */   private void growCollectionIfNecessary(Collection collection, int index, String name, PropertyDescriptor pd, int nestingLevel)
/*  701:     */   {
/*  702: 875 */     if (!this.autoGrowNestedPaths) {
/*  703: 876 */       return;
/*  704:     */     }
/*  705: 878 */     int size = collection.size();
/*  706: 879 */     if ((index >= size) && (index < this.autoGrowCollectionLimit))
/*  707:     */     {
/*  708: 880 */       Class elementType = GenericCollectionTypeResolver.getCollectionReturnType(pd.getReadMethod(), nestingLevel);
/*  709: 881 */       if (elementType != null) {
/*  710: 882 */         for (int i = collection.size(); i < index + 1; i++) {
/*  711: 883 */           collection.add(newValue(elementType, name));
/*  712:     */         }
/*  713:     */       }
/*  714:     */     }
/*  715:     */   }
/*  716:     */   
/*  717:     */   public void setPropertyValue(String propertyName, Object value)
/*  718:     */     throws BeansException
/*  719:     */   {
/*  720:     */     try
/*  721:     */     {
/*  722: 893 */       nestedBw = getBeanWrapperForPropertyPath(propertyName);
/*  723:     */     }
/*  724:     */     catch (NotReadablePropertyException ex)
/*  725:     */     {
/*  726:     */       BeanWrapperImpl nestedBw;
/*  727: 896 */       throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  728: 897 */         "Nested property in path '" + propertyName + "' does not exist", ex);
/*  729:     */     }
/*  730:     */     BeanWrapperImpl nestedBw;
/*  731: 899 */     PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedBw, propertyName));
/*  732: 900 */     nestedBw.setPropertyValue(tokens, new PropertyValue(propertyName, value));
/*  733:     */   }
/*  734:     */   
/*  735:     */   public void setPropertyValue(PropertyValue pv)
/*  736:     */     throws BeansException
/*  737:     */   {
/*  738: 905 */     PropertyTokenHolder tokens = (PropertyTokenHolder)pv.resolvedTokens;
/*  739: 906 */     if (tokens == null)
/*  740:     */     {
/*  741: 907 */       String propertyName = pv.getName();
/*  742:     */       try
/*  743:     */       {
/*  744: 910 */         nestedBw = getBeanWrapperForPropertyPath(propertyName);
/*  745:     */       }
/*  746:     */       catch (NotReadablePropertyException ex)
/*  747:     */       {
/*  748:     */         BeanWrapperImpl nestedBw;
/*  749: 913 */         throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  750: 914 */           "Nested property in path '" + propertyName + "' does not exist", ex);
/*  751:     */       }
/*  752:     */       BeanWrapperImpl nestedBw;
/*  753: 916 */       tokens = getPropertyNameTokens(getFinalPath(nestedBw, propertyName));
/*  754: 917 */       if (nestedBw == this) {
/*  755: 918 */         pv.getOriginalPropertyValue().resolvedTokens = tokens;
/*  756:     */       }
/*  757: 920 */       nestedBw.setPropertyValue(tokens, pv);
/*  758:     */     }
/*  759:     */     else
/*  760:     */     {
/*  761: 923 */       setPropertyValue(tokens, pv);
/*  762:     */     }
/*  763:     */   }
/*  764:     */   
/*  765:     */   private void setPropertyValue(PropertyTokenHolder tokens, PropertyValue pv)
/*  766:     */     throws BeansException
/*  767:     */   {
/*  768: 929 */     String propertyName = tokens.canonicalName;
/*  769: 930 */     String actualName = tokens.actualName;
/*  770: 932 */     if (tokens.keys != null)
/*  771:     */     {
/*  772: 934 */       PropertyTokenHolder getterTokens = new PropertyTokenHolder(null);
/*  773: 935 */       getterTokens.canonicalName = tokens.canonicalName;
/*  774: 936 */       getterTokens.actualName = tokens.actualName;
/*  775: 937 */       getterTokens.keys = new String[tokens.keys.length - 1];
/*  776: 938 */       System.arraycopy(tokens.keys, 0, getterTokens.keys, 0, tokens.keys.length - 1);
/*  777:     */       try
/*  778:     */       {
/*  779: 941 */         propValue = getPropertyValue(getterTokens);
/*  780:     */       }
/*  781:     */       catch (NotReadablePropertyException ex)
/*  782:     */       {
/*  783:     */         Object propValue;
/*  784: 944 */         throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  785: 945 */           "Cannot access indexed value in property referenced in indexed property path '" + 
/*  786: 946 */           propertyName + "'", ex);
/*  787:     */       }
/*  788:     */       Object propValue;
/*  789: 949 */       String key = tokens.keys[(tokens.keys.length - 1)];
/*  790: 950 */       if (propValue == null) {
/*  791: 952 */         if (this.autoGrowNestedPaths)
/*  792:     */         {
/*  793: 954 */           int lastKeyIndex = tokens.canonicalName.lastIndexOf('[');
/*  794: 955 */           getterTokens.canonicalName = tokens.canonicalName.substring(0, lastKeyIndex);
/*  795: 956 */           propValue = setDefaultValue(getterTokens);
/*  796:     */         }
/*  797:     */         else
/*  798:     */         {
/*  799: 959 */           throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, 
/*  800: 960 */             "Cannot access indexed value in property referenced in indexed property path '" + 
/*  801: 961 */             propertyName + "': returned null");
/*  802:     */         }
/*  803:     */       }
/*  804: 964 */       if (propValue.getClass().isArray())
/*  805:     */       {
/*  806: 965 */         PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
/*  807: 966 */         Class requiredType = propValue.getClass().getComponentType();
/*  808: 967 */         int arrayIndex = Integer.parseInt(key);
/*  809: 968 */         Object oldValue = null;
/*  810:     */         try
/*  811:     */         {
/*  812: 970 */           if ((isExtractOldValueForEditor()) && (arrayIndex < Array.getLength(propValue))) {
/*  813: 971 */             oldValue = Array.get(propValue, arrayIndex);
/*  814:     */           }
/*  815: 973 */           Object convertedValue = convertIfNecessary(propertyName, oldValue, pv.getValue(), requiredType, TypeDescriptor.nested(property(pd), tokens.keys.length));
/*  816: 974 */           Array.set(propValue, arrayIndex, convertedValue);
/*  817:     */         }
/*  818:     */         catch (IndexOutOfBoundsException ex)
/*  819:     */         {
/*  820: 977 */           throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  821: 978 */             "Invalid array index in property path '" + propertyName + "'", ex);
/*  822:     */         }
/*  823:     */       }
/*  824: 981 */       else if ((propValue instanceof List))
/*  825:     */       {
/*  826: 982 */         PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
/*  827: 983 */         Class requiredType = GenericCollectionTypeResolver.getCollectionReturnType(
/*  828: 984 */           pd.getReadMethod(), tokens.keys.length);
/*  829: 985 */         List list = (List)propValue;
/*  830: 986 */         int index = Integer.parseInt(key);
/*  831: 987 */         int size = list.size();
/*  832: 988 */         Object oldValue = null;
/*  833: 989 */         if ((isExtractOldValueForEditor()) && (index < size)) {
/*  834: 990 */           oldValue = list.get(index);
/*  835:     */         }
/*  836: 992 */         Object convertedValue = convertIfNecessary(propertyName, oldValue, pv.getValue(), requiredType, TypeDescriptor.nested(property(pd), tokens.keys.length));
/*  837: 993 */         if ((index >= size) && (index < this.autoGrowCollectionLimit))
/*  838:     */         {
/*  839: 994 */           for (int i = size; i < index; i++) {
/*  840:     */             try
/*  841:     */             {
/*  842: 996 */               list.add(null);
/*  843:     */             }
/*  844:     */             catch (NullPointerException localNullPointerException)
/*  845:     */             {
/*  846: 999 */               throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  847:1000 */                 "Cannot set element with index " + index + " in List of size " + 
/*  848:1001 */                 size + ", accessed using property path '" + propertyName + 
/*  849:1002 */                 "': List does not support filling up gaps with null elements");
/*  850:     */             }
/*  851:     */           }
/*  852:1005 */           list.add(convertedValue);
/*  853:     */         }
/*  854:     */         else
/*  855:     */         {
/*  856:     */           try
/*  857:     */           {
/*  858:1009 */             list.set(index, convertedValue);
/*  859:     */           }
/*  860:     */           catch (IndexOutOfBoundsException ex)
/*  861:     */           {
/*  862:1012 */             throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  863:1013 */               "Invalid list index in property path '" + propertyName + "'", ex);
/*  864:     */           }
/*  865:     */         }
/*  866:     */       }
/*  867:1017 */       else if ((propValue instanceof Map))
/*  868:     */       {
/*  869:1018 */         PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
/*  870:1019 */         Class mapKeyType = GenericCollectionTypeResolver.getMapKeyReturnType(
/*  871:1020 */           pd.getReadMethod(), tokens.keys.length);
/*  872:1021 */         Class mapValueType = GenericCollectionTypeResolver.getMapValueReturnType(
/*  873:1022 */           pd.getReadMethod(), tokens.keys.length);
/*  874:1023 */         Map map = (Map)propValue;
/*  875:     */         
/*  876:     */ 
/*  877:1026 */         TypeDescriptor typeDescriptor = mapKeyType != null ? TypeDescriptor.valueOf(mapKeyType) : TypeDescriptor.valueOf(Object.class);
/*  878:1027 */         Object convertedMapKey = convertIfNecessary(null, null, key, mapKeyType, typeDescriptor);
/*  879:1028 */         Object oldValue = null;
/*  880:1029 */         if (isExtractOldValueForEditor()) {
/*  881:1030 */           oldValue = map.get(convertedMapKey);
/*  882:     */         }
/*  883:1034 */         Object convertedMapValue = convertIfNecessary(
/*  884:1035 */           propertyName, oldValue, pv.getValue(), mapValueType, TypeDescriptor.nested(property(pd), tokens.keys.length));
/*  885:1036 */         map.put(convertedMapKey, convertedMapValue);
/*  886:     */       }
/*  887:     */       else
/*  888:     */       {
/*  889:1039 */         throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, 
/*  890:1040 */           "Property referenced in indexed property path '" + propertyName + 
/*  891:1041 */           "' is neither an array nor a List nor a Map; returned value was [" + pv.getValue() + "]");
/*  892:     */       }
/*  893:     */     }
/*  894:     */     else
/*  895:     */     {
/*  896:1046 */       PropertyDescriptor pd = pv.resolvedDescriptor;
/*  897:1047 */       if ((pd == null) || (!pd.getWriteMethod().getDeclaringClass().isInstance(this.object)))
/*  898:     */       {
/*  899:1048 */         pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
/*  900:1049 */         if ((pd == null) || (pd.getWriteMethod() == null))
/*  901:     */         {
/*  902:1050 */           if (pv.isOptional())
/*  903:     */           {
/*  904:1051 */             logger.debug("Ignoring optional value for property '" + actualName + 
/*  905:1052 */               "' - property not found on bean class [" + getRootClass().getName() + "]");
/*  906:1053 */             return;
/*  907:     */           }
/*  908:1056 */           PropertyMatches matches = PropertyMatches.forProperty(propertyName, getRootClass());
/*  909:1057 */           throw new NotWritablePropertyException(
/*  910:1058 */             getRootClass(), this.nestedPath + propertyName, 
/*  911:1059 */             matches.buildErrorMessage(), matches.getPossibleMatches());
/*  912:     */         }
/*  913:1062 */         pv.getOriginalPropertyValue().resolvedDescriptor = pd;
/*  914:     */       }
/*  915:1065 */       Object oldValue = null;
/*  916:     */       try
/*  917:     */       {
/*  918:1067 */         Object originalValue = pv.getValue();
/*  919:1068 */         Object valueToApply = originalValue;
/*  920:1069 */         if (!Boolean.FALSE.equals(pv.conversionNecessary))
/*  921:     */         {
/*  922:1070 */           if (pv.isConverted())
/*  923:     */           {
/*  924:1071 */             valueToApply = pv.getConvertedValue();
/*  925:     */           }
/*  926:     */           else
/*  927:     */           {
/*  928:1074 */             if ((isExtractOldValueForEditor()) && (pd.getReadMethod() != null))
/*  929:     */             {
/*  930:1075 */               final Method readMethod = pd.getReadMethod();
/*  931:1076 */               if ((!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) && 
/*  932:1077 */                 (!readMethod.isAccessible())) {
/*  933:1078 */                 if (System.getSecurityManager() != null) {
/*  934:1079 */                   AccessController.doPrivileged(new PrivilegedAction()
/*  935:     */                   {
/*  936:     */                     public Object run()
/*  937:     */                     {
/*  938:1081 */                       readMethod.setAccessible(true);
/*  939:1082 */                       return null;
/*  940:     */                     }
/*  941:     */                   });
/*  942:     */                 } else {
/*  943:1087 */                   readMethod.setAccessible(true);
/*  944:     */                 }
/*  945:     */               }
/*  946:     */               try
/*  947:     */               {
/*  948:1091 */                 if (System.getSecurityManager() != null) {
/*  949:1092 */                   oldValue = AccessController.doPrivileged(new PrivilegedExceptionAction()
/*  950:     */                   {
/*  951:     */                     public Object run()
/*  952:     */                       throws Exception
/*  953:     */                     {
/*  954:1094 */                       return readMethod.invoke(BeanWrapperImpl.this.object, new Object[0]);
/*  955:     */                     }
/*  956:1096 */                   }, this.acc);
/*  957:     */                 } else {
/*  958:1099 */                   oldValue = readMethod.invoke(this.object, new Object[0]);
/*  959:     */                 }
/*  960:     */               }
/*  961:     */               catch (Exception ex)
/*  962:     */               {
/*  963:1103 */                 if ((ex instanceof PrivilegedActionException)) {
/*  964:1104 */                   ex = ((PrivilegedActionException)ex).getException();
/*  965:     */                 }
/*  966:1106 */                 if (logger.isDebugEnabled()) {
/*  967:1107 */                   logger.debug("Could not read previous value of property '" + 
/*  968:1108 */                     this.nestedPath + propertyName + "'", ex);
/*  969:     */                 }
/*  970:     */               }
/*  971:     */             }
/*  972:1112 */             valueToApply = convertForProperty(propertyName, oldValue, originalValue, pd);
/*  973:     */           }
/*  974:1114 */           pv.getOriginalPropertyValue().conversionNecessary = Boolean.valueOf(valueToApply != originalValue);
/*  975:     */         }
/*  976:1116 */         final Method writeMethod = (pd instanceof GenericTypeAwarePropertyDescriptor) ? 
/*  977:1117 */           ((GenericTypeAwarePropertyDescriptor)pd).getWriteMethodForActualAccess() : 
/*  978:1118 */           pd.getWriteMethod();
/*  979:1119 */         if ((!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) && (!writeMethod.isAccessible())) {
/*  980:1120 */           if (System.getSecurityManager() != null) {
/*  981:1121 */             AccessController.doPrivileged(new PrivilegedAction()
/*  982:     */             {
/*  983:     */               public Object run()
/*  984:     */               {
/*  985:1123 */                 writeMethod.setAccessible(true);
/*  986:1124 */                 return null;
/*  987:     */               }
/*  988:     */             });
/*  989:     */           } else {
/*  990:1129 */             writeMethod.setAccessible(true);
/*  991:     */           }
/*  992:     */         }
/*  993:1132 */         final Object value = valueToApply;
/*  994:1133 */         if (System.getSecurityManager() != null) {
/*  995:     */           try
/*  996:     */           {
/*  997:1135 */             AccessController.doPrivileged(new PrivilegedExceptionAction()
/*  998:     */             {
/*  999:     */               public Object run()
/* 1000:     */                 throws Exception
/* 1001:     */               {
/* 1002:1137 */                 writeMethod.invoke(BeanWrapperImpl.this.object, new Object[] { value });
/* 1003:1138 */                 return null;
/* 1004:     */               }
/* 1005:1140 */             }, this.acc);
/* 1006:     */           }
/* 1007:     */           catch (PrivilegedActionException ex)
/* 1008:     */           {
/* 1009:1143 */             throw ex.getException();
/* 1010:     */           }
/* 1011:     */         } else {
/* 1012:1147 */           writeMethod.invoke(this.object, new Object[] { value });
/* 1013:     */         }
/* 1014:     */       }
/* 1015:     */       catch (TypeMismatchException ex)
/* 1016:     */       {
/* 1017:1151 */         throw ex;
/* 1018:     */       }
/* 1019:     */       catch (InvocationTargetException ex)
/* 1020:     */       {
/* 1021:1154 */         PropertyChangeEvent propertyChangeEvent = 
/* 1022:1155 */           new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
/* 1023:1156 */         if ((ex.getTargetException() instanceof ClassCastException)) {
/* 1024:1157 */           throw new TypeMismatchException(propertyChangeEvent, pd.getPropertyType(), ex.getTargetException());
/* 1025:     */         }
/* 1026:1160 */         throw new MethodInvocationException(propertyChangeEvent, ex.getTargetException());
/* 1027:     */       }
/* 1028:     */       catch (Exception ex)
/* 1029:     */       {
/* 1030:1164 */         PropertyChangeEvent pce = 
/* 1031:1165 */           new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());
/* 1032:1166 */         throw new MethodInvocationException(pce, ex);
/* 1033:     */       }
/* 1034:     */     }
/* 1035:     */   }
/* 1036:     */   
/* 1037:     */   public String toString()
/* 1038:     */   {
/* 1039:1174 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 1040:1175 */     if (this.object != null) {
/* 1041:1176 */       sb.append(": wrapping object [").append(ObjectUtils.identityToString(this.object)).append("]");
/* 1042:     */     } else {
/* 1043:1179 */       sb.append(": no wrapped object set");
/* 1044:     */     }
/* 1045:1181 */     return sb.toString();
/* 1046:     */   }
/* 1047:     */   
/* 1048:     */   private Property property(PropertyDescriptor pd)
/* 1049:     */   {
/* 1050:1199 */     GenericTypeAwarePropertyDescriptor typeAware = (GenericTypeAwarePropertyDescriptor)pd;
/* 1051:1200 */     return new Property(typeAware.getBeanClass(), typeAware.getReadMethod(), typeAware.getWriteMethod());
/* 1052:     */   }
/* 1053:     */   
/* 1054:     */   private static class PropertyTokenHolder
/* 1055:     */   {
/* 1056:     */     public String canonicalName;
/* 1057:     */     public String actualName;
/* 1058:     */     public String[] keys;
/* 1059:     */   }
/* 1060:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.BeanWrapperImpl
 * JD-Core Version:    0.7.0.1
 */