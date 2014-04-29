/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.StringReader;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Stack;
/*   8:    */ import jxl.WorkbookSettings;
/*   9:    */ import jxl.biff.WorkbookMethods;
/*  10:    */ import jxl.common.Logger;
/*  11:    */ 
/*  12:    */ class StringFormulaParser
/*  13:    */   implements Parser
/*  14:    */ {
/*  15: 41 */   private static Logger logger = Logger.getLogger(StringFormulaParser.class);
/*  16:    */   private String formula;
/*  17:    */   private String parsedFormula;
/*  18:    */   private ParseItem root;
/*  19:    */   private Stack arguments;
/*  20:    */   private WorkbookSettings settings;
/*  21:    */   private ExternalSheet externalSheet;
/*  22:    */   private WorkbookMethods nameTable;
/*  23:    */   private ParseContext parseContext;
/*  24:    */   
/*  25:    */   public StringFormulaParser(String f, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws, ParseContext pc)
/*  26:    */   {
/*  27: 95 */     this.formula = f;
/*  28: 96 */     this.settings = ws;
/*  29: 97 */     this.externalSheet = es;
/*  30: 98 */     this.nameTable = nt;
/*  31: 99 */     this.parseContext = pc;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void parse()
/*  35:    */     throws FormulaException
/*  36:    */   {
/*  37:109 */     ArrayList tokens = getTokens();
/*  38:    */     
/*  39:111 */     Iterator i = tokens.iterator();
/*  40:    */     
/*  41:113 */     this.root = parseCurrent(i);
/*  42:    */   }
/*  43:    */   
/*  44:    */   private ParseItem parseCurrent(Iterator i)
/*  45:    */     throws FormulaException
/*  46:    */   {
/*  47:126 */     Stack stack = new Stack();
/*  48:127 */     Stack operators = new Stack();
/*  49:128 */     Stack args = null;
/*  50:    */     
/*  51:130 */     boolean parenthesesClosed = false;
/*  52:131 */     ParseItem lastParseItem = null;
/*  53:    */     do
/*  54:    */     {
/*  55:135 */       ParseItem pi = (ParseItem)i.next();
/*  56:136 */       pi.setParseContext(this.parseContext);
/*  57:138 */       if ((pi instanceof Operand))
/*  58:    */       {
/*  59:140 */         handleOperand((Operand)pi, stack);
/*  60:    */       }
/*  61:142 */       else if ((pi instanceof StringFunction))
/*  62:    */       {
/*  63:144 */         handleFunction((StringFunction)pi, i, stack);
/*  64:    */       }
/*  65:146 */       else if ((pi instanceof Operator))
/*  66:    */       {
/*  67:148 */         Operator op = (Operator)pi;
/*  68:153 */         if ((op instanceof StringOperator))
/*  69:    */         {
/*  70:155 */           StringOperator sop = (StringOperator)op;
/*  71:156 */           if ((stack.isEmpty()) || ((lastParseItem instanceof Operator))) {
/*  72:158 */             op = sop.getUnaryOperator();
/*  73:    */           } else {
/*  74:162 */             op = sop.getBinaryOperator();
/*  75:    */           }
/*  76:    */         }
/*  77:166 */         if (operators.empty())
/*  78:    */         {
/*  79:169 */           operators.push(op);
/*  80:    */         }
/*  81:    */         else
/*  82:    */         {
/*  83:173 */           Operator operator = (Operator)operators.peek();
/*  84:177 */           if (op.getPrecedence() < operator.getPrecedence())
/*  85:    */           {
/*  86:179 */             operators.push(op);
/*  87:    */           }
/*  88:181 */           else if ((op.getPrecedence() == operator.getPrecedence()) && 
/*  89:182 */             ((op instanceof UnaryOperator)))
/*  90:    */           {
/*  91:187 */             operators.push(op);
/*  92:    */           }
/*  93:    */           else
/*  94:    */           {
/*  95:193 */             operators.pop();
/*  96:194 */             operator.getOperands(stack);
/*  97:195 */             stack.push(operator);
/*  98:196 */             operators.push(op);
/*  99:    */           }
/* 100:    */         }
/* 101:    */       }
/* 102:200 */       else if ((pi instanceof ArgumentSeparator))
/* 103:    */       {
/* 104:203 */         while (!operators.isEmpty())
/* 105:    */         {
/* 106:205 */           Operator o = (Operator)operators.pop();
/* 107:206 */           o.getOperands(stack);
/* 108:207 */           stack.push(o);
/* 109:    */         }
/* 110:213 */         if (args == null) {
/* 111:215 */           args = new Stack();
/* 112:    */         }
/* 113:218 */         args.push(stack.pop());
/* 114:219 */         stack.clear();
/* 115:    */       }
/* 116:221 */       else if ((pi instanceof OpenParentheses))
/* 117:    */       {
/* 118:223 */         ParseItem pi2 = parseCurrent(i);
/* 119:224 */         Parenthesis p = new Parenthesis();
/* 120:225 */         pi2.setParent(p);
/* 121:226 */         p.add(pi2);
/* 122:227 */         stack.push(p);
/* 123:    */       }
/* 124:229 */       else if ((pi instanceof CloseParentheses))
/* 125:    */       {
/* 126:231 */         parenthesesClosed = true;
/* 127:    */       }
/* 128:234 */       lastParseItem = pi;
/* 129:133 */       if (!i.hasNext()) {
/* 130:    */         break;
/* 131:    */       }
/* 132:133 */     } while (!parenthesesClosed);
/* 133:237 */     while (!operators.isEmpty())
/* 134:    */     {
/* 135:239 */       Operator o = (Operator)operators.pop();
/* 136:240 */       o.getOperands(stack);
/* 137:241 */       stack.push(o);
/* 138:    */     }
/* 139:244 */     ParseItem rt = !stack.empty() ? (ParseItem)stack.pop() : null;
/* 140:248 */     if ((args != null) && (rt != null)) {
/* 141:250 */       args.push(rt);
/* 142:    */     }
/* 143:253 */     this.arguments = args;
/* 144:255 */     if ((!stack.empty()) || (!operators.empty())) {
/* 145:257 */       logger.warn("Formula " + this.formula + 
/* 146:258 */         " has a non-empty parse stack");
/* 147:    */     }
/* 148:261 */     return rt;
/* 149:    */   }
/* 150:    */   
/* 151:    */   private ArrayList getTokens()
/* 152:    */     throws FormulaException
/* 153:    */   {
/* 154:272 */     ArrayList tokens = new ArrayList();
/* 155:    */     
/* 156:274 */     StringReader sr = new StringReader(this.formula);
/* 157:275 */     Yylex lex = new Yylex(sr);
/* 158:276 */     lex.setExternalSheet(this.externalSheet);
/* 159:277 */     lex.setNameTable(this.nameTable);
/* 160:    */     try
/* 161:    */     {
/* 162:280 */       ParseItem pi = lex.yylex();
/* 163:281 */       while (pi != null)
/* 164:    */       {
/* 165:283 */         tokens.add(pi);
/* 166:284 */         pi = lex.yylex();
/* 167:    */       }
/* 168:    */     }
/* 169:    */     catch (IOException e)
/* 170:    */     {
/* 171:289 */       logger.warn(e.toString());
/* 172:    */     }
/* 173:    */     catch (Error e)
/* 174:    */     {
/* 175:293 */       throw new FormulaException(FormulaException.LEXICAL_ERROR, 
/* 176:294 */         this.formula + " at char  " + lex.getPos());
/* 177:    */     }
/* 178:297 */     return tokens;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public String getFormula()
/* 182:    */   {
/* 183:306 */     if (this.parsedFormula == null)
/* 184:    */     {
/* 185:308 */       StringBuffer sb = new StringBuffer();
/* 186:309 */       this.root.getString(sb);
/* 187:310 */       this.parsedFormula = sb.toString();
/* 188:    */     }
/* 189:313 */     return this.parsedFormula;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public byte[] getBytes()
/* 193:    */   {
/* 194:323 */     byte[] bytes = this.root.getBytes();
/* 195:325 */     if (this.root.isVolatile())
/* 196:    */     {
/* 197:327 */       byte[] newBytes = new byte[bytes.length + 4];
/* 198:328 */       System.arraycopy(bytes, 0, newBytes, 4, bytes.length);
/* 199:329 */       newBytes[0] = Token.ATTRIBUTE.getCode();
/* 200:330 */       newBytes[1] = 1;
/* 201:331 */       bytes = newBytes;
/* 202:    */     }
/* 203:334 */     return bytes;
/* 204:    */   }
/* 205:    */   
/* 206:    */   private void handleFunction(StringFunction sf, Iterator i, Stack stack)
/* 207:    */     throws FormulaException
/* 208:    */   {
/* 209:349 */     ParseItem pi2 = parseCurrent(i);
/* 210:352 */     if (sf.getFunction(this.settings) == Function.UNKNOWN) {
/* 211:354 */       throw new FormulaException(FormulaException.UNRECOGNIZED_FUNCTION);
/* 212:    */     }
/* 213:359 */     if ((sf.getFunction(this.settings) == Function.SUM) && (this.arguments == null))
/* 214:    */     {
/* 215:362 */       Attribute a = new Attribute(sf, this.settings);
/* 216:363 */       a.add(pi2);
/* 217:364 */       stack.push(a);
/* 218:365 */       return;
/* 219:    */     }
/* 220:368 */     if (sf.getFunction(this.settings) == Function.IF)
/* 221:    */     {
/* 222:371 */       Attribute a = new Attribute(sf, this.settings);
/* 223:    */       
/* 224:    */ 
/* 225:    */ 
/* 226:375 */       VariableArgFunction vaf = new VariableArgFunction(this.settings);
/* 227:376 */       int numargs = this.arguments.size();
/* 228:377 */       for (int j = 0; j < numargs; j++)
/* 229:    */       {
/* 230:379 */         ParseItem pi3 = (ParseItem)this.arguments.get(j);
/* 231:380 */         vaf.add(pi3);
/* 232:    */       }
/* 233:383 */       a.setIfConditions(vaf);
/* 234:384 */       stack.push(a);
/* 235:385 */       return;
/* 236:    */     }
/* 237:390 */     if (sf.getFunction(this.settings).getNumArgs() == 255)
/* 238:    */     {
/* 239:395 */       if (this.arguments == null)
/* 240:    */       {
/* 241:397 */         int numArgs = pi2 != null ? 1 : 0;
/* 242:398 */         VariableArgFunction vaf = new VariableArgFunction(
/* 243:399 */           sf.getFunction(this.settings), numArgs, this.settings);
/* 244:401 */         if (pi2 != null) {
/* 245:403 */           vaf.add(pi2);
/* 246:    */         }
/* 247:406 */         stack.push(vaf);
/* 248:    */       }
/* 249:    */       else
/* 250:    */       {
/* 251:411 */         int numargs = this.arguments.size();
/* 252:412 */         VariableArgFunction vaf = new VariableArgFunction(
/* 253:413 */           sf.getFunction(this.settings), numargs, this.settings);
/* 254:    */         
/* 255:415 */         ParseItem[] args = new ParseItem[numargs];
/* 256:416 */         for (int j = 0; j < numargs; j++)
/* 257:    */         {
/* 258:418 */           ParseItem pi3 = (ParseItem)this.arguments.pop();
/* 259:419 */           args[(numargs - j - 1)] = pi3;
/* 260:    */         }
/* 261:422 */         for (int j = 0; j < args.length; j++) {
/* 262:424 */           vaf.add(args[j]);
/* 263:    */         }
/* 264:426 */         stack.push(vaf);
/* 265:427 */         this.arguments.clear();
/* 266:428 */         this.arguments = null;
/* 267:    */       }
/* 268:430 */       return;
/* 269:    */     }
/* 270:434 */     BuiltInFunction bif = new BuiltInFunction(sf.getFunction(this.settings), 
/* 271:435 */       this.settings);
/* 272:    */     
/* 273:437 */     int numargs = sf.getFunction(this.settings).getNumArgs();
/* 274:438 */     if (numargs == 1)
/* 275:    */     {
/* 276:441 */       bif.add(pi2);
/* 277:    */     }
/* 278:    */     else
/* 279:    */     {
/* 280:445 */       if (((this.arguments == null) && (numargs != 0)) || (
/* 281:446 */         (this.arguments != null) && (numargs != this.arguments.size()))) {
/* 282:448 */         throw new FormulaException(FormulaException.INCORRECT_ARGUMENTS);
/* 283:    */       }
/* 284:453 */       for (int j = 0; j < numargs; j++)
/* 285:    */       {
/* 286:455 */         ParseItem pi3 = (ParseItem)this.arguments.get(j);
/* 287:456 */         bif.add(pi3);
/* 288:    */       }
/* 289:    */     }
/* 290:459 */     stack.push(bif);
/* 291:    */   }
/* 292:    */   
/* 293:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/* 294:    */   {
/* 295:470 */     this.root.adjustRelativeCellReferences(colAdjust, rowAdjust);
/* 296:    */   }
/* 297:    */   
/* 298:    */   public void columnInserted(int sheetIndex, int col, boolean currentSheet)
/* 299:    */   {
/* 300:485 */     this.root.columnInserted(sheetIndex, col, currentSheet);
/* 301:    */   }
/* 302:    */   
/* 303:    */   public void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/* 304:    */   {
/* 305:501 */     this.root.columnRemoved(sheetIndex, col, currentSheet);
/* 306:    */   }
/* 307:    */   
/* 308:    */   public void rowInserted(int sheetIndex, int row, boolean currentSheet)
/* 309:    */   {
/* 310:516 */     this.root.rowInserted(sheetIndex, row, currentSheet);
/* 311:    */   }
/* 312:    */   
/* 313:    */   public void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/* 314:    */   {
/* 315:531 */     this.root.rowRemoved(sheetIndex, row, currentSheet);
/* 316:    */   }
/* 317:    */   
/* 318:    */   private void handleOperand(Operand o, Stack stack)
/* 319:    */   {
/* 320:542 */     if (!(o instanceof IntegerValue))
/* 321:    */     {
/* 322:544 */       stack.push(o);
/* 323:545 */       return;
/* 324:    */     }
/* 325:548 */     if ((o instanceof IntegerValue))
/* 326:    */     {
/* 327:550 */       IntegerValue iv = (IntegerValue)o;
/* 328:551 */       if (!iv.isOutOfRange())
/* 329:    */       {
/* 330:553 */         stack.push(iv);
/* 331:    */       }
/* 332:    */       else
/* 333:    */       {
/* 334:558 */         DoubleValue dv = new DoubleValue(iv.getValue());
/* 335:559 */         stack.push(dv);
/* 336:    */       }
/* 337:    */     }
/* 338:    */   }
/* 339:    */   
/* 340:    */   public boolean handleImportedCellReferences()
/* 341:    */   {
/* 342:572 */     this.root.handleImportedCellReferences();
/* 343:573 */     return this.root.isValid();
/* 344:    */   }
/* 345:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.StringFormulaParser
 * JD-Core Version:    0.7.0.1
 */