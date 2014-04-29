/*    1:     */ package org.springframework.util;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.Arrays;
/*    5:     */ import java.util.Collection;
/*    6:     */ import java.util.Collections;
/*    7:     */ import java.util.Enumeration;
/*    8:     */ import java.util.Iterator;
/*    9:     */ import java.util.LinkedList;
/*   10:     */ import java.util.List;
/*   11:     */ import java.util.Locale;
/*   12:     */ import java.util.Properties;
/*   13:     */ import java.util.Set;
/*   14:     */ import java.util.StringTokenizer;
/*   15:     */ import java.util.TreeSet;
/*   16:     */ 
/*   17:     */ public abstract class StringUtils
/*   18:     */ {
/*   19:     */   private static final String FOLDER_SEPARATOR = "/";
/*   20:     */   private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
/*   21:     */   private static final String TOP_PATH = "..";
/*   22:     */   private static final String CURRENT_PATH = ".";
/*   23:     */   private static final char EXTENSION_SEPARATOR = '.';
/*   24:     */   
/*   25:     */   public static boolean hasLength(CharSequence str)
/*   26:     */   {
/*   27:  86 */     return (str != null) && (str.length() > 0);
/*   28:     */   }
/*   29:     */   
/*   30:     */   public static boolean hasLength(String str)
/*   31:     */   {
/*   32:  97 */     return hasLength(str);
/*   33:     */   }
/*   34:     */   
/*   35:     */   public static boolean hasText(CharSequence str)
/*   36:     */   {
/*   37: 117 */     if (!hasLength(str)) {
/*   38: 118 */       return false;
/*   39:     */     }
/*   40: 120 */     int strLen = str.length();
/*   41: 121 */     for (int i = 0; i < strLen; i++) {
/*   42: 122 */       if (!Character.isWhitespace(str.charAt(i))) {
/*   43: 123 */         return true;
/*   44:     */       }
/*   45:     */     }
/*   46: 126 */     return false;
/*   47:     */   }
/*   48:     */   
/*   49:     */   public static boolean hasText(String str)
/*   50:     */   {
/*   51: 139 */     return hasText(str);
/*   52:     */   }
/*   53:     */   
/*   54:     */   public static boolean containsWhitespace(CharSequence str)
/*   55:     */   {
/*   56: 150 */     if (!hasLength(str)) {
/*   57: 151 */       return false;
/*   58:     */     }
/*   59: 153 */     int strLen = str.length();
/*   60: 154 */     for (int i = 0; i < strLen; i++) {
/*   61: 155 */       if (Character.isWhitespace(str.charAt(i))) {
/*   62: 156 */         return true;
/*   63:     */       }
/*   64:     */     }
/*   65: 159 */     return false;
/*   66:     */   }
/*   67:     */   
/*   68:     */   public static boolean containsWhitespace(String str)
/*   69:     */   {
/*   70: 170 */     return containsWhitespace(str);
/*   71:     */   }
/*   72:     */   
/*   73:     */   public static String trimWhitespace(String str)
/*   74:     */   {
/*   75: 180 */     if (!hasLength(str)) {
/*   76: 181 */       return str;
/*   77:     */     }
/*   78: 183 */     StringBuilder sb = new StringBuilder(str);
/*   79:     */     do
/*   80:     */     {
/*   81: 185 */       sb.deleteCharAt(0);
/*   82: 184 */       if (sb.length() <= 0) {
/*   83:     */         break;
/*   84:     */       }
/*   85: 184 */     } while (Character.isWhitespace(sb.charAt(0)));
/*   86: 187 */     while ((sb.length() > 0) && (Character.isWhitespace(sb.charAt(sb.length() - 1)))) {
/*   87: 188 */       sb.deleteCharAt(sb.length() - 1);
/*   88:     */     }
/*   89: 190 */     return sb.toString();
/*   90:     */   }
/*   91:     */   
/*   92:     */   public static String trimAllWhitespace(String str)
/*   93:     */   {
/*   94: 201 */     if (!hasLength(str)) {
/*   95: 202 */       return str;
/*   96:     */     }
/*   97: 204 */     StringBuilder sb = new StringBuilder(str);
/*   98: 205 */     int index = 0;
/*   99: 206 */     while (sb.length() > index) {
/*  100: 207 */       if (Character.isWhitespace(sb.charAt(index))) {
/*  101: 208 */         sb.deleteCharAt(index);
/*  102:     */       } else {
/*  103: 211 */         index++;
/*  104:     */       }
/*  105:     */     }
/*  106: 214 */     return sb.toString();
/*  107:     */   }
/*  108:     */   
/*  109:     */   public static String trimLeadingWhitespace(String str)
/*  110:     */   {
/*  111: 224 */     if (!hasLength(str)) {
/*  112: 225 */       return str;
/*  113:     */     }
/*  114: 227 */     StringBuilder sb = new StringBuilder(str);
/*  115: 228 */     while ((sb.length() > 0) && (Character.isWhitespace(sb.charAt(0)))) {
/*  116: 229 */       sb.deleteCharAt(0);
/*  117:     */     }
/*  118: 231 */     return sb.toString();
/*  119:     */   }
/*  120:     */   
/*  121:     */   public static String trimTrailingWhitespace(String str)
/*  122:     */   {
/*  123: 241 */     if (!hasLength(str)) {
/*  124: 242 */       return str;
/*  125:     */     }
/*  126: 244 */     StringBuilder sb = new StringBuilder(str);
/*  127: 245 */     while ((sb.length() > 0) && (Character.isWhitespace(sb.charAt(sb.length() - 1)))) {
/*  128: 246 */       sb.deleteCharAt(sb.length() - 1);
/*  129:     */     }
/*  130: 248 */     return sb.toString();
/*  131:     */   }
/*  132:     */   
/*  133:     */   public static String trimLeadingCharacter(String str, char leadingCharacter)
/*  134:     */   {
/*  135: 258 */     if (!hasLength(str)) {
/*  136: 259 */       return str;
/*  137:     */     }
/*  138: 261 */     StringBuilder sb = new StringBuilder(str);
/*  139: 262 */     while ((sb.length() > 0) && (sb.charAt(0) == leadingCharacter)) {
/*  140: 263 */       sb.deleteCharAt(0);
/*  141:     */     }
/*  142: 265 */     return sb.toString();
/*  143:     */   }
/*  144:     */   
/*  145:     */   public static String trimTrailingCharacter(String str, char trailingCharacter)
/*  146:     */   {
/*  147: 275 */     if (!hasLength(str)) {
/*  148: 276 */       return str;
/*  149:     */     }
/*  150: 278 */     StringBuilder sb = new StringBuilder(str);
/*  151: 279 */     while ((sb.length() > 0) && (sb.charAt(sb.length() - 1) == trailingCharacter)) {
/*  152: 280 */       sb.deleteCharAt(sb.length() - 1);
/*  153:     */     }
/*  154: 282 */     return sb.toString();
/*  155:     */   }
/*  156:     */   
/*  157:     */   public static boolean startsWithIgnoreCase(String str, String prefix)
/*  158:     */   {
/*  159: 294 */     if ((str == null) || (prefix == null)) {
/*  160: 295 */       return false;
/*  161:     */     }
/*  162: 297 */     if (str.startsWith(prefix)) {
/*  163: 298 */       return true;
/*  164:     */     }
/*  165: 300 */     if (str.length() < prefix.length()) {
/*  166: 301 */       return false;
/*  167:     */     }
/*  168: 303 */     String lcStr = str.substring(0, prefix.length()).toLowerCase();
/*  169: 304 */     String lcPrefix = prefix.toLowerCase();
/*  170: 305 */     return lcStr.equals(lcPrefix);
/*  171:     */   }
/*  172:     */   
/*  173:     */   public static boolean endsWithIgnoreCase(String str, String suffix)
/*  174:     */   {
/*  175: 316 */     if ((str == null) || (suffix == null)) {
/*  176: 317 */       return false;
/*  177:     */     }
/*  178: 319 */     if (str.endsWith(suffix)) {
/*  179: 320 */       return true;
/*  180:     */     }
/*  181: 322 */     if (str.length() < suffix.length()) {
/*  182: 323 */       return false;
/*  183:     */     }
/*  184: 326 */     String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
/*  185: 327 */     String lcSuffix = suffix.toLowerCase();
/*  186: 328 */     return lcStr.equals(lcSuffix);
/*  187:     */   }
/*  188:     */   
/*  189:     */   public static boolean substringMatch(CharSequence str, int index, CharSequence substring)
/*  190:     */   {
/*  191: 339 */     for (int j = 0; j < substring.length(); j++)
/*  192:     */     {
/*  193: 340 */       int i = index + j;
/*  194: 341 */       if ((i >= str.length()) || (str.charAt(i) != substring.charAt(j))) {
/*  195: 342 */         return false;
/*  196:     */       }
/*  197:     */     }
/*  198: 345 */     return true;
/*  199:     */   }
/*  200:     */   
/*  201:     */   public static int countOccurrencesOf(String str, String sub)
/*  202:     */   {
/*  203: 354 */     if ((str == null) || (sub == null) || (str.length() == 0) || (sub.length() == 0)) {
/*  204: 355 */       return 0;
/*  205:     */     }
/*  206: 357 */     int count = 0;
/*  207: 358 */     int pos = 0;
/*  208:     */     int idx;
/*  209: 360 */     while ((idx = str.indexOf(sub, pos)) != -1)
/*  210:     */     {
/*  211:     */       int idx;
/*  212: 361 */       count++;
/*  213: 362 */       pos = idx + sub.length();
/*  214:     */     }
/*  215: 364 */     return count;
/*  216:     */   }
/*  217:     */   
/*  218:     */   public static String replace(String inString, String oldPattern, String newPattern)
/*  219:     */   {
/*  220: 376 */     if ((!hasLength(inString)) || (!hasLength(oldPattern)) || (newPattern == null)) {
/*  221: 377 */       return inString;
/*  222:     */     }
/*  223: 379 */     StringBuilder sb = new StringBuilder();
/*  224: 380 */     int pos = 0;
/*  225: 381 */     int index = inString.indexOf(oldPattern);
/*  226:     */     
/*  227: 383 */     int patLen = oldPattern.length();
/*  228: 384 */     while (index >= 0)
/*  229:     */     {
/*  230: 385 */       sb.append(inString.substring(pos, index));
/*  231: 386 */       sb.append(newPattern);
/*  232: 387 */       pos = index + patLen;
/*  233: 388 */       index = inString.indexOf(oldPattern, pos);
/*  234:     */     }
/*  235: 390 */     sb.append(inString.substring(pos));
/*  236:     */     
/*  237: 392 */     return sb.toString();
/*  238:     */   }
/*  239:     */   
/*  240:     */   public static String delete(String inString, String pattern)
/*  241:     */   {
/*  242: 402 */     return replace(inString, pattern, "");
/*  243:     */   }
/*  244:     */   
/*  245:     */   public static String deleteAny(String inString, String charsToDelete)
/*  246:     */   {
/*  247: 413 */     if ((!hasLength(inString)) || (!hasLength(charsToDelete))) {
/*  248: 414 */       return inString;
/*  249:     */     }
/*  250: 416 */     StringBuilder sb = new StringBuilder();
/*  251: 417 */     for (int i = 0; i < inString.length(); i++)
/*  252:     */     {
/*  253: 418 */       char c = inString.charAt(i);
/*  254: 419 */       if (charsToDelete.indexOf(c) == -1) {
/*  255: 420 */         sb.append(c);
/*  256:     */       }
/*  257:     */     }
/*  258: 423 */     return sb.toString();
/*  259:     */   }
/*  260:     */   
/*  261:     */   public static String quote(String str)
/*  262:     */   {
/*  263: 438 */     return str != null ? "'" + str + "'" : null;
/*  264:     */   }
/*  265:     */   
/*  266:     */   public static Object quoteIfString(Object obj)
/*  267:     */   {
/*  268: 449 */     return (obj instanceof String) ? quote((String)obj) : obj;
/*  269:     */   }
/*  270:     */   
/*  271:     */   public static String unqualify(String qualifiedName)
/*  272:     */   {
/*  273: 458 */     return unqualify(qualifiedName, '.');
/*  274:     */   }
/*  275:     */   
/*  276:     */   public static String unqualify(String qualifiedName, char separator)
/*  277:     */   {
/*  278: 468 */     return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
/*  279:     */   }
/*  280:     */   
/*  281:     */   public static String capitalize(String str)
/*  282:     */   {
/*  283: 479 */     return changeFirstCharacterCase(str, true);
/*  284:     */   }
/*  285:     */   
/*  286:     */   public static String uncapitalize(String str)
/*  287:     */   {
/*  288: 490 */     return changeFirstCharacterCase(str, false);
/*  289:     */   }
/*  290:     */   
/*  291:     */   private static String changeFirstCharacterCase(String str, boolean capitalize)
/*  292:     */   {
/*  293: 494 */     if ((str == null) || (str.length() == 0)) {
/*  294: 495 */       return str;
/*  295:     */     }
/*  296: 497 */     StringBuilder sb = new StringBuilder(str.length());
/*  297: 498 */     if (capitalize) {
/*  298: 499 */       sb.append(Character.toUpperCase(str.charAt(0)));
/*  299:     */     } else {
/*  300: 502 */       sb.append(Character.toLowerCase(str.charAt(0)));
/*  301:     */     }
/*  302: 504 */     sb.append(str.substring(1));
/*  303: 505 */     return sb.toString();
/*  304:     */   }
/*  305:     */   
/*  306:     */   public static String getFilename(String path)
/*  307:     */   {
/*  308: 515 */     if (path == null) {
/*  309: 516 */       return null;
/*  310:     */     }
/*  311: 518 */     int separatorIndex = path.lastIndexOf("/");
/*  312: 519 */     return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
/*  313:     */   }
/*  314:     */   
/*  315:     */   public static String getFilenameExtension(String path)
/*  316:     */   {
/*  317: 529 */     if (path == null) {
/*  318: 530 */       return null;
/*  319:     */     }
/*  320: 532 */     int extIndex = path.lastIndexOf('.');
/*  321: 533 */     if (extIndex == -1) {
/*  322: 534 */       return null;
/*  323:     */     }
/*  324: 536 */     int folderIndex = path.lastIndexOf("/");
/*  325: 537 */     if (folderIndex > extIndex) {
/*  326: 538 */       return null;
/*  327:     */     }
/*  328: 540 */     return path.substring(extIndex + 1);
/*  329:     */   }
/*  330:     */   
/*  331:     */   public static String stripFilenameExtension(String path)
/*  332:     */   {
/*  333: 551 */     if (path == null) {
/*  334: 552 */       return null;
/*  335:     */     }
/*  336: 554 */     int extIndex = path.lastIndexOf('.');
/*  337: 555 */     if (extIndex == -1) {
/*  338: 556 */       return path;
/*  339:     */     }
/*  340: 558 */     int folderIndex = path.lastIndexOf("/");
/*  341: 559 */     if (folderIndex > extIndex) {
/*  342: 560 */       return path;
/*  343:     */     }
/*  344: 562 */     return path.substring(0, extIndex);
/*  345:     */   }
/*  346:     */   
/*  347:     */   public static String applyRelativePath(String path, String relativePath)
/*  348:     */   {
/*  349: 574 */     int separatorIndex = path.lastIndexOf("/");
/*  350: 575 */     if (separatorIndex != -1)
/*  351:     */     {
/*  352: 576 */       String newPath = path.substring(0, separatorIndex);
/*  353: 577 */       if (!relativePath.startsWith("/")) {
/*  354: 578 */         newPath = newPath + "/";
/*  355:     */       }
/*  356: 580 */       return newPath + relativePath;
/*  357:     */     }
/*  358: 583 */     return relativePath;
/*  359:     */   }
/*  360:     */   
/*  361:     */   public static String cleanPath(String path)
/*  362:     */   {
/*  363: 596 */     if (path == null) {
/*  364: 597 */       return null;
/*  365:     */     }
/*  366: 599 */     String pathToUse = replace(path, "\\", "/");
/*  367:     */     
/*  368:     */ 
/*  369:     */ 
/*  370:     */ 
/*  371:     */ 
/*  372: 605 */     int prefixIndex = pathToUse.indexOf(":");
/*  373: 606 */     String prefix = "";
/*  374: 607 */     if (prefixIndex != -1)
/*  375:     */     {
/*  376: 608 */       prefix = pathToUse.substring(0, prefixIndex + 1);
/*  377: 609 */       pathToUse = pathToUse.substring(prefixIndex + 1);
/*  378:     */     }
/*  379: 611 */     if (pathToUse.startsWith("/"))
/*  380:     */     {
/*  381: 612 */       prefix = prefix + "/";
/*  382: 613 */       pathToUse = pathToUse.substring(1);
/*  383:     */     }
/*  384: 616 */     String[] pathArray = delimitedListToStringArray(pathToUse, "/");
/*  385: 617 */     List<String> pathElements = new LinkedList();
/*  386: 618 */     int tops = 0;
/*  387: 620 */     for (int i = pathArray.length - 1; i >= 0; i--)
/*  388:     */     {
/*  389: 621 */       String element = pathArray[i];
/*  390: 622 */       if (!".".equals(element)) {
/*  391: 625 */         if ("..".equals(element)) {
/*  392: 627 */           tops++;
/*  393: 630 */         } else if (tops > 0) {
/*  394: 632 */           tops--;
/*  395:     */         } else {
/*  396: 636 */           pathElements.add(0, element);
/*  397:     */         }
/*  398:     */       }
/*  399:     */     }
/*  400: 642 */     for (int i = 0; i < tops; i++) {
/*  401: 643 */       pathElements.add(0, "..");
/*  402:     */     }
/*  403: 646 */     return prefix + collectionToDelimitedString(pathElements, "/");
/*  404:     */   }
/*  405:     */   
/*  406:     */   public static boolean pathEquals(String path1, String path2)
/*  407:     */   {
/*  408: 656 */     return cleanPath(path1).equals(cleanPath(path2));
/*  409:     */   }
/*  410:     */   
/*  411:     */   public static Locale parseLocaleString(String localeString)
/*  412:     */   {
/*  413: 668 */     for (int i = 0; i < localeString.length(); i++)
/*  414:     */     {
/*  415: 669 */       char ch = localeString.charAt(i);
/*  416: 670 */       if ((ch != '_') && (ch != ' ') && (!Character.isLetterOrDigit(ch))) {
/*  417: 671 */         throw new IllegalArgumentException(
/*  418: 672 */           "Locale value \"" + localeString + "\" contains invalid characters");
/*  419:     */       }
/*  420:     */     }
/*  421: 675 */     String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
/*  422: 676 */     String language = parts.length > 0 ? parts[0] : "";
/*  423: 677 */     String country = parts.length > 1 ? parts[1] : "";
/*  424: 678 */     String variant = "";
/*  425: 679 */     if (parts.length >= 2)
/*  426:     */     {
/*  427: 682 */       int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
/*  428:     */       
/*  429: 684 */       variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
/*  430: 685 */       if (variant.startsWith("_")) {
/*  431: 686 */         variant = trimLeadingCharacter(variant, '_');
/*  432:     */       }
/*  433:     */     }
/*  434: 689 */     return language.length() > 0 ? new Locale(language, country, variant) : null;
/*  435:     */   }
/*  436:     */   
/*  437:     */   public static String toLanguageTag(Locale locale)
/*  438:     */   {
/*  439: 699 */     return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
/*  440:     */   }
/*  441:     */   
/*  442:     */   public static String[] addStringToArray(String[] array, String str)
/*  443:     */   {
/*  444: 715 */     if (ObjectUtils.isEmpty(array)) {
/*  445: 716 */       return new String[] { str };
/*  446:     */     }
/*  447: 718 */     String[] newArr = new String[array.length + 1];
/*  448: 719 */     System.arraycopy(array, 0, newArr, 0, array.length);
/*  449: 720 */     newArr[array.length] = str;
/*  450: 721 */     return newArr;
/*  451:     */   }
/*  452:     */   
/*  453:     */   public static String[] concatenateStringArrays(String[] array1, String[] array2)
/*  454:     */   {
/*  455: 733 */     if (ObjectUtils.isEmpty(array1)) {
/*  456: 734 */       return array2;
/*  457:     */     }
/*  458: 736 */     if (ObjectUtils.isEmpty(array2)) {
/*  459: 737 */       return array1;
/*  460:     */     }
/*  461: 739 */     String[] newArr = new String[array1.length + array2.length];
/*  462: 740 */     System.arraycopy(array1, 0, newArr, 0, array1.length);
/*  463: 741 */     System.arraycopy(array2, 0, newArr, array1.length, array2.length);
/*  464: 742 */     return newArr;
/*  465:     */   }
/*  466:     */   
/*  467:     */   public static String[] mergeStringArrays(String[] array1, String[] array2)
/*  468:     */   {
/*  469: 756 */     if (ObjectUtils.isEmpty(array1)) {
/*  470: 757 */       return array2;
/*  471:     */     }
/*  472: 759 */     if (ObjectUtils.isEmpty(array2)) {
/*  473: 760 */       return array1;
/*  474:     */     }
/*  475: 762 */     List<String> result = new ArrayList();
/*  476: 763 */     result.addAll((Collection)Arrays.asList(array1));
/*  477: 764 */     for (String str : array2) {
/*  478: 765 */       if (!result.contains(str)) {
/*  479: 766 */         result.add(str);
/*  480:     */       }
/*  481:     */     }
/*  482: 769 */     return toStringArray(result);
/*  483:     */   }
/*  484:     */   
/*  485:     */   public static String[] sortStringArray(String[] array)
/*  486:     */   {
/*  487: 778 */     if (ObjectUtils.isEmpty(array)) {
/*  488: 779 */       return new String[0];
/*  489:     */     }
/*  490: 781 */     Arrays.sort(array);
/*  491: 782 */     return array;
/*  492:     */   }
/*  493:     */   
/*  494:     */   public static String[] toStringArray(Collection<String> collection)
/*  495:     */   {
/*  496: 793 */     if (collection == null) {
/*  497: 794 */       return null;
/*  498:     */     }
/*  499: 796 */     return (String[])collection.toArray(new String[collection.size()]);
/*  500:     */   }
/*  501:     */   
/*  502:     */   public static String[] toStringArray(Enumeration<String> enumeration)
/*  503:     */   {
/*  504: 807 */     if (enumeration == null) {
/*  505: 808 */       return null;
/*  506:     */     }
/*  507: 810 */     List<String> list = (List)Collections.list(enumeration);
/*  508: 811 */     return (String[])list.toArray(new String[list.size()]);
/*  509:     */   }
/*  510:     */   
/*  511:     */   public static String[] trimArrayElements(String[] array)
/*  512:     */   {
/*  513: 821 */     if (ObjectUtils.isEmpty(array)) {
/*  514: 822 */       return new String[0];
/*  515:     */     }
/*  516: 824 */     String[] result = new String[array.length];
/*  517: 825 */     for (int i = 0; i < array.length; i++)
/*  518:     */     {
/*  519: 826 */       String element = array[i];
/*  520: 827 */       result[i] = (element != null ? element.trim() : null);
/*  521:     */     }
/*  522: 829 */     return result;
/*  523:     */   }
/*  524:     */   
/*  525:     */   public static String[] removeDuplicateStrings(String[] array)
/*  526:     */   {
/*  527: 839 */     if (ObjectUtils.isEmpty(array)) {
/*  528: 840 */       return array;
/*  529:     */     }
/*  530: 842 */     Set<String> set = new TreeSet();
/*  531: 843 */     String[] arrayOfString = array;int j = array.length;
/*  532: 843 */     for (int i = 0; i < j; i++)
/*  533:     */     {
/*  534: 843 */       String element = arrayOfString[i];
/*  535: 844 */       set.add(element);
/*  536:     */     }
/*  537: 846 */     return toStringArray(set);
/*  538:     */   }
/*  539:     */   
/*  540:     */   public static String[] split(String toSplit, String delimiter)
/*  541:     */   {
/*  542: 859 */     if ((!hasLength(toSplit)) || (!hasLength(delimiter))) {
/*  543: 860 */       return null;
/*  544:     */     }
/*  545: 862 */     int offset = toSplit.indexOf(delimiter);
/*  546: 863 */     if (offset < 0) {
/*  547: 864 */       return null;
/*  548:     */     }
/*  549: 866 */     String beforeDelimiter = toSplit.substring(0, offset);
/*  550: 867 */     String afterDelimiter = toSplit.substring(offset + delimiter.length());
/*  551: 868 */     return new String[] { beforeDelimiter, afterDelimiter };
/*  552:     */   }
/*  553:     */   
/*  554:     */   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter)
/*  555:     */   {
/*  556: 883 */     return splitArrayElementsIntoProperties(array, delimiter, null);
/*  557:     */   }
/*  558:     */   
/*  559:     */   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete)
/*  560:     */   {
/*  561: 903 */     if (ObjectUtils.isEmpty(array)) {
/*  562: 904 */       return null;
/*  563:     */     }
/*  564: 906 */     Properties result = new Properties();
/*  565: 907 */     String[] arrayOfString1 = array;int j = array.length;
/*  566: 907 */     for (int i = 0; i < j; i++)
/*  567:     */     {
/*  568: 907 */       String element = arrayOfString1[i];
/*  569: 908 */       if (charsToDelete != null) {
/*  570: 909 */         element = deleteAny(element, charsToDelete);
/*  571:     */       }
/*  572: 911 */       String[] splittedElement = split(element, delimiter);
/*  573: 912 */       if (splittedElement != null) {
/*  574: 915 */         result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
/*  575:     */       }
/*  576:     */     }
/*  577: 917 */     return result;
/*  578:     */   }
/*  579:     */   
/*  580:     */   public static String[] tokenizeToStringArray(String str, String delimiters)
/*  581:     */   {
/*  582: 936 */     return tokenizeToStringArray(str, delimiters, true, true);
/*  583:     */   }
/*  584:     */   
/*  585:     */   public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens)
/*  586:     */   {
/*  587: 961 */     if (str == null) {
/*  588: 962 */       return null;
/*  589:     */     }
/*  590: 964 */     StringTokenizer st = new StringTokenizer(str, delimiters);
/*  591: 965 */     List<String> tokens = new ArrayList();
/*  592: 966 */     while (st.hasMoreTokens())
/*  593:     */     {
/*  594: 967 */       String token = st.nextToken();
/*  595: 968 */       if (trimTokens) {
/*  596: 969 */         token = token.trim();
/*  597:     */       }
/*  598: 971 */       if ((!ignoreEmptyTokens) || (token.length() > 0)) {
/*  599: 972 */         tokens.add(token);
/*  600:     */       }
/*  601:     */     }
/*  602: 975 */     return toStringArray(tokens);
/*  603:     */   }
/*  604:     */   
/*  605:     */   public static String[] delimitedListToStringArray(String str, String delimiter)
/*  606:     */   {
/*  607: 990 */     return delimitedListToStringArray(str, delimiter, null);
/*  608:     */   }
/*  609:     */   
/*  610:     */   public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete)
/*  611:     */   {
/*  612:1007 */     if (str == null) {
/*  613:1008 */       return new String[0];
/*  614:     */     }
/*  615:1010 */     if (delimiter == null) {
/*  616:1011 */       return new String[] { str };
/*  617:     */     }
/*  618:1013 */     List<String> result = new ArrayList();
/*  619:1014 */     if ("".equals(delimiter))
/*  620:     */     {
/*  621:1015 */       for (int i = 0; i < str.length(); i++) {
/*  622:1016 */         result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
/*  623:     */       }
/*  624:     */     }
/*  625:     */     else
/*  626:     */     {
/*  627:1020 */       int pos = 0;
/*  628:     */       int delPos;
/*  629:1022 */       while ((delPos = str.indexOf(delimiter, pos)) != -1)
/*  630:     */       {
/*  631:     */         int delPos;
/*  632:1023 */         result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
/*  633:1024 */         pos = delPos + delimiter.length();
/*  634:     */       }
/*  635:1026 */       if ((str.length() > 0) && (pos <= str.length())) {
/*  636:1028 */         result.add(deleteAny(str.substring(pos), charsToDelete));
/*  637:     */       }
/*  638:     */     }
/*  639:1031 */     return toStringArray(result);
/*  640:     */   }
/*  641:     */   
/*  642:     */   public static String[] commaDelimitedListToStringArray(String str)
/*  643:     */   {
/*  644:1040 */     return delimitedListToStringArray(str, ",");
/*  645:     */   }
/*  646:     */   
/*  647:     */   public static Set<String> commaDelimitedListToSet(String str)
/*  648:     */   {
/*  649:1050 */     Set<String> set = new TreeSet();
/*  650:1051 */     String[] tokens = commaDelimitedListToStringArray(str);
/*  651:1052 */     for (String token : tokens) {
/*  652:1053 */       set.add(token);
/*  653:     */     }
/*  654:1055 */     return set;
/*  655:     */   }
/*  656:     */   
/*  657:     */   public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix)
/*  658:     */   {
/*  659:1068 */     if (CollectionUtils.isEmpty(coll)) {
/*  660:1069 */       return "";
/*  661:     */     }
/*  662:1071 */     StringBuilder sb = new StringBuilder();
/*  663:1072 */     Iterator<?> it = coll.iterator();
/*  664:1073 */     while (it.hasNext())
/*  665:     */     {
/*  666:1074 */       sb.append(prefix).append(it.next()).append(suffix);
/*  667:1075 */       if (it.hasNext()) {
/*  668:1076 */         sb.append(delim);
/*  669:     */       }
/*  670:     */     }
/*  671:1079 */     return sb.toString();
/*  672:     */   }
/*  673:     */   
/*  674:     */   public static String collectionToDelimitedString(Collection<?> coll, String delim)
/*  675:     */   {
/*  676:1090 */     return collectionToDelimitedString(coll, delim, "", "");
/*  677:     */   }
/*  678:     */   
/*  679:     */   public static String collectionToCommaDelimitedString(Collection<?> coll)
/*  680:     */   {
/*  681:1100 */     return collectionToDelimitedString(coll, ",");
/*  682:     */   }
/*  683:     */   
/*  684:     */   public static String arrayToDelimitedString(Object[] arr, String delim)
/*  685:     */   {
/*  686:1111 */     if (ObjectUtils.isEmpty(arr)) {
/*  687:1112 */       return "";
/*  688:     */     }
/*  689:1114 */     if (arr.length == 1) {
/*  690:1115 */       return ObjectUtils.nullSafeToString(arr[0]);
/*  691:     */     }
/*  692:1117 */     StringBuilder sb = new StringBuilder();
/*  693:1118 */     for (int i = 0; i < arr.length; i++)
/*  694:     */     {
/*  695:1119 */       if (i > 0) {
/*  696:1120 */         sb.append(delim);
/*  697:     */       }
/*  698:1122 */       sb.append(arr[i]);
/*  699:     */     }
/*  700:1124 */     return sb.toString();
/*  701:     */   }
/*  702:     */   
/*  703:     */   public static String arrayToCommaDelimitedString(Object[] arr)
/*  704:     */   {
/*  705:1134 */     return arrayToDelimitedString(arr, ",");
/*  706:     */   }
/*  707:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.StringUtils
 * JD-Core Version:    0.7.0.1
 */