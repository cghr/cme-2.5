/*   1:    */ package org.springframework.expression.spel.standard;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Stack;
/*   6:    */ import org.springframework.expression.ParseException;
/*   7:    */ import org.springframework.expression.ParserContext;
/*   8:    */ import org.springframework.expression.common.TemplateAwareExpressionParser;
/*   9:    */ import org.springframework.expression.spel.InternalParseException;
/*  10:    */ import org.springframework.expression.spel.SpelMessage;
/*  11:    */ import org.springframework.expression.spel.SpelParseException;
/*  12:    */ import org.springframework.expression.spel.SpelParserConfiguration;
/*  13:    */ import org.springframework.expression.spel.ast.Assign;
/*  14:    */ import org.springframework.expression.spel.ast.BeanReference;
/*  15:    */ import org.springframework.expression.spel.ast.BooleanLiteral;
/*  16:    */ import org.springframework.expression.spel.ast.CompoundExpression;
/*  17:    */ import org.springframework.expression.spel.ast.ConstructorReference;
/*  18:    */ import org.springframework.expression.spel.ast.Elvis;
/*  19:    */ import org.springframework.expression.spel.ast.FunctionReference;
/*  20:    */ import org.springframework.expression.spel.ast.Identifier;
/*  21:    */ import org.springframework.expression.spel.ast.Indexer;
/*  22:    */ import org.springframework.expression.spel.ast.InlineList;
/*  23:    */ import org.springframework.expression.spel.ast.Literal;
/*  24:    */ import org.springframework.expression.spel.ast.MethodReference;
/*  25:    */ import org.springframework.expression.spel.ast.NullLiteral;
/*  26:    */ import org.springframework.expression.spel.ast.OpAnd;
/*  27:    */ import org.springframework.expression.spel.ast.OpDivide;
/*  28:    */ import org.springframework.expression.spel.ast.OpEQ;
/*  29:    */ import org.springframework.expression.spel.ast.OpGE;
/*  30:    */ import org.springframework.expression.spel.ast.OpGT;
/*  31:    */ import org.springframework.expression.spel.ast.OpLE;
/*  32:    */ import org.springframework.expression.spel.ast.OpLT;
/*  33:    */ import org.springframework.expression.spel.ast.OpMinus;
/*  34:    */ import org.springframework.expression.spel.ast.OpModulus;
/*  35:    */ import org.springframework.expression.spel.ast.OpMultiply;
/*  36:    */ import org.springframework.expression.spel.ast.OpNE;
/*  37:    */ import org.springframework.expression.spel.ast.OpOr;
/*  38:    */ import org.springframework.expression.spel.ast.OpPlus;
/*  39:    */ import org.springframework.expression.spel.ast.OperatorBetween;
/*  40:    */ import org.springframework.expression.spel.ast.OperatorInstanceof;
/*  41:    */ import org.springframework.expression.spel.ast.OperatorMatches;
/*  42:    */ import org.springframework.expression.spel.ast.OperatorNot;
/*  43:    */ import org.springframework.expression.spel.ast.OperatorPower;
/*  44:    */ import org.springframework.expression.spel.ast.Projection;
/*  45:    */ import org.springframework.expression.spel.ast.PropertyOrFieldReference;
/*  46:    */ import org.springframework.expression.spel.ast.QualifiedIdentifier;
/*  47:    */ import org.springframework.expression.spel.ast.Selection;
/*  48:    */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*  49:    */ import org.springframework.expression.spel.ast.StringLiteral;
/*  50:    */ import org.springframework.expression.spel.ast.Ternary;
/*  51:    */ import org.springframework.expression.spel.ast.TypeReference;
/*  52:    */ import org.springframework.expression.spel.ast.VariableReference;
/*  53:    */ import org.springframework.util.Assert;
/*  54:    */ 
/*  55:    */ class InternalSpelExpressionParser
/*  56:    */   extends TemplateAwareExpressionParser
/*  57:    */ {
/*  58:    */   private String expressionString;
/*  59:    */   private List<Token> tokenStream;
/*  60:    */   private int tokenStreamLength;
/*  61:    */   private int tokenStreamPointer;
/*  62: 92 */   private Stack<SpelNodeImpl> constructedNodes = new Stack();
/*  63:    */   private SpelParserConfiguration configuration;
/*  64:    */   
/*  65:    */   public InternalSpelExpressionParser(SpelParserConfiguration configuration)
/*  66:    */   {
/*  67:102 */     this.configuration = configuration;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected SpelExpression doParseExpression(String expressionString, ParserContext context)
/*  71:    */     throws ParseException
/*  72:    */   {
/*  73:    */     try
/*  74:    */     {
/*  75:109 */       this.expressionString = expressionString;
/*  76:110 */       Tokenizer tokenizer = new Tokenizer(expressionString);
/*  77:111 */       tokenizer.process();
/*  78:112 */       this.tokenStream = tokenizer.getTokens();
/*  79:113 */       this.tokenStreamLength = this.tokenStream.size();
/*  80:114 */       this.tokenStreamPointer = 0;
/*  81:115 */       this.constructedNodes.clear();
/*  82:116 */       SpelNodeImpl ast = eatExpression();
/*  83:117 */       if (moreTokens()) {
/*  84:118 */         throw new SpelParseException(peekToken().startpos, SpelMessage.MORE_INPUT, new Object[] { toString(nextToken()) });
/*  85:    */       }
/*  86:120 */       Assert.isTrue(this.constructedNodes.isEmpty());
/*  87:121 */       return new SpelExpression(expressionString, ast, this.configuration);
/*  88:    */     }
/*  89:    */     catch (InternalParseException ipe)
/*  90:    */     {
/*  91:124 */       throw ipe.getCause();
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   private SpelNodeImpl eatExpression()
/*  96:    */   {
/*  97:135 */     SpelNodeImpl expr = eatLogicalOrExpression();
/*  98:136 */     if (moreTokens())
/*  99:    */     {
/* 100:137 */       Token t = peekToken();
/* 101:138 */       if (t.kind == TokenKind.ASSIGN)
/* 102:    */       {
/* 103:139 */         if (expr == null) {
/* 104:140 */           expr = new NullLiteral(toPos(t.startpos - 1, t.endpos - 1));
/* 105:    */         }
/* 106:142 */         nextToken();
/* 107:143 */         SpelNodeImpl assignedValue = eatLogicalOrExpression();
/* 108:144 */         return new Assign(toPos(t), new SpelNodeImpl[] { expr, assignedValue });
/* 109:    */       }
/* 110:145 */       if (t.kind == TokenKind.ELVIS)
/* 111:    */       {
/* 112:146 */         if (expr == null) {
/* 113:147 */           expr = new NullLiteral(toPos(t.startpos - 1, t.endpos - 2));
/* 114:    */         }
/* 115:149 */         nextToken();
/* 116:150 */         SpelNodeImpl valueIfNull = eatExpression();
/* 117:151 */         if (valueIfNull == null) {
/* 118:152 */           valueIfNull = new NullLiteral(toPos(t.startpos + 1, t.endpos + 1));
/* 119:    */         }
/* 120:154 */         return new Elvis(toPos(t), new SpelNodeImpl[] { expr, valueIfNull });
/* 121:    */       }
/* 122:155 */       if (t.kind == TokenKind.QMARK)
/* 123:    */       {
/* 124:156 */         if (expr == null) {
/* 125:157 */           expr = new NullLiteral(toPos(t.startpos - 1, t.endpos - 1));
/* 126:    */         }
/* 127:159 */         nextToken();
/* 128:160 */         SpelNodeImpl ifTrueExprValue = eatExpression();
/* 129:161 */         eatToken(TokenKind.COLON);
/* 130:162 */         SpelNodeImpl ifFalseExprValue = eatExpression();
/* 131:163 */         return new Ternary(toPos(t), new SpelNodeImpl[] { expr, ifTrueExprValue, ifFalseExprValue });
/* 132:    */       }
/* 133:    */     }
/* 134:166 */     return expr;
/* 135:    */   }
/* 136:    */   
/* 137:    */   private SpelNodeImpl eatLogicalOrExpression()
/* 138:    */   {
/* 139:171 */     SpelNodeImpl expr = eatLogicalAndExpression();
/* 140:172 */     while (peekIdentifierToken("or"))
/* 141:    */     {
/* 142:173 */       Token t = nextToken();
/* 143:174 */       SpelNodeImpl rhExpr = eatLogicalAndExpression();
/* 144:175 */       checkRightOperand(t, rhExpr);
/* 145:176 */       expr = new OpOr(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 146:    */     }
/* 147:178 */     return expr;
/* 148:    */   }
/* 149:    */   
/* 150:    */   private SpelNodeImpl eatLogicalAndExpression()
/* 151:    */   {
/* 152:183 */     SpelNodeImpl expr = eatRelationalExpression();
/* 153:184 */     while (peekIdentifierToken("and"))
/* 154:    */     {
/* 155:185 */       Token t = nextToken();
/* 156:186 */       SpelNodeImpl rhExpr = eatRelationalExpression();
/* 157:187 */       checkRightOperand(t, rhExpr);
/* 158:188 */       expr = new OpAnd(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 159:    */     }
/* 160:190 */     return expr;
/* 161:    */   }
/* 162:    */   
/* 163:    */   private SpelNodeImpl eatRelationalExpression()
/* 164:    */   {
/* 165:195 */     SpelNodeImpl expr = eatSumExpression();
/* 166:196 */     Token relationalOperatorToken = maybeEatRelationalOperator();
/* 167:197 */     if (relationalOperatorToken != null)
/* 168:    */     {
/* 169:198 */       Token t = nextToken();
/* 170:199 */       SpelNodeImpl rhExpr = eatSumExpression();
/* 171:200 */       checkRightOperand(t, rhExpr);
/* 172:201 */       TokenKind tk = relationalOperatorToken.kind;
/* 173:202 */       if (relationalOperatorToken.isNumericRelationalOperator())
/* 174:    */       {
/* 175:203 */         int pos = toPos(t);
/* 176:204 */         if (tk == TokenKind.GT) {
/* 177:205 */           return new OpGT(pos, new SpelNodeImpl[] { expr, rhExpr });
/* 178:    */         }
/* 179:206 */         if (tk == TokenKind.LT) {
/* 180:207 */           return new OpLT(pos, new SpelNodeImpl[] { expr, rhExpr });
/* 181:    */         }
/* 182:208 */         if (tk == TokenKind.LE) {
/* 183:209 */           return new OpLE(pos, new SpelNodeImpl[] { expr, rhExpr });
/* 184:    */         }
/* 185:210 */         if (tk == TokenKind.GE) {
/* 186:211 */           return new OpGE(pos, new SpelNodeImpl[] { expr, rhExpr });
/* 187:    */         }
/* 188:212 */         if (tk == TokenKind.EQ) {
/* 189:213 */           return new OpEQ(pos, new SpelNodeImpl[] { expr, rhExpr });
/* 190:    */         }
/* 191:215 */         Assert.isTrue(tk == TokenKind.NE);
/* 192:216 */         return new OpNE(pos, new SpelNodeImpl[] { expr, rhExpr });
/* 193:    */       }
/* 194:219 */       if (tk == TokenKind.INSTANCEOF) {
/* 195:220 */         return new OperatorInstanceof(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 196:    */       }
/* 197:221 */       if (tk == TokenKind.MATCHES) {
/* 198:222 */         return new OperatorMatches(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 199:    */       }
/* 200:224 */       Assert.isTrue(tk == TokenKind.BETWEEN);
/* 201:225 */       return new OperatorBetween(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 202:    */     }
/* 203:228 */     return expr;
/* 204:    */   }
/* 205:    */   
/* 206:    */   private SpelNodeImpl eatSumExpression()
/* 207:    */   {
/* 208:233 */     SpelNodeImpl expr = eatProductExpression();
/* 209:234 */     while (peekToken(TokenKind.PLUS, TokenKind.MINUS))
/* 210:    */     {
/* 211:235 */       Token t = nextToken();
/* 212:236 */       SpelNodeImpl rhExpr = eatProductExpression();
/* 213:237 */       checkRightOperand(t, rhExpr);
/* 214:238 */       if (t.kind == TokenKind.PLUS)
/* 215:    */       {
/* 216:239 */         expr = new OpPlus(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 217:    */       }
/* 218:    */       else
/* 219:    */       {
/* 220:241 */         Assert.isTrue(t.kind == TokenKind.MINUS);
/* 221:242 */         expr = new OpMinus(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 222:    */       }
/* 223:    */     }
/* 224:245 */     return expr;
/* 225:    */   }
/* 226:    */   
/* 227:    */   private SpelNodeImpl eatProductExpression()
/* 228:    */   {
/* 229:250 */     SpelNodeImpl expr = eatPowerExpression();
/* 230:251 */     while (peekToken(TokenKind.STAR, TokenKind.DIV, TokenKind.MOD))
/* 231:    */     {
/* 232:252 */       Token t = nextToken();
/* 233:253 */       SpelNodeImpl rhExpr = eatPowerExpression();
/* 234:254 */       checkRightOperand(t, rhExpr);
/* 235:255 */       if (t.kind == TokenKind.STAR)
/* 236:    */       {
/* 237:256 */         expr = new OpMultiply(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 238:    */       }
/* 239:257 */       else if (t.kind == TokenKind.DIV)
/* 240:    */       {
/* 241:258 */         expr = new OpDivide(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 242:    */       }
/* 243:    */       else
/* 244:    */       {
/* 245:260 */         Assert.isTrue(t.kind == TokenKind.MOD);
/* 246:261 */         expr = new OpModulus(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 247:    */       }
/* 248:    */     }
/* 249:264 */     return expr;
/* 250:    */   }
/* 251:    */   
/* 252:    */   private SpelNodeImpl eatPowerExpression()
/* 253:    */   {
/* 254:269 */     SpelNodeImpl expr = eatUnaryExpression();
/* 255:270 */     if (peekToken(TokenKind.POWER))
/* 256:    */     {
/* 257:271 */       Token t = nextToken();
/* 258:272 */       SpelNodeImpl rhExpr = eatUnaryExpression();
/* 259:273 */       checkRightOperand(t, rhExpr);
/* 260:274 */       return new OperatorPower(toPos(t), new SpelNodeImpl[] { expr, rhExpr });
/* 261:    */     }
/* 262:276 */     return expr;
/* 263:    */   }
/* 264:    */   
/* 265:    */   private SpelNodeImpl eatUnaryExpression()
/* 266:    */   {
/* 267:281 */     if (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.NOT))
/* 268:    */     {
/* 269:282 */       Token t = nextToken();
/* 270:283 */       SpelNodeImpl expr = eatUnaryExpression();
/* 271:284 */       if (t.kind == TokenKind.NOT) {
/* 272:285 */         return new OperatorNot(toPos(t), expr);
/* 273:    */       }
/* 274:286 */       if (t.kind == TokenKind.PLUS) {
/* 275:287 */         return new OpPlus(toPos(t), new SpelNodeImpl[] { expr });
/* 276:    */       }
/* 277:289 */       Assert.isTrue(t.kind == TokenKind.MINUS);
/* 278:290 */       return new OpMinus(toPos(t), new SpelNodeImpl[] { expr });
/* 279:    */     }
/* 280:293 */     return eatPrimaryExpression();
/* 281:    */   }
/* 282:    */   
/* 283:    */   private SpelNodeImpl eatPrimaryExpression()
/* 284:    */   {
/* 285:299 */     List<SpelNodeImpl> nodes = new ArrayList();
/* 286:300 */     SpelNodeImpl start = eatStartNode();
/* 287:301 */     nodes.add(start);
/* 288:302 */     while (maybeEatNode()) {
/* 289:303 */       nodes.add(pop());
/* 290:    */     }
/* 291:305 */     if (nodes.size() == 1) {
/* 292:306 */       return (SpelNodeImpl)nodes.get(0);
/* 293:    */     }
/* 294:308 */     return new CompoundExpression(toPos(start.getStartPosition(), ((SpelNodeImpl)nodes.get(nodes.size() - 1)).getEndPosition()), (SpelNodeImpl[])nodes.toArray(new SpelNodeImpl[nodes.size()]));
/* 295:    */   }
/* 296:    */   
/* 297:    */   private boolean maybeEatNode()
/* 298:    */   {
/* 299:314 */     SpelNodeImpl expr = null;
/* 300:315 */     if (peekToken(TokenKind.DOT, TokenKind.SAFE_NAVI)) {
/* 301:316 */       expr = eatDottedNode();
/* 302:    */     } else {
/* 303:318 */       expr = maybeEatNonDottedNode();
/* 304:    */     }
/* 305:320 */     if (expr == null) {
/* 306:321 */       return false;
/* 307:    */     }
/* 308:323 */     push(expr);
/* 309:324 */     return true;
/* 310:    */   }
/* 311:    */   
/* 312:    */   private SpelNodeImpl maybeEatNonDottedNode()
/* 313:    */   {
/* 314:330 */     if ((peekToken(TokenKind.LSQUARE)) && 
/* 315:331 */       (maybeEatIndexer())) {
/* 316:332 */       return pop();
/* 317:    */     }
/* 318:335 */     return null;
/* 319:    */   }
/* 320:    */   
/* 321:    */   private SpelNodeImpl eatDottedNode()
/* 322:    */   {
/* 323:348 */     Token t = nextToken();
/* 324:349 */     boolean nullSafeNavigation = t.kind == TokenKind.SAFE_NAVI;
/* 325:350 */     if ((maybeEatMethodOrProperty(nullSafeNavigation)) || (maybeEatFunctionOrVar()) || (maybeEatProjection(nullSafeNavigation)) || (maybeEatSelection(nullSafeNavigation))) {
/* 326:351 */       return pop();
/* 327:    */     }
/* 328:353 */     if (peekToken() == null) {
/* 329:355 */       raiseInternalException(t.startpos, SpelMessage.OOD, new Object[0]);
/* 330:    */     } else {
/* 331:357 */       raiseInternalException(t.startpos, SpelMessage.UNEXPECTED_DATA_AFTER_DOT, new Object[] { toString(peekToken()) });
/* 332:    */     }
/* 333:359 */     return null;
/* 334:    */   }
/* 335:    */   
/* 336:    */   private boolean maybeEatFunctionOrVar()
/* 337:    */   {
/* 338:369 */     if (!peekToken(TokenKind.HASH)) {
/* 339:370 */       return false;
/* 340:    */     }
/* 341:372 */     Token t = nextToken();
/* 342:373 */     Token functionOrVariableName = eatToken(TokenKind.IDENTIFIER);
/* 343:374 */     SpelNodeImpl[] args = maybeEatMethodArgs();
/* 344:375 */     if (args == null)
/* 345:    */     {
/* 346:376 */       push(new VariableReference(functionOrVariableName.data, toPos(t.startpos, functionOrVariableName.endpos)));
/* 347:377 */       return true;
/* 348:    */     }
/* 349:379 */     push(new FunctionReference(functionOrVariableName.data, toPos(t.startpos, functionOrVariableName.endpos), args));
/* 350:380 */     return true;
/* 351:    */   }
/* 352:    */   
/* 353:    */   private SpelNodeImpl[] maybeEatMethodArgs()
/* 354:    */   {
/* 355:386 */     if (!peekToken(TokenKind.LPAREN)) {
/* 356:387 */       return null;
/* 357:    */     }
/* 358:389 */     List<SpelNodeImpl> args = new ArrayList();
/* 359:390 */     consumeArguments(args);
/* 360:391 */     eatToken(TokenKind.RPAREN);
/* 361:392 */     return (SpelNodeImpl[])args.toArray(new SpelNodeImpl[args.size()]);
/* 362:    */   }
/* 363:    */   
/* 364:    */   private void eatConstructorArgs(List<SpelNodeImpl> accumulatedArguments)
/* 365:    */   {
/* 366:396 */     if (!peekToken(TokenKind.LPAREN)) {
/* 367:397 */       throw new InternalParseException(new SpelParseException(this.expressionString, positionOf(peekToken()), SpelMessage.MISSING_CONSTRUCTOR_ARGS, new Object[0]));
/* 368:    */     }
/* 369:399 */     consumeArguments(accumulatedArguments);
/* 370:400 */     eatToken(TokenKind.RPAREN);
/* 371:    */   }
/* 372:    */   
/* 373:    */   private void consumeArguments(List<SpelNodeImpl> accumulatedArguments)
/* 374:    */   {
/* 375:407 */     int pos = peekToken().startpos;
/* 376:408 */     Token next = null;
/* 377:    */     do
/* 378:    */     {
/* 379:410 */       nextToken();
/* 380:411 */       Token t = peekToken();
/* 381:412 */       if (t == null) {
/* 382:413 */         raiseInternalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
/* 383:    */       }
/* 384:415 */       if (t.kind != TokenKind.RPAREN) {
/* 385:416 */         accumulatedArguments.add(eatExpression());
/* 386:    */       }
/* 387:418 */       next = peekToken();
/* 388:419 */     } while ((next != null) && (next.kind == TokenKind.COMMA));
/* 389:420 */     if (next == null) {
/* 390:421 */       raiseInternalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
/* 391:    */     }
/* 392:    */   }
/* 393:    */   
/* 394:    */   private int positionOf(Token t)
/* 395:    */   {
/* 396:426 */     if (t == null) {
/* 397:429 */       return this.expressionString.length();
/* 398:    */     }
/* 399:431 */     return t.startpos;
/* 400:    */   }
/* 401:    */   
/* 402:    */   private SpelNodeImpl eatStartNode()
/* 403:    */   {
/* 404:448 */     if (maybeEatLiteral()) {
/* 405:449 */       return pop();
/* 406:    */     }
/* 407:450 */     if (maybeEatParenExpression()) {
/* 408:451 */       return pop();
/* 409:    */     }
/* 410:452 */     if ((maybeEatTypeReference()) || (maybeEatNullReference()) || (maybeEatConstructorReference()) || (maybeEatMethodOrProperty(false)) || (maybeEatFunctionOrVar())) {
/* 411:453 */       return pop();
/* 412:    */     }
/* 413:454 */     if (maybeEatBeanReference()) {
/* 414:455 */       return pop();
/* 415:    */     }
/* 416:456 */     if ((maybeEatProjection(false)) || (maybeEatSelection(false)) || (maybeEatIndexer())) {
/* 417:457 */       return pop();
/* 418:    */     }
/* 419:458 */     if (maybeEatInlineList()) {
/* 420:459 */       return pop();
/* 421:    */     }
/* 422:461 */     return null;
/* 423:    */   }
/* 424:    */   
/* 425:    */   private boolean maybeEatBeanReference()
/* 426:    */   {
/* 427:468 */     if (peekToken(TokenKind.BEAN_REF))
/* 428:    */     {
/* 429:469 */       Token beanRefToken = nextToken();
/* 430:470 */       Token beanNameToken = null;
/* 431:471 */       String beanname = null;
/* 432:472 */       if (peekToken(TokenKind.IDENTIFIER))
/* 433:    */       {
/* 434:473 */         beanNameToken = eatToken(TokenKind.IDENTIFIER);
/* 435:474 */         beanname = beanNameToken.data;
/* 436:    */       }
/* 437:475 */       else if (peekToken(TokenKind.LITERAL_STRING))
/* 438:    */       {
/* 439:476 */         beanNameToken = eatToken(TokenKind.LITERAL_STRING);
/* 440:477 */         beanname = beanNameToken.stringValue();
/* 441:478 */         beanname = beanname.substring(1, beanname.length() - 1);
/* 442:    */       }
/* 443:    */       else
/* 444:    */       {
/* 445:480 */         raiseInternalException(beanRefToken.startpos, SpelMessage.INVALID_BEAN_REFERENCE, new Object[0]);
/* 446:    */       }
/* 447:483 */       BeanReference beanReference = new BeanReference(toPos(beanNameToken), beanname);
/* 448:484 */       this.constructedNodes.push(beanReference);
/* 449:485 */       return true;
/* 450:    */     }
/* 451:487 */     return false;
/* 452:    */   }
/* 453:    */   
/* 454:    */   private boolean maybeEatTypeReference()
/* 455:    */   {
/* 456:491 */     if (peekToken(TokenKind.IDENTIFIER))
/* 457:    */     {
/* 458:492 */       Token typeName = peekToken();
/* 459:493 */       if (!typeName.stringValue().equals("T")) {
/* 460:494 */         return false;
/* 461:    */       }
/* 462:496 */       nextToken();
/* 463:497 */       eatToken(TokenKind.LPAREN);
/* 464:498 */       SpelNodeImpl node = eatPossiblyQualifiedId();
/* 465:    */       
/* 466:500 */       eatToken(TokenKind.RPAREN);
/* 467:501 */       this.constructedNodes.push(new TypeReference(toPos(typeName), node));
/* 468:502 */       return true;
/* 469:    */     }
/* 470:504 */     return false;
/* 471:    */   }
/* 472:    */   
/* 473:    */   private boolean maybeEatNullReference()
/* 474:    */   {
/* 475:508 */     if (peekToken(TokenKind.IDENTIFIER))
/* 476:    */     {
/* 477:509 */       Token nullToken = peekToken();
/* 478:510 */       if (!nullToken.stringValue().equals("null")) {
/* 479:511 */         return false;
/* 480:    */       }
/* 481:513 */       nextToken();
/* 482:514 */       this.constructedNodes.push(new NullLiteral(toPos(nullToken)));
/* 483:515 */       return true;
/* 484:    */     }
/* 485:517 */     return false;
/* 486:    */   }
/* 487:    */   
/* 488:    */   private boolean maybeEatProjection(boolean nullSafeNavigation)
/* 489:    */   {
/* 490:522 */     Token t = peekToken();
/* 491:523 */     if (!peekToken(TokenKind.PROJECT, true)) {
/* 492:524 */       return false;
/* 493:    */     }
/* 494:526 */     SpelNodeImpl expr = eatExpression();
/* 495:527 */     eatToken(TokenKind.RSQUARE);
/* 496:528 */     this.constructedNodes.push(new Projection(nullSafeNavigation, toPos(t), expr));
/* 497:529 */     return true;
/* 498:    */   }
/* 499:    */   
/* 500:    */   private boolean maybeEatInlineList()
/* 501:    */   {
/* 502:534 */     Token t = peekToken();
/* 503:535 */     if (!peekToken(TokenKind.LCURLY, true)) {
/* 504:536 */       return false;
/* 505:    */     }
/* 506:538 */     SpelNodeImpl expr = null;
/* 507:539 */     Token closingCurly = peekToken();
/* 508:540 */     if (peekToken(TokenKind.RCURLY, true))
/* 509:    */     {
/* 510:542 */       expr = new InlineList(toPos(t.startpos, closingCurly.endpos), new SpelNodeImpl[0]);
/* 511:    */     }
/* 512:    */     else
/* 513:    */     {
/* 514:544 */       List<SpelNodeImpl> listElements = new ArrayList();
/* 515:    */       do
/* 516:    */       {
/* 517:546 */         listElements.add(eatExpression());
/* 518:545 */       } while (
/* 519:    */       
/* 520:547 */         peekToken(TokenKind.COMMA, true));
/* 521:548 */       closingCurly = eatToken(TokenKind.RCURLY);
/* 522:549 */       expr = new InlineList(toPos(t.startpos, closingCurly.endpos), (SpelNodeImpl[])listElements.toArray(new SpelNodeImpl[listElements.size()]));
/* 523:    */     }
/* 524:551 */     this.constructedNodes.push(expr);
/* 525:552 */     return true;
/* 526:    */   }
/* 527:    */   
/* 528:    */   private boolean maybeEatIndexer()
/* 529:    */   {
/* 530:556 */     Token t = peekToken();
/* 531:557 */     if (!peekToken(TokenKind.LSQUARE, true)) {
/* 532:558 */       return false;
/* 533:    */     }
/* 534:560 */     SpelNodeImpl expr = eatExpression();
/* 535:561 */     eatToken(TokenKind.RSQUARE);
/* 536:562 */     this.constructedNodes.push(new Indexer(toPos(t), expr));
/* 537:563 */     return true;
/* 538:    */   }
/* 539:    */   
/* 540:    */   private boolean maybeEatSelection(boolean nullSafeNavigation)
/* 541:    */   {
/* 542:567 */     Token t = peekToken();
/* 543:568 */     if (!peekSelectToken()) {
/* 544:569 */       return false;
/* 545:    */     }
/* 546:571 */     nextToken();
/* 547:572 */     SpelNodeImpl expr = eatExpression();
/* 548:573 */     eatToken(TokenKind.RSQUARE);
/* 549:574 */     if (t.kind == TokenKind.SELECT_FIRST) {
/* 550:575 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 1, toPos(t), expr));
/* 551:576 */     } else if (t.kind == TokenKind.SELECT_LAST) {
/* 552:577 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 2, toPos(t), expr));
/* 553:    */     } else {
/* 554:579 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 0, toPos(t), expr));
/* 555:    */     }
/* 556:581 */     return true;
/* 557:    */   }
/* 558:    */   
/* 559:    */   private SpelNodeImpl eatPossiblyQualifiedId()
/* 560:    */   {
/* 561:589 */     List<SpelNodeImpl> qualifiedIdPieces = new ArrayList();
/* 562:590 */     Token startnode = eatToken(TokenKind.IDENTIFIER);
/* 563:591 */     qualifiedIdPieces.add(new Identifier(startnode.stringValue(), toPos(startnode)));
/* 564:592 */     while (peekToken(TokenKind.DOT, true))
/* 565:    */     {
/* 566:593 */       Token node = eatToken(TokenKind.IDENTIFIER);
/* 567:594 */       qualifiedIdPieces.add(new Identifier(node.stringValue(), toPos(node)));
/* 568:    */     }
/* 569:596 */     return new QualifiedIdentifier(toPos(startnode.startpos, ((SpelNodeImpl)qualifiedIdPieces.get(qualifiedIdPieces.size() - 1)).getEndPosition()), (SpelNodeImpl[])qualifiedIdPieces.toArray(new SpelNodeImpl[qualifiedIdPieces.size()]));
/* 570:    */   }
/* 571:    */   
/* 572:    */   private boolean maybeEatMethodOrProperty(boolean nullSafeNavigation)
/* 573:    */   {
/* 574:602 */     if (peekToken(TokenKind.IDENTIFIER))
/* 575:    */     {
/* 576:603 */       Token methodOrPropertyName = nextToken();
/* 577:604 */       SpelNodeImpl[] args = maybeEatMethodArgs();
/* 578:605 */       if (args == null)
/* 579:    */       {
/* 580:607 */         push(new PropertyOrFieldReference(nullSafeNavigation, methodOrPropertyName.data, toPos(methodOrPropertyName)));
/* 581:608 */         return true;
/* 582:    */       }
/* 583:611 */       push(new MethodReference(nullSafeNavigation, methodOrPropertyName.data, toPos(methodOrPropertyName), args));
/* 584:    */       
/* 585:613 */       return true;
/* 586:    */     }
/* 587:616 */     return false;
/* 588:    */   }
/* 589:    */   
/* 590:    */   private boolean maybeEatConstructorReference()
/* 591:    */   {
/* 592:623 */     if (peekIdentifierToken("new"))
/* 593:    */     {
/* 594:624 */       Token newToken = nextToken();
/* 595:625 */       SpelNodeImpl possiblyQualifiedConstructorName = eatPossiblyQualifiedId();
/* 596:626 */       List<SpelNodeImpl> nodes = new ArrayList();
/* 597:627 */       nodes.add(possiblyQualifiedConstructorName);
/* 598:628 */       if (peekToken(TokenKind.LSQUARE))
/* 599:    */       {
/* 600:630 */         List<SpelNodeImpl> dimensions = new ArrayList();
/* 601:631 */         while (peekToken(TokenKind.LSQUARE, true))
/* 602:    */         {
/* 603:632 */           if (!peekToken(TokenKind.RSQUARE)) {
/* 604:633 */             dimensions.add(eatExpression());
/* 605:    */           } else {
/* 606:635 */             dimensions.add(null);
/* 607:    */           }
/* 608:637 */           eatToken(TokenKind.RSQUARE);
/* 609:    */         }
/* 610:639 */         if (maybeEatInlineList()) {
/* 611:640 */           nodes.add(pop());
/* 612:    */         }
/* 613:642 */         push(new ConstructorReference(toPos(newToken), (SpelNodeImpl[])dimensions.toArray(new SpelNodeImpl[dimensions.size()]), 
/* 614:643 */           (SpelNodeImpl[])nodes.toArray(new SpelNodeImpl[nodes.size()])));
/* 615:    */       }
/* 616:    */       else
/* 617:    */       {
/* 618:646 */         eatConstructorArgs(nodes);
/* 619:    */         
/* 620:648 */         push(new ConstructorReference(toPos(newToken), (SpelNodeImpl[])nodes.toArray(new SpelNodeImpl[nodes.size()])));
/* 621:    */       }
/* 622:650 */       return true;
/* 623:    */     }
/* 624:652 */     return false;
/* 625:    */   }
/* 626:    */   
/* 627:    */   private void push(SpelNodeImpl newNode)
/* 628:    */   {
/* 629:656 */     this.constructedNodes.push(newNode);
/* 630:    */   }
/* 631:    */   
/* 632:    */   private SpelNodeImpl pop()
/* 633:    */   {
/* 634:660 */     return (SpelNodeImpl)this.constructedNodes.pop();
/* 635:    */   }
/* 636:    */   
/* 637:    */   private boolean maybeEatLiteral()
/* 638:    */   {
/* 639:672 */     Token t = peekToken();
/* 640:673 */     if (t == null) {
/* 641:674 */       return false;
/* 642:    */     }
/* 643:676 */     if (t.kind == TokenKind.LITERAL_INT) {
/* 644:677 */       push(Literal.getIntLiteral(t.data, toPos(t), 10));
/* 645:678 */     } else if (t.kind == TokenKind.LITERAL_LONG) {
/* 646:679 */       push(Literal.getLongLiteral(t.data, toPos(t), 10));
/* 647:680 */     } else if (t.kind == TokenKind.LITERAL_HEXINT) {
/* 648:681 */       push(Literal.getIntLiteral(t.data, toPos(t), 16));
/* 649:682 */     } else if (t.kind == TokenKind.LITERAL_HEXLONG) {
/* 650:683 */       push(Literal.getLongLiteral(t.data, toPos(t), 16));
/* 651:684 */     } else if (t.kind == TokenKind.LITERAL_REAL) {
/* 652:685 */       push(Literal.getRealLiteral(t.data, toPos(t), false));
/* 653:686 */     } else if (t.kind == TokenKind.LITERAL_REAL_FLOAT) {
/* 654:687 */       push(Literal.getRealLiteral(t.data, toPos(t), true));
/* 655:688 */     } else if (peekIdentifierToken("true")) {
/* 656:689 */       push(new BooleanLiteral(t.data, toPos(t), true));
/* 657:690 */     } else if (peekIdentifierToken("false")) {
/* 658:691 */       push(new BooleanLiteral(t.data, toPos(t), false));
/* 659:692 */     } else if (t.kind == TokenKind.LITERAL_STRING) {
/* 660:693 */       push(new StringLiteral(t.data, toPos(t), t.data));
/* 661:    */     } else {
/* 662:695 */       return false;
/* 663:    */     }
/* 664:697 */     nextToken();
/* 665:698 */     return true;
/* 666:    */   }
/* 667:    */   
/* 668:    */   private boolean maybeEatParenExpression()
/* 669:    */   {
/* 670:703 */     if (peekToken(TokenKind.LPAREN))
/* 671:    */     {
/* 672:704 */       nextToken();
/* 673:705 */       SpelNodeImpl expr = eatExpression();
/* 674:706 */       eatToken(TokenKind.RPAREN);
/* 675:707 */       push(expr);
/* 676:708 */       return true;
/* 677:    */     }
/* 678:710 */     return false;
/* 679:    */   }
/* 680:    */   
/* 681:    */   private Token maybeEatRelationalOperator()
/* 682:    */   {
/* 683:718 */     Token t = peekToken();
/* 684:719 */     if (t == null) {
/* 685:720 */       return null;
/* 686:    */     }
/* 687:722 */     if (t.isNumericRelationalOperator()) {
/* 688:723 */       return t;
/* 689:    */     }
/* 690:725 */     if (t.isIdentifier())
/* 691:    */     {
/* 692:726 */       String idString = t.stringValue();
/* 693:727 */       if (idString.equalsIgnoreCase("instanceof")) {
/* 694:728 */         return t.asInstanceOfToken();
/* 695:    */       }
/* 696:729 */       if (idString.equalsIgnoreCase("matches")) {
/* 697:730 */         return t.asMatchesToken();
/* 698:    */       }
/* 699:731 */       if (idString.equalsIgnoreCase("between")) {
/* 700:732 */         return t.asBetweenToken();
/* 701:    */       }
/* 702:    */     }
/* 703:735 */     return null;
/* 704:    */   }
/* 705:    */   
/* 706:    */   private Token eatToken(TokenKind expectedKind)
/* 707:    */   {
/* 708:739 */     Token t = nextToken();
/* 709:740 */     if (t == null) {
/* 710:741 */       raiseInternalException(this.expressionString.length(), SpelMessage.OOD, new Object[0]);
/* 711:    */     }
/* 712:743 */     if (t.kind != expectedKind) {
/* 713:744 */       raiseInternalException(t.startpos, SpelMessage.NOT_EXPECTED_TOKEN, new Object[] { expectedKind.toString().toLowerCase(), t.getKind().toString().toLowerCase() });
/* 714:    */     }
/* 715:746 */     return t;
/* 716:    */   }
/* 717:    */   
/* 718:    */   private boolean peekToken(TokenKind desiredTokenKind)
/* 719:    */   {
/* 720:750 */     return peekToken(desiredTokenKind, false);
/* 721:    */   }
/* 722:    */   
/* 723:    */   private boolean peekToken(TokenKind desiredTokenKind, boolean consumeIfMatched)
/* 724:    */   {
/* 725:754 */     if (!moreTokens()) {
/* 726:755 */       return false;
/* 727:    */     }
/* 728:757 */     Token t = peekToken();
/* 729:758 */     if (t.kind == desiredTokenKind)
/* 730:    */     {
/* 731:759 */       if (consumeIfMatched) {
/* 732:760 */         this.tokenStreamPointer += 1;
/* 733:    */       }
/* 734:762 */       return true;
/* 735:    */     }
/* 736:764 */     if (desiredTokenKind == TokenKind.IDENTIFIER) {
/* 737:767 */       if ((t.kind.ordinal() >= TokenKind.DIV.ordinal()) && (t.kind.ordinal() <= TokenKind.NOT.ordinal()) && (t.data != null)) {
/* 738:769 */         return true;
/* 739:    */       }
/* 740:    */     }
/* 741:772 */     return false;
/* 742:    */   }
/* 743:    */   
/* 744:    */   private boolean peekToken(TokenKind possible1, TokenKind possible2)
/* 745:    */   {
/* 746:777 */     if (!moreTokens()) {
/* 747:777 */       return false;
/* 748:    */     }
/* 749:778 */     Token t = peekToken();
/* 750:779 */     return (t.kind == possible1) || (t.kind == possible2);
/* 751:    */   }
/* 752:    */   
/* 753:    */   private boolean peekToken(TokenKind possible1, TokenKind possible2, TokenKind possible3)
/* 754:    */   {
/* 755:783 */     if (!moreTokens()) {
/* 756:783 */       return false;
/* 757:    */     }
/* 758:784 */     Token t = peekToken();
/* 759:785 */     return (t.kind == possible1) || (t.kind == possible2) || (t.kind == possible3);
/* 760:    */   }
/* 761:    */   
/* 762:    */   private boolean peekIdentifierToken(String identifierString)
/* 763:    */   {
/* 764:789 */     if (!moreTokens()) {
/* 765:790 */       return false;
/* 766:    */     }
/* 767:792 */     Token t = peekToken();
/* 768:793 */     return (t.kind == TokenKind.IDENTIFIER) && (t.stringValue().equalsIgnoreCase(identifierString));
/* 769:    */   }
/* 770:    */   
/* 771:    */   private boolean peekSelectToken()
/* 772:    */   {
/* 773:797 */     if (!moreTokens()) {
/* 774:797 */       return false;
/* 775:    */     }
/* 776:798 */     Token t = peekToken();
/* 777:799 */     return (t.kind == TokenKind.SELECT) || (t.kind == TokenKind.SELECT_FIRST) || (t.kind == TokenKind.SELECT_LAST);
/* 778:    */   }
/* 779:    */   
/* 780:    */   private boolean moreTokens()
/* 781:    */   {
/* 782:804 */     return this.tokenStreamPointer < this.tokenStream.size();
/* 783:    */   }
/* 784:    */   
/* 785:    */   private Token nextToken()
/* 786:    */   {
/* 787:808 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/* 788:809 */       return null;
/* 789:    */     }
/* 790:811 */     return (Token)this.tokenStream.get(this.tokenStreamPointer++);
/* 791:    */   }
/* 792:    */   
/* 793:    */   private Token peekToken()
/* 794:    */   {
/* 795:815 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/* 796:816 */       return null;
/* 797:    */     }
/* 798:818 */     return (Token)this.tokenStream.get(this.tokenStreamPointer);
/* 799:    */   }
/* 800:    */   
/* 801:    */   private void raiseInternalException(int pos, SpelMessage message, Object... inserts)
/* 802:    */   {
/* 803:822 */     throw new InternalParseException(new SpelParseException(this.expressionString, pos, message, inserts));
/* 804:    */   }
/* 805:    */   
/* 806:    */   public String toString(Token t)
/* 807:    */   {
/* 808:826 */     if (t.getKind().hasPayload()) {
/* 809:827 */       return t.stringValue();
/* 810:    */     }
/* 811:829 */     return t.kind.toString().toLowerCase();
/* 812:    */   }
/* 813:    */   
/* 814:    */   private void checkRightOperand(Token token, SpelNodeImpl operandExpression)
/* 815:    */   {
/* 816:834 */     if (operandExpression == null) {
/* 817:835 */       raiseInternalException(token.startpos, SpelMessage.RIGHT_OPERAND_PROBLEM, new Object[0]);
/* 818:    */     }
/* 819:    */   }
/* 820:    */   
/* 821:    */   private int toPos(Token t)
/* 822:    */   {
/* 823:843 */     return (t.startpos << 16) + t.endpos;
/* 824:    */   }
/* 825:    */   
/* 826:    */   private int toPos(int start, int end)
/* 827:    */   {
/* 828:847 */     return (start << 16) + end;
/* 829:    */   }
/* 830:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.standard.InternalSpelExpressionParser
 * JD-Core Version:    0.7.0.1
 */