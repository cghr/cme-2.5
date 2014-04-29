/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.Comparator;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.LinkedHashSet;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Locale;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.Map.Entry;
/*  15:    */ import java.util.Set;
/*  16:    */ import java.util.concurrent.ConcurrentHashMap;
/*  17:    */ import java.util.concurrent.ConcurrentMap;
/*  18:    */ import javax.activation.FileTypeMap;
/*  19:    */ import javax.activation.MimetypesFileTypeMap;
/*  20:    */ import javax.servlet.ServletContext;
/*  21:    */ import javax.servlet.http.HttpServletRequest;
/*  22:    */ import javax.servlet.http.HttpServletResponse;
/*  23:    */ import org.apache.commons.logging.Log;
/*  24:    */ import org.apache.commons.logging.LogFactory;
/*  25:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  26:    */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*  27:    */ import org.springframework.context.ApplicationContext;
/*  28:    */ import org.springframework.core.OrderComparator;
/*  29:    */ import org.springframework.core.Ordered;
/*  30:    */ import org.springframework.core.io.ClassPathResource;
/*  31:    */ import org.springframework.core.io.Resource;
/*  32:    */ import org.springframework.http.MediaType;
/*  33:    */ import org.springframework.util.Assert;
/*  34:    */ import org.springframework.util.ClassUtils;
/*  35:    */ import org.springframework.util.CollectionUtils;
/*  36:    */ import org.springframework.util.StringUtils;
/*  37:    */ import org.springframework.web.context.request.RequestAttributes;
/*  38:    */ import org.springframework.web.context.request.RequestContextHolder;
/*  39:    */ import org.springframework.web.context.request.ServletRequestAttributes;
/*  40:    */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*  41:    */ import org.springframework.web.servlet.HandlerMapping;
/*  42:    */ import org.springframework.web.servlet.SmartView;
/*  43:    */ import org.springframework.web.servlet.View;
/*  44:    */ import org.springframework.web.servlet.ViewResolver;
/*  45:    */ import org.springframework.web.util.UrlPathHelper;
/*  46:    */ import org.springframework.web.util.WebUtils;
/*  47:    */ 
/*  48:    */ public class ContentNegotiatingViewResolver
/*  49:    */   extends WebApplicationObjectSupport
/*  50:    */   implements ViewResolver, Ordered
/*  51:    */ {
/*  52:111 */   private static final Log logger = LogFactory.getLog(ContentNegotiatingViewResolver.class);
/*  53:    */   private static final String ACCEPT_HEADER = "Accept";
/*  54:116 */   private static final boolean jafPresent = ClassUtils.isPresent("javax.activation.FileTypeMap", ContentNegotiatingViewResolver.class.getClassLoader());
/*  55:118 */   private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
/*  56:121 */   private int order = -2147483648;
/*  57:123 */   private boolean favorPathExtension = true;
/*  58:125 */   private boolean favorParameter = false;
/*  59:127 */   private String parameterName = "format";
/*  60:129 */   private boolean useNotAcceptableStatusCode = false;
/*  61:131 */   private boolean ignoreAcceptHeader = false;
/*  62:133 */   private boolean useJaf = true;
/*  63:135 */   private ConcurrentMap<String, MediaType> mediaTypes = new ConcurrentHashMap();
/*  64:    */   private List<View> defaultViews;
/*  65:    */   private MediaType defaultContentType;
/*  66:    */   private List<ViewResolver> viewResolvers;
/*  67:    */   
/*  68:    */   public void setOrder(int order)
/*  69:    */   {
/*  70:145 */     this.order = order;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getOrder()
/*  74:    */   {
/*  75:149 */     return this.order;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setFavorPathExtension(boolean favorPathExtension)
/*  79:    */   {
/*  80:160 */     this.favorPathExtension = favorPathExtension;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setFavorParameter(boolean favorParameter)
/*  84:    */   {
/*  85:171 */     this.favorParameter = favorParameter;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setParameterName(String parameterName)
/*  89:    */   {
/*  90:179 */     this.parameterName = parameterName;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setIgnoreAcceptHeader(boolean ignoreAcceptHeader)
/*  94:    */   {
/*  95:189 */     this.ignoreAcceptHeader = ignoreAcceptHeader;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setUseNotAcceptableStatusCode(boolean useNotAcceptableStatusCode)
/*  99:    */   {
/* 100:202 */     this.useNotAcceptableStatusCode = useNotAcceptableStatusCode;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setMediaTypes(Map<String, String> mediaTypes)
/* 104:    */   {
/* 105:211 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 106:212 */     for (Map.Entry<String, String> entry : mediaTypes.entrySet())
/* 107:    */     {
/* 108:213 */       String extension = ((String)entry.getKey()).toLowerCase(Locale.ENGLISH);
/* 109:214 */       MediaType mediaType = MediaType.parseMediaType((String)entry.getValue());
/* 110:215 */       this.mediaTypes.put(extension, mediaType);
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setDefaultViews(List<View> defaultViews)
/* 115:    */   {
/* 116:224 */     this.defaultViews = defaultViews;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setDefaultContentType(MediaType defaultContentType)
/* 120:    */   {
/* 121:233 */     this.defaultContentType = defaultContentType;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setUseJaf(boolean useJaf)
/* 125:    */   {
/* 126:241 */     this.useJaf = useJaf;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setViewResolvers(List<ViewResolver> viewResolvers)
/* 130:    */   {
/* 131:249 */     this.viewResolvers = viewResolvers;
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected void initServletContext(ServletContext servletContext)
/* 135:    */   {
/* 136:255 */     Collection<ViewResolver> matchingBeans = 
/* 137:256 */       BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), ViewResolver.class).values();
/* 138:257 */     if (this.viewResolvers == null)
/* 139:    */     {
/* 140:258 */       this.viewResolvers = new ArrayList(matchingBeans.size());
/* 141:259 */       for (ViewResolver viewResolver : matchingBeans) {
/* 142:260 */         if (this != viewResolver) {
/* 143:261 */           this.viewResolvers.add(viewResolver);
/* 144:    */         }
/* 145:    */       }
/* 146:    */     }
/* 147:    */     else
/* 148:    */     {
/* 149:266 */       for (int i = 0; i < this.viewResolvers.size(); i++) {
/* 150:267 */         if (!matchingBeans.contains(this.viewResolvers.get(i)))
/* 151:    */         {
/* 152:270 */           String name = ((ViewResolver)this.viewResolvers.get(i)).getClass().getName() + i;
/* 153:271 */           getApplicationContext().getAutowireCapableBeanFactory().initializeBean(this.viewResolvers.get(i), name);
/* 154:    */         }
/* 155:    */       }
/* 156:    */     }
/* 157:275 */     if (this.viewResolvers.isEmpty()) {
/* 158:276 */       logger.warn("Did not find any ViewResolvers to delegate to; please configure them using the 'viewResolvers' property on the ContentNegotiatingViewResolver");
/* 159:    */     }
/* 160:279 */     OrderComparator.sort(this.viewResolvers);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public View resolveViewName(String viewName, Locale locale)
/* 164:    */     throws Exception
/* 165:    */   {
/* 166:283 */     RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
/* 167:284 */     Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
/* 168:285 */     List<MediaType> requestedMediaTypes = getMediaTypes(((ServletRequestAttributes)attrs).getRequest());
/* 169:286 */     if (requestedMediaTypes != null)
/* 170:    */     {
/* 171:287 */       List<View> candidateViews = getCandidateViews(viewName, locale, requestedMediaTypes);
/* 172:288 */       View bestView = getBestView(candidateViews, requestedMediaTypes);
/* 173:289 */       if (bestView != null) {
/* 174:290 */         return bestView;
/* 175:    */       }
/* 176:    */     }
/* 177:293 */     if (this.useNotAcceptableStatusCode)
/* 178:    */     {
/* 179:294 */       if (logger.isDebugEnabled()) {
/* 180:295 */         logger.debug("No acceptable view found; returning 406 (Not Acceptable) status code");
/* 181:    */       }
/* 182:297 */       return NOT_ACCEPTABLE_VIEW;
/* 183:    */     }
/* 184:300 */     logger.debug("No acceptable view found; returning null");
/* 185:301 */     return null;
/* 186:    */   }
/* 187:    */   
/* 188:    */   protected List<MediaType> getMediaTypes(HttpServletRequest request)
/* 189:    */   {
/* 190:316 */     if (this.favorPathExtension)
/* 191:    */     {
/* 192:317 */       String requestUri = urlPathHelper.getLookupPathForRequest(request);
/* 193:318 */       String filename = WebUtils.extractFullFilenameFromUrlPath(requestUri);
/* 194:319 */       MediaType mediaType = getMediaTypeFromFilename(filename);
/* 195:320 */       if (mediaType != null)
/* 196:    */       {
/* 197:321 */         if (logger.isDebugEnabled()) {
/* 198:322 */           logger.debug("Requested media type is '" + mediaType + "' (based on filename '" + filename + "')");
/* 199:    */         }
/* 200:324 */         return Collections.singletonList(mediaType);
/* 201:    */       }
/* 202:    */     }
/* 203:327 */     if ((this.favorParameter) && 
/* 204:328 */       (request.getParameter(this.parameterName) != null))
/* 205:    */     {
/* 206:329 */       String parameterValue = request.getParameter(this.parameterName);
/* 207:330 */       MediaType mediaType = getMediaTypeFromParameter(parameterValue);
/* 208:331 */       if (mediaType != null)
/* 209:    */       {
/* 210:332 */         if (logger.isDebugEnabled()) {
/* 211:333 */           logger.debug("Requested media type is '" + mediaType + "' (based on parameter '" + 
/* 212:334 */             this.parameterName + "'='" + parameterValue + "')");
/* 213:    */         }
/* 214:336 */         return Collections.singletonList(mediaType);
/* 215:    */       }
/* 216:    */     }
/* 217:340 */     if (!this.ignoreAcceptHeader)
/* 218:    */     {
/* 219:341 */       String acceptHeader = request.getHeader("Accept");
/* 220:342 */       if (StringUtils.hasText(acceptHeader)) {
/* 221:    */         try
/* 222:    */         {
/* 223:344 */           List<MediaType> acceptableMediaTypes = MediaType.parseMediaTypes(acceptHeader);
/* 224:345 */           List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request);
/* 225:346 */           Set<MediaType> compatibleMediaTypes = new LinkedHashSet();
/* 226:    */           Iterator localIterator2;
/* 227:347 */           for (Iterator localIterator1 = acceptableMediaTypes.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 228:    */           {
/* 229:347 */             MediaType acceptable = (MediaType)localIterator1.next();
/* 230:348 */             localIterator2 = producibleMediaTypes.iterator(); continue;MediaType producible = (MediaType)localIterator2.next();
/* 231:349 */             if (acceptable.isCompatibleWith(producible)) {
/* 232:350 */               compatibleMediaTypes.add(getMostSpecificMediaType(acceptable, producible));
/* 233:    */             }
/* 234:    */           }
/* 235:354 */           List<MediaType> mediaTypes = new ArrayList(compatibleMediaTypes);
/* 236:355 */           MediaType.sortByQualityValue(mediaTypes);
/* 237:356 */           if (logger.isDebugEnabled()) {
/* 238:357 */             logger.debug("Requested media types are " + mediaTypes + " based on Accept header types " + 
/* 239:358 */               "and producible media types " + producibleMediaTypes + ")");
/* 240:    */           }
/* 241:360 */           return mediaTypes;
/* 242:    */         }
/* 243:    */         catch (IllegalArgumentException ex)
/* 244:    */         {
/* 245:363 */           if (logger.isDebugEnabled()) {
/* 246:364 */             logger.debug("Could not parse accept header [" + acceptHeader + "]: " + ex.getMessage());
/* 247:    */           }
/* 248:366 */           return null;
/* 249:    */         }
/* 250:    */       }
/* 251:    */     }
/* 252:370 */     if (this.defaultContentType != null)
/* 253:    */     {
/* 254:371 */       if (logger.isDebugEnabled()) {
/* 255:372 */         logger.debug("Requested media types is " + this.defaultContentType + 
/* 256:373 */           " (based on defaultContentType property)");
/* 257:    */       }
/* 258:375 */       return Collections.singletonList(this.defaultContentType);
/* 259:    */     }
/* 260:378 */     return Collections.emptyList();
/* 261:    */   }
/* 262:    */   
/* 263:    */   private List<MediaType> getProducibleMediaTypes(HttpServletRequest request)
/* 264:    */   {
/* 265:384 */     Set<MediaType> mediaTypes = (Set)
/* 266:385 */       request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
/* 267:386 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 268:387 */       return new ArrayList(mediaTypes);
/* 269:    */     }
/* 270:390 */     return Collections.singletonList(MediaType.ALL);
/* 271:    */   }
/* 272:    */   
/* 273:    */   private MediaType getMostSpecificMediaType(MediaType type1, MediaType type2)
/* 274:    */   {
/* 275:398 */     double quality = type1.getQualityValue();
/* 276:399 */     Map<String, String> params = Collections.singletonMap("q", String.valueOf(quality));
/* 277:400 */     MediaType t1 = new MediaType(type1, params);
/* 278:401 */     MediaType t2 = new MediaType(type2, params);
/* 279:402 */     return MediaType.SPECIFICITY_COMPARATOR.compare(t1, t2) <= 0 ? type1 : type2;
/* 280:    */   }
/* 281:    */   
/* 282:    */   protected MediaType getMediaTypeFromFilename(String filename)
/* 283:    */   {
/* 284:415 */     String extension = StringUtils.getFilenameExtension(filename);
/* 285:416 */     if (!StringUtils.hasText(extension)) {
/* 286:417 */       return null;
/* 287:    */     }
/* 288:419 */     extension = extension.toLowerCase(Locale.ENGLISH);
/* 289:420 */     MediaType mediaType = (MediaType)this.mediaTypes.get(extension);
/* 290:421 */     if ((mediaType == null) && (this.useJaf) && (jafPresent))
/* 291:    */     {
/* 292:422 */       mediaType = ActivationMediaTypeFactory.getMediaType(filename);
/* 293:423 */       if (mediaType != null) {
/* 294:424 */         this.mediaTypes.putIfAbsent(extension, mediaType);
/* 295:    */       }
/* 296:    */     }
/* 297:427 */     return mediaType;
/* 298:    */   }
/* 299:    */   
/* 300:    */   protected MediaType getMediaTypeFromParameter(String parameterValue)
/* 301:    */   {
/* 302:439 */     return (MediaType)this.mediaTypes.get(parameterValue.toLowerCase(Locale.ENGLISH));
/* 303:    */   }
/* 304:    */   
/* 305:    */   private List<View> getCandidateViews(String viewName, Locale locale, List<MediaType> requestedMediaTypes)
/* 306:    */     throws Exception
/* 307:    */   {
/* 308:445 */     List<View> candidateViews = new ArrayList();
/* 309:    */     Iterator localIterator2;
/* 310:446 */     for (Iterator localIterator1 = this.viewResolvers.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 311:    */     {
/* 312:446 */       ViewResolver viewResolver = (ViewResolver)localIterator1.next();
/* 313:447 */       View view = viewResolver.resolveViewName(viewName, locale);
/* 314:448 */       if (view != null) {
/* 315:449 */         candidateViews.add(view);
/* 316:    */       }
/* 317:451 */       localIterator2 = requestedMediaTypes.iterator(); continue;MediaType requestedMediaType = (MediaType)localIterator2.next();
/* 318:452 */       List<String> extensions = getExtensionsForMediaType(requestedMediaType);
/* 319:453 */       for (String extension : extensions)
/* 320:    */       {
/* 321:454 */         String viewNameWithExtension = viewName + "." + extension;
/* 322:455 */         view = viewResolver.resolveViewName(viewNameWithExtension, locale);
/* 323:456 */         if (view != null) {
/* 324:457 */           candidateViews.add(view);
/* 325:    */         }
/* 326:    */       }
/* 327:    */     }
/* 328:463 */     if (!CollectionUtils.isEmpty(this.defaultViews)) {
/* 329:464 */       candidateViews.addAll(this.defaultViews);
/* 330:    */     }
/* 331:466 */     return candidateViews;
/* 332:    */   }
/* 333:    */   
/* 334:    */   private List<String> getExtensionsForMediaType(MediaType requestedMediaType)
/* 335:    */   {
/* 336:470 */     List<String> result = new ArrayList();
/* 337:471 */     for (Map.Entry<String, MediaType> entry : this.mediaTypes.entrySet()) {
/* 338:472 */       if (requestedMediaType.includes((MediaType)entry.getValue())) {
/* 339:473 */         result.add((String)entry.getKey());
/* 340:    */       }
/* 341:    */     }
/* 342:476 */     return result;
/* 343:    */   }
/* 344:    */   
/* 345:    */   private View getBestView(List<View> candidateViews, List<MediaType> requestedMediaTypes)
/* 346:    */   {
/* 347:480 */     for (View candidateView : candidateViews) {
/* 348:481 */       if ((candidateView instanceof SmartView))
/* 349:    */       {
/* 350:482 */         SmartView smartView = (SmartView)candidateView;
/* 351:483 */         if (smartView.isRedirectView())
/* 352:    */         {
/* 353:484 */           if (logger.isDebugEnabled()) {
/* 354:485 */             logger.debug("Returning redirect view [" + candidateView + "]");
/* 355:    */           }
/* 356:487 */           return candidateView;
/* 357:    */         }
/* 358:    */       }
/* 359:    */     }
/* 360:    */     Iterator localIterator2;
/* 361:491 */     for (??? = requestedMediaTypes.iterator(); ???.hasNext(); localIterator2.hasNext())
/* 362:    */     {
/* 363:491 */       MediaType mediaType = (MediaType)???.next();
/* 364:492 */       localIterator2 = candidateViews.iterator(); continue;View candidateView = (View)localIterator2.next();
/* 365:493 */       if (StringUtils.hasText(candidateView.getContentType()))
/* 366:    */       {
/* 367:494 */         MediaType candidateContentType = MediaType.parseMediaType(candidateView.getContentType());
/* 368:495 */         if (mediaType.includes(candidateContentType))
/* 369:    */         {
/* 370:496 */           if (logger.isDebugEnabled()) {
/* 371:497 */             logger.debug("Returning [" + candidateView + "] based on requested media type '" + 
/* 372:498 */               mediaType + "'");
/* 373:    */           }
/* 374:500 */           return candidateView;
/* 375:    */         }
/* 376:    */       }
/* 377:    */     }
/* 378:505 */     return null;
/* 379:    */   }
/* 380:    */   
/* 381:    */   private static class ActivationMediaTypeFactory
/* 382:    */   {
/* 383:517 */     private static final FileTypeMap fileTypeMap = ;
/* 384:    */     
/* 385:    */     private static FileTypeMap loadFileTypeMapFromContextSupportModule()
/* 386:    */     {
/* 387:522 */       Resource mappingLocation = new ClassPathResource("org/springframework/mail/javamail/mime.types");
/* 388:523 */       if (mappingLocation.exists())
/* 389:    */       {
/* 390:524 */         if (ContentNegotiatingViewResolver.logger.isTraceEnabled()) {
/* 391:525 */           ContentNegotiatingViewResolver.logger.trace("Loading Java Activation Framework FileTypeMap from " + mappingLocation);
/* 392:    */         }
/* 393:527 */         InputStream inputStream = null;
/* 394:    */         try
/* 395:    */         {
/* 396:529 */           inputStream = mappingLocation.getInputStream();
/* 397:530 */           return new MimetypesFileTypeMap(inputStream);
/* 398:    */         }
/* 399:    */         catch (IOException localIOException2) {}finally
/* 400:    */         {
/* 401:536 */           if (inputStream != null) {
/* 402:    */             try
/* 403:    */             {
/* 404:538 */               inputStream.close();
/* 405:    */             }
/* 406:    */             catch (IOException localIOException4) {}
/* 407:    */           }
/* 408:    */         }
/* 409:    */       }
/* 410:546 */       if (ContentNegotiatingViewResolver.logger.isTraceEnabled()) {
/* 411:547 */         ContentNegotiatingViewResolver.logger.trace("Loading default Java Activation Framework FileTypeMap");
/* 412:    */       }
/* 413:549 */       return FileTypeMap.getDefaultFileTypeMap();
/* 414:    */     }
/* 415:    */     
/* 416:    */     public static MediaType getMediaType(String fileName)
/* 417:    */     {
/* 418:553 */       String mediaType = fileTypeMap.getContentType(fileName);
/* 419:554 */       return StringUtils.hasText(mediaType) ? MediaType.parseMediaType(mediaType) : null;
/* 420:    */     }
/* 421:    */   }
/* 422:    */   
/* 423:559 */   private static final View NOT_ACCEPTABLE_VIEW = new View()
/* 424:    */   {
/* 425:    */     public String getContentType()
/* 426:    */     {
/* 427:562 */       return null;
/* 428:    */     }
/* 429:    */     
/* 430:    */     public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
/* 431:    */     {
/* 432:566 */       response.setStatus(406);
/* 433:    */     }
/* 434:    */   };
/* 435:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.ContentNegotiatingViewResolver
 * JD-Core Version:    0.7.0.1
 */