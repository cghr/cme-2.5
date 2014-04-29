/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.InputStreamReader;
/*   6:    */ import java.io.Reader;
/*   7:    */ import jxl.biff.WorkbookMethods;
/*   8:    */ 
/*   9:    */ class Yylex
/*  10:    */ {
/*  11:    */   public static final int YYEOF = -1;
/*  12:    */   private static final int ZZ_BUFFERSIZE = 16384;
/*  13:    */   public static final int YYSTRING = 1;
/*  14:    */   public static final int YYINITIAL = 0;
/*  15:    */   private static final String ZZ_CMAP_PACKED = "";
/*  16: 63 */   private static final char[] ZZ_CMAP = zzUnpackCMap("");
/*  17: 68 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*  18:    */   private static final String ZZ_ACTION_PACKED_0 = "";
/*  19:    */   
/*  20:    */   private static int[] zzUnpackAction()
/*  21:    */   {
/*  22: 81 */     int[] result = new int[94];
/*  23: 82 */     int offset = 0;
/*  24: 83 */     offset = zzUnpackAction("", offset, result);
/*  25: 84 */     return result;
/*  26:    */   }
/*  27:    */   
/*  28:    */   private static int zzUnpackAction(String packed, int offset, int[] result)
/*  29:    */   {
/*  30: 88 */     int i = 0;
/*  31: 89 */     int j = offset;
/*  32: 90 */     int l = packed.length();
/*  33:    */     int count;
/*  34: 91 */     for (; i < l; count > 0)
/*  35:    */     {
/*  36: 92 */       count = packed.charAt(i++);
/*  37: 93 */       int value = packed.charAt(i++);
/*  38: 94 */       result[(j++)] = value;count--;
/*  39:    */     }
/*  40: 96 */     return j;
/*  41:    */   }
/*  42:    */   
/*  43:103 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*  44:    */   private static final String ZZ_ROWMAP_PACKED_0 = "";
/*  45:    */   
/*  46:    */   private static int[] zzUnpackRowMap()
/*  47:    */   {
/*  48:120 */     int[] result = new int[94];
/*  49:121 */     int offset = 0;
/*  50:122 */     offset = zzUnpackRowMap("", offset, result);
/*  51:123 */     return result;
/*  52:    */   }
/*  53:    */   
/*  54:    */   private static int zzUnpackRowMap(String packed, int offset, int[] result)
/*  55:    */   {
/*  56:127 */     int i = 0;
/*  57:128 */     int j = offset;
/*  58:129 */     int l = packed.length();
/*  59:130 */     while (i < l)
/*  60:    */     {
/*  61:131 */       int high = packed.charAt(i++) << '\020';
/*  62:132 */       result[(j++)] = (high | packed.charAt(i++));
/*  63:    */     }
/*  64:134 */     return j;
/*  65:    */   }
/*  66:    */   
/*  67:140 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*  68:    */   private static final String ZZ_TRANS_PACKED_0 = "";
/*  69:    */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*  70:    */   private static final int ZZ_NO_MATCH = 1;
/*  71:    */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*  72:    */   
/*  73:    */   private static int[] zzUnpackTrans()
/*  74:    */   {
/*  75:212 */     int[] result = new int[2627];
/*  76:213 */     int offset = 0;
/*  77:214 */     offset = zzUnpackTrans("", offset, result);
/*  78:215 */     return result;
/*  79:    */   }
/*  80:    */   
/*  81:    */   private static int zzUnpackTrans(String packed, int offset, int[] result)
/*  82:    */   {
/*  83:219 */     int i = 0;
/*  84:220 */     int j = offset;
/*  85:221 */     int l = packed.length();
/*  86:    */     int count;
/*  87:222 */     for (; i < l; count > 0)
/*  88:    */     {
/*  89:223 */       count = packed.charAt(i++);
/*  90:224 */       int value = packed.charAt(i++);
/*  91:225 */       value--;
/*  92:226 */       result[(j++)] = value;count--;
/*  93:    */     }
/*  94:228 */     return j;
/*  95:    */   }
/*  96:    */   
/*  97:238 */   private static final String[] ZZ_ERROR_MSG = {
/*  98:239 */     "Unkown internal scanner error", 
/*  99:240 */     "Error: could not match input", 
/* 100:241 */     "Error: pushback value was too large" };
/* 101:247 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/* 102:    */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "";
/* 103:    */   private Reader zzReader;
/* 104:    */   private int zzState;
/* 105:    */   
/* 106:    */   private static int[] zzUnpackAttribute()
/* 107:    */   {
/* 108:259 */     int[] result = new int[94];
/* 109:260 */     int offset = 0;
/* 110:261 */     offset = zzUnpackAttribute("", offset, result);
/* 111:262 */     return result;
/* 112:    */   }
/* 113:    */   
/* 114:    */   private static int zzUnpackAttribute(String packed, int offset, int[] result)
/* 115:    */   {
/* 116:266 */     int i = 0;
/* 117:267 */     int j = offset;
/* 118:268 */     int l = packed.length();
/* 119:    */     int count;
/* 120:269 */     for (; i < l; count > 0)
/* 121:    */     {
/* 122:270 */       count = packed.charAt(i++);
/* 123:271 */       int value = packed.charAt(i++);
/* 124:272 */       result[(j++)] = value;count--;
/* 125:    */     }
/* 126:274 */     return j;
/* 127:    */   }
/* 128:    */   
/* 129:284 */   private int zzLexicalState = 0;
/* 130:288 */   private char[] zzBuffer = new char[16384];
/* 131:    */   private int zzMarkedPos;
/* 132:    */   private int zzPushbackPos;
/* 133:    */   private int zzCurrentPos;
/* 134:    */   private int zzStartRead;
/* 135:    */   private int zzEndRead;
/* 136:    */   private int yyline;
/* 137:    */   private int yychar;
/* 138:    */   private int yycolumn;
/* 139:321 */   private boolean zzAtBOL = true;
/* 140:    */   private boolean zzAtEOF;
/* 141:    */   private boolean emptyString;
/* 142:    */   private ExternalSheet externalSheet;
/* 143:    */   private WorkbookMethods nameTable;
/* 144:    */   
/* 145:    */   int getPos()
/* 146:    */   {
/* 147:327 */     return this.yychar;
/* 148:    */   }
/* 149:    */   
/* 150:    */   void setExternalSheet(ExternalSheet es)
/* 151:    */   {
/* 152:334 */     this.externalSheet = es;
/* 153:    */   }
/* 154:    */   
/* 155:    */   void setNameTable(WorkbookMethods nt)
/* 156:    */   {
/* 157:339 */     this.nameTable = nt;
/* 158:    */   }
/* 159:    */   
/* 160:    */   Yylex(Reader in)
/* 161:    */   {
/* 162:350 */     this.zzReader = in;
/* 163:    */   }
/* 164:    */   
/* 165:    */   Yylex(InputStream in)
/* 166:    */   {
/* 167:360 */     this(new InputStreamReader(in));
/* 168:    */   }
/* 169:    */   
/* 170:    */   private static char[] zzUnpackCMap(String packed)
/* 171:    */   {
/* 172:370 */     char[] map = new char[65536];
/* 173:371 */     int i = 0;
/* 174:372 */     int j = 0;
/* 175:    */     int count;
/* 176:373 */     for (; i < 100; count > 0)
/* 177:    */     {
/* 178:374 */       count = packed.charAt(i++);
/* 179:375 */       char value = packed.charAt(i++);
/* 180:376 */       map[(j++)] = value;count--;
/* 181:    */     }
/* 182:378 */     return map;
/* 183:    */   }
/* 184:    */   
/* 185:    */   private boolean zzRefill()
/* 186:    */     throws IOException
/* 187:    */   {
/* 188:392 */     if (this.zzStartRead > 0)
/* 189:    */     {
/* 190:393 */       System.arraycopy(this.zzBuffer, this.zzStartRead, 
/* 191:394 */         this.zzBuffer, 0, 
/* 192:395 */         this.zzEndRead - this.zzStartRead);
/* 193:    */       
/* 194:    */ 
/* 195:398 */       this.zzEndRead -= this.zzStartRead;
/* 196:399 */       this.zzCurrentPos -= this.zzStartRead;
/* 197:400 */       this.zzMarkedPos -= this.zzStartRead;
/* 198:401 */       this.zzPushbackPos -= this.zzStartRead;
/* 199:402 */       this.zzStartRead = 0;
/* 200:    */     }
/* 201:406 */     if (this.zzCurrentPos >= this.zzBuffer.length)
/* 202:    */     {
/* 203:408 */       char[] newBuffer = new char[this.zzCurrentPos * 2];
/* 204:409 */       System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
/* 205:410 */       this.zzBuffer = newBuffer;
/* 206:    */     }
/* 207:414 */     int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, 
/* 208:415 */       this.zzBuffer.length - this.zzEndRead);
/* 209:417 */     if (numRead < 0) {
/* 210:418 */       return true;
/* 211:    */     }
/* 212:421 */     this.zzEndRead += numRead;
/* 213:422 */     return false;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public final void yyclose()
/* 217:    */     throws IOException
/* 218:    */   {
/* 219:431 */     this.zzAtEOF = true;
/* 220:432 */     this.zzEndRead = this.zzStartRead;
/* 221:434 */     if (this.zzReader != null) {
/* 222:435 */       this.zzReader.close();
/* 223:    */     }
/* 224:    */   }
/* 225:    */   
/* 226:    */   public final void yyreset(Reader reader)
/* 227:    */   {
/* 228:450 */     this.zzReader = reader;
/* 229:451 */     this.zzAtBOL = true;
/* 230:452 */     this.zzAtEOF = false;
/* 231:453 */     this.zzEndRead = (this.zzStartRead = 0);
/* 232:454 */     this.zzCurrentPos = (this.zzMarkedPos = this.zzPushbackPos = 0);
/* 233:455 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 234:456 */     this.zzLexicalState = 0;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public final int yystate()
/* 238:    */   {
/* 239:464 */     return this.zzLexicalState;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public final void yybegin(int newState)
/* 243:    */   {
/* 244:474 */     this.zzLexicalState = newState;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public final String yytext()
/* 248:    */   {
/* 249:482 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
/* 250:    */   }
/* 251:    */   
/* 252:    */   public final char yycharat(int pos)
/* 253:    */   {
/* 254:498 */     return this.zzBuffer[(this.zzStartRead + pos)];
/* 255:    */   }
/* 256:    */   
/* 257:    */   public final int yylength()
/* 258:    */   {
/* 259:506 */     return this.zzMarkedPos - this.zzStartRead;
/* 260:    */   }
/* 261:    */   
/* 262:    */   private void zzScanError(int errorCode)
/* 263:    */   {
/* 264:    */     String message;
/* 265:    */     try
/* 266:    */     {
/* 267:527 */       message = ZZ_ERROR_MSG[errorCode];
/* 268:    */     }
/* 269:    */     catch (ArrayIndexOutOfBoundsException e)
/* 270:    */     {
/* 271:    */       String message;
/* 272:530 */       message = ZZ_ERROR_MSG[0];
/* 273:    */     }
/* 274:533 */     throw new Error(message);
/* 275:    */   }
/* 276:    */   
/* 277:    */   public void yypushback(int number)
/* 278:    */   {
/* 279:546 */     if (number > yylength()) {
/* 280:547 */       zzScanError(2);
/* 281:    */     }
/* 282:549 */     this.zzMarkedPos -= number;
/* 283:    */   }
/* 284:    */   
/* 285:    */   public ParseItem yylex()
/* 286:    */     throws IOException, FormulaException
/* 287:    */   {
/* 288:568 */     int zzEndReadL = this.zzEndRead;
/* 289:569 */     char[] zzBufferL = this.zzBuffer;
/* 290:570 */     char[] zzCMapL = ZZ_CMAP;
/* 291:    */     
/* 292:572 */     int[] zzTransL = ZZ_TRANS;
/* 293:573 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 294:574 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/* 295:    */     for (;;)
/* 296:    */     {
/* 297:577 */       int zzMarkedPosL = this.zzMarkedPos;
/* 298:    */       
/* 299:579 */       this.yychar += zzMarkedPosL - this.zzStartRead;
/* 300:    */       
/* 301:581 */       boolean zzR = false;
/* 302:582 */       for (int zzCurrentPosL = this.zzStartRead; zzCurrentPosL < zzMarkedPosL; zzCurrentPosL++) {
/* 303:584 */         switch (zzBufferL[zzCurrentPosL])
/* 304:    */         {
/* 305:    */         case '\013': 
/* 306:    */         case '\f': 
/* 307:    */         case '': 
/* 308:    */         case ' ': 
/* 309:    */         case ' ': 
/* 310:590 */           this.yyline += 1;
/* 311:591 */           zzR = false;
/* 312:592 */           break;
/* 313:    */         case '\r': 
/* 314:594 */           this.yyline += 1;
/* 315:595 */           zzR = true;
/* 316:596 */           break;
/* 317:    */         case '\n': 
/* 318:598 */           if (zzR) {
/* 319:599 */             zzR = false;
/* 320:    */           } else {
/* 321:601 */             this.yyline += 1;
/* 322:    */           }
/* 323:603 */           break;
/* 324:    */         default: 
/* 325:605 */           zzR = false;
/* 326:    */         }
/* 327:    */       }
/* 328:609 */       if (zzR)
/* 329:    */       {
/* 330:    */         boolean zzPeek;
/* 331:    */         boolean zzPeek;
/* 332:612 */         if (zzMarkedPosL < zzEndReadL)
/* 333:    */         {
/* 334:613 */           zzPeek = zzBufferL[zzMarkedPosL] == '\n';
/* 335:    */         }
/* 336:    */         else
/* 337:    */         {
/* 338:    */           boolean zzPeek;
/* 339:614 */           if (this.zzAtEOF)
/* 340:    */           {
/* 341:615 */             zzPeek = false;
/* 342:    */           }
/* 343:    */           else
/* 344:    */           {
/* 345:617 */             boolean eof = zzRefill();
/* 346:618 */             zzEndReadL = this.zzEndRead;
/* 347:619 */             zzMarkedPosL = this.zzMarkedPos;
/* 348:620 */             zzBufferL = this.zzBuffer;
/* 349:    */             boolean zzPeek;
/* 350:621 */             if (eof) {
/* 351:622 */               zzPeek = false;
/* 352:    */             } else {
/* 353:624 */               zzPeek = zzBufferL[zzMarkedPosL] == '\n';
/* 354:    */             }
/* 355:    */           }
/* 356:    */         }
/* 357:626 */         if (zzPeek) {
/* 358:626 */           this.yyline -= 1;
/* 359:    */         }
/* 360:    */       }
/* 361:628 */       int zzAction = -1;
/* 362:    */       
/* 363:630 */       zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/* 364:    */       
/* 365:632 */       this.zzState = this.zzLexicalState;
/* 366:    */       int zzInput;
/* 367:    */       int zzAttributes;
/* 368:    */       do
/* 369:    */       {
/* 370:    */         do
/* 371:    */         {
/* 372:    */           int zzInput;
/* 373:638 */           if (zzCurrentPosL < zzEndReadL)
/* 374:    */           {
/* 375:639 */             zzInput = zzBufferL[(zzCurrentPosL++)];
/* 376:    */           }
/* 377:    */           else
/* 378:    */           {
/* 379:640 */             if (this.zzAtEOF)
/* 380:    */             {
/* 381:641 */               int zzInput = -1;
/* 382:642 */               break;
/* 383:    */             }
/* 384:646 */             this.zzCurrentPos = zzCurrentPosL;
/* 385:647 */             this.zzMarkedPos = zzMarkedPosL;
/* 386:648 */             boolean eof = zzRefill();
/* 387:    */             
/* 388:650 */             zzCurrentPosL = this.zzCurrentPos;
/* 389:651 */             zzMarkedPosL = this.zzMarkedPos;
/* 390:652 */             zzBufferL = this.zzBuffer;
/* 391:653 */             zzEndReadL = this.zzEndRead;
/* 392:654 */             if (eof)
/* 393:    */             {
/* 394:655 */               int zzInput = -1;
/* 395:656 */               break;
/* 396:    */             }
/* 397:659 */             zzInput = zzBufferL[(zzCurrentPosL++)];
/* 398:    */           }
/* 399:662 */           int zzNext = zzTransL[(zzRowMapL[this.zzState] + zzCMapL[zzInput])];
/* 400:663 */           if (zzNext == -1) {
/* 401:    */             break;
/* 402:    */           }
/* 403:664 */           this.zzState = zzNext;
/* 404:    */           
/* 405:666 */           zzAttributes = zzAttrL[this.zzState];
/* 406:667 */         } while ((zzAttributes & 0x1) != 1);
/* 407:668 */         zzAction = this.zzState;
/* 408:669 */         zzMarkedPosL = zzCurrentPosL;
/* 409:670 */       } while ((zzAttributes & 0x8) != 8);
/* 410:677 */       this.zzMarkedPos = zzMarkedPosL;
/* 411:679 */       switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction])
/* 412:    */       {
/* 413:    */       case 12: 
/* 414:681 */         return new Minus();
/* 415:    */       case 31: 
/* 416:    */         break;
/* 417:    */       case 7: 
/* 418:685 */         return new CloseParentheses();
/* 419:    */       case 32: 
/* 420:    */         break;
/* 421:    */       case 3: 
/* 422:689 */         return new IntegerValue(yytext());
/* 423:    */       case 33: 
/* 424:    */         break;
/* 425:    */       case 24: 
/* 426:693 */         return new DoubleValue(yytext());
/* 427:    */       case 34: 
/* 428:    */         break;
/* 429:    */       case 29: 
/* 430:697 */         return new ColumnRange3d(yytext(), this.externalSheet);
/* 431:    */       case 35: 
/* 432:    */         break;
/* 433:    */       case 4: 
/* 434:701 */         return new RangeSeparator();
/* 435:    */       case 36: 
/* 436:    */         break;
/* 437:    */       case 10: 
/* 438:705 */         return new Divide();
/* 439:    */       case 37: 
/* 440:    */         break;
/* 441:    */       case 25: 
/* 442:709 */         return new CellReference3d(yytext(), this.externalSheet);
/* 443:    */       case 38: 
/* 444:    */         break;
/* 445:    */       case 26: 
/* 446:713 */         return new BooleanValue(yytext());
/* 447:    */       case 39: 
/* 448:    */         break;
/* 449:    */       case 15: 
/* 450:717 */         return new Equal();
/* 451:    */       case 40: 
/* 452:    */         break;
/* 453:    */       case 17: 
/* 454:721 */         yybegin(0);
/* 455:721 */         if (this.emptyString) {
/* 456:721 */           return new StringValue("");
/* 457:    */         }
/* 458:    */         break;
/* 459:    */       case 41: 
/* 460:    */         break;
/* 461:    */       case 8: 
/* 462:725 */         this.emptyString = true;yybegin(1);
/* 463:    */       case 42: 
/* 464:    */         break;
/* 465:    */       case 21: 
/* 466:729 */         return new NotEqual();
/* 467:    */       case 43: 
/* 468:    */         break;
/* 469:    */       case 22: 
/* 470:733 */         return new LessEqual();
/* 471:    */       case 44: 
/* 472:    */         break;
/* 473:    */       case 16: 
/* 474:737 */         return new LessThan();
/* 475:    */       case 45: 
/* 476:    */         break;
/* 477:    */       case 5: 
/* 478:741 */         return new ArgumentSeparator();
/* 479:    */       case 46: 
/* 480:    */         break;
/* 481:    */       case 30: 
/* 482:745 */         return new Area3d(yytext(), this.externalSheet);
/* 483:    */       case 47: 
/* 484:    */         break;
/* 485:    */       case 14: 
/* 486:749 */         return new GreaterThan();
/* 487:    */       case 48: 
/* 488:    */         break;
/* 489:    */       case 18: 
/* 490:753 */         return new CellReference(yytext());
/* 491:    */       case 49: 
/* 492:    */         break;
/* 493:    */       case 20: 
/* 494:757 */         return new GreaterEqual();
/* 495:    */       case 50: 
/* 496:    */         break;
/* 497:    */       case 27: 
/* 498:761 */         return new Area(yytext());
/* 499:    */       case 51: 
/* 500:    */         break;
/* 501:    */       case 23: 
/* 502:765 */         return new ColumnRange(yytext());
/* 503:    */       case 52: 
/* 504:    */         break;
/* 505:    */       case 1: 
/* 506:769 */         this.emptyString = false;return new StringValue(yytext());
/* 507:    */       case 53: 
/* 508:    */         break;
/* 509:    */       case 2: 
/* 510:773 */         return new NameRange(yytext(), this.nameTable);
/* 511:    */       case 54: 
/* 512:    */         break;
/* 513:    */       case 19: 
/* 514:777 */         return new StringFunction(yytext());
/* 515:    */       case 55: 
/* 516:    */         break;
/* 517:    */       case 11: 
/* 518:781 */         return new Plus();
/* 519:    */       case 56: 
/* 520:    */         break;
/* 521:    */       case 28: 
/* 522:785 */         return new ErrorConstant(yytext());
/* 523:    */       case 57: 
/* 524:    */         break;
/* 525:    */       case 9: 
/* 526:    */       case 58: 
/* 527:    */         break;
/* 528:    */       case 13: 
/* 529:793 */         return new Multiply();
/* 530:    */       case 59: 
/* 531:    */         break;
/* 532:    */       case 6: 
/* 533:797 */         return new OpenParentheses();
/* 534:    */       case 60: 
/* 535:    */         break;
/* 536:    */       default: 
/* 537:801 */         if ((zzInput == -1) && (this.zzStartRead == this.zzCurrentPos))
/* 538:    */         {
/* 539:802 */           this.zzAtEOF = true;
/* 540:803 */           return null;
/* 541:    */         }
/* 542:806 */         zzScanError(1);
/* 543:    */       }
/* 544:    */     }
/* 545:    */   }
/* 546:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Yylex
 * JD-Core Version:    0.7.0.1
 */