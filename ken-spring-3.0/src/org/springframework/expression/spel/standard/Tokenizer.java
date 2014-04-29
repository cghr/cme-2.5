/*   1:    */ package org.springframework.expression.spel.standard;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.List;
/*   6:    */ import org.springframework.expression.spel.InternalParseException;
/*   7:    */ import org.springframework.expression.spel.SpelMessage;
/*   8:    */ import org.springframework.expression.spel.SpelParseException;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ 
/*  11:    */ class Tokenizer
/*  12:    */ {
/*  13:    */   String expressionString;
/*  14:    */   char[] toProcess;
/*  15:    */   int pos;
/*  16:    */   int max;
/*  17: 40 */   List<Token> tokens = new ArrayList();
/*  18:    */   
/*  19:    */   public Tokenizer(String inputdata)
/*  20:    */   {
/*  21: 43 */     this.expressionString = inputdata;
/*  22: 44 */     this.toProcess = (inputdata + "").toCharArray();
/*  23: 45 */     this.max = this.toProcess.length;
/*  24: 46 */     this.pos = 0;
/*  25: 47 */     process();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void process()
/*  29:    */   {
/*  30: 51 */     while (this.pos < this.max)
/*  31:    */     {
/*  32: 52 */       char ch = this.toProcess[this.pos];
/*  33: 53 */       if (isAlphabetic(ch)) {
/*  34: 54 */         lexIdentifier();
/*  35:    */       } else {
/*  36: 56 */         switch (ch)
/*  37:    */         {
/*  38:    */         case '+': 
/*  39: 58 */           pushCharToken(TokenKind.PLUS);
/*  40: 59 */           break;
/*  41:    */         case '_': 
/*  42: 61 */           lexIdentifier();
/*  43: 62 */           break;
/*  44:    */         case '-': 
/*  45: 64 */           pushCharToken(TokenKind.MINUS);
/*  46: 65 */           break;
/*  47:    */         case ':': 
/*  48: 67 */           pushCharToken(TokenKind.COLON);
/*  49: 68 */           break;
/*  50:    */         case '.': 
/*  51: 70 */           pushCharToken(TokenKind.DOT);
/*  52: 71 */           break;
/*  53:    */         case ',': 
/*  54: 73 */           pushCharToken(TokenKind.COMMA);
/*  55: 74 */           break;
/*  56:    */         case '*': 
/*  57: 76 */           pushCharToken(TokenKind.STAR);
/*  58: 77 */           break;
/*  59:    */         case '/': 
/*  60: 79 */           pushCharToken(TokenKind.DIV);
/*  61: 80 */           break;
/*  62:    */         case '%': 
/*  63: 82 */           pushCharToken(TokenKind.MOD);
/*  64: 83 */           break;
/*  65:    */         case '(': 
/*  66: 85 */           pushCharToken(TokenKind.LPAREN);
/*  67: 86 */           break;
/*  68:    */         case ')': 
/*  69: 88 */           pushCharToken(TokenKind.RPAREN);
/*  70: 89 */           break;
/*  71:    */         case '[': 
/*  72: 91 */           pushCharToken(TokenKind.LSQUARE);
/*  73: 92 */           break;
/*  74:    */         case '#': 
/*  75: 94 */           pushCharToken(TokenKind.HASH);
/*  76: 95 */           break;
/*  77:    */         case ']': 
/*  78: 97 */           pushCharToken(TokenKind.RSQUARE);
/*  79: 98 */           break;
/*  80:    */         case '{': 
/*  81:100 */           pushCharToken(TokenKind.LCURLY);
/*  82:101 */           break;
/*  83:    */         case '}': 
/*  84:103 */           pushCharToken(TokenKind.RCURLY);
/*  85:104 */           break;
/*  86:    */         case '@': 
/*  87:106 */           pushCharToken(TokenKind.BEAN_REF);
/*  88:107 */           break;
/*  89:    */         case '^': 
/*  90:109 */           if (isTwoCharToken(TokenKind.SELECT_FIRST)) {
/*  91:110 */             pushPairToken(TokenKind.SELECT_FIRST);
/*  92:    */           } else {
/*  93:112 */             pushCharToken(TokenKind.POWER);
/*  94:    */           }
/*  95:114 */           break;
/*  96:    */         case '!': 
/*  97:116 */           if (isTwoCharToken(TokenKind.NE)) {
/*  98:117 */             pushPairToken(TokenKind.NE);
/*  99:118 */           } else if (isTwoCharToken(TokenKind.PROJECT)) {
/* 100:119 */             pushPairToken(TokenKind.PROJECT);
/* 101:    */           } else {
/* 102:121 */             pushCharToken(TokenKind.NOT);
/* 103:    */           }
/* 104:123 */           break;
/* 105:    */         case '=': 
/* 106:125 */           if (isTwoCharToken(TokenKind.EQ)) {
/* 107:126 */             pushPairToken(TokenKind.EQ);
/* 108:    */           } else {
/* 109:128 */             pushCharToken(TokenKind.ASSIGN);
/* 110:    */           }
/* 111:130 */           break;
/* 112:    */         case '?': 
/* 113:132 */           if (isTwoCharToken(TokenKind.SELECT)) {
/* 114:133 */             pushPairToken(TokenKind.SELECT);
/* 115:134 */           } else if (isTwoCharToken(TokenKind.ELVIS)) {
/* 116:135 */             pushPairToken(TokenKind.ELVIS);
/* 117:136 */           } else if (isTwoCharToken(TokenKind.SAFE_NAVI)) {
/* 118:137 */             pushPairToken(TokenKind.SAFE_NAVI);
/* 119:    */           } else {
/* 120:139 */             pushCharToken(TokenKind.QMARK);
/* 121:    */           }
/* 122:141 */           break;
/* 123:    */         case '$': 
/* 124:143 */           if (isTwoCharToken(TokenKind.SELECT_LAST)) {
/* 125:144 */             pushPairToken(TokenKind.SELECT_LAST);
/* 126:    */           } else {
/* 127:146 */             lexIdentifier();
/* 128:    */           }
/* 129:148 */           break;
/* 130:    */         case '>': 
/* 131:150 */           if (isTwoCharToken(TokenKind.GE)) {
/* 132:151 */             pushPairToken(TokenKind.GE);
/* 133:    */           } else {
/* 134:153 */             pushCharToken(TokenKind.GT);
/* 135:    */           }
/* 136:155 */           break;
/* 137:    */         case '<': 
/* 138:157 */           if (isTwoCharToken(TokenKind.LE)) {
/* 139:158 */             pushPairToken(TokenKind.LE);
/* 140:    */           } else {
/* 141:160 */             pushCharToken(TokenKind.LT);
/* 142:    */           }
/* 143:162 */           break;
/* 144:    */         case '0': 
/* 145:    */         case '1': 
/* 146:    */         case '2': 
/* 147:    */         case '3': 
/* 148:    */         case '4': 
/* 149:    */         case '5': 
/* 150:    */         case '6': 
/* 151:    */         case '7': 
/* 152:    */         case '8': 
/* 153:    */         case '9': 
/* 154:173 */           lexNumericLiteral(ch == '0');
/* 155:174 */           break;
/* 156:    */         case '\t': 
/* 157:    */         case '\n': 
/* 158:    */         case '\r': 
/* 159:    */         case ' ': 
/* 160:180 */           this.pos += 1;
/* 161:181 */           break;
/* 162:    */         case '\'': 
/* 163:183 */           lexQuotedStringLiteral();
/* 164:184 */           break;
/* 165:    */         case '"': 
/* 166:186 */           lexDoubleQuotedStringLiteral();
/* 167:187 */           break;
/* 168:    */         case '\000': 
/* 169:190 */           this.pos += 1;
/* 170:191 */           break;
/* 171:    */         default: 
/* 172:193 */           throw new IllegalStateException("Cannot handle (" + Integer.valueOf(ch) + ") '" + ch + "'");
/* 173:    */         }
/* 174:    */       }
/* 175:    */     }
/* 176:    */   }
/* 177:    */   
/* 178:    */   public List<Token> getTokens()
/* 179:    */   {
/* 180:200 */     return this.tokens;
/* 181:    */   }
/* 182:    */   
/* 183:    */   private void lexQuotedStringLiteral()
/* 184:    */   {
/* 185:206 */     int start = this.pos;
/* 186:207 */     boolean terminated = false;
/* 187:208 */     while (!terminated)
/* 188:    */     {
/* 189:209 */       this.pos += 1;
/* 190:210 */       char ch = this.toProcess[this.pos];
/* 191:211 */       if (ch == '\'') {
/* 192:213 */         if (this.toProcess[(this.pos + 1)] == '\'') {
/* 193:214 */           this.pos += 1;
/* 194:    */         } else {
/* 195:216 */           terminated = true;
/* 196:    */         }
/* 197:    */       }
/* 198:219 */       if (ch == 0) {
/* 199:220 */         throw new InternalParseException(new SpelParseException(this.expressionString, start, SpelMessage.NON_TERMINATING_QUOTED_STRING, new Object[0]));
/* 200:    */       }
/* 201:    */     }
/* 202:223 */     this.pos += 1;
/* 203:224 */     this.tokens.add(new Token(TokenKind.LITERAL_STRING, subarray(start, this.pos), start, this.pos));
/* 204:    */   }
/* 205:    */   
/* 206:    */   private void lexDoubleQuotedStringLiteral()
/* 207:    */   {
/* 208:229 */     int start = this.pos;
/* 209:230 */     boolean terminated = false;
/* 210:231 */     while (!terminated)
/* 211:    */     {
/* 212:232 */       this.pos += 1;
/* 213:233 */       char ch = this.toProcess[this.pos];
/* 214:234 */       if (ch == '"') {
/* 215:235 */         terminated = true;
/* 216:    */       }
/* 217:237 */       if (ch == 0) {
/* 218:238 */         throw new InternalParseException(new SpelParseException(this.expressionString, start, SpelMessage.NON_TERMINATING_DOUBLE_QUOTED_STRING, new Object[0]));
/* 219:    */       }
/* 220:    */     }
/* 221:241 */     this.pos += 1;
/* 222:242 */     this.tokens.add(new Token(TokenKind.LITERAL_STRING, subarray(start, this.pos), start, this.pos));
/* 223:    */   }
/* 224:    */   
/* 225:    */   private void lexNumericLiteral(boolean firstCharIsZero)
/* 226:    */   {
/* 227:261 */     boolean isReal = false;
/* 228:262 */     int start = this.pos;
/* 229:263 */     char ch = this.toProcess[(this.pos + 1)];
/* 230:264 */     boolean isHex = (ch == 'x') || (ch == 'X');
/* 231:267 */     if ((firstCharIsZero) && (isHex))
/* 232:    */     {
/* 233:268 */       this.pos += 1;
/* 234:    */       do
/* 235:    */       {
/* 236:270 */         this.pos += 1;
/* 237:269 */       } while (
/* 238:    */       
/* 239:271 */         isHexadecimalDigit(this.toProcess[this.pos]));
/* 240:272 */       if (isChar('L', 'l'))
/* 241:    */       {
/* 242:273 */         pushHexIntToken(subarray(start + 2, this.pos), true, start, this.pos);
/* 243:274 */         this.pos += 1;
/* 244:    */       }
/* 245:    */       else
/* 246:    */       {
/* 247:276 */         pushHexIntToken(subarray(start + 2, this.pos), false, start, this.pos);
/* 248:    */       }
/* 249:    */     }
/* 250:    */     else
/* 251:    */     {
/* 252:    */       do
/* 253:    */       {
/* 254:285 */         this.pos += 1;
/* 255:284 */       } while (
/* 256:    */       
/* 257:286 */         isDigit(this.toProcess[this.pos]));
/* 258:289 */       ch = this.toProcess[this.pos];
/* 259:290 */       if (ch == '.')
/* 260:    */       {
/* 261:291 */         isReal = true;
/* 262:    */         do
/* 263:    */         {
/* 264:294 */           this.pos += 1;
/* 265:293 */         } while (
/* 266:    */         
/* 267:295 */           isDigit(this.toProcess[this.pos]));
/* 268:    */       }
/* 269:298 */       int endOfNumber = this.pos;
/* 270:303 */       if (isChar('L', 'l'))
/* 271:    */       {
/* 272:304 */         if (isReal) {
/* 273:305 */           throw new InternalParseException(new SpelParseException(this.expressionString, start, SpelMessage.REAL_CANNOT_BE_LONG, new Object[0]));
/* 274:    */         }
/* 275:307 */         pushIntToken(subarray(start, endOfNumber), true, start, endOfNumber);
/* 276:308 */         this.pos += 1;
/* 277:    */       }
/* 278:309 */       else if (isExponentChar(this.toProcess[this.pos]))
/* 279:    */       {
/* 280:310 */         isReal = true;
/* 281:311 */         this.pos += 1;
/* 282:312 */         char possibleSign = this.toProcess[this.pos];
/* 283:313 */         if (isSign(possibleSign)) {
/* 284:314 */           this.pos += 1;
/* 285:    */         }
/* 286:    */         do
/* 287:    */         {
/* 288:319 */           this.pos += 1;
/* 289:318 */         } while (
/* 290:    */         
/* 291:320 */           isDigit(this.toProcess[this.pos]));
/* 292:321 */         boolean isFloat = false;
/* 293:322 */         if (isFloatSuffix(this.toProcess[this.pos]))
/* 294:    */         {
/* 295:323 */           isFloat = true;
/* 296:324 */           endOfNumber = ++this.pos;
/* 297:    */         }
/* 298:325 */         else if (isDoubleSuffix(this.toProcess[this.pos]))
/* 299:    */         {
/* 300:326 */           endOfNumber = ++this.pos;
/* 301:    */         }
/* 302:328 */         pushRealToken(subarray(start, this.pos), isFloat, start, this.pos);
/* 303:    */       }
/* 304:    */       else
/* 305:    */       {
/* 306:330 */         ch = this.toProcess[this.pos];
/* 307:331 */         boolean isFloat = false;
/* 308:332 */         if (isFloatSuffix(ch))
/* 309:    */         {
/* 310:333 */           isReal = true;
/* 311:334 */           isFloat = true;
/* 312:335 */           endOfNumber = ++this.pos;
/* 313:    */         }
/* 314:336 */         else if (isDoubleSuffix(ch))
/* 315:    */         {
/* 316:337 */           isReal = true;
/* 317:338 */           endOfNumber = ++this.pos;
/* 318:    */         }
/* 319:340 */         if (isReal) {
/* 320:341 */           pushRealToken(subarray(start, endOfNumber), isFloat, start, endOfNumber);
/* 321:    */         } else {
/* 322:343 */           pushIntToken(subarray(start, endOfNumber), false, start, endOfNumber);
/* 323:    */         }
/* 324:    */       }
/* 325:    */     }
/* 326:    */   }
/* 327:    */   
/* 328:349 */   private static final String[] alternativeOperatorNames = { "DIV", "EQ", "GE", "GT", "LE", "LT", "MOD", "NE", "NOT" };
/* 329:    */   
/* 330:    */   private void lexIdentifier()
/* 331:    */   {
/* 332:352 */     int start = this.pos;
/* 333:    */     do
/* 334:    */     {
/* 335:354 */       this.pos += 1;
/* 336:353 */     } while (
/* 337:    */     
/* 338:355 */       isIdentifier(this.toProcess[this.pos]));
/* 339:356 */     char[] subarray = subarray(start, this.pos);
/* 340:359 */     if ((this.pos - start == 2) || (this.pos - start == 3))
/* 341:    */     {
/* 342:360 */       String asString = new String(subarray).toUpperCase();
/* 343:361 */       int idx = Arrays.binarySearch(alternativeOperatorNames, asString);
/* 344:362 */       if (idx >= 0)
/* 345:    */       {
/* 346:363 */         pushOneCharOrTwoCharToken(TokenKind.valueOf(asString), start, subarray);
/* 347:364 */         return;
/* 348:    */       }
/* 349:    */     }
/* 350:367 */     this.tokens.add(new Token(TokenKind.IDENTIFIER, subarray, start, this.pos));
/* 351:    */   }
/* 352:    */   
/* 353:    */   private void pushIntToken(char[] data, boolean isLong, int start, int end)
/* 354:    */   {
/* 355:371 */     if (isLong) {
/* 356:372 */       this.tokens.add(new Token(TokenKind.LITERAL_LONG, data, start, end));
/* 357:    */     } else {
/* 358:374 */       this.tokens.add(new Token(TokenKind.LITERAL_INT, data, start, end));
/* 359:    */     }
/* 360:    */   }
/* 361:    */   
/* 362:    */   private void pushHexIntToken(char[] data, boolean isLong, int start, int end)
/* 363:    */   {
/* 364:379 */     if (data.length == 0)
/* 365:    */     {
/* 366:380 */       if (isLong) {
/* 367:381 */         throw new InternalParseException(new SpelParseException(this.expressionString, start, SpelMessage.NOT_A_LONG, new Object[] { this.expressionString.substring(start, end + 1) }));
/* 368:    */       }
/* 369:383 */       throw new InternalParseException(new SpelParseException(this.expressionString, start, SpelMessage.NOT_AN_INTEGER, new Object[] { this.expressionString.substring(start, end) }));
/* 370:    */     }
/* 371:386 */     if (isLong) {
/* 372:387 */       this.tokens.add(new Token(TokenKind.LITERAL_HEXLONG, data, start, end));
/* 373:    */     } else {
/* 374:389 */       this.tokens.add(new Token(TokenKind.LITERAL_HEXINT, data, start, end));
/* 375:    */     }
/* 376:    */   }
/* 377:    */   
/* 378:    */   private void pushRealToken(char[] data, boolean isFloat, int start, int end)
/* 379:    */   {
/* 380:394 */     if (isFloat) {
/* 381:395 */       this.tokens.add(new Token(TokenKind.LITERAL_REAL_FLOAT, data, start, end));
/* 382:    */     } else {
/* 383:397 */       this.tokens.add(new Token(TokenKind.LITERAL_REAL, data, start, end));
/* 384:    */     }
/* 385:    */   }
/* 386:    */   
/* 387:    */   private char[] subarray(int start, int end)
/* 388:    */   {
/* 389:402 */     char[] result = new char[end - start];
/* 390:403 */     System.arraycopy(this.toProcess, start, result, 0, end - start);
/* 391:404 */     return result;
/* 392:    */   }
/* 393:    */   
/* 394:    */   private boolean isTwoCharToken(TokenKind kind)
/* 395:    */   {
/* 396:411 */     Assert.isTrue(kind.tokenChars.length == 2);
/* 397:412 */     Assert.isTrue(this.toProcess[this.pos] == kind.tokenChars[0]);
/* 398:413 */     return this.toProcess[(this.pos + 1)] == kind.tokenChars[1];
/* 399:    */   }
/* 400:    */   
/* 401:    */   private void pushCharToken(TokenKind kind)
/* 402:    */   {
/* 403:420 */     this.tokens.add(new Token(kind, this.pos, this.pos + 1));
/* 404:421 */     this.pos += 1;
/* 405:    */   }
/* 406:    */   
/* 407:    */   private void pushPairToken(TokenKind kind)
/* 408:    */   {
/* 409:428 */     this.tokens.add(new Token(kind, this.pos, this.pos + 2));
/* 410:429 */     this.pos += 2;
/* 411:    */   }
/* 412:    */   
/* 413:    */   private void pushOneCharOrTwoCharToken(TokenKind kind, int pos, char[] data)
/* 414:    */   {
/* 415:433 */     this.tokens.add(new Token(kind, data, pos, pos + kind.getLength()));
/* 416:    */   }
/* 417:    */   
/* 418:    */   private boolean isIdentifier(char ch)
/* 419:    */   {
/* 420:438 */     return (isAlphabetic(ch)) || (isDigit(ch)) || (ch == '_') || (ch == '$');
/* 421:    */   }
/* 422:    */   
/* 423:    */   private boolean isChar(char a, char b)
/* 424:    */   {
/* 425:442 */     char ch = this.toProcess[this.pos];
/* 426:443 */     return (ch == a) || (ch == b);
/* 427:    */   }
/* 428:    */   
/* 429:    */   private boolean isExponentChar(char ch)
/* 430:    */   {
/* 431:447 */     return (ch == 'e') || (ch == 'E');
/* 432:    */   }
/* 433:    */   
/* 434:    */   private boolean isFloatSuffix(char ch)
/* 435:    */   {
/* 436:451 */     return (ch == 'f') || (ch == 'F');
/* 437:    */   }
/* 438:    */   
/* 439:    */   private boolean isDoubleSuffix(char ch)
/* 440:    */   {
/* 441:455 */     return (ch == 'd') || (ch == 'D');
/* 442:    */   }
/* 443:    */   
/* 444:    */   private boolean isSign(char ch)
/* 445:    */   {
/* 446:459 */     return (ch == '+') || (ch == '-');
/* 447:    */   }
/* 448:    */   
/* 449:    */   private boolean isDigit(char ch)
/* 450:    */   {
/* 451:463 */     if (ch > 'ÿ') {
/* 452:464 */       return false;
/* 453:    */     }
/* 454:466 */     return (flags[ch] & 0x1) != 0;
/* 455:    */   }
/* 456:    */   
/* 457:    */   private boolean isAlphabetic(char ch)
/* 458:    */   {
/* 459:470 */     if (ch > 'ÿ') {
/* 460:471 */       return false;
/* 461:    */     }
/* 462:473 */     return (flags[ch] & 0x4) != 0;
/* 463:    */   }
/* 464:    */   
/* 465:    */   private boolean isHexadecimalDigit(char ch)
/* 466:    */   {
/* 467:477 */     if (ch > 'ÿ') {
/* 468:478 */       return false;
/* 469:    */     }
/* 470:480 */     return (flags[ch] & 0x2) != 0;
/* 471:    */   }
/* 472:    */   
/* 473:483 */   private static final byte[] flags = new byte[256];
/* 474:    */   private static final byte IS_DIGIT = 1;
/* 475:    */   private static final byte IS_HEXDIGIT = 2;
/* 476:    */   private static final byte IS_ALPHA = 4;
/* 477:    */   
/* 478:    */   static
/* 479:    */   {
/* 480:489 */     for (int ch = 48; ch <= 57; ch++)
/* 481:    */     {
/* 482:490 */       int tmp74_73 = ch; byte[] tmp74_70 = flags;tmp74_70[tmp74_73] = ((byte)(tmp74_70[tmp74_73] | 0x3));
/* 483:    */     }
/* 484:492 */     for (int ch = 65; ch <= 70; ch++)
/* 485:    */     {
/* 486:493 */       int tmp99_98 = ch; byte[] tmp99_95 = flags;tmp99_95[tmp99_98] = ((byte)(tmp99_95[tmp99_98] | 0x2));
/* 487:    */     }
/* 488:495 */     for (int ch = 97; ch <= 102; ch++)
/* 489:    */     {
/* 490:496 */       int tmp124_123 = ch; byte[] tmp124_120 = flags;tmp124_120[tmp124_123] = ((byte)(tmp124_120[tmp124_123] | 0x2));
/* 491:    */     }
/* 492:498 */     for (int ch = 65; ch <= 90; ch++)
/* 493:    */     {
/* 494:499 */       int tmp149_148 = ch; byte[] tmp149_145 = flags;tmp149_145[tmp149_148] = ((byte)(tmp149_145[tmp149_148] | 0x4));
/* 495:    */     }
/* 496:501 */     for (int ch = 97; ch <= 122; ch++)
/* 497:    */     {
/* 498:502 */       int tmp174_173 = ch; byte[] tmp174_170 = flags;tmp174_170[tmp174_173] = ((byte)(tmp174_170[tmp174_173] | 0x4));
/* 499:    */     }
/* 500:    */   }
/* 501:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.standard.Tokenizer
 * JD-Core Version:    0.7.0.1
 */