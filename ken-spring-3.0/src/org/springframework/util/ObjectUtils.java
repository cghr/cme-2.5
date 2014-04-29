/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.util.Arrays;
/*   5:    */ 
/*   6:    */ public abstract class ObjectUtils
/*   7:    */ {
/*   8:    */   private static final int INITIAL_HASH = 7;
/*   9:    */   private static final int MULTIPLIER = 31;
/*  10:    */   private static final String EMPTY_STRING = "";
/*  11:    */   private static final String NULL_STRING = "null";
/*  12:    */   private static final String ARRAY_START = "{";
/*  13:    */   private static final String ARRAY_END = "}";
/*  14:    */   private static final String EMPTY_ARRAY = "{}";
/*  15:    */   private static final String ARRAY_ELEMENT_SEPARATOR = ", ";
/*  16:    */   
/*  17:    */   public static boolean isCheckedException(Throwable ex)
/*  18:    */   {
/*  19: 62 */     return (!(ex instanceof RuntimeException)) && (!(ex instanceof Error));
/*  20:    */   }
/*  21:    */   
/*  22:    */   public static boolean isCompatibleWithThrowsClause(Throwable ex, Class[] declaredExceptions)
/*  23:    */   {
/*  24: 73 */     if (!isCheckedException(ex)) {
/*  25: 74 */       return true;
/*  26:    */     }
/*  27: 76 */     if (declaredExceptions != null)
/*  28:    */     {
/*  29: 77 */       int i = 0;
/*  30: 78 */       while (i < declaredExceptions.length)
/*  31:    */       {
/*  32: 79 */         if (declaredExceptions[i].isAssignableFrom(ex.getClass())) {
/*  33: 80 */           return true;
/*  34:    */         }
/*  35: 82 */         i++;
/*  36:    */       }
/*  37:    */     }
/*  38: 85 */     return false;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static boolean isArray(Object obj)
/*  42:    */   {
/*  43: 94 */     return (obj != null) && (obj.getClass().isArray());
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static boolean isEmpty(Object[] array)
/*  47:    */   {
/*  48:103 */     return (array == null) || (array.length == 0);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static boolean containsElement(Object[] array, Object element)
/*  52:    */   {
/*  53:114 */     if (array == null) {
/*  54:115 */       return false;
/*  55:    */     }
/*  56:117 */     Object[] arrayOfObject = array;int j = array.length;
/*  57:117 */     for (int i = 0; i < j; i++)
/*  58:    */     {
/*  59:117 */       Object arrayEle = arrayOfObject[i];
/*  60:118 */       if (nullSafeEquals(arrayEle, element)) {
/*  61:119 */         return true;
/*  62:    */       }
/*  63:    */     }
/*  64:122 */     return false;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static boolean containsConstant(Enum<?>[] enumValues, String constant)
/*  68:    */   {
/*  69:133 */     return containsConstant(enumValues, constant, false);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive)
/*  73:    */   {
/*  74:144 */     Enum[] arrayOfEnum = enumValues;int j = enumValues.length;
/*  75:144 */     for (int i = 0; i < j; i++)
/*  76:    */     {
/*  77:144 */       Enum<?> candidate = arrayOfEnum[i];
/*  78:145 */       if (caseSensitive ? 
/*  79:146 */         candidate.toString().equals(constant) : 
/*  80:147 */         candidate.toString().equalsIgnoreCase(constant)) {
/*  81:148 */         return true;
/*  82:    */       }
/*  83:    */     }
/*  84:151 */     return false;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static <E extends Enum<?>> E caseInsensitiveValueOf(E[] enumValues, String constant)
/*  88:    */   {
/*  89:163 */     Enum[] arrayOfEnum = enumValues;int j = enumValues.length;
/*  90:163 */     for (int i = 0; i < j; i++)
/*  91:    */     {
/*  92:163 */       E candidate = arrayOfEnum[i];
/*  93:164 */       if (candidate.toString().equalsIgnoreCase(constant)) {
/*  94:165 */         return candidate;
/*  95:    */       }
/*  96:    */     }
/*  97:168 */     throw new IllegalArgumentException(
/*  98:169 */       String.format("constant [%s] does not exist in enum type %s", new Object[] {
/*  99:170 */       constant, enumValues.getClass().getComponentType().getName() }));
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static <A, O extends A> A[] addObjectToArray(A[] array, O obj)
/* 103:    */   {
/* 104:181 */     Class<?> compType = Object.class;
/* 105:182 */     if (array != null) {
/* 106:183 */       compType = array.getClass().getComponentType();
/* 107:185 */     } else if (obj != null) {
/* 108:186 */       compType = obj.getClass();
/* 109:    */     }
/* 110:188 */     int newArrLength = array != null ? array.length + 1 : 1;
/* 111:    */     
/* 112:190 */     Object[] newArr = (Object[])Array.newInstance(compType, newArrLength);
/* 113:191 */     if (array != null) {
/* 114:192 */       System.arraycopy(array, 0, newArr, 0, array.length);
/* 115:    */     }
/* 116:194 */     newArr[(newArr.length - 1)] = obj;
/* 117:195 */     return newArr;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public static Object[] toObjectArray(Object source)
/* 121:    */   {
/* 122:208 */     if ((source instanceof Object[])) {
/* 123:209 */       return (Object[])source;
/* 124:    */     }
/* 125:211 */     if (source == null) {
/* 126:212 */       return new Object[0];
/* 127:    */     }
/* 128:214 */     if (!source.getClass().isArray()) {
/* 129:215 */       throw new IllegalArgumentException("Source is not an array: " + source);
/* 130:    */     }
/* 131:217 */     int length = Array.getLength(source);
/* 132:218 */     if (length == 0) {
/* 133:219 */       return new Object[0];
/* 134:    */     }
/* 135:221 */     Class wrapperType = Array.get(source, 0).getClass();
/* 136:222 */     Object[] newArray = (Object[])Array.newInstance(wrapperType, length);
/* 137:223 */     for (int i = 0; i < length; i++) {
/* 138:224 */       newArray[i] = Array.get(source, i);
/* 139:    */     }
/* 140:226 */     return newArray;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static boolean nullSafeEquals(Object o1, Object o2)
/* 144:    */   {
/* 145:246 */     if (o1 == o2) {
/* 146:247 */       return true;
/* 147:    */     }
/* 148:249 */     if ((o1 == null) || (o2 == null)) {
/* 149:250 */       return false;
/* 150:    */     }
/* 151:252 */     if (o1.equals(o2)) {
/* 152:253 */       return true;
/* 153:    */     }
/* 154:255 */     if ((o1.getClass().isArray()) && (o2.getClass().isArray()))
/* 155:    */     {
/* 156:256 */       if (((o1 instanceof Object[])) && ((o2 instanceof Object[]))) {
/* 157:257 */         return Arrays.equals((Object[])o1, (Object[])o2);
/* 158:    */       }
/* 159:259 */       if (((o1 instanceof boolean[])) && ((o2 instanceof boolean[]))) {
/* 160:260 */         return Arrays.equals((boolean[])o1, (boolean[])o2);
/* 161:    */       }
/* 162:262 */       if (((o1 instanceof byte[])) && ((o2 instanceof byte[]))) {
/* 163:263 */         return Arrays.equals((byte[])o1, (byte[])o2);
/* 164:    */       }
/* 165:265 */       if (((o1 instanceof char[])) && ((o2 instanceof char[]))) {
/* 166:266 */         return Arrays.equals((char[])o1, (char[])o2);
/* 167:    */       }
/* 168:268 */       if (((o1 instanceof double[])) && ((o2 instanceof double[]))) {
/* 169:269 */         return Arrays.equals((double[])o1, (double[])o2);
/* 170:    */       }
/* 171:271 */       if (((o1 instanceof float[])) && ((o2 instanceof float[]))) {
/* 172:272 */         return Arrays.equals((float[])o1, (float[])o2);
/* 173:    */       }
/* 174:274 */       if (((o1 instanceof int[])) && ((o2 instanceof int[]))) {
/* 175:275 */         return Arrays.equals((int[])o1, (int[])o2);
/* 176:    */       }
/* 177:277 */       if (((o1 instanceof long[])) && ((o2 instanceof long[]))) {
/* 178:278 */         return Arrays.equals((long[])o1, (long[])o2);
/* 179:    */       }
/* 180:280 */       if (((o1 instanceof short[])) && ((o2 instanceof short[]))) {
/* 181:281 */         return Arrays.equals((short[])o1, (short[])o2);
/* 182:    */       }
/* 183:    */     }
/* 184:284 */     return false;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public static int nullSafeHashCode(Object obj)
/* 188:    */   {
/* 189:304 */     if (obj == null) {
/* 190:305 */       return 0;
/* 191:    */     }
/* 192:307 */     if (obj.getClass().isArray())
/* 193:    */     {
/* 194:308 */       if ((obj instanceof Object[])) {
/* 195:309 */         return nullSafeHashCode((Object[])obj);
/* 196:    */       }
/* 197:311 */       if ((obj instanceof boolean[])) {
/* 198:312 */         return nullSafeHashCode((boolean[])obj);
/* 199:    */       }
/* 200:314 */       if ((obj instanceof byte[])) {
/* 201:315 */         return nullSafeHashCode((byte[])obj);
/* 202:    */       }
/* 203:317 */       if ((obj instanceof char[])) {
/* 204:318 */         return nullSafeHashCode((char[])obj);
/* 205:    */       }
/* 206:320 */       if ((obj instanceof double[])) {
/* 207:321 */         return nullSafeHashCode((double[])obj);
/* 208:    */       }
/* 209:323 */       if ((obj instanceof float[])) {
/* 210:324 */         return nullSafeHashCode((float[])obj);
/* 211:    */       }
/* 212:326 */       if ((obj instanceof int[])) {
/* 213:327 */         return nullSafeHashCode((int[])obj);
/* 214:    */       }
/* 215:329 */       if ((obj instanceof long[])) {
/* 216:330 */         return nullSafeHashCode((long[])obj);
/* 217:    */       }
/* 218:332 */       if ((obj instanceof short[])) {
/* 219:333 */         return nullSafeHashCode((short[])obj);
/* 220:    */       }
/* 221:    */     }
/* 222:336 */     return obj.hashCode();
/* 223:    */   }
/* 224:    */   
/* 225:    */   public static int nullSafeHashCode(Object[] array)
/* 226:    */   {
/* 227:344 */     if (array == null) {
/* 228:345 */       return 0;
/* 229:    */     }
/* 230:347 */     int hash = 7;
/* 231:348 */     int arraySize = array.length;
/* 232:349 */     for (int i = 0; i < arraySize; i++) {
/* 233:350 */       hash = 31 * hash + nullSafeHashCode(array[i]);
/* 234:    */     }
/* 235:352 */     return hash;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public static int nullSafeHashCode(boolean[] array)
/* 239:    */   {
/* 240:360 */     if (array == null) {
/* 241:361 */       return 0;
/* 242:    */     }
/* 243:363 */     int hash = 7;
/* 244:364 */     int arraySize = array.length;
/* 245:365 */     for (int i = 0; i < arraySize; i++) {
/* 246:366 */       hash = 31 * hash + hashCode(array[i]);
/* 247:    */     }
/* 248:368 */     return hash;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public static int nullSafeHashCode(byte[] array)
/* 252:    */   {
/* 253:376 */     if (array == null) {
/* 254:377 */       return 0;
/* 255:    */     }
/* 256:379 */     int hash = 7;
/* 257:380 */     int arraySize = array.length;
/* 258:381 */     for (int i = 0; i < arraySize; i++) {
/* 259:382 */       hash = 31 * hash + array[i];
/* 260:    */     }
/* 261:384 */     return hash;
/* 262:    */   }
/* 263:    */   
/* 264:    */   public static int nullSafeHashCode(char[] array)
/* 265:    */   {
/* 266:392 */     if (array == null) {
/* 267:393 */       return 0;
/* 268:    */     }
/* 269:395 */     int hash = 7;
/* 270:396 */     int arraySize = array.length;
/* 271:397 */     for (int i = 0; i < arraySize; i++) {
/* 272:398 */       hash = 31 * hash + array[i];
/* 273:    */     }
/* 274:400 */     return hash;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public static int nullSafeHashCode(double[] array)
/* 278:    */   {
/* 279:408 */     if (array == null) {
/* 280:409 */       return 0;
/* 281:    */     }
/* 282:411 */     int hash = 7;
/* 283:412 */     int arraySize = array.length;
/* 284:413 */     for (int i = 0; i < arraySize; i++) {
/* 285:414 */       hash = 31 * hash + hashCode(array[i]);
/* 286:    */     }
/* 287:416 */     return hash;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public static int nullSafeHashCode(float[] array)
/* 291:    */   {
/* 292:424 */     if (array == null) {
/* 293:425 */       return 0;
/* 294:    */     }
/* 295:427 */     int hash = 7;
/* 296:428 */     int arraySize = array.length;
/* 297:429 */     for (int i = 0; i < arraySize; i++) {
/* 298:430 */       hash = 31 * hash + hashCode(array[i]);
/* 299:    */     }
/* 300:432 */     return hash;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public static int nullSafeHashCode(int[] array)
/* 304:    */   {
/* 305:440 */     if (array == null) {
/* 306:441 */       return 0;
/* 307:    */     }
/* 308:443 */     int hash = 7;
/* 309:444 */     int arraySize = array.length;
/* 310:445 */     for (int i = 0; i < arraySize; i++) {
/* 311:446 */       hash = 31 * hash + array[i];
/* 312:    */     }
/* 313:448 */     return hash;
/* 314:    */   }
/* 315:    */   
/* 316:    */   public static int nullSafeHashCode(long[] array)
/* 317:    */   {
/* 318:456 */     if (array == null) {
/* 319:457 */       return 0;
/* 320:    */     }
/* 321:459 */     int hash = 7;
/* 322:460 */     int arraySize = array.length;
/* 323:461 */     for (int i = 0; i < arraySize; i++) {
/* 324:462 */       hash = 31 * hash + hashCode(array[i]);
/* 325:    */     }
/* 326:464 */     return hash;
/* 327:    */   }
/* 328:    */   
/* 329:    */   public static int nullSafeHashCode(short[] array)
/* 330:    */   {
/* 331:472 */     if (array == null) {
/* 332:473 */       return 0;
/* 333:    */     }
/* 334:475 */     int hash = 7;
/* 335:476 */     int arraySize = array.length;
/* 336:477 */     for (int i = 0; i < arraySize; i++) {
/* 337:478 */       hash = 31 * hash + array[i];
/* 338:    */     }
/* 339:480 */     return hash;
/* 340:    */   }
/* 341:    */   
/* 342:    */   public static int hashCode(boolean bool)
/* 343:    */   {
/* 344:488 */     return bool ? 1231 : 1237;
/* 345:    */   }
/* 346:    */   
/* 347:    */   public static int hashCode(double dbl)
/* 348:    */   {
/* 349:496 */     long bits = Double.doubleToLongBits(dbl);
/* 350:497 */     return hashCode(bits);
/* 351:    */   }
/* 352:    */   
/* 353:    */   public static int hashCode(float flt)
/* 354:    */   {
/* 355:505 */     return Float.floatToIntBits(flt);
/* 356:    */   }
/* 357:    */   
/* 358:    */   public static int hashCode(long lng)
/* 359:    */   {
/* 360:513 */     return (int)(lng ^ lng >>> 32);
/* 361:    */   }
/* 362:    */   
/* 363:    */   public static String identityToString(Object obj)
/* 364:    */   {
/* 365:528 */     if (obj == null) {
/* 366:529 */       return "";
/* 367:    */     }
/* 368:531 */     return obj.getClass().getName() + "@" + getIdentityHexString(obj);
/* 369:    */   }
/* 370:    */   
/* 371:    */   public static String getIdentityHexString(Object obj)
/* 372:    */   {
/* 373:540 */     return Integer.toHexString(System.identityHashCode(obj));
/* 374:    */   }
/* 375:    */   
/* 376:    */   public static String getDisplayString(Object obj)
/* 377:    */   {
/* 378:553 */     if (obj == null) {
/* 379:554 */       return "";
/* 380:    */     }
/* 381:556 */     return nullSafeToString(obj);
/* 382:    */   }
/* 383:    */   
/* 384:    */   public static String nullSafeClassName(Object obj)
/* 385:    */   {
/* 386:566 */     return obj != null ? obj.getClass().getName() : "null";
/* 387:    */   }
/* 388:    */   
/* 389:    */   public static String nullSafeToString(Object obj)
/* 390:    */   {
/* 391:577 */     if (obj == null) {
/* 392:578 */       return "null";
/* 393:    */     }
/* 394:580 */     if ((obj instanceof String)) {
/* 395:581 */       return (String)obj;
/* 396:    */     }
/* 397:583 */     if ((obj instanceof Object[])) {
/* 398:584 */       return nullSafeToString((Object[])obj);
/* 399:    */     }
/* 400:586 */     if ((obj instanceof boolean[])) {
/* 401:587 */       return nullSafeToString((boolean[])obj);
/* 402:    */     }
/* 403:589 */     if ((obj instanceof byte[])) {
/* 404:590 */       return nullSafeToString((byte[])obj);
/* 405:    */     }
/* 406:592 */     if ((obj instanceof char[])) {
/* 407:593 */       return nullSafeToString((char[])obj);
/* 408:    */     }
/* 409:595 */     if ((obj instanceof double[])) {
/* 410:596 */       return nullSafeToString((double[])obj);
/* 411:    */     }
/* 412:598 */     if ((obj instanceof float[])) {
/* 413:599 */       return nullSafeToString((float[])obj);
/* 414:    */     }
/* 415:601 */     if ((obj instanceof int[])) {
/* 416:602 */       return nullSafeToString((int[])obj);
/* 417:    */     }
/* 418:604 */     if ((obj instanceof long[])) {
/* 419:605 */       return nullSafeToString((long[])obj);
/* 420:    */     }
/* 421:607 */     if ((obj instanceof short[])) {
/* 422:608 */       return nullSafeToString((short[])obj);
/* 423:    */     }
/* 424:610 */     String str = obj.toString();
/* 425:611 */     return str != null ? str : "";
/* 426:    */   }
/* 427:    */   
/* 428:    */   public static String nullSafeToString(Object[] array)
/* 429:    */   {
/* 430:624 */     if (array == null) {
/* 431:625 */       return "null";
/* 432:    */     }
/* 433:627 */     int length = array.length;
/* 434:628 */     if (length == 0) {
/* 435:629 */       return "{}";
/* 436:    */     }
/* 437:631 */     StringBuilder sb = new StringBuilder();
/* 438:632 */     for (int i = 0; i < length; i++)
/* 439:    */     {
/* 440:633 */       if (i == 0) {
/* 441:634 */         sb.append("{");
/* 442:    */       } else {
/* 443:637 */         sb.append(", ");
/* 444:    */       }
/* 445:639 */       sb.append(String.valueOf(array[i]));
/* 446:    */     }
/* 447:641 */     sb.append("}");
/* 448:642 */     return sb.toString();
/* 449:    */   }
/* 450:    */   
/* 451:    */   public static String nullSafeToString(boolean[] array)
/* 452:    */   {
/* 453:655 */     if (array == null) {
/* 454:656 */       return "null";
/* 455:    */     }
/* 456:658 */     int length = array.length;
/* 457:659 */     if (length == 0) {
/* 458:660 */       return "{}";
/* 459:    */     }
/* 460:662 */     StringBuilder sb = new StringBuilder();
/* 461:663 */     for (int i = 0; i < length; i++)
/* 462:    */     {
/* 463:664 */       if (i == 0) {
/* 464:665 */         sb.append("{");
/* 465:    */       } else {
/* 466:668 */         sb.append(", ");
/* 467:    */       }
/* 468:671 */       sb.append(array[i]);
/* 469:    */     }
/* 470:673 */     sb.append("}");
/* 471:674 */     return sb.toString();
/* 472:    */   }
/* 473:    */   
/* 474:    */   public static String nullSafeToString(byte[] array)
/* 475:    */   {
/* 476:687 */     if (array == null) {
/* 477:688 */       return "null";
/* 478:    */     }
/* 479:690 */     int length = array.length;
/* 480:691 */     if (length == 0) {
/* 481:692 */       return "{}";
/* 482:    */     }
/* 483:694 */     StringBuilder sb = new StringBuilder();
/* 484:695 */     for (int i = 0; i < length; i++)
/* 485:    */     {
/* 486:696 */       if (i == 0) {
/* 487:697 */         sb.append("{");
/* 488:    */       } else {
/* 489:700 */         sb.append(", ");
/* 490:    */       }
/* 491:702 */       sb.append(array[i]);
/* 492:    */     }
/* 493:704 */     sb.append("}");
/* 494:705 */     return sb.toString();
/* 495:    */   }
/* 496:    */   
/* 497:    */   public static String nullSafeToString(char[] array)
/* 498:    */   {
/* 499:718 */     if (array == null) {
/* 500:719 */       return "null";
/* 501:    */     }
/* 502:721 */     int length = array.length;
/* 503:722 */     if (length == 0) {
/* 504:723 */       return "{}";
/* 505:    */     }
/* 506:725 */     StringBuilder sb = new StringBuilder();
/* 507:726 */     for (int i = 0; i < length; i++)
/* 508:    */     {
/* 509:727 */       if (i == 0) {
/* 510:728 */         sb.append("{");
/* 511:    */       } else {
/* 512:731 */         sb.append(", ");
/* 513:    */       }
/* 514:733 */       sb.append("'").append(array[i]).append("'");
/* 515:    */     }
/* 516:735 */     sb.append("}");
/* 517:736 */     return sb.toString();
/* 518:    */   }
/* 519:    */   
/* 520:    */   public static String nullSafeToString(double[] array)
/* 521:    */   {
/* 522:749 */     if (array == null) {
/* 523:750 */       return "null";
/* 524:    */     }
/* 525:752 */     int length = array.length;
/* 526:753 */     if (length == 0) {
/* 527:754 */       return "{}";
/* 528:    */     }
/* 529:756 */     StringBuilder sb = new StringBuilder();
/* 530:757 */     for (int i = 0; i < length; i++)
/* 531:    */     {
/* 532:758 */       if (i == 0) {
/* 533:759 */         sb.append("{");
/* 534:    */       } else {
/* 535:762 */         sb.append(", ");
/* 536:    */       }
/* 537:765 */       sb.append(array[i]);
/* 538:    */     }
/* 539:767 */     sb.append("}");
/* 540:768 */     return sb.toString();
/* 541:    */   }
/* 542:    */   
/* 543:    */   public static String nullSafeToString(float[] array)
/* 544:    */   {
/* 545:781 */     if (array == null) {
/* 546:782 */       return "null";
/* 547:    */     }
/* 548:784 */     int length = array.length;
/* 549:785 */     if (length == 0) {
/* 550:786 */       return "{}";
/* 551:    */     }
/* 552:788 */     StringBuilder sb = new StringBuilder();
/* 553:789 */     for (int i = 0; i < length; i++)
/* 554:    */     {
/* 555:790 */       if (i == 0) {
/* 556:791 */         sb.append("{");
/* 557:    */       } else {
/* 558:794 */         sb.append(", ");
/* 559:    */       }
/* 560:797 */       sb.append(array[i]);
/* 561:    */     }
/* 562:799 */     sb.append("}");
/* 563:800 */     return sb.toString();
/* 564:    */   }
/* 565:    */   
/* 566:    */   public static String nullSafeToString(int[] array)
/* 567:    */   {
/* 568:813 */     if (array == null) {
/* 569:814 */       return "null";
/* 570:    */     }
/* 571:816 */     int length = array.length;
/* 572:817 */     if (length == 0) {
/* 573:818 */       return "{}";
/* 574:    */     }
/* 575:820 */     StringBuilder sb = new StringBuilder();
/* 576:821 */     for (int i = 0; i < length; i++)
/* 577:    */     {
/* 578:822 */       if (i == 0) {
/* 579:823 */         sb.append("{");
/* 580:    */       } else {
/* 581:826 */         sb.append(", ");
/* 582:    */       }
/* 583:828 */       sb.append(array[i]);
/* 584:    */     }
/* 585:830 */     sb.append("}");
/* 586:831 */     return sb.toString();
/* 587:    */   }
/* 588:    */   
/* 589:    */   public static String nullSafeToString(long[] array)
/* 590:    */   {
/* 591:844 */     if (array == null) {
/* 592:845 */       return "null";
/* 593:    */     }
/* 594:847 */     int length = array.length;
/* 595:848 */     if (length == 0) {
/* 596:849 */       return "{}";
/* 597:    */     }
/* 598:851 */     StringBuilder sb = new StringBuilder();
/* 599:852 */     for (int i = 0; i < length; i++)
/* 600:    */     {
/* 601:853 */       if (i == 0) {
/* 602:854 */         sb.append("{");
/* 603:    */       } else {
/* 604:857 */         sb.append(", ");
/* 605:    */       }
/* 606:859 */       sb.append(array[i]);
/* 607:    */     }
/* 608:861 */     sb.append("}");
/* 609:862 */     return sb.toString();
/* 610:    */   }
/* 611:    */   
/* 612:    */   public static String nullSafeToString(short[] array)
/* 613:    */   {
/* 614:875 */     if (array == null) {
/* 615:876 */       return "null";
/* 616:    */     }
/* 617:878 */     int length = array.length;
/* 618:879 */     if (length == 0) {
/* 619:880 */       return "{}";
/* 620:    */     }
/* 621:882 */     StringBuilder sb = new StringBuilder();
/* 622:883 */     for (int i = 0; i < length; i++)
/* 623:    */     {
/* 624:884 */       if (i == 0) {
/* 625:885 */         sb.append("{");
/* 626:    */       } else {
/* 627:888 */         sb.append(", ");
/* 628:    */       }
/* 629:890 */       sb.append(array[i]);
/* 630:    */     }
/* 631:892 */     sb.append("}");
/* 632:893 */     return sb.toString();
/* 633:    */   }
/* 634:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.ObjectUtils
 * JD-Core Version:    0.7.0.1
 */