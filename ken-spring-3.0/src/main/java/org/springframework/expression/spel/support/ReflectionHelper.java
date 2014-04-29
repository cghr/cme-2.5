/*   1:    */ package org.springframework.expression.spel.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import org.springframework.core.MethodParameter;
/*   8:    */ import org.springframework.core.convert.TypeDescriptor;
/*   9:    */ import org.springframework.expression.EvaluationException;
/*  10:    */ import org.springframework.expression.TypeConverter;
/*  11:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*  12:    */ import org.springframework.expression.spel.SpelMessage;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.ClassUtils;
/*  15:    */ 
/*  16:    */ public class ReflectionHelper
/*  17:    */ {
/*  18:    */   static ArgumentsMatchInfo compareArguments(List<TypeDescriptor> expectedArgTypes, List<TypeDescriptor> suppliedArgTypes, TypeConverter typeConverter)
/*  19:    */   {
/*  20: 56 */     Assert.isTrue(expectedArgTypes.size() == suppliedArgTypes.size(), 
/*  21: 57 */       "Expected argument types and supplied argument types should be arrays of same length");
/*  22:    */     
/*  23: 59 */     ArgsMatchKind match = ArgsMatchKind.EXACT;
/*  24: 60 */     List<Integer> argsRequiringConversion = null;
/*  25: 61 */     for (int i = 0; (i < expectedArgTypes.size()) && (match != null); i++)
/*  26:    */     {
/*  27: 62 */       TypeDescriptor suppliedArg = (TypeDescriptor)suppliedArgTypes.get(i);
/*  28: 63 */       TypeDescriptor expectedArg = (TypeDescriptor)expectedArgTypes.get(i);
/*  29: 64 */       if (!expectedArg.equals(suppliedArg)) {
/*  30: 66 */         if (suppliedArg == null)
/*  31:    */         {
/*  32: 67 */           if (expectedArg.isPrimitive()) {
/*  33: 68 */             match = null;
/*  34:    */           }
/*  35:    */         }
/*  36: 72 */         else if (suppliedArg.isAssignableTo(expectedArg))
/*  37:    */         {
/*  38: 73 */           if (match != ArgsMatchKind.REQUIRES_CONVERSION) {
/*  39: 74 */             match = ArgsMatchKind.CLOSE;
/*  40:    */           }
/*  41:    */         }
/*  42: 77 */         else if (typeConverter.canConvert(suppliedArg, expectedArg))
/*  43:    */         {
/*  44: 78 */           if (argsRequiringConversion == null) {
/*  45: 79 */             argsRequiringConversion = new ArrayList();
/*  46:    */           }
/*  47: 81 */           argsRequiringConversion.add(Integer.valueOf(i));
/*  48: 82 */           match = ArgsMatchKind.REQUIRES_CONVERSION;
/*  49:    */         }
/*  50:    */         else
/*  51:    */         {
/*  52: 85 */           match = null;
/*  53:    */         }
/*  54:    */       }
/*  55:    */     }
/*  56: 90 */     if (match == null) {
/*  57: 91 */       return null;
/*  58:    */     }
/*  59: 94 */     if (match == ArgsMatchKind.REQUIRES_CONVERSION)
/*  60:    */     {
/*  61: 95 */       int[] argsArray = new int[argsRequiringConversion.size()];
/*  62: 96 */       for (int i = 0; i < argsRequiringConversion.size(); i++) {
/*  63: 97 */         argsArray[i] = ((Integer)argsRequiringConversion.get(i)).intValue();
/*  64:    */       }
/*  65: 99 */       return new ArgumentsMatchInfo(match, argsArray);
/*  66:    */     }
/*  67:102 */     return new ArgumentsMatchInfo(match);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static int getTypeDifferenceWeight(List<TypeDescriptor> paramTypes, List<TypeDescriptor> argTypes)
/*  71:    */   {
/*  72:111 */     int result = 0;
/*  73:112 */     int i = 0;
/*  74:112 */     for (int max = paramTypes.size(); i < max; i++)
/*  75:    */     {
/*  76:113 */       TypeDescriptor argType = (TypeDescriptor)argTypes.get(i);
/*  77:114 */       TypeDescriptor paramType = (TypeDescriptor)paramTypes.get(i);
/*  78:115 */       if ((argType == null) && 
/*  79:116 */         (paramType.isPrimitive())) {
/*  80:117 */         return 2147483647;
/*  81:    */       }
/*  82:120 */       if (!ClassUtils.isAssignable(paramType.getClass(), argType.getClass())) {
/*  83:121 */         return 2147483647;
/*  84:    */       }
/*  85:123 */       if (argType != null)
/*  86:    */       {
/*  87:124 */         Class paramTypeClazz = paramType.getType();
/*  88:125 */         if (paramTypeClazz.isPrimitive()) {
/*  89:126 */           paramTypeClazz = Object.class;
/*  90:    */         }
/*  91:128 */         Class superClass = argType.getClass().getSuperclass();
/*  92:129 */         while (superClass != null) {
/*  93:130 */           if (paramType.equals(superClass))
/*  94:    */           {
/*  95:131 */             result += 2;
/*  96:132 */             superClass = null;
/*  97:    */           }
/*  98:134 */           else if (ClassUtils.isAssignable(paramTypeClazz, superClass))
/*  99:    */           {
/* 100:135 */             result += 2;
/* 101:136 */             superClass = superClass.getSuperclass();
/* 102:    */           }
/* 103:    */           else
/* 104:    */           {
/* 105:139 */             superClass = null;
/* 106:    */           }
/* 107:    */         }
/* 108:142 */         if (paramTypeClazz.isInterface()) {
/* 109:143 */           result++;
/* 110:    */         }
/* 111:    */       }
/* 112:    */     }
/* 113:147 */     return result;
/* 114:    */   }
/* 115:    */   
/* 116:    */   static ArgumentsMatchInfo compareArgumentsVarargs(List<TypeDescriptor> expectedArgTypes, List<TypeDescriptor> suppliedArgTypes, TypeConverter typeConverter)
/* 117:    */   {
/* 118:162 */     Assert.isTrue((expectedArgTypes != null) && (expectedArgTypes.size() > 0), 
/* 119:163 */       "Expected arguments must at least include one array (the vargargs parameter)");
/* 120:164 */     Assert.isTrue(((TypeDescriptor)expectedArgTypes.get(expectedArgTypes.size() - 1)).isArray(), 
/* 121:165 */       "Final expected argument should be array type (the varargs parameter)");
/* 122:    */     
/* 123:167 */     ArgsMatchKind match = ArgsMatchKind.EXACT;
/* 124:168 */     List<Integer> argsRequiringConversion = null;
/* 125:    */     
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:173 */     int argCountUpToVarargs = expectedArgTypes.size() - 1;
/* 130:174 */     for (int i = 0; (i < argCountUpToVarargs) && (match != null); i++)
/* 131:    */     {
/* 132:175 */       TypeDescriptor suppliedArg = (TypeDescriptor)suppliedArgTypes.get(i);
/* 133:176 */       TypeDescriptor expectedArg = (TypeDescriptor)expectedArgTypes.get(i);
/* 134:177 */       if (suppliedArg == null)
/* 135:    */       {
/* 136:178 */         if (expectedArg.isPrimitive()) {
/* 137:179 */           match = null;
/* 138:    */         }
/* 139:    */       }
/* 140:183 */       else if (!expectedArg.equals(suppliedArg)) {
/* 141:184 */         if (suppliedArg.isAssignableTo(expectedArg))
/* 142:    */         {
/* 143:185 */           if (match != ArgsMatchKind.REQUIRES_CONVERSION) {
/* 144:186 */             match = ArgsMatchKind.CLOSE;
/* 145:    */           }
/* 146:    */         }
/* 147:189 */         else if (typeConverter.canConvert(suppliedArg, expectedArg))
/* 148:    */         {
/* 149:190 */           if (argsRequiringConversion == null) {
/* 150:191 */             argsRequiringConversion = new ArrayList();
/* 151:    */           }
/* 152:193 */           argsRequiringConversion.add(Integer.valueOf(i));
/* 153:194 */           match = ArgsMatchKind.REQUIRES_CONVERSION;
/* 154:    */         }
/* 155:    */         else
/* 156:    */         {
/* 157:197 */           match = null;
/* 158:    */         }
/* 159:    */       }
/* 160:    */     }
/* 161:203 */     if (match == null) {
/* 162:204 */       return null;
/* 163:    */     }
/* 164:207 */     if ((suppliedArgTypes.size() != expectedArgTypes.size()) || 
/* 165:208 */       (!((TypeDescriptor)expectedArgTypes.get(expectedArgTypes.size() - 1)).equals(
/* 166:209 */       suppliedArgTypes.get(suppliedArgTypes.size() - 1))))
/* 167:    */     {
/* 168:216 */       Class varargsParameterType = ((TypeDescriptor)expectedArgTypes.get(expectedArgTypes.size() - 1)).getElementTypeDescriptor().getType();
/* 169:219 */       for (int i = expectedArgTypes.size() - 1; i < suppliedArgTypes.size(); i++)
/* 170:    */       {
/* 171:220 */         TypeDescriptor suppliedArg = (TypeDescriptor)suppliedArgTypes.get(i);
/* 172:221 */         if (suppliedArg == null)
/* 173:    */         {
/* 174:222 */           if (varargsParameterType.isPrimitive()) {
/* 175:223 */             match = null;
/* 176:    */           }
/* 177:    */         }
/* 178:226 */         else if (varargsParameterType != suppliedArg.getType()) {
/* 179:227 */           if (ClassUtils.isAssignable(varargsParameterType, suppliedArg.getType()))
/* 180:    */           {
/* 181:228 */             if (match != ArgsMatchKind.REQUIRES_CONVERSION) {
/* 182:229 */               match = ArgsMatchKind.CLOSE;
/* 183:    */             }
/* 184:    */           }
/* 185:232 */           else if (typeConverter.canConvert(suppliedArg, TypeDescriptor.valueOf(varargsParameterType)))
/* 186:    */           {
/* 187:233 */             if (argsRequiringConversion == null) {
/* 188:234 */               argsRequiringConversion = new ArrayList();
/* 189:    */             }
/* 190:236 */             argsRequiringConversion.add(Integer.valueOf(i));
/* 191:237 */             match = ArgsMatchKind.REQUIRES_CONVERSION;
/* 192:    */           }
/* 193:    */           else
/* 194:    */           {
/* 195:240 */             match = null;
/* 196:    */           }
/* 197:    */         }
/* 198:    */       }
/* 199:    */     }
/* 200:247 */     if (match == null) {
/* 201:248 */       return null;
/* 202:    */     }
/* 203:251 */     if (match == ArgsMatchKind.REQUIRES_CONVERSION)
/* 204:    */     {
/* 205:252 */       int[] argsArray = new int[argsRequiringConversion.size()];
/* 206:253 */       for (int i = 0; i < argsRequiringConversion.size(); i++) {
/* 207:254 */         argsArray[i] = ((Integer)argsRequiringConversion.get(i)).intValue();
/* 208:    */       }
/* 209:256 */       return new ArgumentsMatchInfo(match, argsArray);
/* 210:    */     }
/* 211:259 */     return new ArgumentsMatchInfo(match);
/* 212:    */   }
/* 213:    */   
/* 214:    */   static void convertArguments(TypeConverter converter, Object[] arguments, Object methodOrCtor, int[] argumentsRequiringConversion, Integer varargsPosition)
/* 215:    */     throws EvaluationException
/* 216:    */   {
/* 217:277 */     if (varargsPosition == null)
/* 218:    */     {
/* 219:278 */       for (int i = 0; i < arguments.length; i++)
/* 220:    */       {
/* 221:279 */         TypeDescriptor targetType = new TypeDescriptor(MethodParameter.forMethodOrConstructor(methodOrCtor, i));
/* 222:280 */         Object argument = arguments[i];
/* 223:281 */         arguments[i] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 224:    */       }
/* 225:    */     }
/* 226:    */     else
/* 227:    */     {
/* 228:284 */       for (int i = 0; i < varargsPosition.intValue(); i++)
/* 229:    */       {
/* 230:285 */         TypeDescriptor targetType = new TypeDescriptor(MethodParameter.forMethodOrConstructor(methodOrCtor, i));
/* 231:286 */         Object argument = arguments[i];
/* 232:287 */         arguments[i] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 233:    */       }
/* 234:289 */       MethodParameter methodParam = MethodParameter.forMethodOrConstructor(methodOrCtor, varargsPosition.intValue());
/* 235:290 */       if (varargsPosition.intValue() == arguments.length - 1)
/* 236:    */       {
/* 237:291 */         TypeDescriptor targetType = new TypeDescriptor(methodParam);
/* 238:292 */         Object argument = arguments[varargsPosition.intValue()];
/* 239:293 */         arguments[varargsPosition.intValue()] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 240:    */       }
/* 241:    */       else
/* 242:    */       {
/* 243:295 */         TypeDescriptor targetType = TypeDescriptor.nested(methodParam, 1);
/* 244:296 */         for (int i = varargsPosition.intValue(); i < arguments.length; i++)
/* 245:    */         {
/* 246:297 */           Object argument = arguments[i];
/* 247:298 */           arguments[i] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 248:    */         }
/* 249:    */       }
/* 250:    */     }
/* 251:    */   }
/* 252:    */   
/* 253:    */   public static void convertAllArguments(TypeConverter converter, Object[] arguments, Method method)
/* 254:    */     throws SpelEvaluationException
/* 255:    */   {
/* 256:317 */     Integer varargsPosition = null;
/* 257:318 */     if (method.isVarArgs())
/* 258:    */     {
/* 259:319 */       Class[] paramTypes = method.getParameterTypes();
/* 260:320 */       varargsPosition = Integer.valueOf(paramTypes.length - 1);
/* 261:    */     }
/* 262:322 */     for (int argPosition = 0; argPosition < arguments.length; argPosition++)
/* 263:    */     {
/* 264:    */       TypeDescriptor targetType;
/* 265:    */       TypeDescriptor targetType;
/* 266:324 */       if ((varargsPosition != null) && (argPosition >= varargsPosition.intValue()))
/* 267:    */       {
/* 268:325 */         MethodParameter methodParam = new MethodParameter(method, varargsPosition.intValue());
/* 269:326 */         targetType = TypeDescriptor.nested(methodParam, 1);
/* 270:    */       }
/* 271:    */       else
/* 272:    */       {
/* 273:329 */         targetType = new TypeDescriptor(new MethodParameter(method, argPosition));
/* 274:    */       }
/* 275:    */       try
/* 276:    */       {
/* 277:332 */         Object argument = arguments[argPosition];
/* 278:333 */         if ((argument != null) && (!targetType.getObjectType().isInstance(argument)))
/* 279:    */         {
/* 280:334 */           if (converter == null) {
/* 281:335 */             throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { argument.getClass().getName(), targetType });
/* 282:    */           }
/* 283:337 */           arguments[argPosition] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 284:    */         }
/* 285:    */       }
/* 286:    */       catch (EvaluationException ex)
/* 287:    */       {
/* 288:342 */         if ((ex instanceof SpelEvaluationException)) {
/* 289:343 */           throw ((SpelEvaluationException)ex);
/* 290:    */         }
/* 291:346 */         throw new SpelEvaluationException(ex, SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { arguments[argPosition].getClass().getName(), targetType });
/* 292:    */       }
/* 293:    */     }
/* 294:    */   }
/* 295:    */   
/* 296:    */   public static Object[] setupArgumentsForVarargsInvocation(Class[] requiredParameterTypes, Object... args)
/* 297:    */   {
/* 298:363 */     int parameterCount = requiredParameterTypes.length;
/* 299:364 */     int argumentCount = args.length;
/* 300:367 */     if (parameterCount == args.length)
/* 301:    */     {
/* 302:368 */       if (requiredParameterTypes[(parameterCount - 1)] == 
/* 303:369 */         (args[(argumentCount - 1)] == null ? null : args[(argumentCount - 1)].getClass())) {}
/* 304:    */     }
/* 305:    */     else
/* 306:    */     {
/* 307:370 */       int arraySize = 0;
/* 308:371 */       if (argumentCount >= parameterCount) {
/* 309:372 */         arraySize = argumentCount - (parameterCount - 1);
/* 310:    */       }
/* 311:376 */       Object[] newArgs = new Object[parameterCount];
/* 312:377 */       for (int i = 0; i < newArgs.length - 1; i++) {
/* 313:378 */         newArgs[i] = args[i];
/* 314:    */       }
/* 315:382 */       Class<?> componentType = requiredParameterTypes[(parameterCount - 1)].getComponentType();
/* 316:383 */       if (componentType.isPrimitive())
/* 317:    */       {
/* 318:384 */         if (componentType == Integer.TYPE)
/* 319:    */         {
/* 320:385 */           int[] repackagedArguments = (int[])Array.newInstance(componentType, arraySize);
/* 321:386 */           for (int i = 0; i < arraySize; i++) {
/* 322:387 */             repackagedArguments[i] = ((Integer)args[(parameterCount + i - 1)]).intValue();
/* 323:    */           }
/* 324:389 */           newArgs[(newArgs.length - 1)] = repackagedArguments;
/* 325:    */         }
/* 326:390 */         else if (componentType == Float.TYPE)
/* 327:    */         {
/* 328:391 */           float[] repackagedArguments = (float[])Array.newInstance(componentType, arraySize);
/* 329:392 */           for (int i = 0; i < arraySize; i++) {
/* 330:393 */             repackagedArguments[i] = ((Float)args[(parameterCount + i - 1)]).floatValue();
/* 331:    */           }
/* 332:395 */           newArgs[(newArgs.length - 1)] = repackagedArguments;
/* 333:    */         }
/* 334:396 */         else if (componentType == Double.TYPE)
/* 335:    */         {
/* 336:397 */           double[] repackagedArguments = (double[])Array.newInstance(componentType, arraySize);
/* 337:398 */           for (int i = 0; i < arraySize; i++) {
/* 338:399 */             repackagedArguments[i] = ((Double)args[(parameterCount + i - 1)]).doubleValue();
/* 339:    */           }
/* 340:401 */           newArgs[(newArgs.length - 1)] = repackagedArguments;
/* 341:    */         }
/* 342:402 */         else if (componentType == Short.TYPE)
/* 343:    */         {
/* 344:403 */           short[] repackagedArguments = (short[])Array.newInstance(componentType, arraySize);
/* 345:404 */           for (int i = 0; i < arraySize; i++) {
/* 346:405 */             repackagedArguments[i] = ((Short)args[(parameterCount + i - 1)]).shortValue();
/* 347:    */           }
/* 348:407 */           newArgs[(newArgs.length - 1)] = repackagedArguments;
/* 349:    */         }
/* 350:408 */         else if (componentType == Character.TYPE)
/* 351:    */         {
/* 352:409 */           char[] repackagedArguments = (char[])Array.newInstance(componentType, arraySize);
/* 353:410 */           for (int i = 0; i < arraySize; i++) {
/* 354:411 */             repackagedArguments[i] = ((Character)args[(parameterCount + i - 1)]).charValue();
/* 355:    */           }
/* 356:413 */           newArgs[(newArgs.length - 1)] = repackagedArguments;
/* 357:    */         }
/* 358:414 */         else if (componentType == Byte.TYPE)
/* 359:    */         {
/* 360:415 */           byte[] repackagedArguments = (byte[])Array.newInstance(componentType, arraySize);
/* 361:416 */           for (int i = 0; i < arraySize; i++) {
/* 362:417 */             repackagedArguments[i] = ((Byte)args[(parameterCount + i - 1)]).byteValue();
/* 363:    */           }
/* 364:419 */           newArgs[(newArgs.length - 1)] = repackagedArguments;
/* 365:    */         }
/* 366:420 */         else if (componentType == Boolean.TYPE)
/* 367:    */         {
/* 368:421 */           boolean[] repackagedArguments = (boolean[])Array.newInstance(componentType, arraySize);
/* 369:422 */           for (int i = 0; i < arraySize; i++) {
/* 370:423 */             repackagedArguments[i] = ((Boolean)args[(parameterCount + i - 1)]).booleanValue();
/* 371:    */           }
/* 372:425 */           newArgs[(newArgs.length - 1)] = repackagedArguments;
/* 373:    */         }
/* 374:426 */         else if (componentType == Long.TYPE)
/* 375:    */         {
/* 376:427 */           long[] repackagedArguments = (long[])Array.newInstance(componentType, arraySize);
/* 377:428 */           for (int i = 0; i < arraySize; i++) {
/* 378:429 */             repackagedArguments[i] = ((Long)args[(parameterCount + i - 1)]).longValue();
/* 379:    */           }
/* 380:431 */           newArgs[(newArgs.length - 1)] = repackagedArguments;
/* 381:    */         }
/* 382:    */       }
/* 383:    */       else
/* 384:    */       {
/* 385:434 */         Object[] repackagedArguments = (Object[])Array.newInstance(componentType, arraySize);
/* 386:436 */         for (int i = 0; i < arraySize; i++) {
/* 387:437 */           repackagedArguments[i] = args[(parameterCount + i - 1)];
/* 388:    */         }
/* 389:439 */         newArgs[(newArgs.length - 1)] = repackagedArguments;
/* 390:    */       }
/* 391:441 */       return newArgs;
/* 392:    */     }
/* 393:443 */     return args;
/* 394:    */   }
/* 395:    */   
/* 396:    */   public static enum ArgsMatchKind
/* 397:    */   {
/* 398:449 */     EXACT,  CLOSE,  REQUIRES_CONVERSION;
/* 399:    */   }
/* 400:    */   
/* 401:    */   public static class ArgumentsMatchInfo
/* 402:    */   {
/* 403:    */     public final ReflectionHelper.ArgsMatchKind kind;
/* 404:    */     public int[] argsRequiringConversion;
/* 405:    */     
/* 406:    */     ArgumentsMatchInfo(ReflectionHelper.ArgsMatchKind kind, int[] integers)
/* 407:    */     {
/* 408:470 */       this.kind = kind;
/* 409:471 */       this.argsRequiringConversion = integers;
/* 410:    */     }
/* 411:    */     
/* 412:    */     ArgumentsMatchInfo(ReflectionHelper.ArgsMatchKind kind)
/* 413:    */     {
/* 414:475 */       this.kind = kind;
/* 415:    */     }
/* 416:    */     
/* 417:    */     public boolean isExactMatch()
/* 418:    */     {
/* 419:479 */       return this.kind == ReflectionHelper.ArgsMatchKind.EXACT;
/* 420:    */     }
/* 421:    */     
/* 422:    */     public boolean isCloseMatch()
/* 423:    */     {
/* 424:483 */       return this.kind == ReflectionHelper.ArgsMatchKind.CLOSE;
/* 425:    */     }
/* 426:    */     
/* 427:    */     public boolean isMatchRequiringConversion()
/* 428:    */     {
/* 429:487 */       return this.kind == ReflectionHelper.ArgsMatchKind.REQUIRES_CONVERSION;
/* 430:    */     }
/* 431:    */     
/* 432:    */     public String toString()
/* 433:    */     {
/* 434:491 */       StringBuilder sb = new StringBuilder();
/* 435:492 */       sb.append("ArgumentMatch: ").append(this.kind);
/* 436:493 */       if (this.argsRequiringConversion != null)
/* 437:    */       {
/* 438:494 */         sb.append("  (argsForConversion:");
/* 439:495 */         for (int i = 0; i < this.argsRequiringConversion.length; i++)
/* 440:    */         {
/* 441:496 */           if (i > 0) {
/* 442:497 */             sb.append(",");
/* 443:    */           }
/* 444:499 */           sb.append(this.argsRequiringConversion[i]);
/* 445:    */         }
/* 446:501 */         sb.append(")");
/* 447:    */       }
/* 448:503 */       return sb.toString();
/* 449:    */     }
/* 450:    */   }
/* 451:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.ReflectionHelper
 * JD-Core Version:    0.7.0.1
 */