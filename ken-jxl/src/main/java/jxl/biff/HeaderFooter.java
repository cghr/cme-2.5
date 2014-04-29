/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.common.Logger;
/*   4:    */ 
/*   5:    */ public abstract class HeaderFooter
/*   6:    */ {
/*   7: 38 */   private static Logger logger = Logger.getLogger(HeaderFooter.class);
/*   8:    */   private static final String BOLD_TOGGLE = "&B";
/*   9:    */   private static final String UNDERLINE_TOGGLE = "&U";
/*  10:    */   private static final String ITALICS_TOGGLE = "&I";
/*  11:    */   private static final String STRIKETHROUGH_TOGGLE = "&S";
/*  12:    */   private static final String DOUBLE_UNDERLINE_TOGGLE = "&E";
/*  13:    */   private static final String SUPERSCRIPT_TOGGLE = "&X";
/*  14:    */   private static final String SUBSCRIPT_TOGGLE = "&Y";
/*  15:    */   private static final String OUTLINE_TOGGLE = "&O";
/*  16:    */   private static final String SHADOW_TOGGLE = "&H";
/*  17:    */   private static final String LEFT_ALIGN = "&L";
/*  18:    */   private static final String CENTRE = "&C";
/*  19:    */   private static final String RIGHT_ALIGN = "&R";
/*  20:    */   private static final String PAGENUM = "&P";
/*  21:    */   private static final String TOTAL_PAGENUM = "&N";
/*  22:    */   private static final String DATE = "&D";
/*  23:    */   private static final String TIME = "&T";
/*  24:    */   private static final String WORKBOOK_NAME = "&F";
/*  25:    */   private static final String WORKSHEET_NAME = "&A";
/*  26:    */   private Contents left;
/*  27:    */   private Contents right;
/*  28:    */   private Contents centre;
/*  29:    */   
/*  30:    */   protected static class Contents
/*  31:    */   {
/*  32:    */     private StringBuffer contents;
/*  33:    */     
/*  34:    */     protected Contents()
/*  35:    */     {
/*  36:149 */       this.contents = new StringBuffer();
/*  37:    */     }
/*  38:    */     
/*  39:    */     protected Contents(String s)
/*  40:    */     {
/*  41:160 */       this.contents = new StringBuffer(s);
/*  42:    */     }
/*  43:    */     
/*  44:    */     protected Contents(Contents copy)
/*  45:    */     {
/*  46:170 */       this.contents = new StringBuffer(copy.getContents());
/*  47:    */     }
/*  48:    */     
/*  49:    */     protected String getContents()
/*  50:    */     {
/*  51:181 */       return this.contents != null ? this.contents.toString() : "";
/*  52:    */     }
/*  53:    */     
/*  54:    */     private void appendInternal(String txt)
/*  55:    */     {
/*  56:191 */       if (this.contents == null) {
/*  57:193 */         this.contents = new StringBuffer();
/*  58:    */       }
/*  59:196 */       this.contents.append(txt);
/*  60:    */     }
/*  61:    */     
/*  62:    */     private void appendInternal(char ch)
/*  63:    */     {
/*  64:206 */       if (this.contents == null) {
/*  65:208 */         this.contents = new StringBuffer();
/*  66:    */       }
/*  67:211 */       this.contents.append(ch);
/*  68:    */     }
/*  69:    */     
/*  70:    */     protected void append(String txt)
/*  71:    */     {
/*  72:221 */       appendInternal(txt);
/*  73:    */     }
/*  74:    */     
/*  75:    */     protected void toggleBold()
/*  76:    */     {
/*  77:232 */       appendInternal("&B");
/*  78:    */     }
/*  79:    */     
/*  80:    */     protected void toggleUnderline()
/*  81:    */     {
/*  82:243 */       appendInternal("&U");
/*  83:    */     }
/*  84:    */     
/*  85:    */     protected void toggleItalics()
/*  86:    */     {
/*  87:254 */       appendInternal("&I");
/*  88:    */     }
/*  89:    */     
/*  90:    */     protected void toggleStrikethrough()
/*  91:    */     {
/*  92:265 */       appendInternal("&S");
/*  93:    */     }
/*  94:    */     
/*  95:    */     protected void toggleDoubleUnderline()
/*  96:    */     {
/*  97:276 */       appendInternal("&E");
/*  98:    */     }
/*  99:    */     
/* 100:    */     protected void toggleSuperScript()
/* 101:    */     {
/* 102:287 */       appendInternal("&X");
/* 103:    */     }
/* 104:    */     
/* 105:    */     protected void toggleSubScript()
/* 106:    */     {
/* 107:298 */       appendInternal("&Y");
/* 108:    */     }
/* 109:    */     
/* 110:    */     protected void toggleOutline()
/* 111:    */     {
/* 112:309 */       appendInternal("&O");
/* 113:    */     }
/* 114:    */     
/* 115:    */     protected void toggleShadow()
/* 116:    */     {
/* 117:320 */       appendInternal("&H");
/* 118:    */     }
/* 119:    */     
/* 120:    */     protected void setFontName(String fontName)
/* 121:    */     {
/* 122:335 */       appendInternal("&\"");
/* 123:336 */       appendInternal(fontName);
/* 124:337 */       appendInternal('"');
/* 125:    */     }
/* 126:    */     
/* 127:    */     protected boolean setFontSize(int size)
/* 128:    */     {
/* 129:356 */       if ((size < 1) || (size > 99)) {
/* 130:358 */         return false;
/* 131:    */       }
/* 132:    */       String fontSize;
/* 133:    */       String fontSize;
/* 134:364 */       if (size < 10) {
/* 135:367 */         fontSize = "0" + size;
/* 136:    */       } else {
/* 137:371 */         fontSize = Integer.toString(size);
/* 138:    */       }
/* 139:374 */       appendInternal('&');
/* 140:375 */       appendInternal(fontSize);
/* 141:376 */       return true;
/* 142:    */     }
/* 143:    */     
/* 144:    */     protected void appendPageNumber()
/* 145:    */     {
/* 146:384 */       appendInternal("&P");
/* 147:    */     }
/* 148:    */     
/* 149:    */     protected void appendTotalPages()
/* 150:    */     {
/* 151:392 */       appendInternal("&N");
/* 152:    */     }
/* 153:    */     
/* 154:    */     protected void appendDate()
/* 155:    */     {
/* 156:400 */       appendInternal("&D");
/* 157:    */     }
/* 158:    */     
/* 159:    */     protected void appendTime()
/* 160:    */     {
/* 161:408 */       appendInternal("&T");
/* 162:    */     }
/* 163:    */     
/* 164:    */     protected void appendWorkbookName()
/* 165:    */     {
/* 166:416 */       appendInternal("&F");
/* 167:    */     }
/* 168:    */     
/* 169:    */     protected void appendWorkSheetName()
/* 170:    */     {
/* 171:424 */       appendInternal("&A");
/* 172:    */     }
/* 173:    */     
/* 174:    */     protected void clear()
/* 175:    */     {
/* 176:432 */       this.contents = null;
/* 177:    */     }
/* 178:    */     
/* 179:    */     protected boolean empty()
/* 180:    */     {
/* 181:442 */       if ((this.contents == null) || (this.contents.length() == 0)) {
/* 182:444 */         return true;
/* 183:    */       }
/* 184:448 */       return false;
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */   protected HeaderFooter()
/* 189:    */   {
/* 190:473 */     this.left = createContents();
/* 191:474 */     this.right = createContents();
/* 192:475 */     this.centre = createContents();
/* 193:    */   }
/* 194:    */   
/* 195:    */   protected HeaderFooter(HeaderFooter hf)
/* 196:    */   {
/* 197:485 */     this.left = createContents(hf.left);
/* 198:486 */     this.right = createContents(hf.right);
/* 199:487 */     this.centre = createContents(hf.centre);
/* 200:    */   }
/* 201:    */   
/* 202:    */   protected HeaderFooter(String s)
/* 203:    */   {
/* 204:496 */     if ((s == null) || (s.length() == 0))
/* 205:    */     {
/* 206:498 */       this.left = createContents();
/* 207:499 */       this.right = createContents();
/* 208:500 */       this.centre = createContents();
/* 209:501 */       return;
/* 210:    */     }
/* 211:504 */     int leftPos = s.indexOf("&L");
/* 212:505 */     int rightPos = s.indexOf("&R");
/* 213:506 */     int centrePos = s.indexOf("&C");
/* 214:508 */     if ((leftPos == -1) && (rightPos == -1) && (centrePos == -1))
/* 215:    */     {
/* 216:511 */       this.centre = createContents(s);
/* 217:    */     }
/* 218:    */     else
/* 219:    */     {
/* 220:516 */       if (leftPos != -1)
/* 221:    */       {
/* 222:519 */         int endLeftPos = s.length();
/* 223:520 */         if (centrePos > leftPos)
/* 224:    */         {
/* 225:523 */           endLeftPos = centrePos;
/* 226:524 */           if ((rightPos > leftPos) && (endLeftPos > rightPos)) {
/* 227:527 */             endLeftPos = rightPos;
/* 228:    */           }
/* 229:    */         }
/* 230:537 */         else if (rightPos > leftPos)
/* 231:    */         {
/* 232:540 */           endLeftPos = rightPos;
/* 233:    */         }
/* 234:550 */         this.left = createContents(s.substring(leftPos + 2, endLeftPos));
/* 235:    */       }
/* 236:554 */       if (rightPos != -1)
/* 237:    */       {
/* 238:557 */         int endRightPos = s.length();
/* 239:558 */         if (centrePos > rightPos)
/* 240:    */         {
/* 241:561 */           endRightPos = centrePos;
/* 242:562 */           if ((leftPos > rightPos) && (endRightPos > leftPos)) {
/* 243:565 */             endRightPos = leftPos;
/* 244:    */           }
/* 245:    */         }
/* 246:574 */         else if (leftPos > rightPos)
/* 247:    */         {
/* 248:577 */           endRightPos = leftPos;
/* 249:    */         }
/* 250:585 */         this.right = createContents(s.substring(rightPos + 2, endRightPos));
/* 251:    */       }
/* 252:589 */       if (centrePos != -1)
/* 253:    */       {
/* 254:592 */         int endCentrePos = s.length();
/* 255:593 */         if (rightPos > centrePos)
/* 256:    */         {
/* 257:596 */           endCentrePos = rightPos;
/* 258:597 */           if ((leftPos > centrePos) && (endCentrePos > leftPos)) {
/* 259:600 */             endCentrePos = leftPos;
/* 260:    */           }
/* 261:    */         }
/* 262:609 */         else if (leftPos > centrePos)
/* 263:    */         {
/* 264:612 */           endCentrePos = leftPos;
/* 265:    */         }
/* 266:620 */         this.centre = createContents(s.substring(centrePos + 2, endCentrePos));
/* 267:    */       }
/* 268:    */     }
/* 269:625 */     if (this.left == null) {
/* 270:627 */       this.left = createContents();
/* 271:    */     }
/* 272:630 */     if (this.centre == null) {
/* 273:632 */       this.centre = createContents();
/* 274:    */     }
/* 275:635 */     if (this.right == null) {
/* 276:637 */       this.right = createContents();
/* 277:    */     }
/* 278:    */   }
/* 279:    */   
/* 280:    */   public String toString()
/* 281:    */   {
/* 282:649 */     StringBuffer hf = new StringBuffer();
/* 283:650 */     if (!this.left.empty())
/* 284:    */     {
/* 285:652 */       hf.append("&L");
/* 286:653 */       hf.append(this.left.getContents());
/* 287:    */     }
/* 288:656 */     if (!this.centre.empty())
/* 289:    */     {
/* 290:658 */       hf.append("&C");
/* 291:659 */       hf.append(this.centre.getContents());
/* 292:    */     }
/* 293:662 */     if (!this.right.empty())
/* 294:    */     {
/* 295:664 */       hf.append("&R");
/* 296:665 */       hf.append(this.right.getContents());
/* 297:    */     }
/* 298:668 */     return hf.toString();
/* 299:    */   }
/* 300:    */   
/* 301:    */   protected Contents getRightText()
/* 302:    */   {
/* 303:678 */     return this.right;
/* 304:    */   }
/* 305:    */   
/* 306:    */   protected Contents getCentreText()
/* 307:    */   {
/* 308:688 */     return this.centre;
/* 309:    */   }
/* 310:    */   
/* 311:    */   protected Contents getLeftText()
/* 312:    */   {
/* 313:698 */     return this.left;
/* 314:    */   }
/* 315:    */   
/* 316:    */   protected void clear()
/* 317:    */   {
/* 318:706 */     this.left.clear();
/* 319:707 */     this.right.clear();
/* 320:708 */     this.centre.clear();
/* 321:    */   }
/* 322:    */   
/* 323:    */   protected abstract Contents createContents();
/* 324:    */   
/* 325:    */   protected abstract Contents createContents(String paramString);
/* 326:    */   
/* 327:    */   protected abstract Contents createContents(Contents paramContents);
/* 328:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.HeaderFooter
 * JD-Core Version:    0.7.0.1
 */