/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.InputStreamReader;
/*   6:    */ import java.text.MessageFormat;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Locale;
/*  11:    */ import java.util.Map;
/*  12:    */ import java.util.Properties;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.springframework.context.ResourceLoaderAware;
/*  15:    */ import org.springframework.core.io.DefaultResourceLoader;
/*  16:    */ import org.springframework.core.io.Resource;
/*  17:    */ import org.springframework.core.io.ResourceLoader;
/*  18:    */ import org.springframework.util.Assert;
/*  19:    */ import org.springframework.util.DefaultPropertiesPersister;
/*  20:    */ import org.springframework.util.PropertiesPersister;
/*  21:    */ import org.springframework.util.StringUtils;
/*  22:    */ 
/*  23:    */ public class ReloadableResourceBundleMessageSource
/*  24:    */   extends AbstractMessageSource
/*  25:    */   implements ResourceLoaderAware
/*  26:    */ {
/*  27:    */   private static final String PROPERTIES_SUFFIX = ".properties";
/*  28:    */   private static final String XML_SUFFIX = ".xml";
/*  29: 99 */   private String[] basenames = new String[0];
/*  30:    */   private String defaultEncoding;
/*  31:    */   private Properties fileEncodings;
/*  32:105 */   private boolean fallbackToSystemLocale = true;
/*  33:107 */   private long cacheMillis = -1L;
/*  34:109 */   private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
/*  35:111 */   private ResourceLoader resourceLoader = new DefaultResourceLoader();
/*  36:115 */   private final Map<String, Map<Locale, List<String>>> cachedFilenames = new HashMap();
/*  37:118 */   private final Map<String, PropertiesHolder> cachedProperties = new HashMap();
/*  38:121 */   private final Map<Locale, PropertiesHolder> cachedMergedProperties = new HashMap();
/*  39:    */   
/*  40:    */   public void setBasename(String basename)
/*  41:    */   {
/*  42:138 */     setBasenames(new String[] { basename });
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setBasenames(String[] basenames)
/*  46:    */   {
/*  47:157 */     if (basenames != null)
/*  48:    */     {
/*  49:158 */       this.basenames = new String[basenames.length];
/*  50:159 */       for (int i = 0; i < basenames.length; i++)
/*  51:    */       {
/*  52:160 */         String basename = basenames[i];
/*  53:161 */         Assert.hasText(basename, "Basename must not be empty");
/*  54:162 */         this.basenames[i] = basename.trim();
/*  55:    */       }
/*  56:    */     }
/*  57:    */     else
/*  58:    */     {
/*  59:166 */       this.basenames = new String[0];
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setDefaultEncoding(String defaultEncoding)
/*  64:    */   {
/*  65:181 */     this.defaultEncoding = defaultEncoding;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setFileEncodings(Properties fileEncodings)
/*  69:    */   {
/*  70:195 */     this.fileEncodings = fileEncodings;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setFallbackToSystemLocale(boolean fallbackToSystemLocale)
/*  74:    */   {
/*  75:210 */     this.fallbackToSystemLocale = fallbackToSystemLocale;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setCacheSeconds(int cacheSeconds)
/*  79:    */   {
/*  80:228 */     this.cacheMillis = (cacheSeconds * 1000);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setPropertiesPersister(PropertiesPersister propertiesPersister)
/*  84:    */   {
/*  85:237 */     this.propertiesPersister = 
/*  86:238 */       (propertiesPersister != null ? propertiesPersister : new DefaultPropertiesPersister());
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setResourceLoader(ResourceLoader resourceLoader)
/*  90:    */   {
/*  91:251 */     this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected String resolveCodeWithoutArguments(String code, Locale locale)
/*  95:    */   {
/*  96:    */     String result;
/*  97:261 */     if (this.cacheMillis < 0L)
/*  98:    */     {
/*  99:262 */       PropertiesHolder propHolder = getMergedProperties(locale);
/* 100:263 */       result = propHolder.getProperty(code);
/* 101:264 */       if (result != null) {
/* 102:265 */         return result;
/* 103:    */       }
/* 104:    */     }
/* 105:    */     else
/* 106:    */     {
/* 107:    */       String[] arrayOfString;
/* 108:269 */       String str1 = (arrayOfString = this.basenames).length;
/* 109:269 */       for (result = 0; result < str1; result++)
/* 110:    */       {
/* 111:269 */         String basename = arrayOfString[result];
/* 112:270 */         List<String> filenames = calculateAllFilenames(basename, locale);
/* 113:271 */         for (String filename : filenames)
/* 114:    */         {
/* 115:272 */           PropertiesHolder propHolder = getProperties(filename);
/* 116:273 */           String result = propHolder.getProperty(code);
/* 117:274 */           if (result != null) {
/* 118:275 */             return result;
/* 119:    */           }
/* 120:    */         }
/* 121:    */       }
/* 122:    */     }
/* 123:280 */     return null;
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected MessageFormat resolveCode(String code, Locale locale)
/* 127:    */   {
/* 128:    */     MessageFormat result;
/* 129:289 */     if (this.cacheMillis < 0L)
/* 130:    */     {
/* 131:290 */       PropertiesHolder propHolder = getMergedProperties(locale);
/* 132:291 */       result = propHolder.getMessageFormat(code, locale);
/* 133:292 */       if (result != null) {
/* 134:293 */         return result;
/* 135:    */       }
/* 136:    */     }
/* 137:    */     else
/* 138:    */     {
/* 139:    */       String[] arrayOfString;
/* 140:297 */       MessageFormat localMessageFormat1 = (arrayOfString = this.basenames).length;
/* 141:297 */       for (result = 0; result < localMessageFormat1; result++)
/* 142:    */       {
/* 143:297 */         String basename = arrayOfString[result];
/* 144:298 */         List<String> filenames = calculateAllFilenames(basename, locale);
/* 145:299 */         for (String filename : filenames)
/* 146:    */         {
/* 147:300 */           PropertiesHolder propHolder = getProperties(filename);
/* 148:301 */           MessageFormat result = propHolder.getMessageFormat(code, locale);
/* 149:302 */           if (result != null) {
/* 150:303 */             return result;
/* 151:    */           }
/* 152:    */         }
/* 153:    */       }
/* 154:    */     }
/* 155:308 */     return null;
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected PropertiesHolder getMergedProperties(Locale locale)
/* 159:    */   {
/* 160:321 */     synchronized (this.cachedMergedProperties)
/* 161:    */     {
/* 162:322 */       PropertiesHolder mergedHolder = (PropertiesHolder)this.cachedMergedProperties.get(locale);
/* 163:323 */       if (mergedHolder != null) {
/* 164:324 */         return mergedHolder;
/* 165:    */       }
/* 166:326 */       Properties mergedProps = new Properties();
/* 167:327 */       mergedHolder = new PropertiesHolder(mergedProps, -1L);
/* 168:328 */       for (int i = this.basenames.length - 1; i >= 0; i--)
/* 169:    */       {
/* 170:329 */         List filenames = calculateAllFilenames(this.basenames[i], locale);
/* 171:330 */         for (int j = filenames.size() - 1; j >= 0; j--)
/* 172:    */         {
/* 173:331 */           String filename = (String)filenames.get(j);
/* 174:332 */           PropertiesHolder propHolder = getProperties(filename);
/* 175:333 */           if (propHolder.getProperties() != null) {
/* 176:334 */             mergedProps.putAll(propHolder.getProperties());
/* 177:    */           }
/* 178:    */         }
/* 179:    */       }
/* 180:338 */       this.cachedMergedProperties.put(locale, mergedHolder);
/* 181:339 */       return mergedHolder;
/* 182:    */     }
/* 183:    */   }
/* 184:    */   
/* 185:    */   protected List<String> calculateAllFilenames(String basename, Locale locale)
/* 186:    */   {
/* 187:354 */     synchronized (this.cachedFilenames)
/* 188:    */     {
/* 189:355 */       Map<Locale, List<String>> localeMap = (Map)this.cachedFilenames.get(basename);
/* 190:356 */       if (localeMap != null)
/* 191:    */       {
/* 192:357 */         List<String> filenames = (List)localeMap.get(locale);
/* 193:358 */         if (filenames != null) {
/* 194:359 */           return filenames;
/* 195:    */         }
/* 196:    */       }
/* 197:362 */       List<String> filenames = new ArrayList(7);
/* 198:363 */       filenames.addAll(calculateFilenamesForLocale(basename, locale));
/* 199:364 */       if ((this.fallbackToSystemLocale) && (!locale.equals(Locale.getDefault())))
/* 200:    */       {
/* 201:365 */         List<String> fallbackFilenames = calculateFilenamesForLocale(basename, Locale.getDefault());
/* 202:366 */         for (String fallbackFilename : fallbackFilenames) {
/* 203:367 */           if (!filenames.contains(fallbackFilename)) {
/* 204:369 */             filenames.add(fallbackFilename);
/* 205:    */           }
/* 206:    */         }
/* 207:    */       }
/* 208:373 */       filenames.add(basename);
/* 209:374 */       if (localeMap != null)
/* 210:    */       {
/* 211:375 */         localeMap.put(locale, filenames);
/* 212:    */       }
/* 213:    */       else
/* 214:    */       {
/* 215:378 */         localeMap = new HashMap();
/* 216:379 */         localeMap.put(locale, filenames);
/* 217:380 */         this.cachedFilenames.put(basename, localeMap);
/* 218:    */       }
/* 219:382 */       return filenames;
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   protected List<String> calculateFilenamesForLocale(String basename, Locale locale)
/* 224:    */   {
/* 225:397 */     List<String> result = new ArrayList(3);
/* 226:398 */     String language = locale.getLanguage();
/* 227:399 */     String country = locale.getCountry();
/* 228:400 */     String variant = locale.getVariant();
/* 229:401 */     StringBuilder temp = new StringBuilder(basename);
/* 230:    */     
/* 231:403 */     temp.append('_');
/* 232:404 */     if (language.length() > 0)
/* 233:    */     {
/* 234:405 */       temp.append(language);
/* 235:406 */       result.add(0, temp.toString());
/* 236:    */     }
/* 237:409 */     temp.append('_');
/* 238:410 */     if (country.length() > 0)
/* 239:    */     {
/* 240:411 */       temp.append(country);
/* 241:412 */       result.add(0, temp.toString());
/* 242:    */     }
/* 243:415 */     if ((variant.length() > 0) && ((language.length() > 0) || (country.length() > 0)))
/* 244:    */     {
/* 245:416 */       temp.append('_').append(variant);
/* 246:417 */       result.add(0, temp.toString());
/* 247:    */     }
/* 248:420 */     return result;
/* 249:    */   }
/* 250:    */   
/* 251:    */   protected PropertiesHolder getProperties(String filename)
/* 252:    */   {
/* 253:431 */     synchronized (this.cachedProperties)
/* 254:    */     {
/* 255:432 */       PropertiesHolder propHolder = (PropertiesHolder)this.cachedProperties.get(filename);
/* 256:433 */       if ((propHolder != null) && (
/* 257:434 */         (propHolder.getRefreshTimestamp() < 0L) || 
/* 258:435 */         (propHolder.getRefreshTimestamp() > System.currentTimeMillis() - this.cacheMillis))) {
/* 259:437 */         return propHolder;
/* 260:    */       }
/* 261:439 */       return refreshProperties(filename, propHolder);
/* 262:    */     }
/* 263:    */   }
/* 264:    */   
/* 265:    */   protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder)
/* 266:    */   {
/* 267:451 */     long refreshTimestamp = this.cacheMillis < 0L ? -1L : System.currentTimeMillis();
/* 268:    */     
/* 269:453 */     Resource resource = this.resourceLoader.getResource(filename + ".properties");
/* 270:454 */     if (!resource.exists()) {
/* 271:455 */       resource = this.resourceLoader.getResource(filename + ".xml");
/* 272:    */     }
/* 273:458 */     if (resource.exists())
/* 274:    */     {
/* 275:459 */       long fileTimestamp = -1L;
/* 276:460 */       if (this.cacheMillis >= 0L) {
/* 277:    */         try
/* 278:    */         {
/* 279:463 */           fileTimestamp = resource.lastModified();
/* 280:464 */           if ((propHolder != null) && (propHolder.getFileTimestamp() == fileTimestamp))
/* 281:    */           {
/* 282:465 */             if (this.logger.isDebugEnabled()) {
/* 283:466 */               this.logger.debug("Re-caching properties for filename [" + filename + "] - file hasn't been modified");
/* 284:    */             }
/* 285:468 */             propHolder.setRefreshTimestamp(refreshTimestamp);
/* 286:469 */             return propHolder;
/* 287:    */           }
/* 288:    */         }
/* 289:    */         catch (IOException ex)
/* 290:    */         {
/* 291:474 */           if (this.logger.isDebugEnabled()) {
/* 292:475 */             this.logger.debug(
/* 293:476 */               resource + " could not be resolved in the file system - assuming that is hasn't changed", ex);
/* 294:    */           }
/* 295:478 */           fileTimestamp = -1L;
/* 296:    */         }
/* 297:    */       }
/* 298:    */       try
/* 299:    */       {
/* 300:482 */         Properties props = loadProperties(resource, filename);
/* 301:483 */         propHolder = new PropertiesHolder(props, fileTimestamp);
/* 302:    */       }
/* 303:    */       catch (IOException ex)
/* 304:    */       {
/* 305:486 */         if (this.logger.isWarnEnabled()) {
/* 306:487 */           this.logger.warn("Could not parse properties file [" + resource.getFilename() + "]", ex);
/* 307:    */         }
/* 308:490 */         propHolder = new PropertiesHolder();
/* 309:    */       }
/* 310:    */     }
/* 311:    */     else
/* 312:    */     {
/* 313:496 */       if (this.logger.isDebugEnabled()) {
/* 314:497 */         this.logger.debug("No properties file found for [" + filename + "] - neither plain properties nor XML");
/* 315:    */       }
/* 316:500 */       propHolder = new PropertiesHolder();
/* 317:    */     }
/* 318:503 */     propHolder.setRefreshTimestamp(refreshTimestamp);
/* 319:504 */     this.cachedProperties.put(filename, propHolder);
/* 320:505 */     return propHolder;
/* 321:    */   }
/* 322:    */   
/* 323:    */   protected Properties loadProperties(Resource resource, String filename)
/* 324:    */     throws IOException
/* 325:    */   {
/* 326:516 */     InputStream is = resource.getInputStream();
/* 327:517 */     Properties props = new Properties();
/* 328:    */     try
/* 329:    */     {
/* 330:519 */       if (resource.getFilename().endsWith(".xml"))
/* 331:    */       {
/* 332:520 */         if (this.logger.isDebugEnabled()) {
/* 333:521 */           this.logger.debug("Loading properties [" + resource.getFilename() + "]");
/* 334:    */         }
/* 335:523 */         this.propertiesPersister.loadFromXml(props, is);
/* 336:    */       }
/* 337:    */       else
/* 338:    */       {
/* 339:526 */         String encoding = null;
/* 340:527 */         if (this.fileEncodings != null) {
/* 341:528 */           encoding = this.fileEncodings.getProperty(filename);
/* 342:    */         }
/* 343:530 */         if (encoding == null) {
/* 344:531 */           encoding = this.defaultEncoding;
/* 345:    */         }
/* 346:533 */         if (encoding != null)
/* 347:    */         {
/* 348:534 */           if (this.logger.isDebugEnabled()) {
/* 349:535 */             this.logger.debug("Loading properties [" + resource.getFilename() + "] with encoding '" + encoding + "'");
/* 350:    */           }
/* 351:537 */           this.propertiesPersister.load(props, new InputStreamReader(is, encoding));
/* 352:    */         }
/* 353:    */         else
/* 354:    */         {
/* 355:540 */           if (this.logger.isDebugEnabled()) {
/* 356:541 */             this.logger.debug("Loading properties [" + resource.getFilename() + "]");
/* 357:    */           }
/* 358:543 */           this.propertiesPersister.load(props, is);
/* 359:    */         }
/* 360:    */       }
/* 361:546 */       return props;
/* 362:    */     }
/* 363:    */     finally
/* 364:    */     {
/* 365:549 */       is.close();
/* 366:    */     }
/* 367:    */   }
/* 368:    */   
/* 369:    */   public void clearCache()
/* 370:    */   {
/* 371:559 */     this.logger.debug("Clearing entire resource bundle cache");
/* 372:560 */     synchronized (this.cachedProperties)
/* 373:    */     {
/* 374:561 */       this.cachedProperties.clear();
/* 375:    */     }
/* 376:563 */     synchronized (this.cachedMergedProperties)
/* 377:    */     {
/* 378:564 */       this.cachedMergedProperties.clear();
/* 379:    */     }
/* 380:    */   }
/* 381:    */   
/* 382:    */   public void clearCacheIncludingAncestors()
/* 383:    */   {
/* 384:573 */     clearCache();
/* 385:574 */     if ((getParentMessageSource() instanceof ReloadableResourceBundleMessageSource)) {
/* 386:575 */       ((ReloadableResourceBundleMessageSource)getParentMessageSource()).clearCacheIncludingAncestors();
/* 387:    */     }
/* 388:    */   }
/* 389:    */   
/* 390:    */   public String toString()
/* 391:    */   {
/* 392:582 */     return getClass().getName() + ": basenames=[" + StringUtils.arrayToCommaDelimitedString(this.basenames) + "]";
/* 393:    */   }
/* 394:    */   
/* 395:    */   protected class PropertiesHolder
/* 396:    */   {
/* 397:    */     private Properties properties;
/* 398:596 */     private long fileTimestamp = -1L;
/* 399:598 */     private long refreshTimestamp = -1L;
/* 400:602 */     private final Map<String, Map<Locale, MessageFormat>> cachedMessageFormats = new HashMap();
/* 401:    */     
/* 402:    */     public PropertiesHolder(Properties properties, long fileTimestamp)
/* 403:    */     {
/* 404:605 */       this.properties = properties;
/* 405:606 */       this.fileTimestamp = fileTimestamp;
/* 406:    */     }
/* 407:    */     
/* 408:    */     public PropertiesHolder() {}
/* 409:    */     
/* 410:    */     public Properties getProperties()
/* 411:    */     {
/* 412:613 */       return this.properties;
/* 413:    */     }
/* 414:    */     
/* 415:    */     public long getFileTimestamp()
/* 416:    */     {
/* 417:617 */       return this.fileTimestamp;
/* 418:    */     }
/* 419:    */     
/* 420:    */     public void setRefreshTimestamp(long refreshTimestamp)
/* 421:    */     {
/* 422:621 */       this.refreshTimestamp = refreshTimestamp;
/* 423:    */     }
/* 424:    */     
/* 425:    */     public long getRefreshTimestamp()
/* 426:    */     {
/* 427:625 */       return this.refreshTimestamp;
/* 428:    */     }
/* 429:    */     
/* 430:    */     public String getProperty(String code)
/* 431:    */     {
/* 432:629 */       if (this.properties == null) {
/* 433:630 */         return null;
/* 434:    */       }
/* 435:632 */       return this.properties.getProperty(code);
/* 436:    */     }
/* 437:    */     
/* 438:    */     public MessageFormat getMessageFormat(String code, Locale locale)
/* 439:    */     {
/* 440:636 */       if (this.properties == null) {
/* 441:637 */         return null;
/* 442:    */       }
/* 443:639 */       synchronized (this.cachedMessageFormats)
/* 444:    */       {
/* 445:640 */         Map<Locale, MessageFormat> localeMap = (Map)this.cachedMessageFormats.get(code);
/* 446:641 */         if (localeMap != null)
/* 447:    */         {
/* 448:642 */           MessageFormat result = (MessageFormat)localeMap.get(locale);
/* 449:643 */           if (result != null) {
/* 450:644 */             return result;
/* 451:    */           }
/* 452:    */         }
/* 453:647 */         String msg = this.properties.getProperty(code);
/* 454:648 */         if (msg != null)
/* 455:    */         {
/* 456:649 */           if (localeMap == null)
/* 457:    */           {
/* 458:650 */             localeMap = new HashMap();
/* 459:651 */             this.cachedMessageFormats.put(code, localeMap);
/* 460:    */           }
/* 461:653 */           MessageFormat result = ReloadableResourceBundleMessageSource.this.createMessageFormat(msg, locale);
/* 462:654 */           localeMap.put(locale, result);
/* 463:655 */           return result;
/* 464:    */         }
/* 465:657 */         return null;
/* 466:    */       }
/* 467:    */     }
/* 468:    */   }
/* 469:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.ReloadableResourceBundleMessageSource
 * JD-Core Version:    0.7.0.1
 */