/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ class Attribute
/*   9:    */   extends Operator
/*  10:    */   implements ParsedThing
/*  11:    */ {
/*  12: 38 */   private static Logger logger = Logger.getLogger(Attribute.class);
/*  13:    */   private int options;
/*  14:    */   private int word;
/*  15:    */   private WorkbookSettings settings;
/*  16:    */   private static final int SUM_MASK = 16;
/*  17:    */   private static final int IF_MASK = 2;
/*  18:    */   private static final int CHOOSE_MASK = 4;
/*  19:    */   private static final int GOTO_MASK = 8;
/*  20:    */   private VariableArgFunction ifConditions;
/*  21:    */   
/*  22:    */   public Attribute(WorkbookSettings ws)
/*  23:    */   {
/*  24: 72 */     this.settings = ws;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Attribute(StringFunction sf, WorkbookSettings ws)
/*  28:    */   {
/*  29: 83 */     this.settings = ws;
/*  30: 85 */     if (sf.getFunction(this.settings) == Function.SUM) {
/*  31: 87 */       this.options |= 0x10;
/*  32: 89 */     } else if (sf.getFunction(this.settings) == Function.IF) {
/*  33: 91 */       this.options |= 0x2;
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   void setIfConditions(VariableArgFunction vaf)
/*  38:    */   {
/*  39:102 */     this.ifConditions = vaf;
/*  40:    */     
/*  41:    */ 
/*  42:    */ 
/*  43:106 */     this.options |= 0x2;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int read(byte[] data, int pos)
/*  47:    */   {
/*  48:118 */     this.options = data[pos];
/*  49:119 */     this.word = IntegerHelper.getInt(data[(pos + 1)], data[(pos + 2)]);
/*  50:121 */     if (!isChoose()) {
/*  51:123 */       return 3;
/*  52:    */     }
/*  53:128 */     return 3 + (this.word + 1) * 2;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean isFunction()
/*  57:    */   {
/*  58:138 */     return (this.options & 0x12) != 0;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean isSum()
/*  62:    */   {
/*  63:148 */     return (this.options & 0x10) != 0;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean isIf()
/*  67:    */   {
/*  68:158 */     return (this.options & 0x2) != 0;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public boolean isGoto()
/*  72:    */   {
/*  73:168 */     return (this.options & 0x8) != 0;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public boolean isChoose()
/*  77:    */   {
/*  78:178 */     return (this.options & 0x4) != 0;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void getOperands(Stack s)
/*  82:    */   {
/*  83:188 */     if ((this.options & 0x10) != 0)
/*  84:    */     {
/*  85:190 */       ParseItem o1 = (ParseItem)s.pop();
/*  86:191 */       add(o1);
/*  87:    */     }
/*  88:193 */     else if ((this.options & 0x2) != 0)
/*  89:    */     {
/*  90:195 */       ParseItem o1 = (ParseItem)s.pop();
/*  91:196 */       add(o1);
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void getString(StringBuffer buf)
/*  96:    */   {
/*  97:207 */     if ((this.options & 0x10) != 0)
/*  98:    */     {
/*  99:209 */       ParseItem[] operands = getOperands();
/* 100:210 */       buf.append(Function.SUM.getName(this.settings));
/* 101:211 */       buf.append('(');
/* 102:212 */       operands[0].getString(buf);
/* 103:213 */       buf.append(')');
/* 104:    */     }
/* 105:215 */     else if ((this.options & 0x2) != 0)
/* 106:    */     {
/* 107:217 */       buf.append(Function.IF.getName(this.settings));
/* 108:218 */       buf.append('(');
/* 109:    */       
/* 110:220 */       ParseItem[] operands = this.ifConditions.getOperands();
/* 111:223 */       for (int i = 0; i < operands.length - 1; i++)
/* 112:    */       {
/* 113:225 */         operands[i].getString(buf);
/* 114:226 */         buf.append(',');
/* 115:    */       }
/* 116:228 */       operands[(operands.length - 1)].getString(buf);
/* 117:229 */       buf.append(')');
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   byte[] getBytes()
/* 122:    */   {
/* 123:242 */     byte[] data = new byte[0];
/* 124:243 */     if (isSum())
/* 125:    */     {
/* 126:246 */       ParseItem[] operands = getOperands();
/* 127:249 */       for (int i = operands.length - 1; i >= 0; i--)
/* 128:    */       {
/* 129:251 */         byte[] opdata = operands[i].getBytes();
/* 130:    */         
/* 131:    */ 
/* 132:254 */         byte[] newdata = new byte[data.length + opdata.length];
/* 133:255 */         System.arraycopy(data, 0, newdata, 0, data.length);
/* 134:256 */         System.arraycopy(opdata, 0, newdata, data.length, opdata.length);
/* 135:257 */         data = newdata;
/* 136:    */       }
/* 137:261 */       byte[] newdata = new byte[data.length + 4];
/* 138:262 */       System.arraycopy(data, 0, newdata, 0, data.length);
/* 139:263 */       newdata[data.length] = Token.ATTRIBUTE.getCode();
/* 140:264 */       newdata[(data.length + 1)] = 16;
/* 141:265 */       data = newdata;
/* 142:    */     }
/* 143:267 */     else if (isIf())
/* 144:    */     {
/* 145:269 */       return getIf();
/* 146:    */     }
/* 147:272 */     return data;
/* 148:    */   }
/* 149:    */   
/* 150:    */   private byte[] getIf()
/* 151:    */   {
/* 152:282 */     ParseItem[] operands = this.ifConditions.getOperands();
/* 153:    */     
/* 154:    */ 
/* 155:285 */     int falseOffsetPos = 0;
/* 156:286 */     int gotoEndPos = 0;
/* 157:287 */     int numArgs = operands.length;
/* 158:    */     
/* 159:    */ 
/* 160:290 */     byte[] data = operands[0].getBytes();
/* 161:    */     
/* 162:    */ 
/* 163:293 */     int pos = data.length;
/* 164:294 */     byte[] newdata = new byte[data.length + 4];
/* 165:295 */     System.arraycopy(data, 0, newdata, 0, data.length);
/* 166:296 */     data = newdata;
/* 167:297 */     data[pos] = Token.ATTRIBUTE.getCode();
/* 168:298 */     data[(pos + 1)] = 2;
/* 169:299 */     falseOffsetPos = pos + 2;
/* 170:    */     
/* 171:    */ 
/* 172:302 */     byte[] truedata = operands[1].getBytes();
/* 173:303 */     newdata = new byte[data.length + truedata.length];
/* 174:304 */     System.arraycopy(data, 0, newdata, 0, data.length);
/* 175:305 */     System.arraycopy(truedata, 0, newdata, data.length, truedata.length);
/* 176:306 */     data = newdata;
/* 177:    */     
/* 178:    */ 
/* 179:309 */     pos = data.length;
/* 180:310 */     newdata = new byte[data.length + 4];
/* 181:311 */     System.arraycopy(data, 0, newdata, 0, data.length);
/* 182:312 */     data = newdata;
/* 183:313 */     data[pos] = Token.ATTRIBUTE.getCode();
/* 184:314 */     data[(pos + 1)] = 8;
/* 185:315 */     gotoEndPos = pos + 2;
/* 186:318 */     if (numArgs > 2)
/* 187:    */     {
/* 188:321 */       IntegerHelper.getTwoBytes(data.length - falseOffsetPos - 2, 
/* 189:322 */         data, falseOffsetPos);
/* 190:    */       
/* 191:    */ 
/* 192:325 */       byte[] falsedata = operands[(numArgs - 1)].getBytes();
/* 193:326 */       newdata = new byte[data.length + falsedata.length];
/* 194:327 */       System.arraycopy(data, 0, newdata, 0, data.length);
/* 195:328 */       System.arraycopy(falsedata, 0, newdata, data.length, falsedata.length);
/* 196:329 */       data = newdata;
/* 197:    */       
/* 198:    */ 
/* 199:332 */       pos = data.length;
/* 200:333 */       newdata = new byte[data.length + 4];
/* 201:334 */       System.arraycopy(data, 0, newdata, 0, data.length);
/* 202:335 */       data = newdata;
/* 203:336 */       data[pos] = Token.ATTRIBUTE.getCode();
/* 204:337 */       data[(pos + 1)] = 8;
/* 205:338 */       data[(pos + 2)] = 3;
/* 206:    */     }
/* 207:342 */     pos = data.length;
/* 208:343 */     newdata = new byte[data.length + 4];
/* 209:344 */     System.arraycopy(data, 0, newdata, 0, data.length);
/* 210:345 */     data = newdata;
/* 211:346 */     data[pos] = Token.FUNCTIONVARARG.getCode();
/* 212:347 */     data[(pos + 1)] = ((byte)numArgs);
/* 213:348 */     data[(pos + 2)] = 1;
/* 214:349 */     data[(pos + 3)] = 0;
/* 215:    */     
/* 216:    */ 
/* 217:352 */     int endPos = data.length - 1;
/* 218:354 */     if (numArgs < 3) {
/* 219:357 */       IntegerHelper.getTwoBytes(endPos - falseOffsetPos - 5, 
/* 220:358 */         data, falseOffsetPos);
/* 221:    */     }
/* 222:362 */     IntegerHelper.getTwoBytes(endPos - gotoEndPos - 2, 
/* 223:363 */       data, gotoEndPos);
/* 224:    */     
/* 225:365 */     return data;
/* 226:    */   }
/* 227:    */   
/* 228:    */   int getPrecedence()
/* 229:    */   {
/* 230:376 */     return 3;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/* 234:    */   {
/* 235:387 */     ParseItem[] operands = (ParseItem[])null;
/* 236:389 */     if (isIf()) {
/* 237:391 */       operands = this.ifConditions.getOperands();
/* 238:    */     } else {
/* 239:395 */       operands = getOperands();
/* 240:    */     }
/* 241:398 */     for (int i = 0; i < operands.length; i++) {
/* 242:400 */       operands[i].adjustRelativeCellReferences(colAdjust, rowAdjust);
/* 243:    */     }
/* 244:    */   }
/* 245:    */   
/* 246:    */   void columnInserted(int sheetIndex, int col, boolean currentSheet)
/* 247:    */   {
/* 248:416 */     ParseItem[] operands = (ParseItem[])null;
/* 249:418 */     if (isIf()) {
/* 250:420 */       operands = this.ifConditions.getOperands();
/* 251:    */     } else {
/* 252:424 */       operands = getOperands();
/* 253:    */     }
/* 254:427 */     for (int i = 0; i < operands.length; i++) {
/* 255:429 */       operands[i].columnInserted(sheetIndex, col, currentSheet);
/* 256:    */     }
/* 257:    */   }
/* 258:    */   
/* 259:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/* 260:    */   {
/* 261:445 */     ParseItem[] operands = (ParseItem[])null;
/* 262:447 */     if (isIf()) {
/* 263:449 */       operands = this.ifConditions.getOperands();
/* 264:    */     } else {
/* 265:453 */       operands = getOperands();
/* 266:    */     }
/* 267:456 */     for (int i = 0; i < operands.length; i++) {
/* 268:458 */       operands[i].columnRemoved(sheetIndex, col, currentSheet);
/* 269:    */     }
/* 270:    */   }
/* 271:    */   
/* 272:    */   void rowInserted(int sheetIndex, int row, boolean currentSheet)
/* 273:    */   {
/* 274:474 */     ParseItem[] operands = (ParseItem[])null;
/* 275:476 */     if (isIf()) {
/* 276:478 */       operands = this.ifConditions.getOperands();
/* 277:    */     } else {
/* 278:482 */       operands = getOperands();
/* 279:    */     }
/* 280:485 */     for (int i = 0; i < operands.length; i++) {
/* 281:487 */       operands[i].rowInserted(sheetIndex, row, currentSheet);
/* 282:    */     }
/* 283:    */   }
/* 284:    */   
/* 285:    */   void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/* 286:    */   {
/* 287:503 */     ParseItem[] operands = (ParseItem[])null;
/* 288:505 */     if (isIf()) {
/* 289:507 */       operands = this.ifConditions.getOperands();
/* 290:    */     } else {
/* 291:511 */       operands = getOperands();
/* 292:    */     }
/* 293:514 */     for (int i = 0; i < operands.length; i++) {
/* 294:516 */       operands[i].rowRemoved(sheetIndex, row, currentSheet);
/* 295:    */     }
/* 296:    */   }
/* 297:    */   
/* 298:    */   void handleImportedCellReferences()
/* 299:    */   {
/* 300:527 */     ParseItem[] operands = (ParseItem[])null;
/* 301:529 */     if (isIf()) {
/* 302:531 */       operands = this.ifConditions.getOperands();
/* 303:    */     } else {
/* 304:535 */       operands = getOperands();
/* 305:    */     }
/* 306:538 */     for (int i = 0; i < operands.length; i++) {
/* 307:540 */       operands[i].handleImportedCellReferences();
/* 308:    */     }
/* 309:    */   }
/* 310:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Attribute
 * JD-Core Version:    0.7.0.1
 */