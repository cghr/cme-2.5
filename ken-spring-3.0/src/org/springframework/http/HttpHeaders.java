/*   1:    */ package org.springframework.http;
/*   2:    */ 
/*   3:    */ import java.net.URI;
/*   4:    */ import java.nio.charset.Charset;
/*   5:    */ import java.text.ParseException;
/*   6:    */ import java.text.SimpleDateFormat;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.Collections;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.EnumSet;
/*  12:    */ import java.util.Iterator;
/*  13:    */ import java.util.LinkedHashMap;
/*  14:    */ import java.util.LinkedList;
/*  15:    */ import java.util.List;
/*  16:    */ import java.util.Locale;
/*  17:    */ import java.util.Map;
/*  18:    */ import java.util.Map.Entry;
/*  19:    */ import java.util.Set;
/*  20:    */ import java.util.TimeZone;
/*  21:    */ import org.springframework.util.Assert;
/*  22:    */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*  23:    */ import org.springframework.util.MultiValueMap;
/*  24:    */ import org.springframework.util.StringUtils;
/*  25:    */ 
/*  26:    */ public class HttpHeaders
/*  27:    */   implements MultiValueMap<String, String>
/*  28:    */ {
/*  29:    */   private static final String ACCEPT = "Accept";
/*  30:    */   private static final String ACCEPT_CHARSET = "Accept-Charset";
/*  31:    */   private static final String ALLOW = "Allow";
/*  32:    */   private static final String CACHE_CONTROL = "Cache-Control";
/*  33:    */   private static final String CONTENT_DISPOSITION = "Content-Disposition";
/*  34:    */   private static final String CONTENT_LENGTH = "Content-Length";
/*  35:    */   private static final String CONTENT_TYPE = "Content-Type";
/*  36:    */   private static final String DATE = "Date";
/*  37:    */   private static final String ETAG = "ETag";
/*  38:    */   private static final String EXPIRES = "Expires";
/*  39:    */   private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
/*  40:    */   private static final String IF_NONE_MATCH = "If-None-Match";
/*  41:    */   private static final String LAST_MODIFIED = "Last-Modified";
/*  42:    */   private static final String LOCATION = "Location";
/*  43:    */   private static final String PRAGMA = "Pragma";
/*  44: 90 */   private static final String[] DATE_FORMATS = {
/*  45: 91 */     "EEE, dd MMM yyyy HH:mm:ss zzz", 
/*  46: 92 */     "EEE, dd-MMM-yy HH:mm:ss zzz", 
/*  47: 93 */     "EEE MMM dd HH:mm:ss yyyy" };
/*  48: 96 */   private static TimeZone GMT = TimeZone.getTimeZone("GMT");
/*  49:    */   private final Map<String, List<String>> headers;
/*  50:    */   
/*  51:    */   private HttpHeaders(Map<String, List<String>> headers, boolean readOnly)
/*  52:    */   {
/*  53:105 */     Assert.notNull(headers, "'headers' must not be null");
/*  54:106 */     if (readOnly)
/*  55:    */     {
/*  56:107 */       Map<String, List<String>> map = 
/*  57:108 */         new LinkedCaseInsensitiveMap(headers.size(), Locale.ENGLISH);
/*  58:109 */       for (Map.Entry<String, List<String>> entry : headers.entrySet())
/*  59:    */       {
/*  60:110 */         List<String> values = Collections.unmodifiableList((List)entry.getValue());
/*  61:111 */         map.put((String)entry.getKey(), values);
/*  62:    */       }
/*  63:113 */       this.headers = Collections.unmodifiableMap(map);
/*  64:    */     }
/*  65:    */     else
/*  66:    */     {
/*  67:116 */       this.headers = headers;
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public HttpHeaders()
/*  72:    */   {
/*  73:124 */     this(new LinkedCaseInsensitiveMap(8, Locale.ENGLISH), false);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers)
/*  77:    */   {
/*  78:131 */     return new HttpHeaders(headers, true);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setAccept(List<MediaType> acceptableMediaTypes)
/*  82:    */   {
/*  83:139 */     set("Accept", MediaType.toString(acceptableMediaTypes));
/*  84:    */   }
/*  85:    */   
/*  86:    */   public List<MediaType> getAccept()
/*  87:    */   {
/*  88:148 */     String value = getFirst("Accept");
/*  89:149 */     return value != null ? MediaType.parseMediaTypes(value) : Collections.emptyList();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setAcceptCharset(List<Charset> acceptableCharsets)
/*  93:    */   {
/*  94:157 */     StringBuilder builder = new StringBuilder();
/*  95:158 */     for (Iterator<Charset> iterator = acceptableCharsets.iterator(); iterator.hasNext();)
/*  96:    */     {
/*  97:159 */       Charset charset = (Charset)iterator.next();
/*  98:160 */       builder.append(charset.name().toLowerCase(Locale.ENGLISH));
/*  99:161 */       if (iterator.hasNext()) {
/* 100:162 */         builder.append(", ");
/* 101:    */       }
/* 102:    */     }
/* 103:165 */     set("Accept-Charset", builder.toString());
/* 104:    */   }
/* 105:    */   
/* 106:    */   public List<Charset> getAcceptCharset()
/* 107:    */   {
/* 108:174 */     List<Charset> result = new ArrayList();
/* 109:175 */     String value = getFirst("Accept-Charset");
/* 110:176 */     if (value != null)
/* 111:    */     {
/* 112:177 */       String[] tokens = value.split(",\\s*");
/* 113:178 */       for (String token : tokens)
/* 114:    */       {
/* 115:179 */         int paramIdx = token.indexOf(';');
/* 116:    */         String charsetName;
/* 117:    */         String charsetName;
/* 118:181 */         if (paramIdx == -1) {
/* 119:182 */           charsetName = token;
/* 120:    */         } else {
/* 121:185 */           charsetName = token.substring(0, paramIdx);
/* 122:    */         }
/* 123:187 */         if (!charsetName.equals("*")) {
/* 124:188 */           result.add(Charset.forName(charsetName));
/* 125:    */         }
/* 126:    */       }
/* 127:    */     }
/* 128:192 */     return result;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setAllow(Set<HttpMethod> allowedMethods)
/* 132:    */   {
/* 133:200 */     set("Allow", StringUtils.collectionToCommaDelimitedString(allowedMethods));
/* 134:    */   }
/* 135:    */   
/* 136:    */   public Set<HttpMethod> getAllow()
/* 137:    */   {
/* 138:209 */     String value = getFirst("Allow");
/* 139:210 */     if (value != null)
/* 140:    */     {
/* 141:211 */       List<HttpMethod> allowedMethod = new ArrayList(5);
/* 142:212 */       String[] tokens = value.split(",\\s*");
/* 143:213 */       for (String token : tokens) {
/* 144:214 */         allowedMethod.add(HttpMethod.valueOf(token));
/* 145:    */       }
/* 146:216 */       return (Set)EnumSet.copyOf(allowedMethod);
/* 147:    */     }
/* 148:219 */     return (Set)EnumSet.noneOf(HttpMethod.class);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setCacheControl(String cacheControl)
/* 152:    */   {
/* 153:228 */     set("Cache-Control", cacheControl);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public String getCacheControl()
/* 157:    */   {
/* 158:236 */     return getFirst("Cache-Control");
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void setContentDispositionFormData(String name, String filename)
/* 162:    */   {
/* 163:245 */     Assert.notNull(name, "'name' must not be null");
/* 164:246 */     StringBuilder builder = new StringBuilder("form-data; name=\"");
/* 165:247 */     builder.append(name).append('"');
/* 166:248 */     if (filename != null)
/* 167:    */     {
/* 168:249 */       builder.append("; filename=\"");
/* 169:250 */       builder.append(filename).append('"');
/* 170:    */     }
/* 171:252 */     set("Content-Disposition", builder.toString());
/* 172:    */   }
/* 173:    */   
/* 174:    */   public void setContentLength(long contentLength)
/* 175:    */   {
/* 176:260 */     set("Content-Length", Long.toString(contentLength));
/* 177:    */   }
/* 178:    */   
/* 179:    */   public long getContentLength()
/* 180:    */   {
/* 181:269 */     String value = getFirst("Content-Length");
/* 182:270 */     return value != null ? Long.parseLong(value) : -1L;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setContentType(MediaType mediaType)
/* 186:    */   {
/* 187:278 */     Assert.isTrue(!mediaType.isWildcardType(), "'Content-Type' cannot contain wildcard type '*'");
/* 188:279 */     Assert.isTrue(!mediaType.isWildcardSubtype(), "'Content-Type' cannot contain wildcard subtype '*'");
/* 189:280 */     set("Content-Type", mediaType.toString());
/* 190:    */   }
/* 191:    */   
/* 192:    */   public MediaType getContentType()
/* 193:    */   {
/* 194:289 */     String value = getFirst("Content-Type");
/* 195:290 */     return value != null ? MediaType.parseMediaType(value) : null;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public void setDate(long date)
/* 199:    */   {
/* 200:299 */     setDate("Date", date);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public long getDate()
/* 204:    */   {
/* 205:309 */     return getFirstDate("Date");
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void setETag(String eTag)
/* 209:    */   {
/* 210:317 */     if (eTag != null)
/* 211:    */     {
/* 212:318 */       Assert.isTrue((eTag.startsWith("\"")) || (eTag.startsWith("W/")), "Invalid eTag, does not start with W/ or \"");
/* 213:319 */       Assert.isTrue(eTag.endsWith("\""), "Invalid eTag, does not end with \"");
/* 214:    */     }
/* 215:321 */     set("ETag", eTag);
/* 216:    */   }
/* 217:    */   
/* 218:    */   public String getETag()
/* 219:    */   {
/* 220:329 */     return getFirst("ETag");
/* 221:    */   }
/* 222:    */   
/* 223:    */   public void setExpires(long expires)
/* 224:    */   {
/* 225:338 */     setDate("Expires", expires);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public long getExpires()
/* 229:    */   {
/* 230:347 */     return getFirstDate("Expires");
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void setIfModifiedSince(long ifModifiedSince)
/* 234:    */   {
/* 235:356 */     setDate("If-Modified-Since", ifModifiedSince);
/* 236:    */   }
/* 237:    */   
/* 238:    */   public long getIfNotModifiedSince()
/* 239:    */   {
/* 240:365 */     return getFirstDate("If-Modified-Since");
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void setIfNoneMatch(String ifNoneMatch)
/* 244:    */   {
/* 245:373 */     set("If-None-Match", ifNoneMatch);
/* 246:    */   }
/* 247:    */   
/* 248:    */   public void setIfNoneMatch(List<String> ifNoneMatchList)
/* 249:    */   {
/* 250:381 */     StringBuilder builder = new StringBuilder();
/* 251:382 */     for (Iterator<String> iterator = ifNoneMatchList.iterator(); iterator.hasNext();)
/* 252:    */     {
/* 253:383 */       String ifNoneMatch = (String)iterator.next();
/* 254:384 */       builder.append(ifNoneMatch);
/* 255:385 */       if (iterator.hasNext()) {
/* 256:386 */         builder.append(", ");
/* 257:    */       }
/* 258:    */     }
/* 259:389 */     set("If-None-Match", builder.toString());
/* 260:    */   }
/* 261:    */   
/* 262:    */   public List<String> getIfNoneMatch()
/* 263:    */   {
/* 264:397 */     List<String> result = new ArrayList();
/* 265:    */     
/* 266:399 */     String value = getFirst("If-None-Match");
/* 267:400 */     if (value != null)
/* 268:    */     {
/* 269:401 */       String[] tokens = value.split(",\\s*");
/* 270:402 */       for (String token : tokens) {
/* 271:403 */         result.add(token);
/* 272:    */       }
/* 273:    */     }
/* 274:406 */     return result;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public void setLastModified(long lastModified)
/* 278:    */   {
/* 279:415 */     setDate("Last-Modified", lastModified);
/* 280:    */   }
/* 281:    */   
/* 282:    */   public long getLastModified()
/* 283:    */   {
/* 284:424 */     return getFirstDate("Last-Modified");
/* 285:    */   }
/* 286:    */   
/* 287:    */   public void setLocation(URI location)
/* 288:    */   {
/* 289:432 */     set("Location", location.toASCIIString());
/* 290:    */   }
/* 291:    */   
/* 292:    */   public URI getLocation()
/* 293:    */   {
/* 294:441 */     String value = getFirst("Location");
/* 295:442 */     return value != null ? URI.create(value) : null;
/* 296:    */   }
/* 297:    */   
/* 298:    */   public void setPragma(String pragma)
/* 299:    */   {
/* 300:450 */     set("Pragma", pragma);
/* 301:    */   }
/* 302:    */   
/* 303:    */   public String getPragma()
/* 304:    */   {
/* 305:458 */     return getFirst("Pragma");
/* 306:    */   }
/* 307:    */   
/* 308:    */   private long getFirstDate(String headerName)
/* 309:    */   {
/* 310:464 */     String headerValue = getFirst(headerName);
/* 311:465 */     if (headerValue == null) {
/* 312:466 */       return -1L;
/* 313:    */     }
/* 314:468 */     for (String dateFormat : DATE_FORMATS)
/* 315:    */     {
/* 316:469 */       SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
/* 317:470 */       simpleDateFormat.setTimeZone(GMT);
/* 318:    */       try
/* 319:    */       {
/* 320:472 */         return simpleDateFormat.parse(headerValue).getTime();
/* 321:    */       }
/* 322:    */       catch (ParseException localParseException) {}
/* 323:    */     }
/* 324:478 */     throw new IllegalArgumentException("Cannot parse date value \"" + headerValue + 
/* 325:479 */       "\" for \"" + headerName + "\" header");
/* 326:    */   }
/* 327:    */   
/* 328:    */   private void setDate(String headerName, long date)
/* 329:    */   {
/* 330:483 */     SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATS[0], Locale.US);
/* 331:484 */     dateFormat.setTimeZone(GMT);
/* 332:485 */     set(headerName, dateFormat.format(new Date(date)));
/* 333:    */   }
/* 334:    */   
/* 335:    */   public String getFirst(String headerName)
/* 336:    */   {
/* 337:496 */     List<String> headerValues = (List)this.headers.get(headerName);
/* 338:497 */     return headerValues != null ? (String)headerValues.get(0) : null;
/* 339:    */   }
/* 340:    */   
/* 341:    */   public void add(String headerName, String headerValue)
/* 342:    */   {
/* 343:509 */     List<String> headerValues = (List)this.headers.get(headerName);
/* 344:510 */     if (headerValues == null)
/* 345:    */     {
/* 346:511 */       headerValues = new LinkedList();
/* 347:512 */       this.headers.put(headerName, headerValues);
/* 348:    */     }
/* 349:514 */     headerValues.add(headerValue);
/* 350:    */   }
/* 351:    */   
/* 352:    */   public void set(String headerName, String headerValue)
/* 353:    */   {
/* 354:526 */     List<String> headerValues = new LinkedList();
/* 355:527 */     headerValues.add(headerValue);
/* 356:528 */     this.headers.put(headerName, headerValues);
/* 357:    */   }
/* 358:    */   
/* 359:    */   public void setAll(Map<String, String> values)
/* 360:    */   {
/* 361:532 */     for (Map.Entry<String, String> entry : values.entrySet()) {
/* 362:533 */       set((String)entry.getKey(), (String)entry.getValue());
/* 363:    */     }
/* 364:    */   }
/* 365:    */   
/* 366:    */   public Map<String, String> toSingleValueMap()
/* 367:    */   {
/* 368:538 */     LinkedHashMap<String, String> singleValueMap = new LinkedHashMap(this.headers.size());
/* 369:539 */     for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
/* 370:540 */       singleValueMap.put((String)entry.getKey(), (String)((List)entry.getValue()).get(0));
/* 371:    */     }
/* 372:542 */     return singleValueMap;
/* 373:    */   }
/* 374:    */   
/* 375:    */   public int size()
/* 376:    */   {
/* 377:548 */     return this.headers.size();
/* 378:    */   }
/* 379:    */   
/* 380:    */   public boolean isEmpty()
/* 381:    */   {
/* 382:552 */     return this.headers.isEmpty();
/* 383:    */   }
/* 384:    */   
/* 385:    */   public boolean containsKey(Object key)
/* 386:    */   {
/* 387:556 */     return this.headers.containsKey(key);
/* 388:    */   }
/* 389:    */   
/* 390:    */   public boolean containsValue(Object value)
/* 391:    */   {
/* 392:560 */     return this.headers.containsValue(value);
/* 393:    */   }
/* 394:    */   
/* 395:    */   public List<String> get(Object key)
/* 396:    */   {
/* 397:564 */     return (List)this.headers.get(key);
/* 398:    */   }
/* 399:    */   
/* 400:    */   public List<String> put(String key, List<String> value)
/* 401:    */   {
/* 402:568 */     return (List)this.headers.put(key, value);
/* 403:    */   }
/* 404:    */   
/* 405:    */   public List<String> remove(Object key)
/* 406:    */   {
/* 407:572 */     return (List)this.headers.remove(key);
/* 408:    */   }
/* 409:    */   
/* 410:    */   public void putAll(Map<? extends String, ? extends List<String>> m)
/* 411:    */   {
/* 412:576 */     this.headers.putAll(m);
/* 413:    */   }
/* 414:    */   
/* 415:    */   public void clear()
/* 416:    */   {
/* 417:580 */     this.headers.clear();
/* 418:    */   }
/* 419:    */   
/* 420:    */   public Set<String> keySet()
/* 421:    */   {
/* 422:584 */     return this.headers.keySet();
/* 423:    */   }
/* 424:    */   
/* 425:    */   public Collection<List<String>> values()
/* 426:    */   {
/* 427:588 */     return this.headers.values();
/* 428:    */   }
/* 429:    */   
/* 430:    */   public Set<Map.Entry<String, List<String>>> entrySet()
/* 431:    */   {
/* 432:592 */     return this.headers.entrySet();
/* 433:    */   }
/* 434:    */   
/* 435:    */   public boolean equals(Object other)
/* 436:    */   {
/* 437:598 */     if (this == other) {
/* 438:599 */       return true;
/* 439:    */     }
/* 440:601 */     if (!(other instanceof HttpHeaders)) {
/* 441:602 */       return false;
/* 442:    */     }
/* 443:604 */     HttpHeaders otherHeaders = (HttpHeaders)other;
/* 444:605 */     return this.headers.equals(otherHeaders.headers);
/* 445:    */   }
/* 446:    */   
/* 447:    */   public int hashCode()
/* 448:    */   {
/* 449:610 */     return this.headers.hashCode();
/* 450:    */   }
/* 451:    */   
/* 452:    */   public String toString()
/* 453:    */   {
/* 454:615 */     return this.headers.toString();
/* 455:    */   }
/* 456:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.HttpHeaders
 * JD-Core Version:    0.7.0.1
 */