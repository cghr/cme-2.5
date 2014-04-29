/*   1:    */ package jxl.demo;
/*   2:    */ 
/*   3:    */ import java.io.BufferedWriter;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.io.OutputStreamWriter;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import jxl.WorkbookSettings;
/*  10:    */ import jxl.biff.Type;
/*  11:    */ import jxl.read.biff.BiffException;
/*  12:    */ import jxl.read.biff.BiffRecordReader;
/*  13:    */ import jxl.read.biff.Record;
/*  14:    */ 
/*  15:    */ class BiffDump
/*  16:    */ {
/*  17:    */   private BufferedWriter writer;
/*  18:    */   private BiffRecordReader reader;
/*  19:    */   private HashMap recordNames;
/*  20:    */   private int xfIndex;
/*  21:    */   private int fontIndex;
/*  22:    */   private int bofs;
/*  23:    */   private static final int bytesPerLine = 16;
/*  24:    */   
/*  25:    */   public BiffDump(java.io.File file, OutputStream os)
/*  26:    */     throws IOException, BiffException
/*  27:    */   {
/*  28: 65 */     this.writer = new BufferedWriter(new OutputStreamWriter(os));
/*  29: 66 */     FileInputStream fis = new FileInputStream(file);
/*  30: 67 */     jxl.read.biff.File f = new jxl.read.biff.File(fis, new WorkbookSettings());
/*  31: 68 */     this.reader = new BiffRecordReader(f);
/*  32:    */     
/*  33: 70 */     buildNameHash();
/*  34: 71 */     dump();
/*  35:    */     
/*  36: 73 */     this.writer.flush();
/*  37: 74 */     this.writer.close();
/*  38: 75 */     fis.close();
/*  39:    */   }
/*  40:    */   
/*  41:    */   private void buildNameHash()
/*  42:    */   {
/*  43: 83 */     this.recordNames = new HashMap(50);
/*  44:    */     
/*  45: 85 */     this.recordNames.put(Type.BOF, "BOF");
/*  46: 86 */     this.recordNames.put(Type.EOF, "EOF");
/*  47: 87 */     this.recordNames.put(Type.FONT, "FONT");
/*  48: 88 */     this.recordNames.put(Type.SST, "SST");
/*  49: 89 */     this.recordNames.put(Type.LABELSST, "LABELSST");
/*  50: 90 */     this.recordNames.put(Type.WRITEACCESS, "WRITEACCESS");
/*  51: 91 */     this.recordNames.put(Type.FORMULA, "FORMULA");
/*  52: 92 */     this.recordNames.put(Type.FORMULA2, "FORMULA");
/*  53: 93 */     this.recordNames.put(Type.XF, "XF");
/*  54: 94 */     this.recordNames.put(Type.MULRK, "MULRK");
/*  55: 95 */     this.recordNames.put(Type.NUMBER, "NUMBER");
/*  56: 96 */     this.recordNames.put(Type.BOUNDSHEET, "BOUNDSHEET");
/*  57: 97 */     this.recordNames.put(Type.CONTINUE, "CONTINUE");
/*  58: 98 */     this.recordNames.put(Type.FORMAT, "FORMAT");
/*  59: 99 */     this.recordNames.put(Type.EXTERNSHEET, "EXTERNSHEET");
/*  60:100 */     this.recordNames.put(Type.INDEX, "INDEX");
/*  61:101 */     this.recordNames.put(Type.DIMENSION, "DIMENSION");
/*  62:102 */     this.recordNames.put(Type.ROW, "ROW");
/*  63:103 */     this.recordNames.put(Type.DBCELL, "DBCELL");
/*  64:104 */     this.recordNames.put(Type.BLANK, "BLANK");
/*  65:105 */     this.recordNames.put(Type.MULBLANK, "MULBLANK");
/*  66:106 */     this.recordNames.put(Type.RK, "RK");
/*  67:107 */     this.recordNames.put(Type.RK2, "RK");
/*  68:108 */     this.recordNames.put(Type.COLINFO, "COLINFO");
/*  69:109 */     this.recordNames.put(Type.LABEL, "LABEL");
/*  70:110 */     this.recordNames.put(Type.SHAREDFORMULA, "SHAREDFORMULA");
/*  71:111 */     this.recordNames.put(Type.CODEPAGE, "CODEPAGE");
/*  72:112 */     this.recordNames.put(Type.WINDOW1, "WINDOW1");
/*  73:113 */     this.recordNames.put(Type.WINDOW2, "WINDOW2");
/*  74:114 */     this.recordNames.put(Type.MERGEDCELLS, "MERGEDCELLS");
/*  75:115 */     this.recordNames.put(Type.HLINK, "HLINK");
/*  76:116 */     this.recordNames.put(Type.HEADER, "HEADER");
/*  77:117 */     this.recordNames.put(Type.FOOTER, "FOOTER");
/*  78:118 */     this.recordNames.put(Type.INTERFACEHDR, "INTERFACEHDR");
/*  79:119 */     this.recordNames.put(Type.MMS, "MMS");
/*  80:120 */     this.recordNames.put(Type.INTERFACEEND, "INTERFACEEND");
/*  81:121 */     this.recordNames.put(Type.DSF, "DSF");
/*  82:122 */     this.recordNames.put(Type.FNGROUPCOUNT, "FNGROUPCOUNT");
/*  83:123 */     this.recordNames.put(Type.COUNTRY, "COUNTRY");
/*  84:124 */     this.recordNames.put(Type.TABID, "TABID");
/*  85:125 */     this.recordNames.put(Type.PROTECT, "PROTECT");
/*  86:126 */     this.recordNames.put(Type.SCENPROTECT, "SCENPROTECT");
/*  87:127 */     this.recordNames.put(Type.OBJPROTECT, "OBJPROTECT");
/*  88:128 */     this.recordNames.put(Type.WINDOWPROTECT, "WINDOWPROTECT");
/*  89:129 */     this.recordNames.put(Type.PASSWORD, "PASSWORD");
/*  90:130 */     this.recordNames.put(Type.PROT4REV, "PROT4REV");
/*  91:131 */     this.recordNames.put(Type.PROT4REVPASS, "PROT4REVPASS");
/*  92:132 */     this.recordNames.put(Type.BACKUP, "BACKUP");
/*  93:133 */     this.recordNames.put(Type.HIDEOBJ, "HIDEOBJ");
/*  94:134 */     this.recordNames.put(Type.NINETEENFOUR, "1904");
/*  95:135 */     this.recordNames.put(Type.PRECISION, "PRECISION");
/*  96:136 */     this.recordNames.put(Type.BOOKBOOL, "BOOKBOOL");
/*  97:137 */     this.recordNames.put(Type.STYLE, "STYLE");
/*  98:138 */     this.recordNames.put(Type.EXTSST, "EXTSST");
/*  99:139 */     this.recordNames.put(Type.REFRESHALL, "REFRESHALL");
/* 100:140 */     this.recordNames.put(Type.CALCMODE, "CALCMODE");
/* 101:141 */     this.recordNames.put(Type.CALCCOUNT, "CALCCOUNT");
/* 102:142 */     this.recordNames.put(Type.NAME, "NAME");
/* 103:143 */     this.recordNames.put(Type.MSODRAWINGGROUP, "MSODRAWINGGROUP");
/* 104:144 */     this.recordNames.put(Type.MSODRAWING, "MSODRAWING");
/* 105:145 */     this.recordNames.put(Type.OBJ, "OBJ");
/* 106:146 */     this.recordNames.put(Type.USESELFS, "USESELFS");
/* 107:147 */     this.recordNames.put(Type.SUPBOOK, "SUPBOOK");
/* 108:148 */     this.recordNames.put(Type.LEFTMARGIN, "LEFTMARGIN");
/* 109:149 */     this.recordNames.put(Type.RIGHTMARGIN, "RIGHTMARGIN");
/* 110:150 */     this.recordNames.put(Type.TOPMARGIN, "TOPMARGIN");
/* 111:151 */     this.recordNames.put(Type.BOTTOMMARGIN, "BOTTOMMARGIN");
/* 112:152 */     this.recordNames.put(Type.HCENTER, "HCENTER");
/* 113:153 */     this.recordNames.put(Type.VCENTER, "VCENTER");
/* 114:154 */     this.recordNames.put(Type.ITERATION, "ITERATION");
/* 115:155 */     this.recordNames.put(Type.DELTA, "DELTA");
/* 116:156 */     this.recordNames.put(Type.SAVERECALC, "SAVERECALC");
/* 117:157 */     this.recordNames.put(Type.PRINTHEADERS, "PRINTHEADERS");
/* 118:158 */     this.recordNames.put(Type.PRINTGRIDLINES, "PRINTGRIDLINES");
/* 119:159 */     this.recordNames.put(Type.SETUP, "SETUP");
/* 120:160 */     this.recordNames.put(Type.SELECTION, "SELECTION");
/* 121:161 */     this.recordNames.put(Type.STRING, "STRING");
/* 122:162 */     this.recordNames.put(Type.FONTX, "FONTX");
/* 123:163 */     this.recordNames.put(Type.IFMT, "IFMT");
/* 124:164 */     this.recordNames.put(Type.WSBOOL, "WSBOOL");
/* 125:165 */     this.recordNames.put(Type.GRIDSET, "GRIDSET");
/* 126:166 */     this.recordNames.put(Type.REFMODE, "REFMODE");
/* 127:167 */     this.recordNames.put(Type.GUTS, "GUTS");
/* 128:168 */     this.recordNames.put(Type.EXTERNNAME, "EXTERNNAME");
/* 129:169 */     this.recordNames.put(Type.FBI, "FBI");
/* 130:170 */     this.recordNames.put(Type.CRN, "CRN");
/* 131:171 */     this.recordNames.put(Type.HORIZONTALPAGEBREAKS, "HORIZONTALPAGEBREAKS");
/* 132:172 */     this.recordNames.put(Type.VERTICALPAGEBREAKS, "VERTICALPAGEBREAKS");
/* 133:173 */     this.recordNames.put(Type.DEFAULTROWHEIGHT, "DEFAULTROWHEIGHT");
/* 134:174 */     this.recordNames.put(Type.TEMPLATE, "TEMPLATE");
/* 135:175 */     this.recordNames.put(Type.PANE, "PANE");
/* 136:176 */     this.recordNames.put(Type.SCL, "SCL");
/* 137:177 */     this.recordNames.put(Type.PALETTE, "PALETTE");
/* 138:178 */     this.recordNames.put(Type.PLS, "PLS");
/* 139:179 */     this.recordNames.put(Type.OBJPROJ, "OBJPROJ");
/* 140:180 */     this.recordNames.put(Type.DEFCOLWIDTH, "DEFCOLWIDTH");
/* 141:181 */     this.recordNames.put(Type.ARRAY, "ARRAY");
/* 142:182 */     this.recordNames.put(Type.WEIRD1, "WEIRD1");
/* 143:183 */     this.recordNames.put(Type.BOOLERR, "BOOLERR");
/* 144:184 */     this.recordNames.put(Type.SORT, "SORT");
/* 145:185 */     this.recordNames.put(Type.BUTTONPROPERTYSET, "BUTTONPROPERTYSET");
/* 146:186 */     this.recordNames.put(Type.NOTE, "NOTE");
/* 147:187 */     this.recordNames.put(Type.TXO, "TXO");
/* 148:188 */     this.recordNames.put(Type.DV, "DV");
/* 149:189 */     this.recordNames.put(Type.DVAL, "DVAL");
/* 150:190 */     this.recordNames.put(Type.SERIES, "SERIES");
/* 151:191 */     this.recordNames.put(Type.SERIESLIST, "SERIESLIST");
/* 152:192 */     this.recordNames.put(Type.SBASEREF, "SBASEREF");
/* 153:193 */     this.recordNames.put(Type.CONDFMT, "CONDFMT");
/* 154:194 */     this.recordNames.put(Type.CF, "CF");
/* 155:195 */     this.recordNames.put(Type.FILTERMODE, "FILTERMODE");
/* 156:196 */     this.recordNames.put(Type.AUTOFILTER, "AUTOFILTER");
/* 157:197 */     this.recordNames.put(Type.AUTOFILTERINFO, "AUTOFILTERINFO");
/* 158:198 */     this.recordNames.put(Type.XCT, "XCT");
/* 159:    */     
/* 160:200 */     this.recordNames.put(Type.UNKNOWN, "???");
/* 161:    */   }
/* 162:    */   
/* 163:    */   private void dump()
/* 164:    */     throws IOException
/* 165:    */   {
/* 166:207 */     Record r = null;
/* 167:208 */     boolean cont = true;
/* 168:209 */     while ((this.reader.hasNext()) && (cont))
/* 169:    */     {
/* 170:211 */       r = this.reader.next();
/* 171:212 */       cont = writeRecord(r);
/* 172:    */     }
/* 173:    */   }
/* 174:    */   
/* 175:    */   private boolean writeRecord(Record r)
/* 176:    */     throws IOException
/* 177:    */   {
/* 178:224 */     boolean cont = true;
/* 179:225 */     int pos = this.reader.getPos();
/* 180:226 */     int code = r.getCode();
/* 181:228 */     if (this.bofs == 0) {
/* 182:230 */       cont = r.getType() == Type.BOF;
/* 183:    */     }
/* 184:233 */     if (!cont) {
/* 185:235 */       return cont;
/* 186:    */     }
/* 187:238 */     if (r.getType() == Type.BOF) {
/* 188:240 */       this.bofs += 1;
/* 189:    */     }
/* 190:243 */     if (r.getType() == Type.EOF) {
/* 191:245 */       this.bofs -= 1;
/* 192:    */     }
/* 193:248 */     StringBuffer buf = new StringBuffer();
/* 194:    */     
/* 195:    */ 
/* 196:251 */     writeSixDigitValue(pos, buf);
/* 197:252 */     buf.append(" [");
/* 198:253 */     buf.append(this.recordNames.get(r.getType()));
/* 199:254 */     buf.append("]");
/* 200:255 */     buf.append("  (0x");
/* 201:256 */     buf.append(Integer.toHexString(code));
/* 202:257 */     buf.append(")");
/* 203:259 */     if (code == Type.XF.value)
/* 204:    */     {
/* 205:261 */       buf.append(" (0x");
/* 206:262 */       buf.append(Integer.toHexString(this.xfIndex));
/* 207:263 */       buf.append(")");
/* 208:264 */       this.xfIndex += 1;
/* 209:    */     }
/* 210:267 */     if (code == Type.FONT.value)
/* 211:    */     {
/* 212:269 */       if (this.fontIndex == 4) {
/* 213:271 */         this.fontIndex += 1;
/* 214:    */       }
/* 215:274 */       buf.append(" (0x");
/* 216:275 */       buf.append(Integer.toHexString(this.fontIndex));
/* 217:276 */       buf.append(")");
/* 218:277 */       this.fontIndex += 1;
/* 219:    */     }
/* 220:280 */     this.writer.write(buf.toString());
/* 221:281 */     this.writer.newLine();
/* 222:    */     
/* 223:283 */     byte[] standardData = new byte[4];
/* 224:284 */     standardData[0] = ((byte)(code & 0xFF));
/* 225:285 */     standardData[1] = ((byte)((code & 0xFF00) >> 8));
/* 226:286 */     standardData[2] = ((byte)(r.getLength() & 0xFF));
/* 227:287 */     standardData[3] = ((byte)((r.getLength() & 0xFF00) >> 8));
/* 228:288 */     byte[] recordData = r.getData();
/* 229:289 */     byte[] data = new byte[standardData.length + recordData.length];
/* 230:290 */     System.arraycopy(standardData, 0, data, 0, standardData.length);
/* 231:291 */     System.arraycopy(recordData, 0, data, 
/* 232:292 */       standardData.length, recordData.length);
/* 233:    */     
/* 234:294 */     int byteCount = 0;
/* 235:295 */     int lineBytes = 0;
/* 236:297 */     while (byteCount < data.length)
/* 237:    */     {
/* 238:299 */       buf = new StringBuffer();
/* 239:300 */       writeSixDigitValue(pos + byteCount, buf);
/* 240:301 */       buf.append("   ");
/* 241:    */       
/* 242:303 */       lineBytes = Math.min(16, data.length - byteCount);
/* 243:305 */       for (int i = 0; i < lineBytes; i++)
/* 244:    */       {
/* 245:307 */         writeByte(data[(i + byteCount)], buf);
/* 246:308 */         buf.append(' ');
/* 247:    */       }
/* 248:312 */       if (lineBytes < 16) {
/* 249:314 */         for (int i = 0; i < 16 - lineBytes; i++) {
/* 250:316 */           buf.append("   ");
/* 251:    */         }
/* 252:    */       }
/* 253:320 */       buf.append("  ");
/* 254:322 */       for (int i = 0; i < lineBytes; i++)
/* 255:    */       {
/* 256:324 */         char c = (char)data[(i + byteCount)];
/* 257:325 */         if ((c < ' ') || (c > 'z')) {
/* 258:327 */           c = '.';
/* 259:    */         }
/* 260:329 */         buf.append(c);
/* 261:    */       }
/* 262:332 */       byteCount += lineBytes;
/* 263:    */       
/* 264:334 */       this.writer.write(buf.toString());
/* 265:335 */       this.writer.newLine();
/* 266:    */     }
/* 267:338 */     return cont;
/* 268:    */   }
/* 269:    */   
/* 270:    */   private void writeSixDigitValue(int pos, StringBuffer buf)
/* 271:    */   {
/* 272:346 */     String val = Integer.toHexString(pos);
/* 273:348 */     for (int i = 6; i > val.length(); i--) {
/* 274:350 */       buf.append('0');
/* 275:    */     }
/* 276:352 */     buf.append(val);
/* 277:    */   }
/* 278:    */   
/* 279:    */   private void writeByte(byte val, StringBuffer buf)
/* 280:    */   {
/* 281:360 */     String sv = Integer.toHexString(val & 0xFF);
/* 282:362 */     if (sv.length() == 1) {
/* 283:364 */       buf.append('0');
/* 284:    */     }
/* 285:366 */     buf.append(sv);
/* 286:    */   }
/* 287:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.BiffDump
 * JD-Core Version:    0.7.0.1
 */