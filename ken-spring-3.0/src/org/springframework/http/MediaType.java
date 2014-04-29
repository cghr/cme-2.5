/*   1:    */ package org.springframework.http;
/*   2:    */ 
/*   3:    */ import java.nio.charset.Charset;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.BitSet;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.Comparator;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.LinkedHashMap;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Locale;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.Map.Entry;
/*  15:    */ import java.util.TreeSet;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ import org.springframework.util.CollectionUtils;
/*  18:    */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*  19:    */ import org.springframework.util.StringUtils;
/*  20:    */ 
/*  21:    */ public class MediaType
/*  22:    */   implements Comparable<MediaType>
/*  23:    */ {
/*  24:    */   static
/*  25:    */   {
/*  26:140 */     BitSet ctl = new BitSet(128);
/*  27:141 */     for (int i = 0; i <= 31; i++) {
/*  28:142 */       ctl.set(i);
/*  29:    */     }
/*  30:144 */     ctl.set(127);
/*  31:    */     
/*  32:146 */     BitSet separators = new BitSet(128);
/*  33:147 */     separators.set(40);
/*  34:148 */     separators.set(41);
/*  35:149 */     separators.set(60);
/*  36:150 */     separators.set(62);
/*  37:151 */     separators.set(64);
/*  38:152 */     separators.set(44);
/*  39:153 */     separators.set(59);
/*  40:154 */     separators.set(58);
/*  41:155 */     separators.set(92);
/*  42:156 */     separators.set(34);
/*  43:157 */     separators.set(47);
/*  44:158 */     separators.set(91);
/*  45:159 */     separators.set(93);
/*  46:160 */     separators.set(63);
/*  47:161 */     separators.set(61);
/*  48:162 */     separators.set(123);
/*  49:163 */     separators.set(125);
/*  50:164 */     separators.set(32);
/*  51:165 */     separators.set(9);
/*  52:    */     
/*  53:167 */     TOKEN = new BitSet(128);
/*  54:168 */     TOKEN.set(0, 128);
/*  55:169 */     TOKEN.andNot(ctl);
/*  56:170 */     TOKEN.andNot(separators);
/*  57:    */   }
/*  58:    */   
/*  59:172 */   public static final MediaType ALL = new MediaType("*", "*");
/*  60:173 */   public static final MediaType APPLICATION_ATOM_XML = new MediaType("application", "atom+xml");
/*  61:174 */   public static final MediaType APPLICATION_FORM_URLENCODED = new MediaType("application", "x-www-form-urlencoded");
/*  62:175 */   public static final MediaType APPLICATION_JSON = new MediaType("application", "json");
/*  63:176 */   public static final MediaType APPLICATION_OCTET_STREAM = new MediaType("application", "octet-stream");
/*  64:177 */   public static final MediaType APPLICATION_XHTML_XML = new MediaType("application", "xhtml+xml");
/*  65:178 */   public static final MediaType APPLICATION_XML = new MediaType("application", "xml");
/*  66:179 */   public static final MediaType IMAGE_GIF = new MediaType("image", "gif");
/*  67:180 */   public static final MediaType IMAGE_JPEG = new MediaType("image", "jpeg");
/*  68:181 */   public static final MediaType IMAGE_PNG = new MediaType("image", "png");
/*  69:182 */   public static final MediaType MULTIPART_FORM_DATA = new MediaType("multipart", "form-data");
/*  70:183 */   public static final MediaType TEXT_HTML = new MediaType("text", "html");
/*  71:184 */   public static final MediaType TEXT_PLAIN = new MediaType("text", "plain");
/*  72:185 */   public static final MediaType TEXT_XML = new MediaType("text", "xml");
/*  73:    */   private static final BitSet TOKEN;
/*  74:    */   private static final String WILDCARD_TYPE = "*";
/*  75:    */   private static final String PARAM_QUALITY_FACTOR = "q";
/*  76:    */   private static final String PARAM_CHARSET = "charset";
/*  77:    */   private final String type;
/*  78:    */   private final String subtype;
/*  79:    */   private final Map<String, String> parameters;
/*  80:    */   
/*  81:    */   public MediaType(String type)
/*  82:    */   {
/*  83:196 */     this(type, "*");
/*  84:    */   }
/*  85:    */   
/*  86:    */   public MediaType(String type, String subtype)
/*  87:    */   {
/*  88:207 */     this(type, subtype, Collections.emptyMap());
/*  89:    */   }
/*  90:    */   
/*  91:    */   public MediaType(String type, String subtype, Charset charSet)
/*  92:    */   {
/*  93:218 */     this(type, subtype, Collections.singletonMap("charset", charSet.name()));
/*  94:    */   }
/*  95:    */   
/*  96:    */   public MediaType(String type, String subtype, double qualityValue)
/*  97:    */   {
/*  98:230 */     this(type, subtype, Collections.singletonMap("q", Double.toString(qualityValue)));
/*  99:    */   }
/* 100:    */   
/* 101:    */   public MediaType(MediaType other, Map<String, String> parameters)
/* 102:    */   {
/* 103:241 */     this(other.getType(), other.getSubtype(), parameters);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public MediaType(String type, String subtype, Map<String, String> parameters)
/* 107:    */   {
/* 108:252 */     Assert.hasLength(type, "'type' must not be empty");
/* 109:253 */     Assert.hasLength(subtype, "'subtype' must not be empty");
/* 110:254 */     checkToken(type);
/* 111:255 */     checkToken(subtype);
/* 112:256 */     this.type = type.toLowerCase(Locale.ENGLISH);
/* 113:257 */     this.subtype = subtype.toLowerCase(Locale.ENGLISH);
/* 114:258 */     if (!CollectionUtils.isEmpty(parameters))
/* 115:    */     {
/* 116:259 */       Map<String, String> m = new LinkedCaseInsensitiveMap(parameters.size(), Locale.ENGLISH);
/* 117:260 */       for (Map.Entry<String, String> entry : parameters.entrySet())
/* 118:    */       {
/* 119:261 */         String attribute = (String)entry.getKey();
/* 120:262 */         String value = (String)entry.getValue();
/* 121:263 */         checkParameters(attribute, value);
/* 122:264 */         m.put(attribute, unquote(value));
/* 123:    */       }
/* 124:266 */       this.parameters = Collections.unmodifiableMap(m);
/* 125:    */     }
/* 126:    */     else
/* 127:    */     {
/* 128:269 */       this.parameters = Collections.emptyMap();
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   private void checkToken(String s)
/* 133:    */   {
/* 134:279 */     for (int i = 0; i < s.length(); i++)
/* 135:    */     {
/* 136:280 */       char ch = s.charAt(i);
/* 137:281 */       if (!TOKEN.get(ch)) {
/* 138:282 */         throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + s + "\"");
/* 139:    */       }
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   private void checkParameters(String attribute, String value)
/* 144:    */   {
/* 145:288 */     Assert.hasLength(attribute, "parameter attribute must not be empty");
/* 146:289 */     Assert.hasLength(value, "parameter value must not be empty");
/* 147:290 */     checkToken(attribute);
/* 148:291 */     if ("q".equals(attribute))
/* 149:    */     {
/* 150:292 */       value = unquote(value);
/* 151:293 */       double d = Double.parseDouble(value);
/* 152:294 */       Assert.isTrue((d >= 0.0D) && (d <= 1.0D), 
/* 153:295 */         "Invalid quality value \"" + value + "\": should be between 0.0 and 1.0");
/* 154:    */     }
/* 155:297 */     else if ("charset".equals(attribute))
/* 156:    */     {
/* 157:298 */       value = unquote(value);
/* 158:299 */       Charset.forName(value);
/* 159:    */     }
/* 160:301 */     else if (!isQuotedString(value))
/* 161:    */     {
/* 162:302 */       checkToken(value);
/* 163:    */     }
/* 164:    */   }
/* 165:    */   
/* 166:    */   private boolean isQuotedString(String s)
/* 167:    */   {
/* 168:307 */     return (s.length() > 1) && (s.startsWith("\"")) && (s.endsWith("\""));
/* 169:    */   }
/* 170:    */   
/* 171:    */   private String unquote(String s)
/* 172:    */   {
/* 173:311 */     if (s == null) {
/* 174:312 */       return null;
/* 175:    */     }
/* 176:314 */     return isQuotedString(s) ? s.substring(1, s.length() - 1) : s;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public String getType()
/* 180:    */   {
/* 181:321 */     return this.type;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public boolean isWildcardType()
/* 185:    */   {
/* 186:328 */     return "*".equals(this.type);
/* 187:    */   }
/* 188:    */   
/* 189:    */   public String getSubtype()
/* 190:    */   {
/* 191:335 */     return this.subtype;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public boolean isWildcardSubtype()
/* 195:    */   {
/* 196:343 */     return "*".equals(this.subtype);
/* 197:    */   }
/* 198:    */   
/* 199:    */   public boolean isConcrete()
/* 200:    */   {
/* 201:352 */     return (!isWildcardType()) && (!isWildcardSubtype());
/* 202:    */   }
/* 203:    */   
/* 204:    */   public Charset getCharSet()
/* 205:    */   {
/* 206:360 */     String charSet = getParameter("charset");
/* 207:361 */     return charSet != null ? Charset.forName(charSet) : null;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public double getQualityValue()
/* 211:    */   {
/* 212:370 */     String qualityFactory = getParameter("q");
/* 213:371 */     return qualityFactory != null ? Double.parseDouble(qualityFactory) : 1.0D;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public String getParameter(String name)
/* 217:    */   {
/* 218:380 */     return (String)this.parameters.get(name);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public boolean includes(MediaType other)
/* 222:    */   {
/* 223:391 */     if (other == null) {
/* 224:392 */       return false;
/* 225:    */     }
/* 226:394 */     if (isWildcardType()) {
/* 227:396 */       return true;
/* 228:    */     }
/* 229:398 */     if (this.type.equals(other.type))
/* 230:    */     {
/* 231:399 */       if ((this.subtype.equals(other.subtype)) || (isWildcardSubtype())) {
/* 232:400 */         return true;
/* 233:    */       }
/* 234:403 */       int thisPlusIdx = this.subtype.indexOf('+');
/* 235:404 */       int otherPlusIdx = other.subtype.indexOf('+');
/* 236:405 */       if ((thisPlusIdx != -1) && (otherPlusIdx != -1))
/* 237:    */       {
/* 238:406 */         String thisSubtypeNoSuffix = this.subtype.substring(0, thisPlusIdx);
/* 239:    */         
/* 240:408 */         String thisSubtypeSuffix = this.subtype.substring(thisPlusIdx + 1);
/* 241:409 */         String otherSubtypeSuffix = other.subtype.substring(otherPlusIdx + 1);
/* 242:410 */         if ((thisSubtypeSuffix.equals(otherSubtypeSuffix)) && ("*".equals(thisSubtypeNoSuffix))) {
/* 243:411 */           return true;
/* 244:    */         }
/* 245:    */       }
/* 246:    */     }
/* 247:415 */     return false;
/* 248:    */   }
/* 249:    */   
/* 250:    */   public boolean isCompatibleWith(MediaType other)
/* 251:    */   {
/* 252:426 */     if (other == null) {
/* 253:427 */       return false;
/* 254:    */     }
/* 255:429 */     if ((isWildcardType()) || (other.isWildcardType())) {
/* 256:430 */       return true;
/* 257:    */     }
/* 258:432 */     if (this.type.equals(other.type))
/* 259:    */     {
/* 260:433 */       if ((this.subtype.equals(other.subtype)) || (isWildcardSubtype()) || (other.isWildcardSubtype())) {
/* 261:434 */         return true;
/* 262:    */       }
/* 263:437 */       int thisPlusIdx = this.subtype.indexOf('+');
/* 264:438 */       int otherPlusIdx = other.subtype.indexOf('+');
/* 265:439 */       if ((thisPlusIdx != -1) && (otherPlusIdx != -1))
/* 266:    */       {
/* 267:440 */         String thisSubtypeNoSuffix = this.subtype.substring(0, thisPlusIdx);
/* 268:441 */         String otherSubtypeNoSuffix = other.subtype.substring(0, otherPlusIdx);
/* 269:    */         
/* 270:443 */         String thisSubtypeSuffix = this.subtype.substring(thisPlusIdx + 1);
/* 271:444 */         String otherSubtypeSuffix = other.subtype.substring(otherPlusIdx + 1);
/* 272:446 */         if ((thisSubtypeSuffix.equals(otherSubtypeSuffix)) && (
/* 273:447 */           ("*".equals(thisSubtypeNoSuffix)) || ("*".equals(otherSubtypeNoSuffix)))) {
/* 274:448 */           return true;
/* 275:    */         }
/* 276:    */       }
/* 277:    */     }
/* 278:452 */     return false;
/* 279:    */   }
/* 280:    */   
/* 281:    */   public int compareTo(MediaType other)
/* 282:    */   {
/* 283:461 */     int comp = this.type.compareToIgnoreCase(other.type);
/* 284:462 */     if (comp != 0) {
/* 285:463 */       return comp;
/* 286:    */     }
/* 287:465 */     comp = this.subtype.compareToIgnoreCase(other.subtype);
/* 288:466 */     if (comp != 0) {
/* 289:467 */       return comp;
/* 290:    */     }
/* 291:469 */     comp = this.parameters.size() - other.parameters.size();
/* 292:470 */     if (comp != 0) {
/* 293:471 */       return comp;
/* 294:    */     }
/* 295:473 */     TreeSet<String> thisAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
/* 296:474 */     thisAttributes.addAll((Collection)this.parameters.keySet());
/* 297:475 */     TreeSet<String> otherAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
/* 298:476 */     otherAttributes.addAll((Collection)other.parameters.keySet());
/* 299:477 */     Iterator<String> thisAttributesIterator = thisAttributes.iterator();
/* 300:478 */     Iterator<String> otherAttributesIterator = otherAttributes.iterator();
/* 301:479 */     while (thisAttributesIterator.hasNext())
/* 302:    */     {
/* 303:480 */       String thisAttribute = (String)thisAttributesIterator.next();
/* 304:481 */       String otherAttribute = (String)otherAttributesIterator.next();
/* 305:482 */       comp = thisAttribute.compareToIgnoreCase(otherAttribute);
/* 306:483 */       if (comp != 0) {
/* 307:484 */         return comp;
/* 308:    */       }
/* 309:486 */       String thisValue = (String)this.parameters.get(thisAttribute);
/* 310:487 */       String otherValue = (String)other.parameters.get(otherAttribute);
/* 311:488 */       if (otherValue == null) {
/* 312:489 */         otherValue = "";
/* 313:    */       }
/* 314:491 */       comp = thisValue.compareTo(otherValue);
/* 315:492 */       if (comp != 0) {
/* 316:493 */         return comp;
/* 317:    */       }
/* 318:    */     }
/* 319:496 */     return 0;
/* 320:    */   }
/* 321:    */   
/* 322:    */   public boolean equals(Object other)
/* 323:    */   {
/* 324:501 */     if (this == other) {
/* 325:502 */       return true;
/* 326:    */     }
/* 327:504 */     if (!(other instanceof MediaType)) {
/* 328:505 */       return false;
/* 329:    */     }
/* 330:507 */     MediaType otherType = (MediaType)other;
/* 331:    */     
/* 332:509 */     return (this.type.equalsIgnoreCase(otherType.type)) && (this.subtype.equalsIgnoreCase(otherType.subtype)) && (this.parameters.equals(otherType.parameters));
/* 333:    */   }
/* 334:    */   
/* 335:    */   public int hashCode()
/* 336:    */   {
/* 337:514 */     int result = this.type.hashCode();
/* 338:515 */     result = 31 * result + this.subtype.hashCode();
/* 339:516 */     result = 31 * result + this.parameters.hashCode();
/* 340:517 */     return result;
/* 341:    */   }
/* 342:    */   
/* 343:    */   public String toString()
/* 344:    */   {
/* 345:522 */     StringBuilder builder = new StringBuilder();
/* 346:523 */     appendTo(builder);
/* 347:524 */     return builder.toString();
/* 348:    */   }
/* 349:    */   
/* 350:    */   private void appendTo(StringBuilder builder)
/* 351:    */   {
/* 352:528 */     builder.append(this.type);
/* 353:529 */     builder.append('/');
/* 354:530 */     builder.append(this.subtype);
/* 355:531 */     appendTo(this.parameters, builder);
/* 356:    */   }
/* 357:    */   
/* 358:    */   private void appendTo(Map<String, String> map, StringBuilder builder)
/* 359:    */   {
/* 360:535 */     for (Map.Entry<String, String> entry : map.entrySet())
/* 361:    */     {
/* 362:536 */       builder.append(';');
/* 363:537 */       builder.append((String)entry.getKey());
/* 364:538 */       builder.append('=');
/* 365:539 */       builder.append((String)entry.getValue());
/* 366:    */     }
/* 367:    */   }
/* 368:    */   
/* 369:    */   public static MediaType valueOf(String value)
/* 370:    */   {
/* 371:551 */     return parseMediaType(value);
/* 372:    */   }
/* 373:    */   
/* 374:    */   public static MediaType parseMediaType(String mediaType)
/* 375:    */   {
/* 376:561 */     Assert.hasLength(mediaType, "'mediaType' must not be empty");
/* 377:562 */     String[] parts = StringUtils.tokenizeToStringArray(mediaType, ";");
/* 378:    */     
/* 379:564 */     String fullType = parts[0].trim();
/* 380:566 */     if ("*".equals(fullType)) {
/* 381:567 */       fullType = "*/*";
/* 382:    */     }
/* 383:569 */     int subIndex = fullType.indexOf('/');
/* 384:570 */     if (subIndex == -1) {
/* 385:571 */       throw new IllegalArgumentException("\"" + mediaType + "\" does not contain '/'");
/* 386:    */     }
/* 387:573 */     if (subIndex == fullType.length() - 1) {
/* 388:574 */       throw new IllegalArgumentException("\"" + mediaType + "\" does not contain subtype after '/'");
/* 389:    */     }
/* 390:576 */     String type = fullType.substring(0, subIndex);
/* 391:577 */     String subtype = fullType.substring(subIndex + 1, fullType.length());
/* 392:    */     
/* 393:579 */     Map<String, String> parameters = null;
/* 394:580 */     if (parts.length > 1)
/* 395:    */     {
/* 396:581 */       parameters = new LinkedHashMap(parts.length - 1);
/* 397:582 */       for (int i = 1; i < parts.length; i++)
/* 398:    */       {
/* 399:583 */         String parameter = parts[i];
/* 400:584 */         int eqIndex = parameter.indexOf('=');
/* 401:585 */         if (eqIndex != -1)
/* 402:    */         {
/* 403:586 */           String attribute = parameter.substring(0, eqIndex);
/* 404:587 */           String value = parameter.substring(eqIndex + 1, parameter.length());
/* 405:588 */           parameters.put(attribute, value);
/* 406:    */         }
/* 407:    */       }
/* 408:    */     }
/* 409:593 */     return new MediaType(type, subtype, parameters);
/* 410:    */   }
/* 411:    */   
/* 412:    */   public static List<MediaType> parseMediaTypes(String mediaTypes)
/* 413:    */   {
/* 414:605 */     if (!StringUtils.hasLength(mediaTypes)) {
/* 415:606 */       return Collections.emptyList();
/* 416:    */     }
/* 417:608 */     String[] tokens = mediaTypes.split(",\\s*");
/* 418:609 */     List<MediaType> result = new ArrayList(tokens.length);
/* 419:610 */     for (String token : tokens) {
/* 420:611 */       result.add(parseMediaType(token));
/* 421:    */     }
/* 422:613 */     return result;
/* 423:    */   }
/* 424:    */   
/* 425:    */   public static String toString(Collection<MediaType> mediaTypes)
/* 426:    */   {
/* 427:624 */     StringBuilder builder = new StringBuilder();
/* 428:625 */     for (Iterator<MediaType> iterator = mediaTypes.iterator(); iterator.hasNext();)
/* 429:    */     {
/* 430:626 */       MediaType mediaType = (MediaType)iterator.next();
/* 431:627 */       mediaType.appendTo(builder);
/* 432:628 */       if (iterator.hasNext()) {
/* 433:629 */         builder.append(", ");
/* 434:    */       }
/* 435:    */     }
/* 436:632 */     return builder.toString();
/* 437:    */   }
/* 438:    */   
/* 439:    */   public static void sortBySpecificity(List<MediaType> mediaTypes)
/* 440:    */   {
/* 441:662 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 442:663 */     if (mediaTypes.size() > 1) {
/* 443:664 */       Collections.sort(mediaTypes, SPECIFICITY_COMPARATOR);
/* 444:    */     }
/* 445:    */   }
/* 446:    */   
/* 447:    */   public static void sortByQualityValue(List<MediaType> mediaTypes)
/* 448:    */   {
/* 449:689 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 450:690 */     if (mediaTypes.size() > 1) {
/* 451:691 */       Collections.sort(mediaTypes, QUALITY_VALUE_COMPARATOR);
/* 452:    */     }
/* 453:    */   }
/* 454:    */   
/* 455:699 */   public static final Comparator<MediaType> SPECIFICITY_COMPARATOR = new Comparator()
/* 456:    */   {
/* 457:    */     public int compare(MediaType mediaType1, MediaType mediaType2)
/* 458:    */     {
/* 459:702 */       if ((mediaType1.isWildcardType()) && (!mediaType2.isWildcardType())) {
/* 460:703 */         return 1;
/* 461:    */       }
/* 462:705 */       if ((mediaType2.isWildcardType()) && (!mediaType1.isWildcardType())) {
/* 463:706 */         return -1;
/* 464:    */       }
/* 465:708 */       if (!mediaType1.getType().equals(mediaType2.getType())) {
/* 466:709 */         return 0;
/* 467:    */       }
/* 468:712 */       if ((mediaType1.isWildcardSubtype()) && (!mediaType2.isWildcardSubtype())) {
/* 469:713 */         return 1;
/* 470:    */       }
/* 471:715 */       if ((mediaType2.isWildcardSubtype()) && (!mediaType1.isWildcardSubtype())) {
/* 472:716 */         return -1;
/* 473:    */       }
/* 474:718 */       if (!mediaType1.getSubtype().equals(mediaType2.getSubtype())) {
/* 475:719 */         return 0;
/* 476:    */       }
/* 477:722 */       double quality1 = mediaType1.getQualityValue();
/* 478:723 */       double quality2 = mediaType2.getQualityValue();
/* 479:724 */       int qualityComparison = Double.compare(quality2, quality1);
/* 480:725 */       if (qualityComparison != 0) {
/* 481:726 */         return qualityComparison;
/* 482:    */       }
/* 483:729 */       int paramsSize1 = mediaType1.parameters.size();
/* 484:730 */       int paramsSize2 = mediaType2.parameters.size();
/* 485:731 */       return paramsSize2 == paramsSize1 ? 0 : paramsSize2 < paramsSize1 ? -1 : 1;
/* 486:    */     }
/* 487:    */   };
/* 488:742 */   public static final Comparator<MediaType> QUALITY_VALUE_COMPARATOR = new Comparator()
/* 489:    */   {
/* 490:    */     public int compare(MediaType mediaType1, MediaType mediaType2)
/* 491:    */     {
/* 492:745 */       double quality1 = mediaType1.getQualityValue();
/* 493:746 */       double quality2 = mediaType2.getQualityValue();
/* 494:747 */       int qualityComparison = Double.compare(quality2, quality1);
/* 495:748 */       if (qualityComparison != 0) {
/* 496:749 */         return qualityComparison;
/* 497:    */       }
/* 498:751 */       if ((mediaType1.isWildcardType()) && (!mediaType2.isWildcardType())) {
/* 499:752 */         return 1;
/* 500:    */       }
/* 501:754 */       if ((mediaType2.isWildcardType()) && (!mediaType1.isWildcardType())) {
/* 502:755 */         return -1;
/* 503:    */       }
/* 504:757 */       if (!mediaType1.getType().equals(mediaType2.getType())) {
/* 505:758 */         return 0;
/* 506:    */       }
/* 507:761 */       if ((mediaType1.isWildcardSubtype()) && (!mediaType2.isWildcardSubtype())) {
/* 508:762 */         return 1;
/* 509:    */       }
/* 510:764 */       if ((mediaType2.isWildcardSubtype()) && (!mediaType1.isWildcardSubtype())) {
/* 511:765 */         return -1;
/* 512:    */       }
/* 513:767 */       if (!mediaType1.getSubtype().equals(mediaType2.getSubtype())) {
/* 514:768 */         return 0;
/* 515:    */       }
/* 516:771 */       int paramsSize1 = mediaType1.parameters.size();
/* 517:772 */       int paramsSize2 = mediaType2.parameters.size();
/* 518:773 */       return paramsSize2 == paramsSize1 ? 0 : paramsSize2 < paramsSize1 ? -1 : 1;
/* 519:    */     }
/* 520:    */   };
/* 521:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.MediaType
 * JD-Core Version:    0.7.0.1
 */