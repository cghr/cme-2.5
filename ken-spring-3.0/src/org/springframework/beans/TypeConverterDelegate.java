/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.lang.reflect.Array;
/*   5:    */ import java.lang.reflect.Constructor;
/*   6:    */ import java.lang.reflect.Field;
/*   7:    */ import java.lang.reflect.Modifier;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Map.Entry;
/*  12:    */ import java.util.Set;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.apache.commons.logging.LogFactory;
/*  15:    */ import org.springframework.core.CollectionFactory;
/*  16:    */ import org.springframework.core.MethodParameter;
/*  17:    */ import org.springframework.core.convert.ConversionFailedException;
/*  18:    */ import org.springframework.core.convert.ConversionService;
/*  19:    */ import org.springframework.core.convert.TypeDescriptor;
/*  20:    */ import org.springframework.util.ClassUtils;
/*  21:    */ import org.springframework.util.StringUtils;
/*  22:    */ 
/*  23:    */ class TypeConverterDelegate
/*  24:    */ {
/*  25: 53 */   private static final Log logger = LogFactory.getLog(TypeConverterDelegate.class);
/*  26:    */   private final PropertyEditorRegistrySupport propertyEditorRegistry;
/*  27:    */   private final Object targetObject;
/*  28:    */   
/*  29:    */   public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry)
/*  30:    */   {
/*  31: 65 */     this(propertyEditorRegistry, null);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry, Object targetObject)
/*  35:    */   {
/*  36: 74 */     this.propertyEditorRegistry = propertyEditorRegistry;
/*  37: 75 */     this.targetObject = targetObject;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public <T> T convertIfNecessary(Object newValue, Class<T> requiredType, MethodParameter methodParam)
/*  41:    */     throws IllegalArgumentException
/*  42:    */   {
/*  43: 92 */     return convertIfNecessary(null, null, newValue, requiredType, 
/*  44: 93 */       methodParam != null ? new TypeDescriptor(methodParam) : TypeDescriptor.valueOf(requiredType));
/*  45:    */   }
/*  46:    */   
/*  47:    */   public <T> T convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<T> requiredType)
/*  48:    */     throws IllegalArgumentException
/*  49:    */   {
/*  50:110 */     return convertIfNecessary(propertyName, oldValue, newValue, requiredType, TypeDescriptor.valueOf(requiredType));
/*  51:    */   }
/*  52:    */   
/*  53:    */   public <T> T convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<T> requiredType, TypeDescriptor typeDescriptor)
/*  54:    */     throws IllegalArgumentException
/*  55:    */   {
/*  56:129 */     Object convertedValue = newValue;
/*  57:    */     
/*  58:    */ 
/*  59:132 */     PropertyEditor editor = this.propertyEditorRegistry.findCustomEditor(requiredType, propertyName);
/*  60:    */     
/*  61:    */ 
/*  62:135 */     ConversionService conversionService = this.propertyEditorRegistry.getConversionService();
/*  63:136 */     if ((editor == null) && (conversionService != null) && (convertedValue != null) && (typeDescriptor != null))
/*  64:    */     {
/*  65:137 */       TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
/*  66:138 */       TypeDescriptor targetTypeDesc = typeDescriptor;
/*  67:139 */       if (conversionService.canConvert(sourceTypeDesc, targetTypeDesc)) {
/*  68:    */         try
/*  69:    */         {
/*  70:141 */           return conversionService.convert(convertedValue, sourceTypeDesc, targetTypeDesc);
/*  71:    */         }
/*  72:    */         catch (ConversionFailedException localConversionFailedException) {}
/*  73:    */       }
/*  74:    */     }
/*  75:149 */     if ((editor != null) || ((requiredType != null) && (!ClassUtils.isAssignableValue(requiredType, convertedValue))))
/*  76:    */     {
/*  77:150 */       if ((requiredType != null) && (Collection.class.isAssignableFrom(requiredType)) && ((convertedValue instanceof String)))
/*  78:    */       {
/*  79:151 */         TypeDescriptor elementType = typeDescriptor.getElementTypeDescriptor();
/*  80:152 */         if ((elementType != null) && (Enum.class.isAssignableFrom(elementType.getType()))) {
/*  81:153 */           convertedValue = StringUtils.commaDelimitedListToStringArray((String)convertedValue);
/*  82:    */         }
/*  83:    */       }
/*  84:156 */       if (editor == null) {
/*  85:157 */         editor = findDefaultEditor(requiredType, typeDescriptor);
/*  86:    */       }
/*  87:159 */       convertedValue = doConvertValue(oldValue, convertedValue, requiredType, editor);
/*  88:    */     }
/*  89:162 */     if (requiredType != null)
/*  90:    */     {
/*  91:165 */       if (convertedValue != null)
/*  92:    */       {
/*  93:166 */         if (requiredType.isArray())
/*  94:    */         {
/*  95:168 */           if (((convertedValue instanceof String)) && (Enum.class.isAssignableFrom(requiredType.getComponentType()))) {
/*  96:169 */             convertedValue = StringUtils.commaDelimitedListToStringArray((String)convertedValue);
/*  97:    */           }
/*  98:171 */           return convertToTypedArray(convertedValue, propertyName, requiredType.getComponentType());
/*  99:    */         }
/* 100:173 */         if ((convertedValue instanceof Collection)) {
/* 101:175 */           convertedValue = convertToTypedCollection(
/* 102:176 */             (Collection)convertedValue, propertyName, requiredType, typeDescriptor);
/* 103:178 */         } else if ((convertedValue instanceof Map)) {
/* 104:180 */           convertedValue = convertToTypedMap(
/* 105:181 */             (Map)convertedValue, propertyName, requiredType, typeDescriptor);
/* 106:    */         }
/* 107:183 */         if ((convertedValue.getClass().isArray()) && (Array.getLength(convertedValue) == 1)) {
/* 108:184 */           convertedValue = Array.get(convertedValue, 0);
/* 109:    */         }
/* 110:186 */         if ((String.class.equals(requiredType)) && (ClassUtils.isPrimitiveOrWrapper(convertedValue.getClass()))) {
/* 111:188 */           return convertedValue.toString();
/* 112:    */         }
/* 113:190 */         if (((convertedValue instanceof String)) && (!requiredType.isInstance(convertedValue)))
/* 114:    */         {
/* 115:191 */           if ((!requiredType.isInterface()) && (!requiredType.isEnum())) {
/* 116:    */             try
/* 117:    */             {
/* 118:193 */               Constructor strCtor = requiredType.getConstructor(new Class[] { String.class });
/* 119:194 */               return BeanUtils.instantiateClass(strCtor, new Object[] { convertedValue });
/* 120:    */             }
/* 121:    */             catch (NoSuchMethodException ex)
/* 122:    */             {
/* 123:198 */               if (logger.isTraceEnabled()) {
/* 124:199 */                 logger.trace("No String constructor found on type [" + requiredType.getName() + "]", ex);
/* 125:    */               }
/* 126:    */             }
/* 127:    */             catch (Exception ex)
/* 128:    */             {
/* 129:203 */               if (logger.isDebugEnabled()) {
/* 130:204 */                 logger.debug("Construction via String failed for type [" + requiredType.getName() + "]", ex);
/* 131:    */               }
/* 132:    */             }
/* 133:    */           }
/* 134:208 */           String trimmedValue = ((String)convertedValue).trim();
/* 135:209 */           if ((requiredType.isEnum()) && ("".equals(trimmedValue))) {
/* 136:211 */             return null;
/* 137:    */           }
/* 138:214 */           convertedValue = attemptToConvertStringToEnum(requiredType, trimmedValue, convertedValue);
/* 139:    */         }
/* 140:    */       }
/* 141:218 */       if (!ClassUtils.isAssignableValue(requiredType, convertedValue))
/* 142:    */       {
/* 143:220 */         StringBuilder msg = new StringBuilder();
/* 144:221 */         msg.append("Cannot convert value of type [").append(ClassUtils.getDescriptiveType(newValue));
/* 145:222 */         msg.append("] to required type [").append(ClassUtils.getQualifiedName(requiredType)).append("]");
/* 146:223 */         if (propertyName != null) {
/* 147:224 */           msg.append(" for property '").append(propertyName).append("'");
/* 148:    */         }
/* 149:226 */         if (editor != null)
/* 150:    */         {
/* 151:229 */           msg.append(": PropertyEditor [").append(editor.getClass().getName()).append("] returned inappropriate value of type [").append(ClassUtils.getDescriptiveType(convertedValue)).append("]");
/* 152:230 */           throw new IllegalArgumentException(msg.toString());
/* 153:    */         }
/* 154:233 */         msg.append(": no matching editors or conversion strategy found");
/* 155:234 */         throw new IllegalStateException(msg.toString());
/* 156:    */       }
/* 157:    */     }
/* 158:239 */     return convertedValue;
/* 159:    */   }
/* 160:    */   
/* 161:    */   private Object attemptToConvertStringToEnum(Class<?> requiredType, String trimmedValue, Object currentConvertedValue)
/* 162:    */   {
/* 163:243 */     Object convertedValue = currentConvertedValue;
/* 164:245 */     if (Enum.class.equals(requiredType))
/* 165:    */     {
/* 166:247 */       int index = trimmedValue.lastIndexOf(".");
/* 167:248 */       if (index > -1)
/* 168:    */       {
/* 169:249 */         String enumType = trimmedValue.substring(0, index);
/* 170:250 */         String fieldName = trimmedValue.substring(index + 1);
/* 171:251 */         ClassLoader loader = this.targetObject.getClass().getClassLoader();
/* 172:    */         try
/* 173:    */         {
/* 174:253 */           Class<?> enumValueType = loader.loadClass(enumType);
/* 175:254 */           Field enumField = enumValueType.getField(fieldName);
/* 176:255 */           convertedValue = enumField.get(null);
/* 177:    */         }
/* 178:    */         catch (ClassNotFoundException ex)
/* 179:    */         {
/* 180:258 */           if (logger.isTraceEnabled()) {
/* 181:259 */             logger.trace("Enum class [" + enumType + "] cannot be loaded from [" + loader + "]", ex);
/* 182:    */           }
/* 183:    */         }
/* 184:    */         catch (Throwable ex)
/* 185:    */         {
/* 186:263 */           if (logger.isTraceEnabled()) {
/* 187:264 */             logger.trace("Field [" + fieldName + "] isn't an enum value for type [" + enumType + "]", ex);
/* 188:    */           }
/* 189:    */         }
/* 190:    */       }
/* 191:    */     }
/* 192:270 */     if (convertedValue == currentConvertedValue) {
/* 193:    */       try
/* 194:    */       {
/* 195:275 */         Field enumField = requiredType.getField(trimmedValue);
/* 196:276 */         convertedValue = enumField.get(null);
/* 197:    */       }
/* 198:    */       catch (Throwable ex)
/* 199:    */       {
/* 200:279 */         if (logger.isTraceEnabled()) {
/* 201:280 */           logger.trace("Field [" + convertedValue + "] isn't an enum value", ex);
/* 202:    */         }
/* 203:    */       }
/* 204:    */     }
/* 205:286 */     return convertedValue;
/* 206:    */   }
/* 207:    */   
/* 208:    */   protected PropertyEditor findDefaultEditor(Class requiredType, TypeDescriptor typeDescriptor)
/* 209:    */   {
/* 210:295 */     PropertyEditor editor = null;
/* 211:300 */     if ((editor == null) && (requiredType != null))
/* 212:    */     {
/* 213:302 */       editor = this.propertyEditorRegistry.getDefaultEditor(requiredType);
/* 214:303 */       if ((editor == null) && (!String.class.equals(requiredType))) {
/* 215:305 */         editor = BeanUtils.findEditorByConvention(requiredType);
/* 216:    */       }
/* 217:    */     }
/* 218:308 */     return editor;
/* 219:    */   }
/* 220:    */   
/* 221:    */   protected Object doConvertValue(Object oldValue, Object newValue, Class<?> requiredType, PropertyEditor editor)
/* 222:    */   {
/* 223:323 */     Object convertedValue = newValue;
/* 224:324 */     boolean sharedEditor = false;
/* 225:326 */     if (editor != null) {
/* 226:327 */       sharedEditor = this.propertyEditorRegistry.isSharedEditor(editor);
/* 227:    */     }
/* 228:330 */     if ((editor != null) && (!(convertedValue instanceof String))) {
/* 229:    */       try
/* 230:    */       {
/* 231:337 */         if (sharedEditor)
/* 232:    */         {
/* 233:    */           Object newConvertedValue;
/* 234:339 */           synchronized (editor)
/* 235:    */           {
/* 236:340 */             editor.setValue(convertedValue);
/* 237:341 */             newConvertedValue = editor.getValue();
/* 238:    */           }
/* 239:    */         }
/* 240:346 */         editor.setValue(convertedValue);
/* 241:347 */         Object newConvertedValue = editor.getValue();
/* 242:349 */         if (newConvertedValue != convertedValue)
/* 243:    */         {
/* 244:350 */           convertedValue = newConvertedValue;
/* 245:    */           
/* 246:    */ 
/* 247:353 */           editor = null;
/* 248:    */         }
/* 249:    */       }
/* 250:    */       catch (Exception ex)
/* 251:    */       {
/* 252:357 */         if (logger.isDebugEnabled()) {
/* 253:358 */           logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", ex);
/* 254:    */         }
/* 255:    */       }
/* 256:    */     }
/* 257:364 */     Object returnValue = convertedValue;
/* 258:366 */     if ((requiredType != null) && (!requiredType.isArray()) && ((convertedValue instanceof String[])))
/* 259:    */     {
/* 260:370 */       if (logger.isTraceEnabled()) {
/* 261:371 */         logger.trace("Converting String array to comma-delimited String [" + convertedValue + "]");
/* 262:    */       }
/* 263:373 */       convertedValue = StringUtils.arrayToCommaDelimitedString((String[])convertedValue);
/* 264:    */     }
/* 265:376 */     if ((convertedValue instanceof String))
/* 266:    */     {
/* 267:377 */       if (editor != null)
/* 268:    */       {
/* 269:379 */         if (logger.isTraceEnabled()) {
/* 270:380 */           logger.trace("Converting String to [" + requiredType + "] using property editor [" + editor + "]");
/* 271:    */         }
/* 272:382 */         String newTextValue = (String)convertedValue;
/* 273:383 */         if (sharedEditor) {
/* 274:385 */           synchronized (editor)
/* 275:    */           {
/* 276:386 */             return doConvertTextValue(oldValue, newTextValue, editor);
/* 277:    */           }
/* 278:    */         }
/* 279:391 */         return doConvertTextValue(oldValue, newTextValue, editor);
/* 280:    */       }
/* 281:394 */       if (String.class.equals(requiredType)) {
/* 282:395 */         returnValue = convertedValue;
/* 283:    */       }
/* 284:    */     }
/* 285:399 */     return returnValue;
/* 286:    */   }
/* 287:    */   
/* 288:    */   protected Object doConvertTextValue(Object oldValue, String newTextValue, PropertyEditor editor)
/* 289:    */   {
/* 290:    */     try
/* 291:    */     {
/* 292:411 */       editor.setValue(oldValue);
/* 293:    */     }
/* 294:    */     catch (Exception ex)
/* 295:    */     {
/* 296:414 */       if (logger.isDebugEnabled()) {
/* 297:415 */         logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", ex);
/* 298:    */       }
/* 299:    */     }
/* 300:419 */     editor.setAsText(newTextValue);
/* 301:420 */     return editor.getValue();
/* 302:    */   }
/* 303:    */   
/* 304:    */   protected Object convertToTypedArray(Object input, String propertyName, Class<?> componentType)
/* 305:    */   {
/* 306:424 */     if ((input instanceof Collection))
/* 307:    */     {
/* 308:426 */       Collection coll = (Collection)input;
/* 309:427 */       Object result = Array.newInstance(componentType, coll.size());
/* 310:428 */       int i = 0;
/* 311:429 */       for (Iterator it = coll.iterator(); it.hasNext(); i++)
/* 312:    */       {
/* 313:430 */         Object value = convertIfNecessary(
/* 314:431 */           buildIndexedPropertyName(propertyName, i), null, it.next(), componentType);
/* 315:432 */         Array.set(result, i, value);
/* 316:    */       }
/* 317:434 */       return result;
/* 318:    */     }
/* 319:436 */     if (input.getClass().isArray())
/* 320:    */     {
/* 321:438 */       if ((componentType.equals(input.getClass().getComponentType())) && 
/* 322:439 */         (!this.propertyEditorRegistry.hasCustomEditorForElement(componentType, propertyName))) {
/* 323:440 */         return input;
/* 324:    */       }
/* 325:442 */       int arrayLength = Array.getLength(input);
/* 326:443 */       Object result = Array.newInstance(componentType, arrayLength);
/* 327:444 */       for (int i = 0; i < arrayLength; i++)
/* 328:    */       {
/* 329:445 */         Object value = convertIfNecessary(
/* 330:446 */           buildIndexedPropertyName(propertyName, i), null, Array.get(input, i), componentType);
/* 331:447 */         Array.set(result, i, value);
/* 332:    */       }
/* 333:449 */       return result;
/* 334:    */     }
/* 335:453 */     Object result = Array.newInstance(componentType, 1);
/* 336:454 */     Object value = convertIfNecessary(
/* 337:455 */       buildIndexedPropertyName(propertyName, 0), null, input, componentType);
/* 338:456 */     Array.set(result, 0, value);
/* 339:457 */     return result;
/* 340:    */   }
/* 341:    */   
/* 342:    */   protected Collection convertToTypedCollection(Collection original, String propertyName, Class requiredType, TypeDescriptor typeDescriptor)
/* 343:    */   {
/* 344:465 */     if (!Collection.class.isAssignableFrom(requiredType)) {
/* 345:466 */       return original;
/* 346:    */     }
/* 347:469 */     boolean approximable = CollectionFactory.isApproximableCollectionType(requiredType);
/* 348:470 */     if ((!approximable) && (!canCreateCopy(requiredType)))
/* 349:    */     {
/* 350:471 */       if (logger.isDebugEnabled()) {
/* 351:472 */         logger.debug("Custom Collection type [" + original.getClass().getName() + 
/* 352:473 */           "] does not allow for creating a copy - injecting original Collection as-is");
/* 353:    */       }
/* 354:475 */       return original;
/* 355:    */     }
/* 356:478 */     boolean originalAllowed = requiredType.isInstance(original);
/* 357:479 */     typeDescriptor = typeDescriptor.narrow(original);
/* 358:480 */     TypeDescriptor elementType = typeDescriptor.getElementTypeDescriptor();
/* 359:481 */     if ((elementType == null) && (originalAllowed) && 
/* 360:482 */       (!this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName))) {
/* 361:483 */       return original;
/* 362:    */     }
/* 363:    */     try
/* 364:    */     {
/* 365:488 */       Iterator it = original.iterator();
/* 366:489 */       if (it == null)
/* 367:    */       {
/* 368:490 */         if (logger.isDebugEnabled()) {
/* 369:491 */           logger.debug("Collection of type [" + original.getClass().getName() + 
/* 370:492 */             "] returned null Iterator - injecting original Collection as-is");
/* 371:    */         }
/* 372:494 */         return original;
/* 373:    */       }
/* 374:    */     }
/* 375:    */     catch (Throwable ex)
/* 376:    */     {
/* 377:498 */       if (logger.isDebugEnabled()) {
/* 378:499 */         logger.debug("Cannot access Collection of type [" + original.getClass().getName() + 
/* 379:500 */           "] - injecting original Collection as-is: " + ex);
/* 380:    */       }
/* 381:502 */       return original;
/* 382:    */     }
/* 383:    */     Iterator it;
/* 384:    */     try
/* 385:    */     {
/* 386:    */       Collection convertedCopy;
/* 387:507 */       if (approximable) {
/* 388:508 */         convertedCopy = CollectionFactory.createApproximateCollection(original, original.size());
/* 389:    */       } else {
/* 390:511 */         convertedCopy = (Collection)requiredType.newInstance();
/* 391:    */       }
/* 392:    */     }
/* 393:    */     catch (Throwable ex)
/* 394:    */     {
/* 395:    */       Collection convertedCopy;
/* 396:515 */       if (logger.isDebugEnabled()) {
/* 397:516 */         logger.debug("Cannot create copy of Collection type [" + original.getClass().getName() + 
/* 398:517 */           "] - injecting original Collection as-is: " + ex);
/* 399:    */       }
/* 400:519 */       return original;
/* 401:    */     }
/* 402:    */     Collection convertedCopy;
/* 403:522 */     for (int i = 0; it.hasNext(); i++)
/* 404:    */     {
/* 405:524 */       Object element = it.next();
/* 406:525 */       String indexedPropertyName = buildIndexedPropertyName(propertyName, i);
/* 407:526 */       Object convertedElement = convertIfNecessary(indexedPropertyName, null, element, 
/* 408:527 */         elementType != null ? elementType.getType() : null, typeDescriptor.getElementTypeDescriptor());
/* 409:    */       try
/* 410:    */       {
/* 411:529 */         convertedCopy.add(convertedElement);
/* 412:    */       }
/* 413:    */       catch (Throwable ex)
/* 414:    */       {
/* 415:532 */         if (logger.isDebugEnabled()) {
/* 416:533 */           logger.debug("Collection type [" + original.getClass().getName() + 
/* 417:534 */             "] seems to be read-only - injecting original Collection as-is: " + ex);
/* 418:    */         }
/* 419:536 */         return original;
/* 420:    */       }
/* 421:538 */       originalAllowed = (originalAllowed) && (element == convertedElement);
/* 422:    */     }
/* 423:540 */     return originalAllowed ? original : convertedCopy;
/* 424:    */   }
/* 425:    */   
/* 426:    */   protected Map convertToTypedMap(Map original, String propertyName, Class requiredType, TypeDescriptor typeDescriptor)
/* 427:    */   {
/* 428:547 */     if (!Map.class.isAssignableFrom(requiredType)) {
/* 429:548 */       return original;
/* 430:    */     }
/* 431:551 */     boolean approximable = CollectionFactory.isApproximableMapType(requiredType);
/* 432:552 */     if ((!approximable) && (!canCreateCopy(requiredType)))
/* 433:    */     {
/* 434:553 */       if (logger.isDebugEnabled()) {
/* 435:554 */         logger.debug("Custom Map type [" + original.getClass().getName() + 
/* 436:555 */           "] does not allow for creating a copy - injecting original Map as-is");
/* 437:    */       }
/* 438:557 */       return original;
/* 439:    */     }
/* 440:560 */     boolean originalAllowed = requiredType.isInstance(original);
/* 441:561 */     typeDescriptor = typeDescriptor.narrow(original);
/* 442:562 */     TypeDescriptor keyType = typeDescriptor.getMapKeyTypeDescriptor();
/* 443:563 */     TypeDescriptor valueType = typeDescriptor.getMapValueTypeDescriptor();
/* 444:564 */     if ((keyType == null) && (valueType == null) && (originalAllowed) && 
/* 445:565 */       (!this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName))) {
/* 446:566 */       return original;
/* 447:    */     }
/* 448:    */     try
/* 449:    */     {
/* 450:571 */       Iterator it = original.entrySet().iterator();
/* 451:572 */       if (it == null)
/* 452:    */       {
/* 453:573 */         if (logger.isDebugEnabled()) {
/* 454:574 */           logger.debug("Map of type [" + original.getClass().getName() + 
/* 455:575 */             "] returned null Iterator - injecting original Map as-is");
/* 456:    */         }
/* 457:577 */         return original;
/* 458:    */       }
/* 459:    */     }
/* 460:    */     catch (Throwable ex)
/* 461:    */     {
/* 462:581 */       if (logger.isDebugEnabled()) {
/* 463:582 */         logger.debug("Cannot access Map of type [" + original.getClass().getName() + 
/* 464:583 */           "] - injecting original Map as-is: " + ex);
/* 465:    */       }
/* 466:585 */       return original;
/* 467:    */     }
/* 468:    */     Iterator it;
/* 469:    */     try
/* 470:    */     {
/* 471:    */       Map convertedCopy;
/* 472:590 */       if (approximable) {
/* 473:591 */         convertedCopy = CollectionFactory.createApproximateMap(original, original.size());
/* 474:    */       } else {
/* 475:594 */         convertedCopy = (Map)requiredType.newInstance();
/* 476:    */       }
/* 477:    */     }
/* 478:    */     catch (Throwable ex)
/* 479:    */     {
/* 480:    */       Map convertedCopy;
/* 481:598 */       if (logger.isDebugEnabled()) {
/* 482:599 */         logger.debug("Cannot create copy of Map type [" + original.getClass().getName() + 
/* 483:600 */           "] - injecting original Map as-is: " + ex);
/* 484:    */       }
/* 485:602 */       return original;
/* 486:    */     }
/* 487:    */     Map convertedCopy;
/* 488:605 */     while (it.hasNext())
/* 489:    */     {
/* 490:606 */       Map.Entry entry = (Map.Entry)it.next();
/* 491:607 */       Object key = entry.getKey();
/* 492:608 */       Object value = entry.getValue();
/* 493:609 */       String keyedPropertyName = buildKeyedPropertyName(propertyName, key);
/* 494:610 */       Object convertedKey = convertIfNecessary(keyedPropertyName, null, key, 
/* 495:611 */         keyType != null ? keyType.getType() : null, typeDescriptor.getMapKeyTypeDescriptor());
/* 496:612 */       Object convertedValue = convertIfNecessary(keyedPropertyName, null, value, 
/* 497:613 */         valueType != null ? valueType.getType() : null, typeDescriptor.getMapValueTypeDescriptor());
/* 498:    */       try
/* 499:    */       {
/* 500:615 */         convertedCopy.put(convertedKey, convertedValue);
/* 501:    */       }
/* 502:    */       catch (Throwable ex)
/* 503:    */       {
/* 504:618 */         if (logger.isDebugEnabled()) {
/* 505:619 */           logger.debug("Map type [" + original.getClass().getName() + 
/* 506:620 */             "] seems to be read-only - injecting original Map as-is: " + ex);
/* 507:    */         }
/* 508:622 */         return original;
/* 509:    */       }
/* 510:624 */       originalAllowed = (originalAllowed) && (key == convertedKey) && (value == convertedValue);
/* 511:    */     }
/* 512:626 */     return originalAllowed ? original : convertedCopy;
/* 513:    */   }
/* 514:    */   
/* 515:    */   private String buildIndexedPropertyName(String propertyName, int index)
/* 516:    */   {
/* 517:630 */     return propertyName != null ? 
/* 518:631 */       propertyName + "[" + index + "]" : 
/* 519:632 */       null;
/* 520:    */   }
/* 521:    */   
/* 522:    */   private String buildKeyedPropertyName(String propertyName, Object key)
/* 523:    */   {
/* 524:636 */     return propertyName != null ? 
/* 525:637 */       propertyName + "[" + key + "]" : 
/* 526:638 */       null;
/* 527:    */   }
/* 528:    */   
/* 529:    */   private boolean canCreateCopy(Class requiredType)
/* 530:    */   {
/* 531:643 */     return (!requiredType.isInterface()) && (!Modifier.isAbstract(requiredType.getModifiers())) && (Modifier.isPublic(requiredType.getModifiers())) && (ClassUtils.hasConstructor(requiredType, new Class[0]));
/* 532:    */   }
/* 533:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.TypeConverterDelegate
 * JD-Core Version:    0.7.0.1
 */