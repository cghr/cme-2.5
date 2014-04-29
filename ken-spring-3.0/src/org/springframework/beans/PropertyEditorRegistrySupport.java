/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.math.BigDecimal;
/*   7:    */ import java.math.BigInteger;
/*   8:    */ import java.net.URI;
/*   9:    */ import java.net.URL;
/*  10:    */ import java.nio.charset.Charset;
/*  11:    */ import java.util.Collection;
/*  12:    */ import java.util.Currency;
/*  13:    */ import java.util.HashMap;
/*  14:    */ import java.util.HashSet;
/*  15:    */ import java.util.Iterator;
/*  16:    */ import java.util.LinkedHashMap;
/*  17:    */ import java.util.LinkedList;
/*  18:    */ import java.util.List;
/*  19:    */ import java.util.Locale;
/*  20:    */ import java.util.Map;
/*  21:    */ import java.util.Map.Entry;
/*  22:    */ import java.util.Properties;
/*  23:    */ import java.util.Set;
/*  24:    */ import java.util.SortedMap;
/*  25:    */ import java.util.SortedSet;
/*  26:    */ import java.util.TimeZone;
/*  27:    */ import java.util.UUID;
/*  28:    */ import java.util.regex.Pattern;
/*  29:    */ import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
/*  30:    */ import org.springframework.beans.propertyeditors.CharArrayPropertyEditor;
/*  31:    */ import org.springframework.beans.propertyeditors.CharacterEditor;
/*  32:    */ import org.springframework.beans.propertyeditors.CharsetEditor;
/*  33:    */ import org.springframework.beans.propertyeditors.ClassArrayEditor;
/*  34:    */ import org.springframework.beans.propertyeditors.ClassEditor;
/*  35:    */ import org.springframework.beans.propertyeditors.CurrencyEditor;
/*  36:    */ import org.springframework.beans.propertyeditors.CustomBooleanEditor;
/*  37:    */ import org.springframework.beans.propertyeditors.CustomCollectionEditor;
/*  38:    */ import org.springframework.beans.propertyeditors.CustomMapEditor;
/*  39:    */ import org.springframework.beans.propertyeditors.CustomNumberEditor;
/*  40:    */ import org.springframework.beans.propertyeditors.FileEditor;
/*  41:    */ import org.springframework.beans.propertyeditors.InputSourceEditor;
/*  42:    */ import org.springframework.beans.propertyeditors.InputStreamEditor;
/*  43:    */ import org.springframework.beans.propertyeditors.LocaleEditor;
/*  44:    */ import org.springframework.beans.propertyeditors.PatternEditor;
/*  45:    */ import org.springframework.beans.propertyeditors.PropertiesEditor;
/*  46:    */ import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
/*  47:    */ import org.springframework.beans.propertyeditors.TimeZoneEditor;
/*  48:    */ import org.springframework.beans.propertyeditors.URIEditor;
/*  49:    */ import org.springframework.beans.propertyeditors.URLEditor;
/*  50:    */ import org.springframework.beans.propertyeditors.UUIDEditor;
/*  51:    */ import org.springframework.core.convert.ConversionService;
/*  52:    */ import org.springframework.core.io.Resource;
/*  53:    */ import org.springframework.core.io.support.ResourceArrayPropertyEditor;
/*  54:    */ import org.springframework.util.ClassUtils;
/*  55:    */ import org.xml.sax.InputSource;
/*  56:    */ 
/*  57:    */ public class PropertyEditorRegistrySupport
/*  58:    */   implements PropertyEditorRegistry
/*  59:    */ {
/*  60:    */   private ConversionService conversionService;
/*  61: 90 */   private boolean defaultEditorsActive = false;
/*  62: 92 */   private boolean configValueEditorsActive = false;
/*  63:    */   private Map<Class<?>, PropertyEditor> defaultEditors;
/*  64:    */   private Map<Class<?>, PropertyEditor> overriddenDefaultEditors;
/*  65:    */   private Map<Class<?>, PropertyEditor> customEditors;
/*  66:    */   private Map<String, CustomEditorHolder> customEditorsForPath;
/*  67:    */   private Set<PropertyEditor> sharedEditors;
/*  68:    */   private Map<Class<?>, PropertyEditor> customEditorCache;
/*  69:    */   
/*  70:    */   public void setConversionService(ConversionService conversionService)
/*  71:    */   {
/*  72:112 */     this.conversionService = conversionService;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public ConversionService getConversionService()
/*  76:    */   {
/*  77:119 */     return this.conversionService;
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected void registerDefaultEditors()
/*  81:    */   {
/*  82:132 */     this.defaultEditorsActive = true;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void useConfigValueEditors()
/*  86:    */   {
/*  87:143 */     this.configValueEditorsActive = true;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void overrideDefaultEditor(Class<?> requiredType, PropertyEditor propertyEditor)
/*  91:    */   {
/*  92:156 */     if (this.overriddenDefaultEditors == null) {
/*  93:157 */       this.overriddenDefaultEditors = new HashMap();
/*  94:    */     }
/*  95:159 */     this.overriddenDefaultEditors.put(requiredType, propertyEditor);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public PropertyEditor getDefaultEditor(Class<?> requiredType)
/*  99:    */   {
/* 100:170 */     if (!this.defaultEditorsActive) {
/* 101:171 */       return null;
/* 102:    */     }
/* 103:173 */     if (this.overriddenDefaultEditors != null)
/* 104:    */     {
/* 105:174 */       PropertyEditor editor = (PropertyEditor)this.overriddenDefaultEditors.get(requiredType);
/* 106:175 */       if (editor != null) {
/* 107:176 */         return editor;
/* 108:    */       }
/* 109:    */     }
/* 110:179 */     if (this.defaultEditors == null) {
/* 111:180 */       createDefaultEditors();
/* 112:    */     }
/* 113:182 */     return (PropertyEditor)this.defaultEditors.get(requiredType);
/* 114:    */   }
/* 115:    */   
/* 116:    */   private void createDefaultEditors()
/* 117:    */   {
/* 118:189 */     this.defaultEditors = new HashMap(64);
/* 119:    */     
/* 120:    */ 
/* 121:    */ 
/* 122:193 */     this.defaultEditors.put(Charset.class, new CharsetEditor());
/* 123:194 */     this.defaultEditors.put(Class.class, new ClassEditor());
/* 124:195 */     this.defaultEditors.put([Ljava.lang.Class.class, new ClassArrayEditor());
/* 125:196 */     this.defaultEditors.put(Currency.class, new CurrencyEditor());
/* 126:197 */     this.defaultEditors.put(File.class, new FileEditor());
/* 127:198 */     this.defaultEditors.put(InputStream.class, new InputStreamEditor());
/* 128:199 */     this.defaultEditors.put(InputSource.class, new InputSourceEditor());
/* 129:200 */     this.defaultEditors.put(Locale.class, new LocaleEditor());
/* 130:201 */     this.defaultEditors.put(Pattern.class, new PatternEditor());
/* 131:202 */     this.defaultEditors.put(Properties.class, new PropertiesEditor());
/* 132:203 */     this.defaultEditors.put([Lorg.springframework.core.io.Resource.class, new ResourceArrayPropertyEditor());
/* 133:204 */     this.defaultEditors.put(TimeZone.class, new TimeZoneEditor());
/* 134:205 */     this.defaultEditors.put(URI.class, new URIEditor());
/* 135:206 */     this.defaultEditors.put(URL.class, new URLEditor());
/* 136:207 */     this.defaultEditors.put(UUID.class, new UUIDEditor());
/* 137:    */     
/* 138:    */ 
/* 139:    */ 
/* 140:211 */     this.defaultEditors.put(Collection.class, new CustomCollectionEditor(Collection.class));
/* 141:212 */     this.defaultEditors.put(Set.class, new CustomCollectionEditor(Set.class));
/* 142:213 */     this.defaultEditors.put(SortedSet.class, new CustomCollectionEditor(SortedSet.class));
/* 143:214 */     this.defaultEditors.put(List.class, new CustomCollectionEditor(List.class));
/* 144:215 */     this.defaultEditors.put(SortedMap.class, new CustomMapEditor(SortedMap.class));
/* 145:    */     
/* 146:    */ 
/* 147:218 */     this.defaultEditors.put([B.class, new ByteArrayPropertyEditor());
/* 148:219 */     this.defaultEditors.put([C.class, new CharArrayPropertyEditor());
/* 149:    */     
/* 150:    */ 
/* 151:222 */     this.defaultEditors.put(Character.TYPE, new CharacterEditor(false));
/* 152:223 */     this.defaultEditors.put(Character.class, new CharacterEditor(true));
/* 153:    */     
/* 154:    */ 
/* 155:226 */     this.defaultEditors.put(Boolean.TYPE, new CustomBooleanEditor(false));
/* 156:227 */     this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));
/* 157:    */     
/* 158:    */ 
/* 159:    */ 
/* 160:231 */     this.defaultEditors.put(Byte.TYPE, new CustomNumberEditor(Byte.class, false));
/* 161:232 */     this.defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, true));
/* 162:233 */     this.defaultEditors.put(Short.TYPE, new CustomNumberEditor(Short.class, false));
/* 163:234 */     this.defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, true));
/* 164:235 */     this.defaultEditors.put(Integer.TYPE, new CustomNumberEditor(Integer.class, false));
/* 165:236 */     this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
/* 166:237 */     this.defaultEditors.put(Long.TYPE, new CustomNumberEditor(Long.class, false));
/* 167:238 */     this.defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, true));
/* 168:239 */     this.defaultEditors.put(Float.TYPE, new CustomNumberEditor(Float.class, false));
/* 169:240 */     this.defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
/* 170:241 */     this.defaultEditors.put(Double.TYPE, new CustomNumberEditor(Double.class, false));
/* 171:242 */     this.defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
/* 172:243 */     this.defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
/* 173:244 */     this.defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
/* 174:247 */     if (this.configValueEditorsActive)
/* 175:    */     {
/* 176:248 */       StringArrayPropertyEditor sae = new StringArrayPropertyEditor();
/* 177:249 */       this.defaultEditors.put([Ljava.lang.String.class, sae);
/* 178:250 */       this.defaultEditors.put([S.class, sae);
/* 179:251 */       this.defaultEditors.put([I.class, sae);
/* 180:252 */       this.defaultEditors.put([J.class, sae);
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   protected void copyDefaultEditorsTo(PropertyEditorRegistrySupport target)
/* 185:    */   {
/* 186:261 */     target.defaultEditorsActive = this.defaultEditorsActive;
/* 187:262 */     target.configValueEditorsActive = this.configValueEditorsActive;
/* 188:263 */     target.defaultEditors = this.defaultEditors;
/* 189:264 */     target.overriddenDefaultEditors = this.overriddenDefaultEditors;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor)
/* 193:    */   {
/* 194:273 */     registerCustomEditor(requiredType, null, propertyEditor);
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void registerCustomEditor(Class<?> requiredType, String propertyPath, PropertyEditor propertyEditor)
/* 198:    */   {
/* 199:277 */     if ((requiredType == null) && (propertyPath == null)) {
/* 200:278 */       throw new IllegalArgumentException("Either requiredType or propertyPath is required");
/* 201:    */     }
/* 202:280 */     if (propertyPath != null)
/* 203:    */     {
/* 204:281 */       if (this.customEditorsForPath == null) {
/* 205:282 */         this.customEditorsForPath = new LinkedHashMap(16);
/* 206:    */       }
/* 207:284 */       this.customEditorsForPath.put(propertyPath, new CustomEditorHolder(propertyEditor, requiredType, null));
/* 208:    */     }
/* 209:    */     else
/* 210:    */     {
/* 211:287 */       if (this.customEditors == null) {
/* 212:288 */         this.customEditors = new LinkedHashMap(16);
/* 213:    */       }
/* 214:290 */       this.customEditors.put(requiredType, propertyEditor);
/* 215:291 */       this.customEditorCache = null;
/* 216:    */     }
/* 217:    */   }
/* 218:    */   
/* 219:    */   @Deprecated
/* 220:    */   public void registerSharedEditor(Class<?> requiredType, PropertyEditor propertyEditor)
/* 221:    */   {
/* 222:305 */     registerCustomEditor(requiredType, null, propertyEditor);
/* 223:306 */     if (this.sharedEditors == null) {
/* 224:307 */       this.sharedEditors = new HashSet();
/* 225:    */     }
/* 226:309 */     this.sharedEditors.add(propertyEditor);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public boolean isSharedEditor(PropertyEditor propertyEditor)
/* 230:    */   {
/* 231:319 */     return (this.sharedEditors != null) && (this.sharedEditors.contains(propertyEditor));
/* 232:    */   }
/* 233:    */   
/* 234:    */   public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath)
/* 235:    */   {
/* 236:323 */     Class<?> requiredTypeToUse = requiredType;
/* 237:324 */     if (propertyPath != null)
/* 238:    */     {
/* 239:325 */       if (this.customEditorsForPath != null)
/* 240:    */       {
/* 241:327 */         PropertyEditor editor = getCustomEditor(propertyPath, requiredType);
/* 242:328 */         if (editor == null)
/* 243:    */         {
/* 244:329 */           List<String> strippedPaths = new LinkedList();
/* 245:330 */           addStrippedPropertyPaths(strippedPaths, "", propertyPath);
/* 246:331 */           for (Iterator<String> it = strippedPaths.iterator(); (it.hasNext()) && (editor == null);)
/* 247:    */           {
/* 248:332 */             String strippedPath = (String)it.next();
/* 249:333 */             editor = getCustomEditor(strippedPath, requiredType);
/* 250:    */           }
/* 251:    */         }
/* 252:336 */         if (editor != null) {
/* 253:337 */           return editor;
/* 254:    */         }
/* 255:    */       }
/* 256:340 */       if (requiredType == null) {
/* 257:341 */         requiredTypeToUse = getPropertyType(propertyPath);
/* 258:    */       }
/* 259:    */     }
/* 260:345 */     return getCustomEditor(requiredTypeToUse);
/* 261:    */   }
/* 262:    */   
/* 263:    */   public boolean hasCustomEditorForElement(Class<?> elementType, String propertyPath)
/* 264:    */   {
/* 265:358 */     if ((propertyPath != null) && (this.customEditorsForPath != null)) {
/* 266:359 */       for (Map.Entry<String, CustomEditorHolder> entry : this.customEditorsForPath.entrySet()) {
/* 267:360 */         if ((PropertyAccessorUtils.matchesProperty((String)entry.getKey(), propertyPath)) && 
/* 268:361 */           (((CustomEditorHolder)entry.getValue()).getPropertyEditor(elementType) != null)) {
/* 269:362 */           return true;
/* 270:    */         }
/* 271:    */       }
/* 272:    */     }
/* 273:368 */     return (elementType != null) && (this.customEditors != null) && (this.customEditors.containsKey(elementType));
/* 274:    */   }
/* 275:    */   
/* 276:    */   protected Class<?> getPropertyType(String propertyPath)
/* 277:    */   {
/* 278:383 */     return null;
/* 279:    */   }
/* 280:    */   
/* 281:    */   private PropertyEditor getCustomEditor(String propertyName, Class<?> requiredType)
/* 282:    */   {
/* 283:393 */     CustomEditorHolder holder = (CustomEditorHolder)this.customEditorsForPath.get(propertyName);
/* 284:394 */     return holder != null ? holder.getPropertyEditor(requiredType) : null;
/* 285:    */   }
/* 286:    */   
/* 287:    */   private PropertyEditor getCustomEditor(Class<?> requiredType)
/* 288:    */   {
/* 289:406 */     if ((requiredType == null) || (this.customEditors == null)) {
/* 290:407 */       return null;
/* 291:    */     }
/* 292:410 */     PropertyEditor editor = (PropertyEditor)this.customEditors.get(requiredType);
/* 293:411 */     if (editor == null)
/* 294:    */     {
/* 295:413 */       if (this.customEditorCache != null) {
/* 296:414 */         editor = (PropertyEditor)this.customEditorCache.get(requiredType);
/* 297:    */       }
/* 298:416 */       if (editor == null) {
/* 299:418 */         for (Iterator<Class<?>> it = this.customEditors.keySet().iterator(); (it.hasNext()) && (editor == null);)
/* 300:    */         {
/* 301:419 */           Class<?> key = (Class)it.next();
/* 302:420 */           if (key.isAssignableFrom(requiredType))
/* 303:    */           {
/* 304:421 */             editor = (PropertyEditor)this.customEditors.get(key);
/* 305:424 */             if (this.customEditorCache == null) {
/* 306:425 */               this.customEditorCache = new HashMap();
/* 307:    */             }
/* 308:427 */             this.customEditorCache.put(requiredType, editor);
/* 309:    */           }
/* 310:    */         }
/* 311:    */       }
/* 312:    */     }
/* 313:432 */     return editor;
/* 314:    */   }
/* 315:    */   
/* 316:    */   protected Class<?> guessPropertyTypeFromEditors(String propertyName)
/* 317:    */   {
/* 318:442 */     if (this.customEditorsForPath != null)
/* 319:    */     {
/* 320:443 */       CustomEditorHolder editorHolder = (CustomEditorHolder)this.customEditorsForPath.get(propertyName);
/* 321:444 */       if (editorHolder == null)
/* 322:    */       {
/* 323:445 */         List<String> strippedPaths = new LinkedList();
/* 324:446 */         addStrippedPropertyPaths(strippedPaths, "", propertyName);
/* 325:447 */         for (Iterator<String> it = strippedPaths.iterator(); (it.hasNext()) && (editorHolder == null);)
/* 326:    */         {
/* 327:448 */           String strippedName = (String)it.next();
/* 328:449 */           editorHolder = (CustomEditorHolder)this.customEditorsForPath.get(strippedName);
/* 329:    */         }
/* 330:    */       }
/* 331:452 */       if (editorHolder != null) {
/* 332:453 */         return CustomEditorHolder.access$2(editorHolder);
/* 333:    */       }
/* 334:    */     }
/* 335:456 */     return null;
/* 336:    */   }
/* 337:    */   
/* 338:    */   protected void copyCustomEditorsTo(PropertyEditorRegistry target, String nestedProperty)
/* 339:    */   {
/* 340:467 */     String actualPropertyName = 
/* 341:468 */       nestedProperty != null ? PropertyAccessorUtils.getPropertyName(nestedProperty) : null;
/* 342:469 */     if (this.customEditors != null) {
/* 343:470 */       for (Map.Entry<Class<?>, PropertyEditor> entry : this.customEditors.entrySet()) {
/* 344:471 */         target.registerCustomEditor((Class)entry.getKey(), (PropertyEditor)entry.getValue());
/* 345:    */       }
/* 346:    */     }
/* 347:474 */     if (this.customEditorsForPath != null) {
/* 348:475 */       for (Map.Entry<String, CustomEditorHolder> entry : this.customEditorsForPath.entrySet())
/* 349:    */       {
/* 350:476 */         String editorPath = (String)entry.getKey();
/* 351:477 */         CustomEditorHolder editorHolder = (CustomEditorHolder)entry.getValue();
/* 352:478 */         if (nestedProperty != null)
/* 353:    */         {
/* 354:479 */           int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(editorPath);
/* 355:480 */           if (pos != -1)
/* 356:    */           {
/* 357:481 */             String editorNestedProperty = editorPath.substring(0, pos);
/* 358:482 */             String editorNestedPath = editorPath.substring(pos + 1);
/* 359:483 */             if ((editorNestedProperty.equals(nestedProperty)) || (editorNestedProperty.equals(actualPropertyName))) {
/* 360:484 */               target.registerCustomEditor(
/* 361:485 */                 CustomEditorHolder.access$2(editorHolder), editorNestedPath, editorHolder.getPropertyEditor());
/* 362:    */             }
/* 363:    */           }
/* 364:    */         }
/* 365:    */         else
/* 366:    */         {
/* 367:490 */           target.registerCustomEditor(
/* 368:491 */             CustomEditorHolder.access$2(editorHolder), editorPath, editorHolder.getPropertyEditor());
/* 369:    */         }
/* 370:    */       }
/* 371:    */     }
/* 372:    */   }
/* 373:    */   
/* 374:    */   private void addStrippedPropertyPaths(List<String> strippedPaths, String nestedPath, String propertyPath)
/* 375:    */   {
/* 376:506 */     int startIndex = propertyPath.indexOf('[');
/* 377:507 */     if (startIndex != -1)
/* 378:    */     {
/* 379:508 */       int endIndex = propertyPath.indexOf(']');
/* 380:509 */       if (endIndex != -1)
/* 381:    */       {
/* 382:510 */         String prefix = propertyPath.substring(0, startIndex);
/* 383:511 */         String key = propertyPath.substring(startIndex, endIndex + 1);
/* 384:512 */         String suffix = propertyPath.substring(endIndex + 1, propertyPath.length());
/* 385:    */         
/* 386:514 */         strippedPaths.add(nestedPath + prefix + suffix);
/* 387:    */         
/* 388:516 */         addStrippedPropertyPaths(strippedPaths, nestedPath + prefix, suffix);
/* 389:    */         
/* 390:518 */         addStrippedPropertyPaths(strippedPaths, nestedPath + prefix + key, suffix);
/* 391:    */       }
/* 392:    */     }
/* 393:    */   }
/* 394:    */   
/* 395:    */   private static class CustomEditorHolder
/* 396:    */   {
/* 397:    */     private final PropertyEditor propertyEditor;
/* 398:    */     private final Class<?> registeredType;
/* 399:    */     
/* 400:    */     private CustomEditorHolder(PropertyEditor propertyEditor, Class<?> registeredType)
/* 401:    */     {
/* 402:535 */       this.propertyEditor = propertyEditor;
/* 403:536 */       this.registeredType = registeredType;
/* 404:    */     }
/* 405:    */     
/* 406:    */     private PropertyEditor getPropertyEditor()
/* 407:    */     {
/* 408:540 */       return this.propertyEditor;
/* 409:    */     }
/* 410:    */     
/* 411:    */     private Class<?> getRegisteredType()
/* 412:    */     {
/* 413:544 */       return this.registeredType;
/* 414:    */     }
/* 415:    */     
/* 416:    */     private PropertyEditor getPropertyEditor(Class<?> requiredType)
/* 417:    */     {
/* 418:554 */       if ((this.registeredType == null) || 
/* 419:555 */         ((requiredType != null) && (
/* 420:556 */         (ClassUtils.isAssignable(this.registeredType, requiredType)) || 
/* 421:557 */         (ClassUtils.isAssignable(requiredType, this.registeredType)))) || (
/* 422:558 */         (requiredType == null) && 
/* 423:559 */         (!Collection.class.isAssignableFrom(this.registeredType)) && (!this.registeredType.isArray()))) {
/* 424:560 */         return this.propertyEditor;
/* 425:    */       }
/* 426:563 */       return null;
/* 427:    */     }
/* 428:    */   }
/* 429:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyEditorRegistrySupport
 * JD-Core Version:    0.7.0.1
 */