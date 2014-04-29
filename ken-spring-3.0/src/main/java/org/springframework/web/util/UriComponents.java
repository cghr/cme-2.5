/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.UnsupportedEncodingException;
/*   5:    */ import java.net.URI;
/*   6:    */ import java.net.URISyntaxException;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Arrays;
/*   9:    */ import java.util.Collections;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.Map.Entry;
/*  14:    */ import java.util.regex.Matcher;
/*  15:    */ import java.util.regex.Pattern;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ import org.springframework.util.CollectionUtils;
/*  18:    */ import org.springframework.util.LinkedMultiValueMap;
/*  19:    */ import org.springframework.util.MultiValueMap;
/*  20:    */ import org.springframework.util.StringUtils;
/*  21:    */ 
/*  22:    */ public final class UriComponents
/*  23:    */ {
/*  24:    */   private static final String DEFAULT_ENCODING = "UTF-8";
/*  25:    */   private static final char PATH_DELIMITER = '/';
/*  26: 58 */   private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
/*  27:    */   private final String scheme;
/*  28:    */   private final String userInfo;
/*  29:    */   private final String host;
/*  30:    */   private final int port;
/*  31:    */   private final PathComponent path;
/*  32:    */   private final MultiValueMap<String, String> queryParams;
/*  33:    */   private final String fragment;
/*  34:    */   private final boolean encoded;
/*  35:    */   
/*  36:    */   UriComponents(String scheme, String userInfo, String host, int port, PathComponent path, MultiValueMap<String, String> queryParams, String fragment, boolean encoded)
/*  37:    */   {
/*  38: 84 */     this.scheme = scheme;
/*  39: 85 */     this.userInfo = userInfo;
/*  40: 86 */     this.host = host;
/*  41: 87 */     this.port = port;
/*  42: 88 */     this.path = (path != null ? path : NULL_PATH_COMPONENT);
/*  43: 89 */     if (queryParams == null) {
/*  44: 90 */       queryParams = new LinkedMultiValueMap(0);
/*  45:    */     }
/*  46: 92 */     this.queryParams = CollectionUtils.unmodifiableMultiValueMap(queryParams);
/*  47: 93 */     this.fragment = fragment;
/*  48: 94 */     this.encoded = encoded;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getScheme()
/*  52:    */   {
/*  53:105 */     return this.scheme;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getUserInfo()
/*  57:    */   {
/*  58:114 */     return this.userInfo;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getHost()
/*  62:    */   {
/*  63:123 */     return this.host;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int getPort()
/*  67:    */   {
/*  68:132 */     return this.port;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String getPath()
/*  72:    */   {
/*  73:141 */     return this.path.getPath();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public List<String> getPathSegments()
/*  77:    */   {
/*  78:150 */     return this.path.getPathSegments();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String getQuery()
/*  82:    */   {
/*  83:159 */     if (!this.queryParams.isEmpty())
/*  84:    */     {
/*  85:160 */       StringBuilder queryBuilder = new StringBuilder();
/*  86:161 */       for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet())
/*  87:    */       {
/*  88:162 */         String name = (String)entry.getKey();
/*  89:163 */         List<String> values = (List)entry.getValue();
/*  90:164 */         if (CollectionUtils.isEmpty(values))
/*  91:    */         {
/*  92:165 */           if (queryBuilder.length() != 0) {
/*  93:166 */             queryBuilder.append('&');
/*  94:    */           }
/*  95:168 */           queryBuilder.append(name);
/*  96:    */         }
/*  97:    */         else
/*  98:    */         {
/*  99:171 */           for (Object value : values)
/* 100:    */           {
/* 101:172 */             if (queryBuilder.length() != 0) {
/* 102:173 */               queryBuilder.append('&');
/* 103:    */             }
/* 104:175 */             queryBuilder.append(name);
/* 105:177 */             if (value != null)
/* 106:    */             {
/* 107:178 */               queryBuilder.append('=');
/* 108:179 */               queryBuilder.append(value.toString());
/* 109:    */             }
/* 110:    */           }
/* 111:    */         }
/* 112:    */       }
/* 113:184 */       return queryBuilder.toString();
/* 114:    */     }
/* 115:187 */     return null;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public MultiValueMap<String, String> getQueryParams()
/* 119:    */   {
/* 120:197 */     return this.queryParams;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public String getFragment()
/* 124:    */   {
/* 125:206 */     return this.fragment;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public UriComponents encode()
/* 129:    */   {
/* 130:    */     try
/* 131:    */     {
/* 132:219 */       return encode("UTF-8");
/* 133:    */     }
/* 134:    */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/* 135:    */     {
/* 136:222 */       throw new InternalError("\"UTF-8\" not supported");
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   public UriComponents encode(String encoding)
/* 141:    */     throws UnsupportedEncodingException
/* 142:    */   {
/* 143:235 */     Assert.hasLength(encoding, "'encoding' must not be empty");
/* 144:237 */     if (this.encoded) {
/* 145:238 */       return this;
/* 146:    */     }
/* 147:241 */     String encodedScheme = encodeUriComponent(this.scheme, encoding, Type.SCHEME);
/* 148:242 */     String encodedUserInfo = encodeUriComponent(this.userInfo, encoding, Type.USER_INFO);
/* 149:243 */     String encodedHost = encodeUriComponent(this.host, encoding, Type.HOST);
/* 150:244 */     PathComponent encodedPath = this.path.encode(encoding);
/* 151:245 */     MultiValueMap<String, String> encodedQueryParams = 
/* 152:246 */       new LinkedMultiValueMap(this.queryParams.size());
/* 153:247 */     for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet())
/* 154:    */     {
/* 155:248 */       String encodedName = encodeUriComponent((String)entry.getKey(), encoding, Type.QUERY_PARAM);
/* 156:249 */       List<String> encodedValues = new ArrayList(((List)entry.getValue()).size());
/* 157:250 */       for (String value : (List)entry.getValue())
/* 158:    */       {
/* 159:251 */         String encodedValue = encodeUriComponent(value, encoding, Type.QUERY_PARAM);
/* 160:252 */         encodedValues.add(encodedValue);
/* 161:    */       }
/* 162:254 */       encodedQueryParams.put(encodedName, encodedValues);
/* 163:    */     }
/* 164:256 */     String encodedFragment = encodeUriComponent(this.fragment, encoding, Type.FRAGMENT);
/* 165:    */     
/* 166:258 */     return new UriComponents(encodedScheme, encodedUserInfo, encodedHost, this.port, encodedPath, 
/* 167:259 */       encodedQueryParams, encodedFragment, true);
/* 168:    */   }
/* 169:    */   
/* 170:    */   static String encodeUriComponent(String source, String encoding, Type type)
/* 171:    */     throws UnsupportedEncodingException
/* 172:    */   {
/* 173:274 */     if (source == null) {
/* 174:275 */       return null;
/* 175:    */     }
/* 176:278 */     Assert.hasLength(encoding, "'encoding' must not be empty");
/* 177:    */     
/* 178:280 */     byte[] bytes = encodeBytes(source.getBytes(encoding), type);
/* 179:281 */     return new String(bytes, "US-ASCII");
/* 180:    */   }
/* 181:    */   
/* 182:    */   private static byte[] encodeBytes(byte[] source, Type type)
/* 183:    */   {
/* 184:285 */     Assert.notNull(source, "'source' must not be null");
/* 185:286 */     Assert.notNull(type, "'type' must not be null");
/* 186:    */     
/* 187:288 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length);
/* 188:289 */     for (int i = 0; i < source.length; i++)
/* 189:    */     {
/* 190:290 */       int b = source[i];
/* 191:291 */       if (b < 0) {
/* 192:292 */         b += 256;
/* 193:    */       }
/* 194:294 */       if (type.isAllowed(b))
/* 195:    */       {
/* 196:295 */         bos.write(b);
/* 197:    */       }
/* 198:    */       else
/* 199:    */       {
/* 200:298 */         bos.write(37);
/* 201:    */         
/* 202:300 */         char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 203:301 */         char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 204:    */         
/* 205:303 */         bos.write(hex1);
/* 206:304 */         bos.write(hex2);
/* 207:    */       }
/* 208:    */     }
/* 209:307 */     return bos.toByteArray();
/* 210:    */   }
/* 211:    */   
/* 212:    */   public UriComponents expand(Map<String, ?> uriVariables)
/* 213:    */   {
/* 214:320 */     Assert.notNull(uriVariables, "'uriVariables' must not be null");
/* 215:    */     
/* 216:322 */     return expandInternal(new MapTemplateVariables(uriVariables));
/* 217:    */   }
/* 218:    */   
/* 219:    */   public UriComponents expand(Object... uriVariableValues)
/* 220:    */   {
/* 221:333 */     Assert.notNull(uriVariableValues, "'uriVariableValues' must not be null");
/* 222:    */     
/* 223:335 */     return expandInternal(new VarArgsTemplateVariables(uriVariableValues));
/* 224:    */   }
/* 225:    */   
/* 226:    */   private UriComponents expandInternal(UriTemplateVariables uriVariables)
/* 227:    */   {
/* 228:339 */     String expandedScheme = expandUriComponent(this.scheme, uriVariables);
/* 229:340 */     String expandedUserInfo = expandUriComponent(this.userInfo, uriVariables);
/* 230:341 */     String expandedHost = expandUriComponent(this.host, uriVariables);
/* 231:342 */     PathComponent expandedPath = this.path.expand(uriVariables);
/* 232:343 */     MultiValueMap<String, String> expandedQueryParams = 
/* 233:344 */       new LinkedMultiValueMap(this.queryParams.size());
/* 234:345 */     for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet())
/* 235:    */     {
/* 236:346 */       String expandedName = expandUriComponent((String)entry.getKey(), uriVariables);
/* 237:347 */       List<String> expandedValues = new ArrayList(((List)entry.getValue()).size());
/* 238:348 */       for (String value : (List)entry.getValue())
/* 239:    */       {
/* 240:349 */         String expandedValue = expandUriComponent(value, uriVariables);
/* 241:350 */         expandedValues.add(expandedValue);
/* 242:    */       }
/* 243:352 */       expandedQueryParams.put(expandedName, expandedValues);
/* 244:    */     }
/* 245:354 */     String expandedFragment = expandUriComponent(this.fragment, uriVariables);
/* 246:    */     
/* 247:356 */     return new UriComponents(expandedScheme, expandedUserInfo, expandedHost, this.port, expandedPath, 
/* 248:357 */       expandedQueryParams, expandedFragment, false);
/* 249:    */   }
/* 250:    */   
/* 251:    */   private static String expandUriComponent(String source, UriTemplateVariables uriVariables)
/* 252:    */   {
/* 253:361 */     if (source == null) {
/* 254:362 */       return null;
/* 255:    */     }
/* 256:364 */     if (source.indexOf('{') == -1) {
/* 257:365 */       return source;
/* 258:    */     }
/* 259:367 */     Matcher matcher = NAMES_PATTERN.matcher(source);
/* 260:368 */     StringBuffer sb = new StringBuffer();
/* 261:369 */     while (matcher.find())
/* 262:    */     {
/* 263:370 */       String match = matcher.group(1);
/* 264:371 */       String variableName = getVariableName(match);
/* 265:372 */       Object variableValue = uriVariables.getValue(variableName);
/* 266:373 */       String variableValueString = getVariableValueAsString(variableValue);
/* 267:374 */       String replacement = Matcher.quoteReplacement(variableValueString);
/* 268:375 */       matcher.appendReplacement(sb, replacement);
/* 269:    */     }
/* 270:377 */     matcher.appendTail(sb);
/* 271:378 */     return sb.toString();
/* 272:    */   }
/* 273:    */   
/* 274:    */   private static String getVariableName(String match)
/* 275:    */   {
/* 276:382 */     int colonIdx = match.indexOf(':');
/* 277:383 */     return colonIdx == -1 ? match : match.substring(0, colonIdx);
/* 278:    */   }
/* 279:    */   
/* 280:    */   private static String getVariableValueAsString(Object variableValue)
/* 281:    */   {
/* 282:387 */     return variableValue != null ? variableValue.toString() : "";
/* 283:    */   }
/* 284:    */   
/* 285:    */   public String toUriString()
/* 286:    */   {
/* 287:398 */     StringBuilder uriBuilder = new StringBuilder();
/* 288:400 */     if (this.scheme != null)
/* 289:    */     {
/* 290:401 */       uriBuilder.append(this.scheme);
/* 291:402 */       uriBuilder.append(':');
/* 292:    */     }
/* 293:405 */     if ((this.userInfo != null) || (this.host != null))
/* 294:    */     {
/* 295:406 */       uriBuilder.append("//");
/* 296:407 */       if (this.userInfo != null)
/* 297:    */       {
/* 298:408 */         uriBuilder.append(this.userInfo);
/* 299:409 */         uriBuilder.append('@');
/* 300:    */       }
/* 301:411 */       if (this.host != null) {
/* 302:412 */         uriBuilder.append(this.host);
/* 303:    */       }
/* 304:414 */       if (this.port != -1)
/* 305:    */       {
/* 306:415 */         uriBuilder.append(':');
/* 307:416 */         uriBuilder.append(this.port);
/* 308:    */       }
/* 309:    */     }
/* 310:420 */     String path = getPath();
/* 311:421 */     if (StringUtils.hasLength(path))
/* 312:    */     {
/* 313:422 */       if ((uriBuilder.length() != 0) && (path.charAt(0) != '/')) {
/* 314:423 */         uriBuilder.append('/');
/* 315:    */       }
/* 316:425 */       uriBuilder.append(path);
/* 317:    */     }
/* 318:428 */     String query = getQuery();
/* 319:429 */     if (query != null)
/* 320:    */     {
/* 321:430 */       uriBuilder.append('?');
/* 322:431 */       uriBuilder.append(query);
/* 323:    */     }
/* 324:434 */     if (this.fragment != null)
/* 325:    */     {
/* 326:435 */       uriBuilder.append('#');
/* 327:436 */       uriBuilder.append(this.fragment);
/* 328:    */     }
/* 329:439 */     return uriBuilder.toString();
/* 330:    */   }
/* 331:    */   
/* 332:    */   public URI toUri()
/* 333:    */   {
/* 334:    */     try
/* 335:    */     {
/* 336:449 */       if (this.encoded) {
/* 337:450 */         return new URI(toUriString());
/* 338:    */       }
/* 339:453 */       String path = getPath();
/* 340:454 */       if ((StringUtils.hasLength(path)) && (path.charAt(0) != '/')) {
/* 341:455 */         path = '/' + path;
/* 342:    */       }
/* 343:457 */       return new URI(getScheme(), getUserInfo(), getHost(), getPort(), path, getQuery(), 
/* 344:458 */         getFragment());
/* 345:    */     }
/* 346:    */     catch (URISyntaxException ex)
/* 347:    */     {
/* 348:462 */       throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
/* 349:    */     }
/* 350:    */   }
/* 351:    */   
/* 352:    */   public boolean equals(Object o)
/* 353:    */   {
/* 354:468 */     if (this == o) {
/* 355:469 */       return true;
/* 356:    */     }
/* 357:471 */     if ((o instanceof UriComponents))
/* 358:    */     {
/* 359:472 */       UriComponents other = (UriComponents)o;
/* 360:474 */       if (this.scheme != null ? !this.scheme.equals(other.scheme) : other.scheme != null) {
/* 361:475 */         return false;
/* 362:    */       }
/* 363:477 */       if (this.userInfo != null ? !this.userInfo.equals(other.userInfo) : other.userInfo != null) {
/* 364:478 */         return false;
/* 365:    */       }
/* 366:480 */       if (this.host != null ? !this.host.equals(other.host) : other.host != null) {
/* 367:481 */         return false;
/* 368:    */       }
/* 369:483 */       if (this.port != other.port) {
/* 370:484 */         return false;
/* 371:    */       }
/* 372:486 */       if (!this.path.equals(other.path)) {
/* 373:487 */         return false;
/* 374:    */       }
/* 375:489 */       if (!this.queryParams.equals(other.queryParams)) {
/* 376:490 */         return false;
/* 377:    */       }
/* 378:492 */       if (this.fragment != null ? !this.fragment.equals(other.fragment) : other.fragment != null) {
/* 379:493 */         return false;
/* 380:    */       }
/* 381:495 */       return true;
/* 382:    */     }
/* 383:498 */     return false;
/* 384:    */   }
/* 385:    */   
/* 386:    */   public int hashCode()
/* 387:    */   {
/* 388:504 */     int result = this.scheme != null ? this.scheme.hashCode() : 0;
/* 389:505 */     result = 31 * result + (this.userInfo != null ? this.userInfo.hashCode() : 0);
/* 390:506 */     result = 31 * result + (this.host != null ? this.host.hashCode() : 0);
/* 391:507 */     result = 31 * result + this.port;
/* 392:508 */     result = 31 * result + this.path.hashCode();
/* 393:509 */     result = 31 * result + this.queryParams.hashCode();
/* 394:510 */     result = 31 * result + (this.fragment != null ? this.fragment.hashCode() : 0);
/* 395:511 */     return result;
/* 396:    */   }
/* 397:    */   
/* 398:    */   public String toString()
/* 399:    */   {
/* 400:516 */     return toUriString();
/* 401:    */   }
/* 402:    */   
/* 403:    */   static abstract enum Type
/* 404:    */   {
/* 405:531 */     SCHEME,  AUTHORITY,  USER_INFO,  HOST,  PORT,  PATH,  PATH_SEGMENT,  QUERY,  QUERY_PARAM,  FRAGMENT;
/* 406:    */     
/* 407:    */     public abstract boolean isAllowed(int paramInt);
/* 408:    */     
/* 409:    */     protected boolean isAlpha(int c)
/* 410:    */     {
/* 411:611 */       return ((c >= 97) && (c <= 122)) || ((c >= 65) && (c <= 90));
/* 412:    */     }
/* 413:    */     
/* 414:    */     protected boolean isDigit(int c)
/* 415:    */     {
/* 416:620 */       return (c >= 48) && (c <= 57);
/* 417:    */     }
/* 418:    */     
/* 419:    */     protected boolean isGenericDelimiter(int c)
/* 420:    */     {
/* 421:629 */       return (58 == c) || (47 == c) || (63 == c) || (35 == c) || (91 == c) || (93 == c) || (64 == c);
/* 422:    */     }
/* 423:    */     
/* 424:    */     protected boolean isSubDelimiter(int c)
/* 425:    */     {
/* 426:639 */       return (33 == c) || (36 == c) || (38 == c) || (39 == c) || (40 == c) || (41 == c) || (42 == c) || (43 == c) || (44 == c) || (59 == c) || (61 == c);
/* 427:    */     }
/* 428:    */     
/* 429:    */     protected boolean isReserved(char c)
/* 430:    */     {
/* 431:648 */       return (isGenericDelimiter(c)) || (isReserved(c));
/* 432:    */     }
/* 433:    */     
/* 434:    */     protected boolean isUnreserved(int c)
/* 435:    */     {
/* 436:657 */       return (isAlpha(c)) || (isDigit(c)) || (45 == c) || (46 == c) || (95 == c) || (126 == c);
/* 437:    */     }
/* 438:    */     
/* 439:    */     protected boolean isPchar(int c)
/* 440:    */     {
/* 441:666 */       return (isUnreserved(c)) || (isSubDelimiter(c)) || (58 == c) || (64 == c);
/* 442:    */     }
/* 443:    */   }
/* 444:    */   
/* 445:    */   static final class FullPathComponent
/* 446:    */     implements UriComponents.PathComponent
/* 447:    */   {
/* 448:    */     private final String path;
/* 449:    */     
/* 450:    */     FullPathComponent(String path)
/* 451:    */     {
/* 452:694 */       this.path = path;
/* 453:    */     }
/* 454:    */     
/* 455:    */     public String getPath()
/* 456:    */     {
/* 457:698 */       return this.path;
/* 458:    */     }
/* 459:    */     
/* 460:    */     public List<String> getPathSegments()
/* 461:    */     {
/* 462:702 */       String delimiter = new String(new char[] { '/' });
/* 463:703 */       String[] pathSegments = StringUtils.tokenizeToStringArray(this.path, delimiter);
/* 464:704 */       return Collections.unmodifiableList(Arrays.asList(pathSegments));
/* 465:    */     }
/* 466:    */     
/* 467:    */     public UriComponents.PathComponent encode(String encoding)
/* 468:    */       throws UnsupportedEncodingException
/* 469:    */     {
/* 470:708 */       String encodedPath = UriComponents.encodeUriComponent(getPath(), encoding, UriComponents.Type.PATH);
/* 471:709 */       return new FullPathComponent(encodedPath);
/* 472:    */     }
/* 473:    */     
/* 474:    */     public UriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables)
/* 475:    */     {
/* 476:713 */       String expandedPath = UriComponents.expandUriComponent(getPath(), uriVariables);
/* 477:714 */       return new FullPathComponent(expandedPath);
/* 478:    */     }
/* 479:    */     
/* 480:    */     public boolean equals(Object o)
/* 481:    */     {
/* 482:719 */       if (this == o) {
/* 483:720 */         return true;
/* 484:    */       }
/* 485:721 */       if ((o instanceof FullPathComponent))
/* 486:    */       {
/* 487:722 */         FullPathComponent other = (FullPathComponent)o;
/* 488:723 */         return getPath().equals(other.getPath());
/* 489:    */       }
/* 490:725 */       return false;
/* 491:    */     }
/* 492:    */     
/* 493:    */     public int hashCode()
/* 494:    */     {
/* 495:730 */       return getPath().hashCode();
/* 496:    */     }
/* 497:    */   }
/* 498:    */   
/* 499:    */   static final class PathSegmentComponent
/* 500:    */     implements UriComponents.PathComponent
/* 501:    */   {
/* 502:    */     private final List<String> pathSegments;
/* 503:    */     
/* 504:    */     PathSegmentComponent(List<String> pathSegments)
/* 505:    */     {
/* 506:742 */       this.pathSegments = Collections.unmodifiableList(pathSegments);
/* 507:    */     }
/* 508:    */     
/* 509:    */     public String getPath()
/* 510:    */     {
/* 511:746 */       StringBuilder pathBuilder = new StringBuilder();
/* 512:747 */       pathBuilder.append('/');
/* 513:748 */       for (Iterator<String> iterator = this.pathSegments.iterator(); iterator.hasNext();)
/* 514:    */       {
/* 515:749 */         String pathSegment = (String)iterator.next();
/* 516:750 */         pathBuilder.append(pathSegment);
/* 517:751 */         if (iterator.hasNext()) {
/* 518:752 */           pathBuilder.append('/');
/* 519:    */         }
/* 520:    */       }
/* 521:755 */       return pathBuilder.toString();
/* 522:    */     }
/* 523:    */     
/* 524:    */     public List<String> getPathSegments()
/* 525:    */     {
/* 526:759 */       return this.pathSegments;
/* 527:    */     }
/* 528:    */     
/* 529:    */     public UriComponents.PathComponent encode(String encoding)
/* 530:    */       throws UnsupportedEncodingException
/* 531:    */     {
/* 532:763 */       List<String> pathSegments = getPathSegments();
/* 533:764 */       List<String> encodedPathSegments = new ArrayList(pathSegments.size());
/* 534:765 */       for (String pathSegment : pathSegments)
/* 535:    */       {
/* 536:766 */         String encodedPathSegment = UriComponents.encodeUriComponent(pathSegment, encoding, UriComponents.Type.PATH_SEGMENT);
/* 537:767 */         encodedPathSegments.add(encodedPathSegment);
/* 538:    */       }
/* 539:769 */       return new PathSegmentComponent(encodedPathSegments);
/* 540:    */     }
/* 541:    */     
/* 542:    */     public UriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables)
/* 543:    */     {
/* 544:773 */       List<String> pathSegments = getPathSegments();
/* 545:774 */       List<String> expandedPathSegments = new ArrayList(pathSegments.size());
/* 546:775 */       for (String pathSegment : pathSegments)
/* 547:    */       {
/* 548:776 */         String expandedPathSegment = UriComponents.expandUriComponent(pathSegment, uriVariables);
/* 549:777 */         expandedPathSegments.add(expandedPathSegment);
/* 550:    */       }
/* 551:779 */       return new PathSegmentComponent(expandedPathSegments);
/* 552:    */     }
/* 553:    */     
/* 554:    */     public boolean equals(Object o)
/* 555:    */     {
/* 556:784 */       if (this == o) {
/* 557:785 */         return true;
/* 558:    */       }
/* 559:786 */       if ((o instanceof PathSegmentComponent))
/* 560:    */       {
/* 561:787 */         PathSegmentComponent other = (PathSegmentComponent)o;
/* 562:788 */         return getPathSegments().equals(other.getPathSegments());
/* 563:    */       }
/* 564:790 */       return false;
/* 565:    */     }
/* 566:    */     
/* 567:    */     public int hashCode()
/* 568:    */     {
/* 569:795 */       return getPathSegments().hashCode();
/* 570:    */     }
/* 571:    */   }
/* 572:    */   
/* 573:    */   static final class PathComponentComposite
/* 574:    */     implements UriComponents.PathComponent
/* 575:    */   {
/* 576:    */     private final List<UriComponents.PathComponent> pathComponents;
/* 577:    */     
/* 578:    */     PathComponentComposite(List<UriComponents.PathComponent> pathComponents)
/* 579:    */     {
/* 580:808 */       this.pathComponents = pathComponents;
/* 581:    */     }
/* 582:    */     
/* 583:    */     public String getPath()
/* 584:    */     {
/* 585:812 */       StringBuilder pathBuilder = new StringBuilder();
/* 586:813 */       for (UriComponents.PathComponent pathComponent : this.pathComponents) {
/* 587:814 */         pathBuilder.append(pathComponent.getPath());
/* 588:    */       }
/* 589:816 */       return pathBuilder.toString();
/* 590:    */     }
/* 591:    */     
/* 592:    */     public List<String> getPathSegments()
/* 593:    */     {
/* 594:820 */       List<String> result = new ArrayList();
/* 595:821 */       for (UriComponents.PathComponent pathComponent : this.pathComponents) {
/* 596:822 */         result.addAll(pathComponent.getPathSegments());
/* 597:    */       }
/* 598:824 */       return result;
/* 599:    */     }
/* 600:    */     
/* 601:    */     public UriComponents.PathComponent encode(String encoding)
/* 602:    */       throws UnsupportedEncodingException
/* 603:    */     {
/* 604:828 */       List<UriComponents.PathComponent> encodedComponents = new ArrayList(this.pathComponents.size());
/* 605:829 */       for (UriComponents.PathComponent pathComponent : this.pathComponents) {
/* 606:830 */         encodedComponents.add(pathComponent.encode(encoding));
/* 607:    */       }
/* 608:832 */       return new PathComponentComposite(encodedComponents);
/* 609:    */     }
/* 610:    */     
/* 611:    */     public UriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables)
/* 612:    */     {
/* 613:836 */       List<UriComponents.PathComponent> expandedComponents = new ArrayList(this.pathComponents.size());
/* 614:837 */       for (UriComponents.PathComponent pathComponent : this.pathComponents) {
/* 615:838 */         expandedComponents.add(pathComponent.expand(uriVariables));
/* 616:    */       }
/* 617:840 */       return new PathComponentComposite(expandedComponents);
/* 618:    */     }
/* 619:    */   }
/* 620:    */   
/* 621:849 */   static final PathComponent NULL_PATH_COMPONENT = new PathComponent()
/* 622:    */   {
/* 623:    */     public String getPath()
/* 624:    */     {
/* 625:852 */       return null;
/* 626:    */     }
/* 627:    */     
/* 628:    */     public List<String> getPathSegments()
/* 629:    */     {
/* 630:856 */       return Collections.emptyList();
/* 631:    */     }
/* 632:    */     
/* 633:    */     public UriComponents.PathComponent encode(String encoding)
/* 634:    */       throws UnsupportedEncodingException
/* 635:    */     {
/* 636:860 */       return this;
/* 637:    */     }
/* 638:    */     
/* 639:    */     public UriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables)
/* 640:    */     {
/* 641:864 */       return this;
/* 642:    */     }
/* 643:    */     
/* 644:    */     public boolean equals(Object o)
/* 645:    */     {
/* 646:869 */       return this == o;
/* 647:    */     }
/* 648:    */     
/* 649:    */     public int hashCode()
/* 650:    */     {
/* 651:874 */       return 42;
/* 652:    */     }
/* 653:    */   };
/* 654:    */   
/* 655:    */   private static class MapTemplateVariables
/* 656:    */     implements UriComponents.UriTemplateVariables
/* 657:    */   {
/* 658:    */     private final Map<String, ?> uriVariables;
/* 659:    */     
/* 660:    */     public MapTemplateVariables(Map<String, ?> uriVariables)
/* 661:    */     {
/* 662:899 */       this.uriVariables = uriVariables;
/* 663:    */     }
/* 664:    */     
/* 665:    */     public Object getValue(String name)
/* 666:    */     {
/* 667:903 */       return this.uriVariables.get(name);
/* 668:    */     }
/* 669:    */   }
/* 670:    */   
/* 671:    */   private static class VarArgsTemplateVariables
/* 672:    */     implements UriComponents.UriTemplateVariables
/* 673:    */   {
/* 674:    */     private final Iterator<Object> valueIterator;
/* 675:    */     
/* 676:    */     public VarArgsTemplateVariables(Object... uriVariableValues)
/* 677:    */     {
/* 678:914 */       this.valueIterator = Arrays.asList(uriVariableValues).iterator();
/* 679:    */     }
/* 680:    */     
/* 681:    */     public Object getValue(String name)
/* 682:    */     {
/* 683:918 */       if (!this.valueIterator.hasNext()) {
/* 684:919 */         throw new IllegalArgumentException("Not enough variable values available to expand [" + name + "]");
/* 685:    */       }
/* 686:921 */       return this.valueIterator.next();
/* 687:    */     }
/* 688:    */   }
/* 689:    */   
/* 690:    */   static abstract interface PathComponent
/* 691:    */   {
/* 692:    */     public abstract String getPath();
/* 693:    */     
/* 694:    */     public abstract List<String> getPathSegments();
/* 695:    */     
/* 696:    */     public abstract PathComponent encode(String paramString)
/* 697:    */       throws UnsupportedEncodingException;
/* 698:    */     
/* 699:    */     public abstract PathComponent expand(UriComponents.UriTemplateVariables paramUriTemplateVariables);
/* 700:    */   }
/* 701:    */   
/* 702:    */   private static abstract interface UriTemplateVariables
/* 703:    */   {
/* 704:    */     public abstract Object getValue(String paramString);
/* 705:    */   }
/* 706:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.UriComponents
 * JD-Core Version:    0.7.0.1
 */