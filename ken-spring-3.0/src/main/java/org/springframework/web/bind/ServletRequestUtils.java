/*   1:    */ package org.springframework.web.bind;
/*   2:    */ 
/*   3:    */ import javax.servlet.ServletRequest;
/*   4:    */ 
/*   5:    */ public abstract class ServletRequestUtils
/*   6:    */ {
/*   7: 34 */   private static final IntParser INT_PARSER = new IntParser(null);
/*   8: 36 */   private static final LongParser LONG_PARSER = new LongParser(null);
/*   9: 38 */   private static final FloatParser FLOAT_PARSER = new FloatParser(null);
/*  10: 40 */   private static final DoubleParser DOUBLE_PARSER = new DoubleParser(null);
/*  11: 42 */   private static final BooleanParser BOOLEAN_PARSER = new BooleanParser(null);
/*  12: 44 */   private static final StringParser STRING_PARSER = new StringParser(null);
/*  13:    */   
/*  14:    */   public static Integer getIntParameter(ServletRequest request, String name)
/*  15:    */     throws ServletRequestBindingException
/*  16:    */   {
/*  17: 59 */     if (request.getParameter(name) == null) {
/*  18: 60 */       return null;
/*  19:    */     }
/*  20: 62 */     return Integer.valueOf(getRequiredIntParameter(request, name));
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static int getIntParameter(ServletRequest request, String name, int defaultVal)
/*  24:    */   {
/*  25: 73 */     if (request.getParameter(name) == null) {
/*  26: 74 */       return defaultVal;
/*  27:    */     }
/*  28:    */     try
/*  29:    */     {
/*  30: 77 */       return getRequiredIntParameter(request, name);
/*  31:    */     }
/*  32:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/*  33: 80 */     return defaultVal;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static int[] getIntParameters(ServletRequest request, String name)
/*  37:    */   {
/*  38:    */     try
/*  39:    */     {
/*  40: 91 */       return getRequiredIntParameters(request, name);
/*  41:    */     }
/*  42:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/*  43: 94 */     return new int[0];
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static int getRequiredIntParameter(ServletRequest request, String name)
/*  47:    */     throws ServletRequestBindingException
/*  48:    */   {
/*  49:108 */     return INT_PARSER.parseInt(name, request.getParameter(name));
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static int[] getRequiredIntParameters(ServletRequest request, String name)
/*  53:    */     throws ServletRequestBindingException
/*  54:    */   {
/*  55:121 */     return INT_PARSER.parseInts(name, request.getParameterValues(name));
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static Long getLongParameter(ServletRequest request, String name)
/*  59:    */     throws ServletRequestBindingException
/*  60:    */   {
/*  61:137 */     if (request.getParameter(name) == null) {
/*  62:138 */       return null;
/*  63:    */     }
/*  64:140 */     return Long.valueOf(getRequiredLongParameter(request, name));
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static long getLongParameter(ServletRequest request, String name, long defaultVal)
/*  68:    */   {
/*  69:151 */     if (request.getParameter(name) == null) {
/*  70:152 */       return defaultVal;
/*  71:    */     }
/*  72:    */     try
/*  73:    */     {
/*  74:155 */       return getRequiredLongParameter(request, name);
/*  75:    */     }
/*  76:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/*  77:158 */     return defaultVal;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static long[] getLongParameters(ServletRequest request, String name)
/*  81:    */   {
/*  82:    */     try
/*  83:    */     {
/*  84:169 */       return getRequiredLongParameters(request, name);
/*  85:    */     }
/*  86:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/*  87:172 */     return new long[0];
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static long getRequiredLongParameter(ServletRequest request, String name)
/*  91:    */     throws ServletRequestBindingException
/*  92:    */   {
/*  93:186 */     return LONG_PARSER.parseLong(name, request.getParameter(name));
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static long[] getRequiredLongParameters(ServletRequest request, String name)
/*  97:    */     throws ServletRequestBindingException
/*  98:    */   {
/*  99:199 */     return LONG_PARSER.parseLongs(name, request.getParameterValues(name));
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static Float getFloatParameter(ServletRequest request, String name)
/* 103:    */     throws ServletRequestBindingException
/* 104:    */   {
/* 105:215 */     if (request.getParameter(name) == null) {
/* 106:216 */       return null;
/* 107:    */     }
/* 108:218 */     return Float.valueOf(getRequiredFloatParameter(request, name));
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static float getFloatParameter(ServletRequest request, String name, float defaultVal)
/* 112:    */   {
/* 113:229 */     if (request.getParameter(name) == null) {
/* 114:230 */       return defaultVal;
/* 115:    */     }
/* 116:    */     try
/* 117:    */     {
/* 118:233 */       return getRequiredFloatParameter(request, name);
/* 119:    */     }
/* 120:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/* 121:236 */     return defaultVal;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public static float[] getFloatParameters(ServletRequest request, String name)
/* 125:    */   {
/* 126:    */     try
/* 127:    */     {
/* 128:247 */       return getRequiredFloatParameters(request, name);
/* 129:    */     }
/* 130:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/* 131:250 */     return new float[0];
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static float getRequiredFloatParameter(ServletRequest request, String name)
/* 135:    */     throws ServletRequestBindingException
/* 136:    */   {
/* 137:264 */     return FLOAT_PARSER.parseFloat(name, request.getParameter(name));
/* 138:    */   }
/* 139:    */   
/* 140:    */   public static float[] getRequiredFloatParameters(ServletRequest request, String name)
/* 141:    */     throws ServletRequestBindingException
/* 142:    */   {
/* 143:277 */     return FLOAT_PARSER.parseFloats(name, request.getParameterValues(name));
/* 144:    */   }
/* 145:    */   
/* 146:    */   public static Double getDoubleParameter(ServletRequest request, String name)
/* 147:    */     throws ServletRequestBindingException
/* 148:    */   {
/* 149:293 */     if (request.getParameter(name) == null) {
/* 150:294 */       return null;
/* 151:    */     }
/* 152:296 */     return Double.valueOf(getRequiredDoubleParameter(request, name));
/* 153:    */   }
/* 154:    */   
/* 155:    */   public static double getDoubleParameter(ServletRequest request, String name, double defaultVal)
/* 156:    */   {
/* 157:307 */     if (request.getParameter(name) == null) {
/* 158:308 */       return defaultVal;
/* 159:    */     }
/* 160:    */     try
/* 161:    */     {
/* 162:311 */       return getRequiredDoubleParameter(request, name);
/* 163:    */     }
/* 164:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/* 165:314 */     return defaultVal;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public static double[] getDoubleParameters(ServletRequest request, String name)
/* 169:    */   {
/* 170:    */     try
/* 171:    */     {
/* 172:325 */       return getRequiredDoubleParameters(request, name);
/* 173:    */     }
/* 174:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/* 175:328 */     return new double[0];
/* 176:    */   }
/* 177:    */   
/* 178:    */   public static double getRequiredDoubleParameter(ServletRequest request, String name)
/* 179:    */     throws ServletRequestBindingException
/* 180:    */   {
/* 181:342 */     return DOUBLE_PARSER.parseDouble(name, request.getParameter(name));
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static double[] getRequiredDoubleParameters(ServletRequest request, String name)
/* 185:    */     throws ServletRequestBindingException
/* 186:    */   {
/* 187:355 */     return DOUBLE_PARSER.parseDoubles(name, request.getParameterValues(name));
/* 188:    */   }
/* 189:    */   
/* 190:    */   public static Boolean getBooleanParameter(ServletRequest request, String name)
/* 191:    */     throws ServletRequestBindingException
/* 192:    */   {
/* 193:373 */     if (request.getParameter(name) == null) {
/* 194:374 */       return null;
/* 195:    */     }
/* 196:376 */     return Boolean.valueOf(getRequiredBooleanParameter(request, name));
/* 197:    */   }
/* 198:    */   
/* 199:    */   public static boolean getBooleanParameter(ServletRequest request, String name, boolean defaultVal)
/* 200:    */   {
/* 201:389 */     if (request.getParameter(name) == null) {
/* 202:390 */       return defaultVal;
/* 203:    */     }
/* 204:    */     try
/* 205:    */     {
/* 206:393 */       return getRequiredBooleanParameter(request, name);
/* 207:    */     }
/* 208:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/* 209:396 */     return defaultVal;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public static boolean[] getBooleanParameters(ServletRequest request, String name)
/* 213:    */   {
/* 214:    */     try
/* 215:    */     {
/* 216:409 */       return getRequiredBooleanParameters(request, name);
/* 217:    */     }
/* 218:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/* 219:412 */     return new boolean[0];
/* 220:    */   }
/* 221:    */   
/* 222:    */   public static boolean getRequiredBooleanParameter(ServletRequest request, String name)
/* 223:    */     throws ServletRequestBindingException
/* 224:    */   {
/* 225:429 */     return BOOLEAN_PARSER.parseBoolean(name, request.getParameter(name));
/* 226:    */   }
/* 227:    */   
/* 228:    */   public static boolean[] getRequiredBooleanParameters(ServletRequest request, String name)
/* 229:    */     throws ServletRequestBindingException
/* 230:    */   {
/* 231:445 */     return BOOLEAN_PARSER.parseBooleans(name, request.getParameterValues(name));
/* 232:    */   }
/* 233:    */   
/* 234:    */   public static String getStringParameter(ServletRequest request, String name)
/* 235:    */     throws ServletRequestBindingException
/* 236:    */   {
/* 237:460 */     if (request.getParameter(name) == null) {
/* 238:461 */       return null;
/* 239:    */     }
/* 240:463 */     return getRequiredStringParameter(request, name);
/* 241:    */   }
/* 242:    */   
/* 243:    */   public static String getStringParameter(ServletRequest request, String name, String defaultVal)
/* 244:    */   {
/* 245:474 */     String val = request.getParameter(name);
/* 246:475 */     return val != null ? val : defaultVal;
/* 247:    */   }
/* 248:    */   
/* 249:    */   public static String[] getStringParameters(ServletRequest request, String name)
/* 250:    */   {
/* 251:    */     try
/* 252:    */     {
/* 253:485 */       return getRequiredStringParameters(request, name);
/* 254:    */     }
/* 255:    */     catch (ServletRequestBindingException localServletRequestBindingException) {}
/* 256:488 */     return new String[0];
/* 257:    */   }
/* 258:    */   
/* 259:    */   public static String getRequiredStringParameter(ServletRequest request, String name)
/* 260:    */     throws ServletRequestBindingException
/* 261:    */   {
/* 262:502 */     return STRING_PARSER.validateRequiredString(name, request.getParameter(name));
/* 263:    */   }
/* 264:    */   
/* 265:    */   public static String[] getRequiredStringParameters(ServletRequest request, String name)
/* 266:    */     throws ServletRequestBindingException
/* 267:    */   {
/* 268:515 */     return STRING_PARSER.validateRequiredStrings(name, request.getParameterValues(name));
/* 269:    */   }
/* 270:    */   
/* 271:    */   private static abstract class ParameterParser<T>
/* 272:    */   {
/* 273:    */     protected final T parse(String name, String parameter)
/* 274:    */       throws ServletRequestBindingException
/* 275:    */     {
/* 276:522 */       validateRequiredParameter(name, parameter);
/* 277:    */       try
/* 278:    */       {
/* 279:524 */         return doParse(parameter);
/* 280:    */       }
/* 281:    */       catch (NumberFormatException ex)
/* 282:    */       {
/* 283:527 */         throw new ServletRequestBindingException(
/* 284:528 */           "Required " + getType() + " parameter '" + name + "' with value of '" + 
/* 285:529 */           parameter + "' is not a valid number", ex);
/* 286:    */       }
/* 287:    */     }
/* 288:    */     
/* 289:    */     protected final void validateRequiredParameter(String name, Object parameter)
/* 290:    */       throws ServletRequestBindingException
/* 291:    */     {
/* 292:536 */       if (parameter == null) {
/* 293:537 */         throw new MissingServletRequestParameterException(name, getType());
/* 294:    */       }
/* 295:    */     }
/* 296:    */     
/* 297:    */     protected abstract String getType();
/* 298:    */     
/* 299:    */     protected abstract T doParse(String paramString)
/* 300:    */       throws NumberFormatException;
/* 301:    */   }
/* 302:    */   
/* 303:    */   private static class IntParser
/* 304:    */     extends ServletRequestUtils.ParameterParser<Integer>
/* 305:    */   {
/* 306:    */     private IntParser()
/* 307:    */     {
/* 308:547 */       super();
/* 309:    */     }
/* 310:    */     
/* 311:    */     protected String getType()
/* 312:    */     {
/* 313:551 */       return "int";
/* 314:    */     }
/* 315:    */     
/* 316:    */     protected Integer doParse(String s)
/* 317:    */       throws NumberFormatException
/* 318:    */     {
/* 319:556 */       return Integer.valueOf(s);
/* 320:    */     }
/* 321:    */     
/* 322:    */     public int parseInt(String name, String parameter)
/* 323:    */       throws ServletRequestBindingException
/* 324:    */     {
/* 325:560 */       return ((Integer)parse(name, parameter)).intValue();
/* 326:    */     }
/* 327:    */     
/* 328:    */     public int[] parseInts(String name, String[] values)
/* 329:    */       throws ServletRequestBindingException
/* 330:    */     {
/* 331:564 */       validateRequiredParameter(name, values);
/* 332:565 */       int[] parameters = new int[values.length];
/* 333:566 */       for (int i = 0; i < values.length; i++) {
/* 334:567 */         parameters[i] = parseInt(name, values[i]);
/* 335:    */       }
/* 336:569 */       return parameters;
/* 337:    */     }
/* 338:    */   }
/* 339:    */   
/* 340:    */   private static class LongParser
/* 341:    */     extends ServletRequestUtils.ParameterParser<Long>
/* 342:    */   {
/* 343:    */     private LongParser()
/* 344:    */     {
/* 345:574 */       super();
/* 346:    */     }
/* 347:    */     
/* 348:    */     protected String getType()
/* 349:    */     {
/* 350:578 */       return "long";
/* 351:    */     }
/* 352:    */     
/* 353:    */     protected Long doParse(String parameter)
/* 354:    */       throws NumberFormatException
/* 355:    */     {
/* 356:583 */       return Long.valueOf(parameter);
/* 357:    */     }
/* 358:    */     
/* 359:    */     public long parseLong(String name, String parameter)
/* 360:    */       throws ServletRequestBindingException
/* 361:    */     {
/* 362:587 */       return ((Long)parse(name, parameter)).longValue();
/* 363:    */     }
/* 364:    */     
/* 365:    */     public long[] parseLongs(String name, String[] values)
/* 366:    */       throws ServletRequestBindingException
/* 367:    */     {
/* 368:591 */       validateRequiredParameter(name, values);
/* 369:592 */       long[] parameters = new long[values.length];
/* 370:593 */       for (int i = 0; i < values.length; i++) {
/* 371:594 */         parameters[i] = parseLong(name, values[i]);
/* 372:    */       }
/* 373:596 */       return parameters;
/* 374:    */     }
/* 375:    */   }
/* 376:    */   
/* 377:    */   private static class FloatParser
/* 378:    */     extends ServletRequestUtils.ParameterParser<Float>
/* 379:    */   {
/* 380:    */     private FloatParser()
/* 381:    */     {
/* 382:601 */       super();
/* 383:    */     }
/* 384:    */     
/* 385:    */     protected String getType()
/* 386:    */     {
/* 387:605 */       return "float";
/* 388:    */     }
/* 389:    */     
/* 390:    */     protected Float doParse(String parameter)
/* 391:    */       throws NumberFormatException
/* 392:    */     {
/* 393:610 */       return Float.valueOf(parameter);
/* 394:    */     }
/* 395:    */     
/* 396:    */     public float parseFloat(String name, String parameter)
/* 397:    */       throws ServletRequestBindingException
/* 398:    */     {
/* 399:614 */       return ((Float)parse(name, parameter)).floatValue();
/* 400:    */     }
/* 401:    */     
/* 402:    */     public float[] parseFloats(String name, String[] values)
/* 403:    */       throws ServletRequestBindingException
/* 404:    */     {
/* 405:618 */       validateRequiredParameter(name, values);
/* 406:619 */       float[] parameters = new float[values.length];
/* 407:620 */       for (int i = 0; i < values.length; i++) {
/* 408:621 */         parameters[i] = parseFloat(name, values[i]);
/* 409:    */       }
/* 410:623 */       return parameters;
/* 411:    */     }
/* 412:    */   }
/* 413:    */   
/* 414:    */   private static class DoubleParser
/* 415:    */     extends ServletRequestUtils.ParameterParser<Double>
/* 416:    */   {
/* 417:    */     private DoubleParser()
/* 418:    */     {
/* 419:628 */       super();
/* 420:    */     }
/* 421:    */     
/* 422:    */     protected String getType()
/* 423:    */     {
/* 424:632 */       return "double";
/* 425:    */     }
/* 426:    */     
/* 427:    */     protected Double doParse(String parameter)
/* 428:    */       throws NumberFormatException
/* 429:    */     {
/* 430:637 */       return Double.valueOf(parameter);
/* 431:    */     }
/* 432:    */     
/* 433:    */     public double parseDouble(String name, String parameter)
/* 434:    */       throws ServletRequestBindingException
/* 435:    */     {
/* 436:641 */       return ((Double)parse(name, parameter)).doubleValue();
/* 437:    */     }
/* 438:    */     
/* 439:    */     public double[] parseDoubles(String name, String[] values)
/* 440:    */       throws ServletRequestBindingException
/* 441:    */     {
/* 442:645 */       validateRequiredParameter(name, values);
/* 443:646 */       double[] parameters = new double[values.length];
/* 444:647 */       for (int i = 0; i < values.length; i++) {
/* 445:648 */         parameters[i] = parseDouble(name, values[i]);
/* 446:    */       }
/* 447:650 */       return parameters;
/* 448:    */     }
/* 449:    */   }
/* 450:    */   
/* 451:    */   private static class BooleanParser
/* 452:    */     extends ServletRequestUtils.ParameterParser<Boolean>
/* 453:    */   {
/* 454:    */     private BooleanParser()
/* 455:    */     {
/* 456:655 */       super();
/* 457:    */     }
/* 458:    */     
/* 459:    */     protected String getType()
/* 460:    */     {
/* 461:659 */       return "boolean";
/* 462:    */     }
/* 463:    */     
/* 464:    */     protected Boolean doParse(String parameter)
/* 465:    */       throws NumberFormatException
/* 466:    */     {
/* 467:664 */       if ((!parameter.equalsIgnoreCase("true")) && (!parameter.equalsIgnoreCase("on")) && 
/* 468:665 */         (!parameter.equalsIgnoreCase("yes")) && (!parameter.equals("1"))) {
/* 469:665 */         return Boolean.valueOf(false);
/* 470:    */       }
/* 471:664 */       return 
/* 472:665 */         Boolean.valueOf(true);
/* 473:    */     }
/* 474:    */     
/* 475:    */     public boolean parseBoolean(String name, String parameter)
/* 476:    */       throws ServletRequestBindingException
/* 477:    */     {
/* 478:669 */       return ((Boolean)parse(name, parameter)).booleanValue();
/* 479:    */     }
/* 480:    */     
/* 481:    */     public boolean[] parseBooleans(String name, String[] values)
/* 482:    */       throws ServletRequestBindingException
/* 483:    */     {
/* 484:673 */       validateRequiredParameter(name, values);
/* 485:674 */       boolean[] parameters = new boolean[values.length];
/* 486:675 */       for (int i = 0; i < values.length; i++) {
/* 487:676 */         parameters[i] = parseBoolean(name, values[i]);
/* 488:    */       }
/* 489:678 */       return parameters;
/* 490:    */     }
/* 491:    */   }
/* 492:    */   
/* 493:    */   private static class StringParser
/* 494:    */     extends ServletRequestUtils.ParameterParser<String>
/* 495:    */   {
/* 496:    */     private StringParser()
/* 497:    */     {
/* 498:683 */       super();
/* 499:    */     }
/* 500:    */     
/* 501:    */     protected String getType()
/* 502:    */     {
/* 503:687 */       return "string";
/* 504:    */     }
/* 505:    */     
/* 506:    */     protected String doParse(String parameter)
/* 507:    */       throws NumberFormatException
/* 508:    */     {
/* 509:692 */       return parameter;
/* 510:    */     }
/* 511:    */     
/* 512:    */     public String validateRequiredString(String name, String value)
/* 513:    */       throws ServletRequestBindingException
/* 514:    */     {
/* 515:696 */       validateRequiredParameter(name, value);
/* 516:697 */       return value;
/* 517:    */     }
/* 518:    */     
/* 519:    */     public String[] validateRequiredStrings(String name, String[] values)
/* 520:    */       throws ServletRequestBindingException
/* 521:    */     {
/* 522:701 */       validateRequiredParameter(name, values);
/* 523:702 */       for (String value : values) {
/* 524:703 */         validateRequiredParameter(name, value);
/* 525:    */       }
/* 526:705 */       return values;
/* 527:    */     }
/* 528:    */   }
/* 529:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.ServletRequestUtils
 * JD-Core Version:    0.7.0.1
 */