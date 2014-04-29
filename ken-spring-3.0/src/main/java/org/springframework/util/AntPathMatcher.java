/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.util.Comparator;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.regex.Matcher;
/*   7:    */ import java.util.regex.Pattern;
/*   8:    */ 
/*   9:    */ public class AntPathMatcher
/*  10:    */   implements PathMatcher
/*  11:    */ {
/*  12: 49 */   private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?\\}");
/*  13:    */   public static final String DEFAULT_PATH_SEPARATOR = "/";
/*  14: 54 */   private String pathSeparator = "/";
/*  15:    */   
/*  16:    */   public void setPathSeparator(String pathSeparator)
/*  17:    */   {
/*  18: 59 */     this.pathSeparator = (pathSeparator != null ? pathSeparator : "/");
/*  19:    */   }
/*  20:    */   
/*  21:    */   public boolean isPattern(String path)
/*  22:    */   {
/*  23: 64 */     return (path.indexOf('*') != -1) || (path.indexOf('?') != -1);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public boolean match(String pattern, String path)
/*  27:    */   {
/*  28: 68 */     return doMatch(pattern, path, true, null);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean matchStart(String pattern, String path)
/*  32:    */   {
/*  33: 72 */     return doMatch(pattern, path, false, null);
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected boolean doMatch(String pattern, String path, boolean fullMatch, Map<String, String> uriTemplateVariables)
/*  37:    */   {
/*  38: 87 */     if (path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
/*  39: 88 */       return false;
/*  40:    */     }
/*  41: 91 */     String[] pattDirs = StringUtils.tokenizeToStringArray(pattern, this.pathSeparator);
/*  42: 92 */     String[] pathDirs = StringUtils.tokenizeToStringArray(path, this.pathSeparator);
/*  43:    */     
/*  44: 94 */     int pattIdxStart = 0;
/*  45: 95 */     int pattIdxEnd = pattDirs.length - 1;
/*  46: 96 */     int pathIdxStart = 0;
/*  47: 97 */     int pathIdxEnd = pathDirs.length - 1;
/*  48:100 */     while ((pattIdxStart <= pattIdxEnd) && (pathIdxStart <= pathIdxEnd))
/*  49:    */     {
/*  50:101 */       String patDir = pattDirs[pattIdxStart];
/*  51:102 */       if ("**".equals(patDir)) {
/*  52:    */         break;
/*  53:    */       }
/*  54:105 */       if (!matchStrings(patDir, pathDirs[pathIdxStart], uriTemplateVariables)) {
/*  55:106 */         return false;
/*  56:    */       }
/*  57:108 */       pattIdxStart++;
/*  58:109 */       pathIdxStart++;
/*  59:    */     }
/*  60:112 */     if (pathIdxStart > pathIdxEnd)
/*  61:    */     {
/*  62:114 */       if (pattIdxStart > pattIdxEnd) {
/*  63:115 */         return 
/*  64:116 */           path.endsWith(this.pathSeparator) ? false : pattern.endsWith(this.pathSeparator) ? path.endsWith(this.pathSeparator) : true;
/*  65:    */       }
/*  66:118 */       if (!fullMatch) {
/*  67:119 */         return true;
/*  68:    */       }
/*  69:121 */       if ((pattIdxStart == pattIdxEnd) && (pattDirs[pattIdxStart].equals("*")) && (path.endsWith(this.pathSeparator))) {
/*  70:122 */         return true;
/*  71:    */       }
/*  72:124 */       for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
/*  73:125 */         if (!pattDirs[i].equals("**")) {
/*  74:126 */           return false;
/*  75:    */         }
/*  76:    */       }
/*  77:129 */       return true;
/*  78:    */     }
/*  79:131 */     if (pattIdxStart > pattIdxEnd) {
/*  80:133 */       return false;
/*  81:    */     }
/*  82:135 */     if ((!fullMatch) && ("**".equals(pattDirs[pattIdxStart]))) {
/*  83:137 */       return true;
/*  84:    */     }
/*  85:141 */     while ((pattIdxStart <= pattIdxEnd) && (pathIdxStart <= pathIdxEnd))
/*  86:    */     {
/*  87:142 */       String patDir = pattDirs[pattIdxEnd];
/*  88:143 */       if (patDir.equals("**")) {
/*  89:    */         break;
/*  90:    */       }
/*  91:146 */       if (!matchStrings(patDir, pathDirs[pathIdxEnd], uriTemplateVariables)) {
/*  92:147 */         return false;
/*  93:    */       }
/*  94:149 */       pattIdxEnd--;
/*  95:150 */       pathIdxEnd--;
/*  96:    */     }
/*  97:152 */     if (pathIdxStart > pathIdxEnd)
/*  98:    */     {
/*  99:154 */       for (i = pattIdxStart; i <= pattIdxEnd; i++) {
/* 100:155 */         if (!pattDirs[i].equals("**")) {
/* 101:156 */           return false;
/* 102:    */         }
/* 103:    */       }
/* 104:159 */       return true;
/* 105:    */     }
/* 106:162 */     while ((pattIdxStart != pattIdxEnd) && (pathIdxStart <= pathIdxEnd))
/* 107:    */     {
/* 108:    */       int i;
/* 109:163 */       int patIdxTmp = -1;
/* 110:164 */       for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
/* 111:165 */         if (pattDirs[i].equals("**"))
/* 112:    */         {
/* 113:166 */           patIdxTmp = i;
/* 114:167 */           break;
/* 115:    */         }
/* 116:    */       }
/* 117:170 */       if (patIdxTmp == pattIdxStart + 1)
/* 118:    */       {
/* 119:172 */         pattIdxStart++;
/* 120:    */       }
/* 121:    */       else
/* 122:    */       {
/* 123:177 */         int patLength = patIdxTmp - pattIdxStart - 1;
/* 124:178 */         int strLength = pathIdxEnd - pathIdxStart + 1;
/* 125:179 */         int foundIdx = -1;
/* 126:182 */         for (int i = 0; i <= strLength - patLength; i++)
/* 127:    */         {
/* 128:183 */           for (int j = 0; j < patLength; j++)
/* 129:    */           {
/* 130:184 */             String subPat = pattDirs[(pattIdxStart + j + 1)];
/* 131:185 */             String subStr = pathDirs[(pathIdxStart + i + j)];
/* 132:186 */             if (!matchStrings(subPat, subStr, uriTemplateVariables)) {
/* 133:    */               break;
/* 134:    */             }
/* 135:    */           }
/* 136:190 */           foundIdx = pathIdxStart + i;
/* 137:191 */           break;
/* 138:    */         }
/* 139:194 */         if (foundIdx == -1) {
/* 140:195 */           return false;
/* 141:    */         }
/* 142:198 */         pattIdxStart = patIdxTmp;
/* 143:199 */         pathIdxStart = foundIdx + patLength;
/* 144:    */       }
/* 145:    */     }
/* 146:202 */     for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
/* 147:203 */       if (!pattDirs[i].equals("**")) {
/* 148:204 */         return false;
/* 149:    */       }
/* 150:    */     }
/* 151:208 */     return true;
/* 152:    */   }
/* 153:    */   
/* 154:    */   private boolean matchStrings(String pattern, String str, Map<String, String> uriTemplateVariables)
/* 155:    */   {
/* 156:219 */     AntPathStringMatcher matcher = new AntPathStringMatcher(pattern, str, uriTemplateVariables);
/* 157:220 */     return matcher.matchStrings();
/* 158:    */   }
/* 159:    */   
/* 160:    */   public String extractPathWithinPattern(String pattern, String path)
/* 161:    */   {
/* 162:237 */     String[] patternParts = StringUtils.tokenizeToStringArray(pattern, this.pathSeparator);
/* 163:238 */     String[] pathParts = StringUtils.tokenizeToStringArray(path, this.pathSeparator);
/* 164:    */     
/* 165:240 */     StringBuilder builder = new StringBuilder();
/* 166:    */     
/* 167:    */ 
/* 168:243 */     int puts = 0;
/* 169:244 */     for (int i = 0; i < patternParts.length; i++)
/* 170:    */     {
/* 171:245 */       String patternPart = patternParts[i];
/* 172:246 */       if (((patternPart.indexOf('*') > -1) || (patternPart.indexOf('?') > -1)) && (pathParts.length >= i + 1))
/* 173:    */       {
/* 174:247 */         if ((puts > 0) || ((i == 0) && (!pattern.startsWith(this.pathSeparator)))) {
/* 175:248 */           builder.append(this.pathSeparator);
/* 176:    */         }
/* 177:250 */         builder.append(pathParts[i]);
/* 178:251 */         puts++;
/* 179:    */       }
/* 180:    */     }
/* 181:256 */     for (int i = patternParts.length; i < pathParts.length; i++)
/* 182:    */     {
/* 183:257 */       if ((puts > 0) || (i > 0)) {
/* 184:258 */         builder.append(this.pathSeparator);
/* 185:    */       }
/* 186:260 */       builder.append(pathParts[i]);
/* 187:    */     }
/* 188:263 */     return builder.toString();
/* 189:    */   }
/* 190:    */   
/* 191:    */   public Map<String, String> extractUriTemplateVariables(String pattern, String path)
/* 192:    */   {
/* 193:267 */     Map<String, String> variables = new LinkedHashMap();
/* 194:268 */     boolean result = doMatch(pattern, path, true, variables);
/* 195:269 */     Assert.state(result, "Pattern \"" + pattern + "\" is not a match for \"" + path + "\"");
/* 196:270 */     return variables;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public String combine(String pattern1, String pattern2)
/* 200:    */   {
/* 201:293 */     if ((!StringUtils.hasText(pattern1)) && (!StringUtils.hasText(pattern2))) {
/* 202:294 */       return "";
/* 203:    */     }
/* 204:296 */     if (!StringUtils.hasText(pattern1)) {
/* 205:297 */       return pattern2;
/* 206:    */     }
/* 207:299 */     if (!StringUtils.hasText(pattern2)) {
/* 208:300 */       return pattern1;
/* 209:    */     }
/* 210:302 */     if (match(pattern1, pattern2)) {
/* 211:303 */       return pattern2;
/* 212:    */     }
/* 213:305 */     if (pattern1.endsWith("/*"))
/* 214:    */     {
/* 215:306 */       if (pattern2.startsWith("/")) {
/* 216:308 */         return pattern1.substring(0, pattern1.length() - 1) + pattern2.substring(1);
/* 217:    */       }
/* 218:312 */       return pattern1.substring(0, pattern1.length() - 1) + pattern2;
/* 219:    */     }
/* 220:315 */     if (pattern1.endsWith("/**"))
/* 221:    */     {
/* 222:316 */       if (pattern2.startsWith("/")) {
/* 223:318 */         return pattern1 + pattern2;
/* 224:    */       }
/* 225:322 */       return pattern1 + "/" + pattern2;
/* 226:    */     }
/* 227:326 */     int dotPos1 = pattern1.indexOf('.');
/* 228:327 */     if (dotPos1 == -1)
/* 229:    */     {
/* 230:329 */       if ((pattern1.endsWith("/")) || (pattern2.startsWith("/"))) {
/* 231:330 */         return pattern1 + pattern2;
/* 232:    */       }
/* 233:333 */       return pattern1 + "/" + pattern2;
/* 234:    */     }
/* 235:336 */     String fileName1 = pattern1.substring(0, dotPos1);
/* 236:337 */     String extension1 = pattern1.substring(dotPos1);
/* 237:    */     
/* 238:    */ 
/* 239:340 */     int dotPos2 = pattern2.indexOf('.');
/* 240:    */     String extension2;
/* 241:    */     String fileName2;
/* 242:    */     String extension2;
/* 243:341 */     if (dotPos2 != -1)
/* 244:    */     {
/* 245:342 */       String fileName2 = pattern2.substring(0, dotPos2);
/* 246:343 */       extension2 = pattern2.substring(dotPos2);
/* 247:    */     }
/* 248:    */     else
/* 249:    */     {
/* 250:346 */       fileName2 = pattern2;
/* 251:347 */       extension2 = "";
/* 252:    */     }
/* 253:349 */     String fileName = fileName1.endsWith("*") ? fileName2 : fileName1;
/* 254:350 */     String extension = extension1.startsWith("*") ? extension2 : extension1;
/* 255:    */     
/* 256:352 */     return fileName + extension;
/* 257:    */   }
/* 258:    */   
/* 259:    */   public Comparator<String> getPatternComparator(String path)
/* 260:    */   {
/* 261:369 */     return new AntPatternComparator(path, null);
/* 262:    */   }
/* 263:    */   
/* 264:    */   private static class AntPatternComparator
/* 265:    */     implements Comparator<String>
/* 266:    */   {
/* 267:    */     private final String path;
/* 268:    */     
/* 269:    */     private AntPatternComparator(String path)
/* 270:    */     {
/* 271:378 */       this.path = path;
/* 272:    */     }
/* 273:    */     
/* 274:    */     public int compare(String pattern1, String pattern2)
/* 275:    */     {
/* 276:382 */       if ((pattern1 == null) && (pattern2 == null)) {
/* 277:383 */         return 0;
/* 278:    */       }
/* 279:385 */       if (pattern1 == null) {
/* 280:386 */         return 1;
/* 281:    */       }
/* 282:388 */       if (pattern2 == null) {
/* 283:389 */         return -1;
/* 284:    */       }
/* 285:391 */       boolean pattern1EqualsPath = pattern1.equals(this.path);
/* 286:392 */       boolean pattern2EqualsPath = pattern2.equals(this.path);
/* 287:393 */       if ((pattern1EqualsPath) && (pattern2EqualsPath)) {
/* 288:394 */         return 0;
/* 289:    */       }
/* 290:396 */       if (pattern1EqualsPath) {
/* 291:397 */         return -1;
/* 292:    */       }
/* 293:399 */       if (pattern2EqualsPath) {
/* 294:400 */         return 1;
/* 295:    */       }
/* 296:402 */       int wildCardCount1 = getWildCardCount(pattern1);
/* 297:403 */       int wildCardCount2 = getWildCardCount(pattern2);
/* 298:    */       
/* 299:405 */       int bracketCount1 = StringUtils.countOccurrencesOf(pattern1, "{");
/* 300:406 */       int bracketCount2 = StringUtils.countOccurrencesOf(pattern2, "{");
/* 301:    */       
/* 302:408 */       int totalCount1 = wildCardCount1 + bracketCount1;
/* 303:409 */       int totalCount2 = wildCardCount2 + bracketCount2;
/* 304:411 */       if (totalCount1 != totalCount2) {
/* 305:412 */         return totalCount1 - totalCount2;
/* 306:    */       }
/* 307:415 */       int pattern1Length = getPatternLength(pattern1);
/* 308:416 */       int pattern2Length = getPatternLength(pattern2);
/* 309:418 */       if (pattern1Length != pattern2Length) {
/* 310:419 */         return pattern2Length - pattern1Length;
/* 311:    */       }
/* 312:422 */       if (wildCardCount1 < wildCardCount2) {
/* 313:423 */         return -1;
/* 314:    */       }
/* 315:425 */       if (wildCardCount2 < wildCardCount1) {
/* 316:426 */         return 1;
/* 317:    */       }
/* 318:429 */       if (bracketCount1 < bracketCount2) {
/* 319:430 */         return -1;
/* 320:    */       }
/* 321:432 */       if (bracketCount2 < bracketCount1) {
/* 322:433 */         return 1;
/* 323:    */       }
/* 324:436 */       return 0;
/* 325:    */     }
/* 326:    */     
/* 327:    */     private int getWildCardCount(String pattern)
/* 328:    */     {
/* 329:440 */       if (pattern.endsWith(".*")) {
/* 330:441 */         pattern = pattern.substring(0, pattern.length() - 2);
/* 331:    */       }
/* 332:443 */       return StringUtils.countOccurrencesOf(pattern, "*");
/* 333:    */     }
/* 334:    */     
/* 335:    */     private int getPatternLength(String pattern)
/* 336:    */     {
/* 337:450 */       Matcher m = AntPathMatcher.VARIABLE_PATTERN.matcher(pattern);
/* 338:451 */       return m.replaceAll("#").length();
/* 339:    */     }
/* 340:    */   }
/* 341:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.AntPathMatcher
 * JD-Core Version:    0.7.0.1
 */