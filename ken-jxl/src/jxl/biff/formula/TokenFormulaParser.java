/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ import jxl.Cell;
/*   5:    */ import jxl.WorkbookSettings;
/*   6:    */ import jxl.biff.WorkbookMethods;
/*   7:    */ import jxl.common.Assert;
/*   8:    */ import jxl.common.Logger;
/*   9:    */ 
/*  10:    */ class TokenFormulaParser
/*  11:    */   implements Parser
/*  12:    */ {
/*  13: 39 */   private static Logger logger = Logger.getLogger(TokenFormulaParser.class);
/*  14:    */   private byte[] tokenData;
/*  15:    */   private Cell relativeTo;
/*  16:    */   private int pos;
/*  17:    */   private ParseItem root;
/*  18:    */   private Stack tokenStack;
/*  19:    */   private ExternalSheet workbook;
/*  20:    */   private WorkbookMethods nameTable;
/*  21:    */   private WorkbookSettings settings;
/*  22:    */   private ParseContext parseContext;
/*  23:    */   
/*  24:    */   public TokenFormulaParser(byte[] data, Cell c, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws, ParseContext pc)
/*  25:    */   {
/*  26: 98 */     this.tokenData = data;
/*  27: 99 */     this.pos = 0;
/*  28:100 */     this.relativeTo = c;
/*  29:101 */     this.workbook = es;
/*  30:102 */     this.nameTable = nt;
/*  31:103 */     this.tokenStack = new Stack();
/*  32:104 */     this.settings = ws;
/*  33:105 */     this.parseContext = pc;
/*  34:    */     
/*  35:107 */     Assert.verify(this.nameTable != null);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void parse()
/*  39:    */     throws FormulaException
/*  40:    */   {
/*  41:118 */     parseSubExpression(this.tokenData.length);
/*  42:    */     
/*  43:    */ 
/*  44:    */ 
/*  45:122 */     this.root = ((ParseItem)this.tokenStack.pop());
/*  46:    */     
/*  47:124 */     Assert.verify(this.tokenStack.empty());
/*  48:    */   }
/*  49:    */   
/*  50:    */   private void parseSubExpression(int len)
/*  51:    */     throws FormulaException
/*  52:    */   {
/*  53:137 */     int tokenVal = 0;
/*  54:138 */     Token t = null;
/*  55:    */     
/*  56:    */ 
/*  57:    */ 
/*  58:142 */     Stack ifStack = new Stack();
/*  59:    */     
/*  60:    */ 
/*  61:145 */     int endpos = this.pos + len;
/*  62:147 */     while (this.pos < endpos)
/*  63:    */     {
/*  64:149 */       tokenVal = this.tokenData[this.pos];
/*  65:150 */       this.pos += 1;
/*  66:    */       
/*  67:152 */       t = Token.getToken(tokenVal);
/*  68:154 */       if (t == Token.UNKNOWN) {
/*  69:156 */         throw new FormulaException(
/*  70:157 */           FormulaException.UNRECOGNIZED_TOKEN, tokenVal);
/*  71:    */       }
/*  72:160 */       Assert.verify(t != Token.UNKNOWN);
/*  73:163 */       if (t == Token.REF)
/*  74:    */       {
/*  75:165 */         CellReference cr = new CellReference(this.relativeTo);
/*  76:166 */         this.pos += cr.read(this.tokenData, this.pos);
/*  77:167 */         this.tokenStack.push(cr);
/*  78:    */       }
/*  79:169 */       else if (t == Token.REFERR)
/*  80:    */       {
/*  81:171 */         CellReferenceError cr = new CellReferenceError();
/*  82:172 */         this.pos += cr.read(this.tokenData, this.pos);
/*  83:173 */         this.tokenStack.push(cr);
/*  84:    */       }
/*  85:175 */       else if (t == Token.ERR)
/*  86:    */       {
/*  87:177 */         ErrorConstant ec = new ErrorConstant();
/*  88:178 */         this.pos += ec.read(this.tokenData, this.pos);
/*  89:179 */         this.tokenStack.push(ec);
/*  90:    */       }
/*  91:181 */       else if (t == Token.REFV)
/*  92:    */       {
/*  93:183 */         SharedFormulaCellReference cr = 
/*  94:184 */           new SharedFormulaCellReference(this.relativeTo);
/*  95:185 */         this.pos += cr.read(this.tokenData, this.pos);
/*  96:186 */         this.tokenStack.push(cr);
/*  97:    */       }
/*  98:188 */       else if (t == Token.REF3D)
/*  99:    */       {
/* 100:190 */         CellReference3d cr = new CellReference3d(this.relativeTo, this.workbook);
/* 101:191 */         this.pos += cr.read(this.tokenData, this.pos);
/* 102:192 */         this.tokenStack.push(cr);
/* 103:    */       }
/* 104:194 */       else if (t == Token.AREA)
/* 105:    */       {
/* 106:196 */         Area a = new Area();
/* 107:197 */         this.pos += a.read(this.tokenData, this.pos);
/* 108:198 */         this.tokenStack.push(a);
/* 109:    */       }
/* 110:200 */       else if (t == Token.AREAV)
/* 111:    */       {
/* 112:202 */         SharedFormulaArea a = new SharedFormulaArea(this.relativeTo);
/* 113:203 */         this.pos += a.read(this.tokenData, this.pos);
/* 114:204 */         this.tokenStack.push(a);
/* 115:    */       }
/* 116:206 */       else if (t == Token.AREA3D)
/* 117:    */       {
/* 118:208 */         Area3d a = new Area3d(this.workbook);
/* 119:209 */         this.pos += a.read(this.tokenData, this.pos);
/* 120:210 */         this.tokenStack.push(a);
/* 121:    */       }
/* 122:212 */       else if (t == Token.NAME)
/* 123:    */       {
/* 124:214 */         Name n = new Name();
/* 125:215 */         this.pos += n.read(this.tokenData, this.pos);
/* 126:216 */         n.setParseContext(this.parseContext);
/* 127:217 */         this.tokenStack.push(n);
/* 128:    */       }
/* 129:219 */       else if (t == Token.NAMED_RANGE)
/* 130:    */       {
/* 131:221 */         NameRange nr = new NameRange(this.nameTable);
/* 132:222 */         this.pos += nr.read(this.tokenData, this.pos);
/* 133:223 */         nr.setParseContext(this.parseContext);
/* 134:224 */         this.tokenStack.push(nr);
/* 135:    */       }
/* 136:226 */       else if (t == Token.INTEGER)
/* 137:    */       {
/* 138:228 */         IntegerValue i = new IntegerValue();
/* 139:229 */         this.pos += i.read(this.tokenData, this.pos);
/* 140:230 */         this.tokenStack.push(i);
/* 141:    */       }
/* 142:232 */       else if (t == Token.DOUBLE)
/* 143:    */       {
/* 144:234 */         DoubleValue d = new DoubleValue();
/* 145:235 */         this.pos += d.read(this.tokenData, this.pos);
/* 146:236 */         this.tokenStack.push(d);
/* 147:    */       }
/* 148:238 */       else if (t == Token.BOOL)
/* 149:    */       {
/* 150:240 */         BooleanValue bv = new BooleanValue();
/* 151:241 */         this.pos += bv.read(this.tokenData, this.pos);
/* 152:242 */         this.tokenStack.push(bv);
/* 153:    */       }
/* 154:244 */       else if (t == Token.STRING)
/* 155:    */       {
/* 156:246 */         StringValue sv = new StringValue(this.settings);
/* 157:247 */         this.pos += sv.read(this.tokenData, this.pos);
/* 158:248 */         this.tokenStack.push(sv);
/* 159:    */       }
/* 160:250 */       else if (t == Token.MISSING_ARG)
/* 161:    */       {
/* 162:252 */         MissingArg ma = new MissingArg();
/* 163:253 */         this.pos += ma.read(this.tokenData, this.pos);
/* 164:254 */         this.tokenStack.push(ma);
/* 165:    */       }
/* 166:258 */       else if (t == Token.UNARY_PLUS)
/* 167:    */       {
/* 168:260 */         UnaryPlus up = new UnaryPlus();
/* 169:261 */         this.pos += up.read(this.tokenData, this.pos);
/* 170:262 */         addOperator(up);
/* 171:    */       }
/* 172:264 */       else if (t == Token.UNARY_MINUS)
/* 173:    */       {
/* 174:266 */         UnaryMinus um = new UnaryMinus();
/* 175:267 */         this.pos += um.read(this.tokenData, this.pos);
/* 176:268 */         addOperator(um);
/* 177:    */       }
/* 178:270 */       else if (t == Token.PERCENT)
/* 179:    */       {
/* 180:272 */         Percent p = new Percent();
/* 181:273 */         this.pos += p.read(this.tokenData, this.pos);
/* 182:274 */         addOperator(p);
/* 183:    */       }
/* 184:278 */       else if (t == Token.SUBTRACT)
/* 185:    */       {
/* 186:280 */         Subtract s = new Subtract();
/* 187:281 */         this.pos += s.read(this.tokenData, this.pos);
/* 188:282 */         addOperator(s);
/* 189:    */       }
/* 190:284 */       else if (t == Token.ADD)
/* 191:    */       {
/* 192:286 */         Add s = new Add();
/* 193:287 */         this.pos += s.read(this.tokenData, this.pos);
/* 194:288 */         addOperator(s);
/* 195:    */       }
/* 196:290 */       else if (t == Token.MULTIPLY)
/* 197:    */       {
/* 198:292 */         Multiply s = new Multiply();
/* 199:293 */         this.pos += s.read(this.tokenData, this.pos);
/* 200:294 */         addOperator(s);
/* 201:    */       }
/* 202:296 */       else if (t == Token.DIVIDE)
/* 203:    */       {
/* 204:298 */         Divide s = new Divide();
/* 205:299 */         this.pos += s.read(this.tokenData, this.pos);
/* 206:300 */         addOperator(s);
/* 207:    */       }
/* 208:302 */       else if (t == Token.CONCAT)
/* 209:    */       {
/* 210:304 */         Concatenate c = new Concatenate();
/* 211:305 */         this.pos += c.read(this.tokenData, this.pos);
/* 212:306 */         addOperator(c);
/* 213:    */       }
/* 214:308 */       else if (t == Token.POWER)
/* 215:    */       {
/* 216:310 */         Power p = new Power();
/* 217:311 */         this.pos += p.read(this.tokenData, this.pos);
/* 218:312 */         addOperator(p);
/* 219:    */       }
/* 220:314 */       else if (t == Token.LESS_THAN)
/* 221:    */       {
/* 222:316 */         LessThan lt = new LessThan();
/* 223:317 */         this.pos += lt.read(this.tokenData, this.pos);
/* 224:318 */         addOperator(lt);
/* 225:    */       }
/* 226:320 */       else if (t == Token.LESS_EQUAL)
/* 227:    */       {
/* 228:322 */         LessEqual lte = new LessEqual();
/* 229:323 */         this.pos += lte.read(this.tokenData, this.pos);
/* 230:324 */         addOperator(lte);
/* 231:    */       }
/* 232:326 */       else if (t == Token.GREATER_THAN)
/* 233:    */       {
/* 234:328 */         GreaterThan gt = new GreaterThan();
/* 235:329 */         this.pos += gt.read(this.tokenData, this.pos);
/* 236:330 */         addOperator(gt);
/* 237:    */       }
/* 238:332 */       else if (t == Token.GREATER_EQUAL)
/* 239:    */       {
/* 240:334 */         GreaterEqual gte = new GreaterEqual();
/* 241:335 */         this.pos += gte.read(this.tokenData, this.pos);
/* 242:336 */         addOperator(gte);
/* 243:    */       }
/* 244:338 */       else if (t == Token.NOT_EQUAL)
/* 245:    */       {
/* 246:340 */         NotEqual ne = new NotEqual();
/* 247:341 */         this.pos += ne.read(this.tokenData, this.pos);
/* 248:342 */         addOperator(ne);
/* 249:    */       }
/* 250:344 */       else if (t == Token.EQUAL)
/* 251:    */       {
/* 252:346 */         Equal e = new Equal();
/* 253:347 */         this.pos += e.read(this.tokenData, this.pos);
/* 254:348 */         addOperator(e);
/* 255:    */       }
/* 256:350 */       else if (t == Token.PARENTHESIS)
/* 257:    */       {
/* 258:352 */         Parenthesis p = new Parenthesis();
/* 259:353 */         this.pos += p.read(this.tokenData, this.pos);
/* 260:354 */         addOperator(p);
/* 261:    */       }
/* 262:358 */       else if (t == Token.ATTRIBUTE)
/* 263:    */       {
/* 264:360 */         Attribute a = new Attribute(this.settings);
/* 265:361 */         this.pos += a.read(this.tokenData, this.pos);
/* 266:363 */         if (a.isSum()) {
/* 267:365 */           addOperator(a);
/* 268:367 */         } else if (a.isIf()) {
/* 269:370 */           ifStack.push(a);
/* 270:    */         }
/* 271:    */       }
/* 272:373 */       else if (t == Token.FUNCTION)
/* 273:    */       {
/* 274:375 */         BuiltInFunction bif = new BuiltInFunction(this.settings);
/* 275:376 */         this.pos += bif.read(this.tokenData, this.pos);
/* 276:    */         
/* 277:378 */         addOperator(bif);
/* 278:    */       }
/* 279:380 */       else if (t == Token.FUNCTIONVARARG)
/* 280:    */       {
/* 281:382 */         VariableArgFunction vaf = new VariableArgFunction(this.settings);
/* 282:383 */         this.pos += vaf.read(this.tokenData, this.pos);
/* 283:385 */         if (vaf.getFunction() != Function.ATTRIBUTE)
/* 284:    */         {
/* 285:387 */           addOperator(vaf);
/* 286:    */         }
/* 287:    */         else
/* 288:    */         {
/* 289:393 */           vaf.getOperands(this.tokenStack);
/* 290:    */           
/* 291:395 */           Attribute ifattr = null;
/* 292:396 */           if (ifStack.empty()) {
/* 293:398 */             ifattr = new Attribute(this.settings);
/* 294:    */           } else {
/* 295:402 */             ifattr = (Attribute)ifStack.pop();
/* 296:    */           }
/* 297:405 */           ifattr.setIfConditions(vaf);
/* 298:406 */           this.tokenStack.push(ifattr);
/* 299:    */         }
/* 300:    */       }
/* 301:411 */       else if (t == Token.MEM_FUNC)
/* 302:    */       {
/* 303:413 */         MemFunc memFunc = new MemFunc();
/* 304:414 */         handleMemoryFunction(memFunc);
/* 305:    */       }
/* 306:416 */       else if (t == Token.MEM_AREA)
/* 307:    */       {
/* 308:418 */         MemArea memArea = new MemArea();
/* 309:419 */         handleMemoryFunction(memArea);
/* 310:    */       }
/* 311:    */     }
/* 312:    */   }
/* 313:    */   
/* 314:    */   private void handleMemoryFunction(SubExpression subxp)
/* 315:    */     throws FormulaException
/* 316:    */   {
/* 317:430 */     this.pos += subxp.read(this.tokenData, this.pos);
/* 318:    */     
/* 319:    */ 
/* 320:433 */     Stack oldStack = this.tokenStack;
/* 321:434 */     this.tokenStack = new Stack();
/* 322:    */     
/* 323:436 */     parseSubExpression(subxp.getLength());
/* 324:    */     
/* 325:438 */     ParseItem[] subexpr = new ParseItem[this.tokenStack.size()];
/* 326:439 */     int i = 0;
/* 327:440 */     while (!this.tokenStack.isEmpty())
/* 328:    */     {
/* 329:442 */       subexpr[i] = ((ParseItem)this.tokenStack.pop());
/* 330:443 */       i++;
/* 331:    */     }
/* 332:446 */     subxp.setSubExpression(subexpr);
/* 333:    */     
/* 334:448 */     this.tokenStack = oldStack;
/* 335:449 */     this.tokenStack.push(subxp);
/* 336:    */   }
/* 337:    */   
/* 338:    */   private void addOperator(Operator o)
/* 339:    */   {
/* 340:459 */     o.getOperands(this.tokenStack);
/* 341:    */     
/* 342:    */ 
/* 343:462 */     this.tokenStack.push(o);
/* 344:    */   }
/* 345:    */   
/* 346:    */   public String getFormula()
/* 347:    */   {
/* 348:470 */     StringBuffer sb = new StringBuffer();
/* 349:471 */     this.root.getString(sb);
/* 350:472 */     return sb.toString();
/* 351:    */   }
/* 352:    */   
/* 353:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/* 354:    */   {
/* 355:484 */     this.root.adjustRelativeCellReferences(colAdjust, rowAdjust);
/* 356:    */   }
/* 357:    */   
/* 358:    */   public byte[] getBytes()
/* 359:    */   {
/* 360:495 */     return this.root.getBytes();
/* 361:    */   }
/* 362:    */   
/* 363:    */   public void columnInserted(int sheetIndex, int col, boolean currentSheet)
/* 364:    */   {
/* 365:510 */     this.root.columnInserted(sheetIndex, col, currentSheet);
/* 366:    */   }
/* 367:    */   
/* 368:    */   public void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/* 369:    */   {
/* 370:524 */     this.root.columnRemoved(sheetIndex, col, currentSheet);
/* 371:    */   }
/* 372:    */   
/* 373:    */   public void rowInserted(int sheetIndex, int row, boolean currentSheet)
/* 374:    */   {
/* 375:539 */     this.root.rowInserted(sheetIndex, row, currentSheet);
/* 376:    */   }
/* 377:    */   
/* 378:    */   public void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/* 379:    */   {
/* 380:554 */     this.root.rowRemoved(sheetIndex, row, currentSheet);
/* 381:    */   }
/* 382:    */   
/* 383:    */   public boolean handleImportedCellReferences()
/* 384:    */   {
/* 385:565 */     this.root.handleImportedCellReferences();
/* 386:566 */     return this.root.isValid();
/* 387:    */   }
/* 388:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.TokenFormulaParser
 * JD-Core Version:    0.7.0.1
 */